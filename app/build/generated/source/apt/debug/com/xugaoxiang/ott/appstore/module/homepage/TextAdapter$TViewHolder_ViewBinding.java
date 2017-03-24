// Generated code from Butter Knife. Do not modify!
package com.xugaoxiang.ott.appstore.module.homepage;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xugaoxiang.ott.appstore.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TextAdapter$TViewHolder_ViewBinding<T extends TextAdapter.TViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public TextAdapter$TViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.tvCatagroy = Utils.findRequiredViewAsType(source, R.id.tv_category, "field 'tvCatagroy'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvCatagroy = null;

    this.target = null;
  }
}
