// Generated code from Butter Knife. Do not modify!
package com.xugaoxiang.ott.appstore.module.appinfo;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xugaoxiang.ott.appstore.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScreenshotsAdapter$SSViewHolder_ViewBinding<T extends ScreenshotsAdapter.SSViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ScreenshotsAdapter$SSViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.ivScreenshot = Utils.findRequiredViewAsType(source, R.id.iv_screenshot, "field 'ivScreenshot'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivScreenshot = null;

    this.target = null;
  }
}
