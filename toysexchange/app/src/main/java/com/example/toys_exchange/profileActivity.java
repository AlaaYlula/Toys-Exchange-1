package com.example.toys_exchange;

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


import androidx.appcompat.app.AppCompatActivity;


import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.UI.eventListActivity;
import com.example.toys_exchange.UI.toyListActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class profileActivity extends AppCompatActivity {

    private Handler handler;
    Event event;
    Account acc;


    private static final String TAG = profileActivity.class.getSimpleName();
    private View.OnClickListener mClickEventsList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            mEventsList.setText("My events");
//            mEventsList.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), eventListActivity.class);
            startAllTasksIntent.putExtra("userId", userId);
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
    public String userId ;
    ArrayList<Account> acclist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        ////////////////*********             Wish List Button                **********//////////////////

        TextView wishList = findViewById(R.id.tvWishlist);
        wishList.setOnClickListener(view -> {
            startActivity(new Intent(this, WishListActivity.class));
        });



        ////////////////*********             Event List Button                **********//////////////////


        TextView btnEvents = findViewById(R.id.eventList);
        btnEvents.setOnClickListener(mClickEventsList);


        ////////////////*********             Toys List Button                **********//////////////////



         TextView btnToys = findViewById(R.id.toysList);
         btnToys.setOnClickListener(mClickToysList);


        ////////////////*********             get UserName                **********//////////////////

         handler = new Handler(Looper.getMainLooper(), msg -> {
             String user = msg.getData().getString("name");
             TextView name = findViewById(R.id.txt_username);
             name.setText(user);
             userId = msg.getData().getString("id");
             Intent intent = getIntent();
             userId = intent.getStringExtra("userId");
             Log.i(TAG, "aya: " + name);
             Log.i(TAG, "aya: " + userId);

             return true;
         });

//        Intent intent = getIntent();
//        userId = intent.getStringExtra("userId");
        Log.i(TAG, "userIdaya: "  + userId);
         authAttribute();

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String dima =  logedInUser.getUserId();
        Log.i(TAG, "Dima " + dima);
        Log.i(TAG, "yousssi: " + logedInUser.getUserId());


//        String accId = "userId";
        final String[] acId = new String[1];


        runOnUiThread(() -> {
                    Amplify.API.query(
        ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),
            accs -> {
                if(accs.hasData()) {
                    for (Account acc :
                            accs.getData()) {

                        Log.i(TAG, "dimaaa: ");
                        Log.i(TAG, "codId: " + acc.getIdcognito().toString());
                        Log.i(TAG, "coogId: " + logedInUser);
                        if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                            acclist.add(acc);
                            String acc_id = acc.getId().toString();
                            Log.i(TAG, "InGetEventsList: " + acc.getId());
                        }
                    }
                }
//                    acc = accs.getData();
//                Log.i(TAG, "onte: " + acc);
//                    Log.i(TAG, "Data: " + acc.getId().toString());
                    // Use To do Sync
//                    acId[0] =  acc.getId().toString();
                    Log.i(TAG, "Account Id" + acId[0]);

        },
                error -> Log.e(TAG, error.toString(), error)
        );
    });

                                                    ////////////////*********             Logout Button                **********//////////////////

        MaterialButton btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(mClickLogout);

}


    private void authAttribute() {
        Amplify.Auth.fetchUserAttributes(
                attribute -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", attribute.get(2).getValue());
                    bundle.putString("id", attribute.get(0).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(0).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(2).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(1).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(3).getValue());

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
                        bundle.putString("userId", "userId");
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