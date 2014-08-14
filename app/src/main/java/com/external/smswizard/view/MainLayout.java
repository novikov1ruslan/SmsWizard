package com.external.smswizard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.external.smswizard.R;

public class MainLayout extends LinearLayout {
    private LayoutListener layoutListener;
    private CheckBox appOnCheckBox;

    public MainLayout(Context context, AttributeSet attrs) {
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

        appOnCheckBox = (CheckBox) findViewById(R.id.on_off);
//        appOnCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (layoutListener != null) {
//                    if (isChecked) {
//                        layoutListener.onApplicationOn();
//                    }
//                    else {
//                        layoutListener.onApplicationOff();
//                    }
//                }
//            }
//        });
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public void setApplicationStatus(boolean applicationOn) {
        appOnCheckBox.setChecked(applicationOn);
    }

    public interface LayoutListener {
        void onApplicationOn();

        void onApplicationOff();

        void onLogout();
    }
}
