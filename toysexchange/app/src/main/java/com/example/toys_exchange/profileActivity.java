package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.example.toys_exchange.UI.LoginActivity;

public class profileActivity extends AppCompatActivity {

    private Handler handler;


    private static final String TAG = profileActivity.class.getSimpleName();
    private View.OnClickListener mClickEventsList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            mEventsList.setText("My events");
//            mEventsList.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), eventListActivity.class);
            startActivity(startAllTasksIntent);

        }
    };


    private View.OnClickListener mClickToysList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            mToysList.setText("My toys");
//            mToysList.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), toyListActivity.class);
            startActivity(startAllTasksIntent);

        }
    };

    private View.OnClickListener mClickLogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            logout();
        }
    };


    private TextView mEventsList;
    private TextView mToysList;
    private String userId = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ////////////////*********             Event List Button                **********//////////////////


        Button btnEvents = findViewById(R.id.eventList);
        btnEvents.setOnClickListener(mClickEventsList);


        ////////////////*********             Toys List Button                **********//////////////////



         Button btnToys = findViewById(R.id.toysList);
         btnToys.setOnClickListener(mClickToysList);


        ////////////////*********             get UserName                **********//////////////////

         handler = new Handler(Looper.getMainLooper(), msg -> {
             String user = msg.getData().getString("name");
             TextView name = findViewById(R.id.txt_username);
             name.setText(user);
             userId = msg.getData().getString("id");
             return true;
         });

         authAttribute();


         ////////////////*********             Logout Button                **********//////////////////

        Button btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(mClickLogout);

}


    private void authAttribute() {
        Amplify.Auth.fetchUserAttributes(
                attribute -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", attribute.get(2).getValue());
                    bundle.putString("id", attribute.get(0).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> Log.e(TAG, "authAttribute: ", error)
        );
    }



    private void authSessionUserName(String method) {
//        https://github.com/aws-amplify/amplify-android/issues/851
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, result.toString());
                    Toast.makeText(this, "${Amplify.Auth.currentUser.userId} is logged in", Toast.LENGTH_LONG).show();
                },
                error -> Log.e(TAG, error.toString())
        );
    }


    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i(TAG, "Auth Session => " + method + result.toString());
                    if(result.isSignedIn())
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", "username");

                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);
                    }
                },

                error -> Log.e(TAG, error.toString())
        );
    }


    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(profileActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

}