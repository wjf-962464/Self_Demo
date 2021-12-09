package com.wjf.self_demo;

import com.google.gson.Gson;

public class ToIntJava {
    public void method1() {
        int a = Integer.parseInt("123");
        Integer b = Integer.valueOf("123");
        Object ob = new Gson().fromJson("s", Object.class);
    }
}
