package com.wjf.self_demo.entity;

import com.google.gson.annotations.SerializedName;

/** @author WJF */
public class AccessibilityTab {

    @SerializedName("id")
    private String id;

    @SerializedName("fullId")
    private String fullId;

    @SerializedName("packageName")
    private String packageName;

    @SerializedName("text")
    private String text;

    public AccessibilityTab(String id, String packageName, String text) {
        this.id = id;
        this.fullId = packageName + ":id/" + id;
        this.packageName = packageName;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "AccessibilityTab{"
                + "id='"
                + id
                + '\''
                + ", fullId='"
                + fullId
                + '\''
                + ", packageName='"
                + packageName
                + '\''
                + ", text='"
                + text
                + '\''
                + '}';
    }
}
