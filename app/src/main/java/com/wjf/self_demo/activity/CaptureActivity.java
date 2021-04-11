package com.wjf.self_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.wjf.self_demo.R;
import com.wjf.self_demo.Zxing.BeepManager;
import com.wjf.self_demo.Zxing.InactivityTimer;
import com.wjf.self_demo.Zxing.camera.CameraManager;
import com.wjf.self_demo.Zxing.decode.CaptureActivityHandler;
import com.wjf.self_demo.Zxing.decode.DecodeFormatManager;
import com.wjf.self_demo.databinding.ActivityCaptureBinding;
import com.wjf.self_library.common.BaseActivity;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;

import com.google.zxing.Result;

public class CaptureActivity extends BaseActivity<ActivityCaptureBinding>
        implements SurfaceHolder.Callback {

    private String TAG = "debug_wjf";
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private boolean hasSurface;

    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private Rect mCropRect = null;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private boolean isOpenTorch; // 判断是否开启闪光灯

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public int setLayout() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initView() {

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager=BeepManager.getInstance();
        Animation animationSet = AnimationUtils.loadAnimation(this, R.anim.scan_anim);
        animationSet.setInterpolator(new DecelerateInterpolator());
        view.captureScanLine.startAnimation(animationSet);
    }

    @Override
    protected void initData() {}

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        handler = null;
        view.btnOpenFlashlight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOpenTorch) {
                            isOpenTorch = false;
                            view.btnOpenFlashlight.setText(
                                    getString(R.string.btn_open_flashlight_text));
                        } else {
                            isOpenTorch = true;
                            view.btnOpenFlashlight.setText(
                                    getString(R.string.btn_close_flashlight_text));
                        }
                        cameraManager.setTorch(isOpenTorch);
                    }
                });

        view.btnScanLocalPic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickPictureFromAblum(v);
                    }
                });

        inactivityTimer.onResume();
        decodeFormats = null;
        characterSet = null;
        SurfaceHolder surfaceHolder = view.capturePreview.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_270:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        //        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            view.capturePreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler =
                        new CaptureActivityHandler(
                                this, decodeFormats, characterSet, cameraManager);
            }
            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /** 初始化截取的矩形区域 */
    private void initCrop() {
        int cameraWidth = cameraManager.getBestPreviewSize().y;
        int cameraHeight = cameraManager.getBestPreviewSize().x;

        /** 获取布局中扫描框的位置和宽高 */
        int[] location = new int[2];
        view.captureCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = view.captureCropView.getWidth();
        int cropHeight = view.captureCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = view.captureContainer.getWidth();
        int containerHeight = view.captureContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    private int getStatusBarHeight() {
        try {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            return frame.top;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 扫描本地图片上的二维码
     *
     * @param v
     */
    public void pickPictureFromAblum(View v) {
        Intent mIntent =
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mIntent, 1);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent) 对相册获取的结果进行分析
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor =
                            getContentResolver()
                                    .query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    final Result resultString = scanImageQR(picturePath);
                    if (resultString == null) {
                        Toast.makeText(
                                        getApplicationContext(),
                                        getString(R.string.scan_fail),
                                        Toast.LENGTH_LONG)
                                .show();
                    } else {
                        // 扫描成功
                        handleDecode(resultString);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 解析QR图内容
     *
     * @return Result
     */
    private Result scanImageQR(String picturePath) {

        if (TextUtils.isEmpty(picturePath)) {
            return null;
        }

        Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
        decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);

        Map<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 获得待解析的图片
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
            result = reader.decode(bitmap1, (Hashtable<DecodeHintType, Object>) hints);
            return result;
        } catch (NotFoundException e) {
            Toast.makeText(CaptureActivity.this, getString(R.string.scan_fail), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        } catch (ChecksumException e) {
            Toast.makeText(CaptureActivity.this, getString(R.string.scan_fail), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        } catch (com.google.zxing.FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate(this);
        // TODO: 2017/11/13 返回扫描结果
        view.imgScanSuccess.setVisibility(View.VISIBLE);
        CountDownTimer count =
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate(this);
        /*        Bundle bundle = new Bundle();
        Intent resultIntent = new Intent();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        resultIntent.putExtras(bundle);*/
        String result = rawResult.getText();
        Intent intent = null;
        if (result.matches("^[0-9]+$")) {
            int spot_id = Integer.valueOf(rawResult.getText());
            /*            intent=new Intent(this,SpotControlActivity.class);
            intent.putExtra("spot_id",spot_id);*/
            Log.d(TAG, "扫描结果：纯数字" + result);
        } else {
            Log.d(TAG, "扫描结果：非纯数字" + result);
            /*
            intent=new Intent(this,WebActivity.class);
            intent.putExtra("url",result);*/
        }
        startActivity(intent);
        finish();
        //        this.setResult(RESULT_OK, resultIntent);
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
        builder.show();
    }
}
