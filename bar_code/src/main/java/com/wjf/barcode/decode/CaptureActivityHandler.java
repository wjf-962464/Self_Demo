/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wjf.barcode.decode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.wjf.barcode.CaptureActivity;
import com.wjf.barcode.MsgConstants;
import com.wjf.barcode.camera.CameraManager;

import java.util.Collection;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CaptureActivityHandler extends Handler {

    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    public CaptureActivityHandler(
            CaptureActivity activity,
            Collection<BarcodeFormat> decodeFormats,
            String characterSet,
            CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case MsgConstants.RESTART_PREVIEW:
                restartPreviewAndDecode();
                break;
            case MsgConstants.DECODE_SUCCEEDED:
                state = State.SUCCESS;
                Bundle bundle = message.getData();
                activity.handleDecode((Result) message.obj);
                break;
            case MsgConstants.DECODE_FAILED:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), MsgConstants.DECODE);
                break;
            case MsgConstants.RETURN_SCAN_RESULT:
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
            default:
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), MsgConstants.QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(MsgConstants.DECODE_SUCCEEDED);
        removeMessages(MsgConstants.DECODE_FAILED);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), MsgConstants.DECODE);
        }
    }

    private enum State {
        /** 预览 */
        PREVIEW,
        /** 成功 */
        SUCCESS,
        /** 结束 */
        DONE
    }
}
