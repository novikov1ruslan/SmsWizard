package com.external.smswizard.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.smswizard.R;

public class LoginLayout extends LinearLayout {
    private LayoutListener layoutListener;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mButtonView;
    private View mProgressView;
    private View mLoginFormView;

    public LoginLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (layoutListener != null) {
                        layoutListener.onLogin();
                    }
                    return true;
                }
                return false;
            }
        });

        mButtonView = findViewById(R.id.sign_in_button);
        mButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutListener != null) {
                    layoutListener.onLogin();
                }
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        mProgressView.setVisibility(View.GONE);

        mLoginFormView = findViewById(R.id.login_form);
//        hideProgress();
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public String getEmail() {
        return mEmailView.getText().toString();
    }

    public String getPassword() {
        return mPasswordView.getText().toString();
    }

    public void setEmailError(String msg) {
        mEmailView.setError(msg);
        mEmailView.requestFocus();
    }

    public void showProgress() {
        showProgress(true);
    }

    public void hideProgress() {
        showProgress(false);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void setEmailAdapter(ArrayAdapter<String> adapter) {
        mEmailView.setAdapter(adapter);
    }

    public interface LayoutListener {
        void onLogin();
    }
}