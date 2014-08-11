package com.external.smswizard;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.external.smswizard.model.ApplicationModel;
import com.external.smswizard.view.LoginLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.util.Ln;

public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>, LoginLayout.LayoutListener {
    private ApplicationModel applicationModel;
    private LoginLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationModel = new ApplicationModel(getApplicationContext());
        if (applicationModel.hasToken()) {
            Ln.d("token present, skipping login phase");
            startSmsWizard();
            return;
        }

        layout = (LoginLayout) getLayoutInflater().inflate(R.layout.login, null);
        layout.setLayoutListener(this);
        setContentView(layout);
        populateAutoComplete();
        Ln.d("UI created");
    }

    private void startSmsWizard() {
        Ln.d("staring main app screen");
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Ln.d("UI is becoming visible, registered for event bus");
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Ln.d("UI is NOT visible, unregistered from event bus");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
        Ln.d("UI is destroyed, croutons cancelled");
    }

    /**
     * EventBus takes care of calling the method in the main thread without any further code required.
     */
    public void onEventMainThread(RestService.Token token) {
        layout.hideProgress();
        Ln.d("token=%s", token);
        if (token.token == null) {
            Crouton.makeText(LoginActivity.this, getString(R.string.login_failed), Style.ALERT).show();
        } else {
            applicationModel.setApplicationOn();
            AlarmReceiver.schedulePolling(this);
            startSmsWizard();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        Ln.d("finished loading email addresses");

        addEmailsToAutoComplete(emails);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        layout.setEmailAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onLogin() {
        String email = layout.getEmail();
        Ln.d("login attempt for [%s]", email);
        if (TextUtils.isEmpty(email)) {
            layout.setEmailError(getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            layout.setEmailError(getString(R.string.error_invalid_email));
        } else {
            layout.showProgress();
            RestService.login(this, email, layout.getPassword());
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        int ADDRESS = 0;
    }

}



