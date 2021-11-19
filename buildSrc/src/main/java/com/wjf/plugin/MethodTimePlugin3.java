package com.wjf.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class MethodTimePlugin3 implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("Aaaa");
    }
}
