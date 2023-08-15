package com.wjf.self_demo.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.wjf.self_demo.databinding.ActivityFragmentTestBinding
import org.jxxy.debug.corekit.common.BaseActivity

class FragmentTestActivity : BaseActivity<ActivityFragmentTestBinding>() {
    override fun bindLayout(): ActivityFragmentTestBinding =
        ActivityFragmentTestBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun subscribeUi() {
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.i("wjftc", "onCreate: 1")
        super.onCreate(savedInstanceState, persistentState)
        Log.i("wjftc", "onCreate: 2")
    }

    override fun onStart() {
        Log.i("wjftc", "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.i("wjftc", "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.i("wjftc", "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.i("wjftc", "onStop: ")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("wjftc", "onDestroy: ")
        super.onDestroy()
    }
}
