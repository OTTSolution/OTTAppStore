package com.xugaoxiang.ott.appstore.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xugaoxiang.ott.appstore.R;

/**
 * Created by zero on 2016/10/18.
 */

public class DialogCommon extends Dialog {

    public DialogCommon(Context context) {
        super(context, R.style.DialogCommon);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public DialogCommon create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final DialogCommon dialog = new DialogCommon(context);
            View rootview = inflater.inflate(R.layout.dialog_common, null);
            ((TextView) rootview.findViewById(R.id.tv_title)).setText(title);
            ((TextView) rootview.findViewById(R.id.tv_content)).setText(content);
            if (positiveButtonText != null) {
                ((TextView) rootview.findViewById(R.id.tv_confirm))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    rootview.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else
                rootview.findViewById(R.id.tv_confirm).setVisibility(View.GONE);
            if (negativeButtonText != null) {
                ((TextView) rootview.findViewById(R.id.tv_cancel)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    rootview.findViewById(R.id.tv_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else
                rootview.findViewById(R.id.tv_cancel).setVisibility(View.GONE);
            dialog.setContentView(rootview, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            return dialog;
        }
    }
}
