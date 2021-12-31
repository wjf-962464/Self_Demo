package com.wjf.selfPlugin

import com.wjf.selfPlugin.config.Extension
import com.wjf.selfPlugin.config.WjfPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project;

class HelloGradlePlugin implements Plugin<Project> {
    void apply(Project project) {

        def wjfPlugin = project.extensions.create("wjf", WjfPlugin)

        def extension = wjfPlugin.extensions.create("extension", Extension)

        project.task('hello') {
            group = "wjf"
            description = "gradle build script demo"
            doLast {
                println "Hello from the com.wjf.selfPlug.HelloGradlePlugin ${wjfPlugin.enable} ${extension.msg}"
            }
            doFirst {
                println "开始了wjf"
                if (wjfPlugin.enable) {
                    startApply(extension)
                }
                println "结束了wjf ${wjfPlugin.enable} ${extension.msg}"
            }
        }

        println 'wjf'
    }

    private void startApply(Extension extension) {
        println '------------------------'
        println "apply com.wjf.selfPlug.HelloGradlePlugin ${extension.msg} ${extension.msg}"
        println '------------------------'
    }
}



