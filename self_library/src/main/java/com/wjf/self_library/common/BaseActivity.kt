package com.wjf.self_library.common

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.R
import com.wjf.self_library.util.ActivityCollector
import com.wjf.self_library.util.StatusBar.StatusBarUtil
import com.wjf.self_library.util.common.AppUtils
import com.wjf.self_library.view.codingUI.CommonToolbar
import com.wjf.self_library.view.codingUI.NormalDialog
import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout
import com.wjf.self_library.view.swipebacklayout.app.SwipeBackActivity
import java.util.*

/**
 * @author : WJF
 * @date : 2021/1/19
 */
abstract class BaseActivity<T : ViewDataBinding> : SwipeBackActivity() {
    /** 封装toolbar  */
    protected var commonToolbar: CommonToolbar? = null
    private val permissionString = ArrayList<String>()

    protected lateinit var view: T
    private val permissionDialog: NormalDialog by lazy {
        NormalDialog(this).setPositiveButton("前往设置") {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
            it.dismiss()
        }.setNegativeButton("取消") {
            it.dismiss()
            finish()
        }.addData(NormalDialog.TITLE, "温馨提示").addData(
            NormalDialog.MESSAGE,
            "请前往设置->应用->${AppUtils.getAppName(this)}->权限中打开相关权限，否则功能无法正常运行！"
        ).gravity(Gravity.CENTER) as NormalDialog
    }

    private val PERMISSIONS_REQUEST = 0x001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = DataBindingUtil.setContentView<T>(this, setLayout())
        // 手动入栈
        ActivityCollector.addActivity(this)
        // 隐藏actionbar
        supportActionBar?.hide()
        // 初始化右滑返回
        initSwipeActivity()
        // 设置自动根据标题栏变色
        initColorStatusBar()
        initView()
        initData()
        subscribeUi()
    }

    private fun initSwipeActivity() {
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }

    private fun initColorStatusBar() {
        commonToolbar = findViewById(R.id.commonToolbar)
        commonToolbar?.let {
            val color = it.backgroundColor
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(this, it.isDarkTheme)
            } else {
                StatusBarUtil.setStatusBarColor(this, color, it.isDarkTheme)
            }
        }
        if (commonToolbar == null) {
            // 没有标题栏，默认判定为透明且白色主题
            StatusBarUtil.setTranslucentStatus(this, false)
            disableSwipeOut()
        }
    }

    /** 黑色主题透明状态栏  */
    fun darkTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, true)
    }

    /** 禁用右滑返回  */
    protected fun disableSwipeOut() {
        swipeBackLayout.setEnableGesture(false)
    }

    protected fun toastShort(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun addPermission(permissionString: String): BaseActivity<T> {
        this.permissionString.add(permissionString)
        return this
    }

    fun requestPermission() {
        if (checkPermissionAllGranted(permissionString)) {
            doSomethingAfterGranted()
            Log.d(BuildConfig.TAG, "onRequestPermissionsResult granted")
        } else {
            val permissions = arrayOfNulls<String>(permissionString.size)
            ActivityCompat.requestPermissions(
                this, permissionString.toArray(permissions), PERMISSIONS_REQUEST
            )
        }
    }

    /** 检查是否拥有指定的所有权限  */
    private fun checkPermissionAllGranted(permissions: ArrayList<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(BuildConfig.TAG, "error \$permission 没有授权")
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                var isAllGranted = true
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false
                    }
                }
                if (isAllGranted) {
                    Log.i(BuildConfig.TAG, "onRequestPermissionsResult granted")
                    doSomethingAfterGranted()
                } else {
                    Log.w(BuildConfig.TAG, "onRequestPermissionsResult denied")
                    permissionDialog.show()
                }
            }
            else -> {
            }
        }
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    abstract fun setLayout(): Int

    /** 初始化控件  */
    protected abstract fun initView()

    /** 初始化数据  */
    protected abstract fun initData()

    /** 订阅ViewModel  */
    protected open fun subscribeUi() {}
    protected open fun doSomethingAfterGranted() {}
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
