package com.wjf.self_demo;

import org.junit.Test;

public class TestJava {
    @Test
    public void fun() {
        System.out.println("wjf");
        try {
            String[] a = null;
            System.out.println(a.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
