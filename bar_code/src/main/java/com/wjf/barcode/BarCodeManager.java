package com.wjf.barcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.wjf.barcode.decode.DecodeFormatManager;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author : Wangjf
 * @date : 2021/4/12
 */
public class BarCodeManager {
    private static final String TAG = BarCodeManager.class.getSimpleName();

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

        Map<DecodeHintType, Object> hints = new Hashtable<>();
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
            result = reader.decode(bitmap1, hints);
            return result;
        } catch (NotFoundException | ChecksumException e) {
            Logger.d("扫描失败");
            e.printStackTrace();
        } catch (com.google.zxing.FormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
