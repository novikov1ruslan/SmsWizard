package com.external.smswizard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class SmsWizardLayout extends LinearLayout {
    private LayoutListener layoutListener;

    public SmsWizardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.on_off).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutListener == null) {
                    return;
                }

                if (((CheckBox) v).isChecked()) {
                    layoutListener.onApplicationOn();
                } else {
                    layoutListener.onApplicationOff();
                }
            }
        });

        findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutListener == null) {
                    return;
                }

                layoutListener.onLogout();
            }
        });

    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public interface LayoutListener {
        void onApplicationOn();

        void onApplicationOff();

        void onLogout();
    }
}
