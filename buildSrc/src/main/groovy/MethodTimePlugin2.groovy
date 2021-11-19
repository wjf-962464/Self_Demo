import org.gradle.api.Plugin
import org.gradle.api.Project

class MethodTimePlugin2 implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("Aaaa")
//        target.extensions.create()
        target.afterEvaluate {
            target.tasks.create("methodTime", MethodTimeTask.class)
        }
    }
}
