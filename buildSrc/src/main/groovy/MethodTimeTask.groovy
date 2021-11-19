import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MethodTimeTask extends DefaultTask {
    MethodTimeTask() {
        group = "wjf2"
    }

    @TaskAction
    def run() {
        println("sss")
    }
}
