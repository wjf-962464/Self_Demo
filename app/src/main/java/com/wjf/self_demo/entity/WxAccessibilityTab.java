package com.wjf.self_demo.entity;

import com.wjf.self_demo.util.AccessibilityHelper;

/** @author WJF */
public class WxAccessibilityTab extends AccessibilityTab {

    public WxAccessibilityTab(String id, String text) {
        super(id, AccessibilityHelper.PACKAGE_WEWORK, text);
    }
}
