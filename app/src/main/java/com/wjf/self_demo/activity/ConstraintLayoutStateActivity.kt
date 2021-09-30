package com.wjf.self_demo.activity

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityConstraintLayoutStateBinding
import com.wjf.self_demo.databinding.ItemListTestBinding
import com.wjf.self_library.common.BaseActivity
import com.wjf.self_library.common.click

/**
 * @author WJF
 */
class ConstraintLayoutStateActivity : BaseActivity<ActivityConstraintLayoutStateBinding>() {
    val handler = Handler(Looper.getMainLooper())
    override fun setLayout(): Int {
        return R.layout.activity_constraint_layout_state
    }

    override fun initView() {

        var stubView: View? = null
//        view.stateConstraintLayout.loadLayoutDescription(R.xml.constraint_layout_states_example)
        view.changStateBtn.click {
/*            view.stateConstraintLayout.setState(R.id.loading, 0, 0)
            handler.postDelayed(
                {
                    view.stateConstraintLayout.setState(R.id.error, 0, 0)
                },
                3000L
            )*/

            view.viewStub.viewStub?.let { viewStub ->
                stubView = viewStub.inflate()
                stubView?.let {
                    val userName = it.findViewById<TextView>(R.id.userName)
                    (view.viewStub.binding as? ItemListTestBinding)?.userName?.text = "用户名"
                    val userDes = it.findViewById<TextView>(R.id.userDes)
                    (view.viewStub.binding as? ItemListTestBinding)?.userDes?.text = "userDes"
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

    override fun initData() {}
}
