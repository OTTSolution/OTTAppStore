// Generated code from Butter Knife. Do not modify!
package com.xugaoxiang.ott.appstore.module.homepage;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xugaoxiang.ott.appstore.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomoPageFragment_ViewBinding<T extends HomoPageFragment> implements Unbinder {
  protected T target;

  @UiThread
  public HomoPageFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.rvCategory = Utils.findRequiredViewAsType(source, R.id.rv_category, "field 'rvCategory'", RecyclerView.class);
    target.rvAppCategory = Utils.findRequiredViewAsType(source, R.id.rv_app_category, "field 'rvAppCategory'", RecyclerView.class);
    target.rvApp = Utils.findRequiredViewAsType(source, R.id.rv_app, "field 'rvApp'", RecyclerView.class);
    target.tvPage = Utils.findRequiredViewAsType(source, R.id.tv_page, "field 'tvPage'", TextView.class);
    target.pbLoading = Utils.findRequiredViewAsType(source, R.id.pb_loading, "field 'pbLoading'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rvCategory = null;
    target.rvAppCategory = null;
    target.rvApp = null;
    target.tvPage = null;
    target.pbLoading = null;

    this.target = null;
  }
}
