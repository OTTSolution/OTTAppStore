package com.xugaoxiang.ott.appstore.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zero on 2016/10/19.
 */

public class CommonUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null)
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        else
            toast.setText(content);
        toast.show();
    }
}
