// Generated code from Butter Knife. Do not modify!
package com.xugaoxiang.ott.appstore.module.homepage;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xugaoxiang.ott.appstore.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppAdapter$AAViewHolder_ViewBinding<T extends AppAdapter.AAViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public AppAdapter$AAViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.ivAppIcon = Utils.findRequiredViewAsType(source, R.id.iv_app_icon, "field 'ivAppIcon'", ImageView.class);
    target.tvAppName = Utils.findRequiredViewAsType(source, R.id.tv_app_name, "field 'tvAppName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivAppIcon = null;
    target.tvAppName = null;

    this.target = null;
  }
}
