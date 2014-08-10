package com.external.smswizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.external.smswizard.model.ApplicationModel;

public class HomeActivity extends Activity {
    private ApplicationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ApplicationModel(getApplicationContext());
        SmsWizardLayout layout = (SmsWizardLayout) getLayoutInflater().inflate(R.layout.activiry_sms_wizard, null);
        layout.setLayoutListener(new SmsWizardLayout.LayoutListener() {
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
        setContentView(layout);
    }
}
