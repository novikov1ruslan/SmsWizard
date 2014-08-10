package com.external.smswizard;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>, LoginLayout.LayoutListener {

    private static final String TAG = "LoginActivity";

    private ApplicationModel applicationModel;
    private LoginLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationModel = new ApplicationModel(getApplicationContext());
        if (applicationModel.hasToken()) {
            startSmsWizard();
            finish();
            return;
        }

        layout = (LoginLayout) getLayoutInflater().inflate(R.layout.activity_login, null);
        setContentView(layout);
        populateAutoComplete();
    }

    private void startSmsWizard() {
        startActivity(new Intent(getBaseContext(), HomeActivity.class));
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    public void onEventMainThread(RestService.Token token) {
        if (token.token == null) {
            Crouton.makeText(LoginActivity.this, getString(R.string.login_failed), Style.ALERT).show();
        }
        else {
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

        if (TextUtils.isEmpty(email)) {
            layout.setEmailError(getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            layout.setEmailError(getString(R.string.error_invalid_email));
        } else {
            layout.showProgress();
            String password = layout.getPassword();
            startLogin(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void startLogin(String email, String password) {
        Intent intent = new Intent(this, RestService.class);
        intent.putExtra(RestService.EXTRA_EMAIL, email);
        intent.putExtra(RestService.EXTRA_PASSWORD, password);
        startService(intent);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        int ADDRESS = 0;
    }

}



