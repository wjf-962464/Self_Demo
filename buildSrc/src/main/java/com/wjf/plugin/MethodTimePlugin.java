package com.wjf.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class MethodTimePlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("Aaaa");
        //        target.getExtensions().getByType(ApplicationExtension.class).

    }
}
