package com.wjf.self_library.common;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.wjf.self_library.BuildConfig;
import com.wjf.self_library.R;
import com.wjf.self_library.util.ActivityCollector;
import com.wjf.self_library.util.StatusBar.StatusBarUtil;
import com.wjf.self_library.view.codingUI.CommonToolbar;
import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout;
import com.wjf.self_library.view.swipebacklayout.app.SwipeBackActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity {
    /** 封装toolbar */
    protected CommonToolbar commonToolbar;

    private ArrayList<String> permissionString = new ArrayList<>();
    protected T view;
    private final int PERMISSIONS_REQUEST = 0x001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 手动入栈
        ActivityCollector.addActivity(this);
        // 设置控件绑定
        view = DataBindingUtil.setContentView(this, setLayout());
        // 隐藏actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // 初始化右滑返回
        initSwipeActivity();
        // 设置自动根据标题栏变色
        initColorStatusBar();
        initView();
        initData();
    }

    private void initSwipeActivity() {
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    private void initColorStatusBar() {
        commonToolbar = findViewById(R.id.commonToolbar);
        if (commonToolbar != null) {
            int color = commonToolbar.getBackgroundColor();
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(this, commonToolbar.isDarkTheme());
            } else {
                StatusBarUtil.setStatusBarColor(this, color, commonToolbar.isDarkTheme());
            }
        } else {
            // 没有标题栏，默认判定为透明且白色主题
            StatusBarUtil.setTranslucentStatus(this, false);
            disableSwipeOut();
        }
    }

    /** 黑色主题透明状态栏 */
    public void darkTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this, true);
    }

    /** 禁用右滑返回 */
    public void disableSwipeOut() {
        getSwipeBackLayout().setEnableGesture(false);
    }

    public void toastShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public BaseActivity<T> addPermission(String permissionString) {
        this.permissionString.add(permissionString);
        return this;
    }

    public void requestPermission() {
        if (checkPermissionAllGranted(permissionString)) {
            doSomethingAfterGranted();
            Log.d(BuildConfig.TAG, "onRequestPermissionsResult granted");
        } else {
            String[] permissions = new String[permissionString.size()];
            ActivityCompat.requestPermissions(
                    this, permissionString.toArray(permissions), PERMISSIONS_REQUEST);
        }
    }

    /** 检查是否拥有指定的所有权限 */
    private boolean checkPermissionAllGranted(ArrayList<String> permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.w(BuildConfig.TAG, "error $permission 没有授权");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull @NotNull String[] permissions,
            @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                boolean isAllGranted = true;
                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false;
                    }
                }
                if (isAllGranted) {
                    Log.i(BuildConfig.TAG, "onRequestPermissionsResult granted");
                    doSomethingAfterGranted();
                } else {
                    Log.w(BuildConfig.TAG, "onRequestPermissionsResult denied");
                    showWaringDialog();
                }
                break;
            default:
                break;
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("警告！")
                        .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                        .setPositiveButton(
                                "前往设置",
                                (dialog1, which) -> {
                                    Intent intent =
                                            new Intent(
                                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                        .setNegativeButton("取消", (dialog12, which) -> finish())
                        .show();
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    public abstract int setLayout();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();

    protected void doSomethingAfterGranted() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
