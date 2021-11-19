import org.gradle.api.Plugin
import org.gradle.api.Project

class MethodTimePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("Aaaa")
//        target.extensions.create()
/*        target.afterEvaluate {
            target.tasks.create("methodTime", MethodTimeTask::class.java)
        }*/
    }
}
