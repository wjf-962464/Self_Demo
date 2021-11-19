package com.wjf.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MethodTimeTask3() : DefaultTask() {
    init {
        group = "wjf2"
    }

    @TaskAction
    fun run() {
        println("sss")
    }
}
