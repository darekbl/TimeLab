package projekt.sqlmulti;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import projekt.sqlmulti.helper.DatabaseHelper;
import projekt.sqlmulti.model.User_model;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG = RegisterActivity.class.getName();

    private UserRegisterTask mAuthTask = null;

    EditText etName,etEmail,etPassword;
    private Button btnRegister, btnLinkToLoginScreen;
    DatabaseHelper db;
    ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(getApplicationContext());

        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });


        // Listening to Login Screen link
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                Intent Login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(Login);
                finish();
            }
        });

        mProgressView = (ProgressBar) findViewById(R.id.register_progress);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        etEmail.setError(null);
        etName.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.error_field_required));
            focusView = etName;
            cancel = true;
        } else if (!isNameValid(name)) {
            etEmail.setError(getString(R.string.error_invalid_name));
            focusView = etName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new RegisterActivity.UserRegisterTask(email, password, name);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mName;

        UserRegisterTask(String email, String password, String name){
            mEmail = email;
            mPassword = password;
            mName = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            DatabaseHelper db = new DatabaseHelper(getApplicationContext());

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
try {
    int userCount = db.getUsersCount();
    if (userCount > 0) {
        List<User_model> Users = db.getAllUsers();
        ListIterator<User_model> iterator = Users.listIterator();

        while (iterator.hasNext()) {
            User_model User = iterator.next();
            if (User.getEmail().equals(mEmail)) {
                return false;
            }
            else{
                User_model um = new User_model(mName, mEmail, mPassword);
                long id = db.createUser(um);
                if (id > 0)
                    return true;
                else
                   // Toast.makeText(getApplicationContext(), "Błąd przy dodawaniu do bazy...", Toast.LENGTH_LONG).show();
                return false;
            }

        }
    } else {
        User_model um = new User_model(mName, mEmail, mPassword);
        long id = db.createUser(um);
        if (id > 0)
            return true;
    }
}
catch (Exception e){
    e.printStackTrace();
}

            ////////////////////////////////////////////////////////////////////////
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Log.i(LOG,"SUCCESFULLY REGISTERED");
                Intent Login = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(Login);
                Toast.makeText(getApplicationContext(),getString(R.string.succes_registered),Toast.LENGTH_SHORT).show();
                finish();
            } else {
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
                    Toast.makeText(getApplicationContext(),getString(R.string.error_user_exist),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
