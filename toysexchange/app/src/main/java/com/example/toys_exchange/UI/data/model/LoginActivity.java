package com.example.toys_exchange.UI.data.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;

import com.amplifyframework.datastore.generated.model.UserWishList;
import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.SignUpActivity;
import com.google.android.material.button.MaterialButton;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final String USERNAME = "username";
    public static final String NAMEUSERNAME = "name";

    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shophop_fragment_sign_in);


//        final TextView signUpPrompt = findViewById(R.id.sign_up_prompt);
        final EditText usernameEditText = findViewById(R.id.edtEmail);
        final EditText passwordEditText =findViewById(R.id.edtPassword);
        final MaterialButton loginButton = findViewById(R.id.btnSignIn);
//        loadingProgressBar = findViewById(R.id.loading);

        final MaterialButton signUp = findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(view -> {
                       Intent navigateToSignUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(navigateToSignUpIntent);
        });

//        signUpPrompt.setOnClickListener(view -> {
//            Intent navigateToSignUpIntent = new Intent(this, SignUpActivity.class);
//            startActivity(navigateToSignUpIntent);
//        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.setEnabled(true);

                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);

                login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });
    }


    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome) + "";
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {
                    Log.i(TAG, result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

//                    loadingProgressBar.setVisibility(View.INVISIBLE);

                    getLoggedInAccountData();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                },
                error -> {
                    Log.e(TAG, error.toString());
                    // show a dialog of the error below
                    // error.getMessage()
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Error")
                                        .setMessage(error.getMessage())
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                                            }
                                        }).show();
                            }
                        }
                    });
                }
        );
    }

    private void getLoggedInAccountData(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.get(0).getValue());
                    Amplify.API.query(
                            ModelQuery.list(Account.class),
                            accounts -> {
                                Log.i(TAG, "getUserName: -----------------------------------<> " + accounts.getData());
                                for (Account user : accounts.getData()) {
                                    if (user.getIdcognito().equals(attributes.get(0).getValue())) {
                                        runOnUiThread(()->{
                                            Log.i(TAG, "getUserId: -----------------------------------<> " + user.getId());

                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                                            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

                                            // save the text to shared preferences
                                            preferenceEditor.putString(USERNAME, user.getId());
                                            preferenceEditor.putString(NAMEUSERNAME, user.getUsername());
                                            preferenceEditor.apply();
                                        });

                                        // create shared preference object and set up an editor


                                    }
                                }
                            },
                            error -> Log.e(TAG, error.toString(), error)
                    );
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }

}