package com.wjf.self_demo.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wjf.self_demo.databinding.FragmentABinding
import org.jxxy.debug.corekit.common.BaseFragment

class AFragment : BaseFragment<FragmentABinding>() {
    override fun bindLayout(): FragmentABinding = FragmentABinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun subscribeUi() {
    }

    override fun onAttach(context: Context) {
        Log.d("wjftc", "onAttach: ")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("wjftc", "onCreate: ")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("wjftc", "onCreateView: ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("wjftc", "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("wjftc", "onActivityCreated: ")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d("wjftc", "onStart: ")
        super.onStart()
    }

    override fun onResume() {
        Log.d("wjftc", "onResume: ")
        super.onResume()
    }

    override fun onPause() {
        Log.d("wjftc", "onPause: ")
        super.onPause()
    }

    override fun onStop() {
        Log.d("wjftc", "onStop: ")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("wjftc", "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("wjftc", "onDestroy: ")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("wjftc", "onDetach: ")
        super.onDetach()
    }
}
