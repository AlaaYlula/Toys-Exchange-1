package com.example.toys_exchange.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.datastore.generated.model.Account;
import com.example.toys_exchange.R;


public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();
//    private String username = "username";
//    private String userId = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        String email = intent.getStringExtra(SignUpActivity.EMAIL);
        String username = intent.getStringExtra(SignUpActivity.USERNAME);
        String userId = intent.getStringExtra(SignUpActivity.USERID);

        EditText verificationCode = findViewById(R.id.verification_code);
        Button verifyButton = findViewById(R.id.verify_button);

        verifyButton.setOnClickListener(view -> {
            verify(verificationCode.getText().toString(), email, username, userId);
        });
    }


    private void verify(String code, String email , String username , String userId) {
        Amplify.Auth.confirmSignUp(
                email,
                code,
                result -> {

                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    // Add the user To API
                    Account user = Account.builder()
                            .username(username)
                            .idcognito(userId)
                            .build();

                    Amplify.DataStore.save(user,
                            success -> Log.i(TAG, "Saved User DataStore: " + success.item().getUsername()),
                            error -> Log.e(TAG, "Could not save item to DataStore", error)
                    );
                    // API save to backend
                    Amplify.API.mutate(
                            ModelMutation.create(user),
                            success -> {
                                Log.i(TAG, "Saved User API: " + success.getData().getUsername());
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                        });
                            },
                            error -> Log.e(TAG, "Could not save item to API", error)
                    );

                },
                error -> Log.e(TAG, error.toString())
        );
    }
}