package com.example.toys_exchange.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.toys_exchange.R;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String USERID = "userId";
    private View loadingProgressBar;
    public static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_fragment_sign_up);

        final EditText usernameEditText = findViewById(R.id.edtFirstName);
        final EditText emailEditText = findViewById(R.id.edtEmail);
        final EditText passwordEditText =findViewById(R.id.edtPassword);
//        final EditText edtConfirmPassword =findViewById(R.id.edtConfirmPassword);
        final MaterialButton signUpButton = findViewById(R.id.btnSignUp);
        final MaterialButton signIpButton = findViewById(R.id.btnSignIn);
//        loadingProgressBar = findViewById(R.id.loading);


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    signUpButton.setEnabled(true);
                }
                    return false;
                }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);

                String username = usernameEditText.getText().toString();

                signUp(username,
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });
    }


    private void signUp(String username, String email, String password) {

        // add as many attributes as you wish
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.nickname(), username)
                .build();

        Amplify.Auth.signUp(email, password, options,
                result -> {
                    Log.i(TAG, "Result: " + result.toString());

//                    loadingProgressBar.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                    intent.putExtra(EMAIL, email);
                    intent.putExtra(USERNAME, username);
                    intent.putExtra(USERID, result.getUser().getUserId());
                    startActivity(intent);

                    finish();
                },
                error -> {
                    Log.e(TAG, "Sign up failed", error);
                    // show a dialog of the error below
                    // error.getMessage()
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(SignUpActivity.this)
                                        .setTitle("Error")
                                        .setMessage(error.getMessage())
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                                startActivity(new Intent(SignUpActivity.this,SignUpActivity.class));
                                            }
                                        }).show();
                            }
                        }
                    });
                }
        );

    }

}