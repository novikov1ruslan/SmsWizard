package com.external.smswizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.external.smswizard.view.MainLayout;
import com.external.smswizard.model.ApplicationModel;

public class MainActivity extends Activity {
    private ApplicationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainLayout layout = (MainLayout) getLayoutInflater().inflate(R.layout.main, null);
        layout.setLayoutListener(new MainLayout.LayoutListener() {
            @Override
            public void onApplicationOn() {
                model.setApplicationOn();
            }

            @Override
            public void onApplicationOff() {
                model.setApplicationOff();
            }

            @Override
            public void onLogout() {
                model.forgetToken();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        });

        model = new ApplicationModel(getApplicationContext());
        layout.setApplicationStatus(model.isApplicationOn());
        setContentView(layout);
    }
}
