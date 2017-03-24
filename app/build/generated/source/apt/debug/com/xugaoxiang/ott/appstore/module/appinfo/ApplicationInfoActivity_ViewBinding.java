// Generated code from Butter Knife. Do not modify!
package com.xugaoxiang.ott.appstore.module.appinfo;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.xugaoxiang.ott.appstore.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ApplicationInfoActivity_ViewBinding<T extends ApplicationInfoActivity> implements Unbinder {
  protected T target;

  private View view2131755133;

  private View view2131755134;

  private View view2131755135;

  @UiThread
  public ApplicationInfoActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.ivAppIcon = Utils.findRequiredViewAsType(source, R.id.iv_app_icon, "field 'ivAppIcon'", ImageView.class);
    target.tvAppName = Utils.findRequiredViewAsType(source, R.id.tv_app_name, "field 'tvAppName'", TextView.class);
    target.tvAppInfo = Utils.findRequiredViewAsType(source, R.id.tv_app_info, "field 'tvAppInfo'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_install_open, "field 'tvInstallOpen' and method 'onClick'");
    target.tvInstallOpen = Utils.castView(view, R.id.tv_install_open, "field 'tvInstallOpen'", TextView.class);
    view2131755133 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_uninstall, "field 'tvUninstall' and method 'onClick'");
    target.tvUninstall = Utils.castView(view, R.id.tv_uninstall, "field 'tvUninstall'", TextView.class);
    view2131755134 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_update, "field 'tvUpdate' and method 'onClick'");
    target.tvUpdate = Utils.castView(view, R.id.tv_update, "field 'tvUpdate'", TextView.class);
    view2131755135 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvDescription = Utils.findRequiredViewAsType(source, R.id.tv_description, "field 'tvDescription'", TextView.class);
    target.rvScreenshots = Utils.findRequiredViewAsType(source, R.id.rv_screenshots, "field 'rvScreenshots'", RecyclerView.class);
    target.llRootview = Utils.findRequiredViewAsType(source, R.id.ll_rootview, "field 'llRootview'", LinearLayout.class);
    target.pbLoading = Utils.findRequiredViewAsType(source, R.id.pb_loading, "field 'pbLoading'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivAppIcon = null;
    target.tvAppName = null;
    target.tvAppInfo = null;
    target.tvInstallOpen = null;
    target.tvUninstall = null;
    target.tvUpdate = null;
    target.tvDescription = null;
    target.rvScreenshots = null;
    target.llRootview = null;
    target.pbLoading = null;

    view2131755133.setOnClickListener(null);
    view2131755133 = null;
    view2131755134.setOnClickListener(null);
    view2131755134 = null;
    view2131755135.setOnClickListener(null);
    view2131755135 = null;

    this.target = null;
  }
}
