package com.wjf.plugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.dsl.DependencyHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

class MethodTimePlugin implements Plugin<Project> {
    private final ArrayList<String> list =
            new ArrayList<>(Collections.singletonList("androidx.palette:palette:1.0.0"));

    @Override
    public void apply(Project project) {
        System.out.println("Aaaa");

        DependencySet dependencies =
                project.getConfigurations().getByName("implementation").getDependencies();
        DependencyHandler dependencyHandler = project.getDependencies();
        for (String item : list) {
            dependencies.add(dependencyHandler.create(item));
        }

        Project parent = project.getParent();
        Set<Project> allprojects1 = parent.getAllprojects();
        //        parent.getPluginManager().apply(MethodTimePlugin.class);
        parent.allprojects(
                new Action<Project>() {
                    @Override
                    public void execute(Project project) {
                        System.out.println(
                                project.getName() + "---" + project.getRootProject().getName());
                    }
                });
        /*        System.out.println(project.getParent().getName());
        ConfigurationContainer configurations = project.getConfigurations();
        for (int i=0,len=configurations.size();i<len;i++){
            System.out.println("111："+configurations.get(i).getName());
        }*/
        //        System.out.println(Arrays.toString(allprojects1.toArray()));
    }

    public void doMyself() {
        System.out.println("略略路");
    }
}
