import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MethodTimeTask2 : DefaultTask() {

    @TaskAction
    fun run() {
        println("sss")
    }
}
