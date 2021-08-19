package com.wjf.barcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.wjf.barcode.camera.CameraManager;
import com.wjf.barcode.databinding.ActivityCaptureBinding;
import com.wjf.barcode.decode.CaptureActivityHandler;
import com.wjf.self_library.common.BaseActivity;

import java.io.IOException;
import java.util.Collection;

public class CaptureActivity extends BaseActivity<ActivityCaptureBinding>
        implements SurfaceHolder.Callback {

    private final String TAG = "WJF_DEBUG";
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private boolean hasSurface;

    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private Rect mCropRect = null;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private boolean isOpenTorch; // 判断是否开启闪光灯
    private static DecodeResultCallback mDecodeResultCallback;

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
        beepManager = BeepManager.getInstance();

        Animation animationSet = AnimationUtils.loadAnimation(this, R.anim.scan_anim);
        animationSet.setInterpolator(new DecelerateInterpolator());
        getView().captureScanLine.startAnimation(animationSet);
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
                            view.btnOpenFlashlight.setText("打开手电筒");
                        } else {
                            isOpenTorch = true;
                            view.btnOpenFlashlight.setText("关闭手电筒");
                        }
                        cameraManager.setTorch(isOpenTorch);
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
        if (mDecodeResultCallback != null) {
            mDecodeResultCallback.decode(result);
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("应用名称");
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

    public static void setDecodeResultCallback(DecodeResultCallback decodeResultCallback) {
        mDecodeResultCallback = decodeResultCallback;
    }

    public interface DecodeResultCallback {
        void decode(String result);
    }
}
