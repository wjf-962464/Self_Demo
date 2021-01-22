package com.wjf.self_library.common;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.wjf.self_library.R;
import com.wjf.self_library.util.ActivityCollector;
import com.wjf.self_library.util.StatusBar.StatusBarUtil;
import com.wjf.self_library.view.codingUI.CommonToolbar;
import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout;
import com.wjf.self_library.view.swipebacklayout.app.SwipeBackActivity;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity {
  /** 封装toolbar */
  protected CommonToolbar commonToolbar;

  protected T view;

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
    }
  }

  /** 黑色主题透明状态栏 */
  protected void darkTranslucentStatus() {
    StatusBarUtil.setTranslucentStatus(this, true);
  }

  /** 禁用右滑返回 */
  protected void disableSwipeOut() {
    getSwipeBackLayout().setEnableGesture(false);
  }

  protected void toastShort(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ActivityCollector.removeActivity(this);
  }
}
