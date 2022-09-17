package org.jxxy.debug.corekit.common

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import org.jxxy.debug.corekit.BuildConfig
import org.jxxy.debug.corekit.R
import org.jxxy.debug.corekit.http.TokenManager
import org.jxxy.debug.corekit.util.AppUtils
import org.jxxy.debug.corekit.util.ResourceUtil
import org.jxxy.debug.corekit.util.nullOrNot
import org.jxxy.debug.corekit.widget.CommonToolbar
import org.jxxy.debug.corekit.widget.NormalDialog
import org.jxxy.debug.corekit.widget.statusBar.StatusBarUtil

/**
 * @author : WJF
 * @date : 2021/1/19
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    /** 封装toolbar  */
    protected var commonToolbar: CommonToolbar? = null
    private val permissionString = ArrayList<String>()

    protected lateinit var view: T
        private set
    val permissionDialog: NormalDialog by lazy {
        NormalDialog().apply {
            listener = object : NormalDialog.NormalDialogListener {
                override fun onPositiveClick() {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.data = Uri.fromParts("package", packageName, null)
                    startActivity(intent)
                    dismiss()
                }

                override fun onNegativeClick() {
                    dismiss()
                    finish()
                }
            }
            positiveText = ResourceUtil.getString(R.string.gotoSetting)
            negativeText = ResourceUtil.getString(R.string.cancel)
            title = ResourceUtil.getString(R.string.warmHint)
            message = ResourceUtil.getString(R.string.gotoOpenPermission, AppUtils.getAppName())
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST = 0x001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = bindLayout()
        setContentView(view.root)
        // 隐藏actionbar
        supportActionBar?.hide()
        // 设置自动根据标题栏变色
        initColorStatusBar()
        initView()
        subscribeUi()
    }

    private fun initColorStatusBar() {
        commonToolbar = findViewById(R.id.commonToolbar)
        commonToolbar.nullOrNot(
            ifNull = {
                // 没有标题栏，默认判定为透明且黑色主题
                darkTheme(true)
            }
        ) {
            val color = it.backgroundNormalColor
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(
                    this,
                    it.isDarkTheme
                )
            } else {
                StatusBarUtil.setStatusBarColor(
                    this,
                    color,
                    it.isDarkTheme
                )
            }
        }
    }

    /** 黑色主题透明状态栏  */
    protected fun darkTheme(dark: Boolean) {
        StatusBarUtil.setTranslucentStatus(this, dark)
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
                this, permissionString.toArray(permissions),
                PERMISSIONS_REQUEST
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
                    permissionDialog.show(supportFragmentManager, "")
                }
            }
            else -> {
            }
        }
    }

    /** 绑定viewBinding  */
    protected abstract fun bindLayout(): T

    /** 初始化控件  */
    protected abstract fun initView()

    /** 订阅ViewModel  */
    protected abstract fun subscribeUi()

    protected open fun doSomethingAfterGranted() {}

    override fun onResume() {
        super.onResume()
        TokenManager.register(this)
    }

    override fun onPause() {
        super.onPause()
        TokenManager.unRegister(this)
    }
}
