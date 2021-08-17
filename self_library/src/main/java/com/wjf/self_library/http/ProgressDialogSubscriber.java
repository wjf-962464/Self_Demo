package com.wjf.self_library.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public abstract class ProgressDialogSubscriber<T> extends Subscriber<T> {
    private final Context mContext;
    private ProgressDialog mDialog;

    protected ProgressDialogSubscriber(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCompleted() {

    }


    @Override
    public void onError(Throwable e) {
        if(e instanceof SocketTimeoutException){
            Toast.makeText(mContext, "请检查您的网络", Toast.LENGTH_SHORT).show();
        }else if(e instanceof ConnectException){
            Toast.makeText(mContext, "网络繁忙，请稍后再试试吧", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "服务器繁忙，请稍后再试试吧", Toast.LENGTH_SHORT).show();
            System.out.println("error:"+e.getMessage()+e.getLocalizedMessage());
        }
//        dismissProgressDialog();
    }
    private void dismissProgressDialog(){
        if (mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
            mDialog=null;
        }
    }
    private void showProgressDialog(){
        if(mDialog==null){
            mDialog=new ProgressDialog(mContext);
            mDialog.setCancelable(true);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ProgressDialogSubscriber.this.unsubscribe();
                }
            });
        }
        if(mDialog!=null&&!mDialog.isShowing()){
            mDialog.show();
        }
    }
}
