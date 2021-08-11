package com.wjf.self_demo.entity;

import android.os.Bundle;

/**
 * @author : Wangjf
 * @date : 2021/4/6
 */
public class IndexListMenu {
    private Class<?> gotoClass;
    private String des;
    private Bundle data;

    public IndexListMenu(Class<?> gotoClass, String des) {
        this.gotoClass = gotoClass;
        this.des = des;
    }

    public IndexListMenu(Class<?> gotoClass, String des, Bundle data) {
        this.gotoClass = gotoClass;
        this.des = des;
        this.data = data;
    }

    public Class<?> getGotoClass() {
        return gotoClass;
    }

    public void setGotoClass(Class<?> gotoClass) {
        this.gotoClass = gotoClass;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
