package com.wjf.self_demo.activity

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityConstraintLayoutStateBinding
import com.wjf.self_demo.databinding.ItemListTestBinding
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.util.singleClick

/**
 * @author WJF
 */
class ConstraintLayoutStateActivity : BaseActivity<ActivityConstraintLayoutStateBinding>() {
    val handler = Handler(Looper.getMainLooper())
    private var flag = false
    override fun initView() {

        var stubView: View? = null
//        view.stateConstraintLayout.loadLayoutDescription(R.xml.constraint_layout_states_example)
        view.changStateBtn.singleClick {
/*            view.stateConstraintLayout.setState(R.id.loading, 0, 0)
            handler.postDelayed(
                {
                    view.stateConstraintLayout.setState(R.id.error, 0, 0)
                },
                3000L
            )*/
            if (flag.not()) {
                view.viewStub.let { viewStub ->
                    stubView = viewStub.inflate()
                    stubView?.let {
                        val userName = it.findViewById<TextView>(R.id.userName)
                        (view.viewStub as? ItemListTestBinding)?.userName?.text = "用户名"
                        val userDes = it.findViewById<TextView>(R.id.userDes)
                        (view.viewStub as? ItemListTestBinding)?.userDes?.text = "userDes"
                    }
                    flag = true
                }
            }

            stubView?.let {
                if (it.visibility == View.GONE) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = View.GONE
                }
            }
        }
    }

    override fun bindLayout(): ActivityConstraintLayoutStateBinding {
        return ActivityConstraintLayoutStateBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }
}
