package com.wjf.self_library.http;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class HttpResult<T> implements Serializable {
    private int resultCode;
    private String message;
    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "HttpResult{"+
                "data="+data+
                ", resultCode="+resultCode+
                ", message="+message+'\''+
                '}';
    }
}
