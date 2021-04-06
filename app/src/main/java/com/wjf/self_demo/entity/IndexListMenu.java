package com.wjf.self_demo.entity;

/**
 * @author : Wangjf
 * @date : 2021/4/6
 */
public class IndexListMenu {
    private Class<?> gotoClass;
    private String des;

    public IndexListMenu(Class<?> gotoClass, String des) {
        this.gotoClass = gotoClass;
        this.des = des;
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
}
