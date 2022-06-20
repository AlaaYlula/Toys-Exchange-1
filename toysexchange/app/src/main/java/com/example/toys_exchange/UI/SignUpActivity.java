package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Users;
import com.example.toys_exchange.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicReference;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String USERID = "userId";
    private View loadingProgressBar;
    public static final String EMAIL = "email";
    String flag = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText =findViewById(R.id.password);
        final Button signUpButton = findViewById(R.id.signup);
        loadingProgressBar = findViewById(R.id.loading);


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
                loadingProgressBar.setVisibility(View.VISIBLE);

                String username = usernameEditText.getText().toString();

                Amplify.API.query(
                        ModelQuery.list(Users.class, Users.USERNAME.eq(username)),
                        response -> {
                            if(response.hasData()) {
                                for (Users user : response.getData()) {
                                    if (user.getUsername().equals(username)) {
                                        Log.i(TAG, "User "+ user);
                                        flag = "false";
                                    }
                                }
                            }
                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

                if(flag.equals("true")){
                    signUp(username,
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    flag = "false";

                }else{
                    Toast.makeText(getApplicationContext(), "Username already exist... ", Toast.LENGTH_LONG).show();
                    startActivity(  new Intent(getApplicationContext(), SignUpActivity.class));
                }

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

                    loadingProgressBar.setVisibility(View.INVISIBLE);

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
                }
        );

    }

}