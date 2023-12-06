import com.android.build.gradle.AppExtension
import com.wjf.plugins.MyTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class MethodTimePlugin2 implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("Aaaa")
//        target.extensions.create()
        target.afterEvaluate {
//            target.tasks.create("methodTime", com.wjf.plugin.MethodTimeTask.class)
            AppExtension appExtension= project.extensions.getByType(AppExtension)
            //注册Transform
//            appExtension.registerTransform(new MyTransform())
        }
    }
}
