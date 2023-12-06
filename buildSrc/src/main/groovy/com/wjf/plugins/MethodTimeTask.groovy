package com.wjf.plugins

import com.android.build.gradle.AppExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class MethodTimeTask implements Plugin<Project> {
    @Override
    void apply(Project target) {
        println("Aaa")
        // 获取Android插件的扩展对象
        AppExtension appExtension = target.extensions.getByType(AppExtension)

        // 注册自定义的Transform
        appExtension.registerTransform(new MyTransform())
        println("bbbb")

        // 在项目评估完毕后执行的代码块
        target.afterEvaluate {
            // 这里你可以添加依赖于项目配置的任务创建或其他操作
        }
    }
}

