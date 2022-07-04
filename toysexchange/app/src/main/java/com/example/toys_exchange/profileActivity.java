package com.example.toys_exchange;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.UI.StoreListActivity;
import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.UI.eventListActivity;
import com.example.toys_exchange.UI.toyListActivity;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {
    private static final String TAG = profileActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 123;

    private Handler handler;

    AuthUser logedInUser;

    public String userId ;
    ArrayList<Account> acclist = new ArrayList<>();
    public String acc_id;

    String cognitoId;
    private String username;
    private String URL;
    private String userIdShared;
    CircleImageView imageView;
    TextView bioView;
    TextView usernameView;
    MaterialButton updateBtn;

    Dialog myDialog;
    String usernameDisplay;
    String bioDisplay;

    private View.OnClickListener mClickEventsList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startAllTasksIntent = new Intent(getApplicationContext(), eventListActivity.class);
            startAllTasksIntent.putExtra("userId", userId);
            startActivity(startAllTasksIntent);

        }
    };


    private View.OnClickListener mClickToysList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        Intent passedIntent = getIntent();
         usernameDisplay = passedIntent.getStringExtra("username");
         bioDisplay = passedIntent.getStringExtra("bio");
        /////////////////////////////////////////////////////////////////////
        bioView = findViewById(R.id.txt_Bio);
        usernameView = findViewById(R.id.txt_username);
        usernameView.setText(usernameDisplay);
        bioView.setText(bioDisplay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////////////////*********             Wish List Button                **********//////////////////

        TextView wishList = findViewById(R.id.tvWishlist);
        wishList.setOnClickListener(view -> {
            startActivity(new Intent(this, WishListActivity.class));
        });

         logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId =  logedInUser.getUserId();

        ////////////////*********             Event List Button                **********//////////////////


        TextView btnEvents = findViewById(R.id.eventList);
        btnEvents.setOnClickListener(mClickEventsList);


        ////////////////*********             Toys List Button                **********//////////////////



         TextView btnToys = findViewById(R.id.toysList);
         btnToys.setOnClickListener(mClickToysList);

         ///////////////***********    Store List
        TextView btnStore = findViewById(R.id.tvStoreList);
        btnStore.setOnClickListener(view -> {
            startActivity(new Intent(this, StoreListActivity.class));

        });


        ////////////////*********             get UserName                **********//////////////////

         handler = new Handler(Looper.getMainLooper(), msg -> {
             userId = msg.getData().getString("id");
             Intent intent = getIntent();
             userId = intent.getStringExtra("userId");
             return true;
         });

        authAttribute();
         getUserId();


           ////////////////*********             Logout Button                **********//////////////////

        MaterialButton btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(mClickLogout);

        /////////////////////////////////////////// add Image /////////////////////

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        username =  sharedPreferences.getString(LoginActivity.NAMEUSERNAME, "No username");
        userIdShared =  sharedPreferences.getString(LoginActivity.USERNAME, "No User Id");
        Log.i(TAG, "SharedPreferences => " + username);

         imageView = findViewById(R.id.ivProfileImage);
         getUser();

}

    @Override
    protected void onResume() {
        Log.i(TAG, "OnResume ...........");

        logedInUser = Amplify.Auth.getCurrentUser();
        authAttribute();
        getUserId();
        super.onResume();
    }

    private void getUser() {
        Amplify.API.query(ModelQuery.get(Account.class,userIdShared),
                user -> {
                    if(user.hasData()) {
                        Log.i(TAG, "check for Image ..... "+ user.getData().getImage());

                        if(user.getData().getImage() != null)
                            Log.i(TAG, "check for Image ..... Not null ");
                        runOnUiThread(()->{
                           getUrl(user.getData().getImage());
                        });

                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    private void getUrl(String image){
        Amplify.Storage.getUrl(
                image,
                result -> {
                    runOnUiThread(()->{
                        Picasso.get().load(result.getUrl().toString()).into(imageView);
                    });
                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );
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

    private void getUserId() {
        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),
                    accs -> {
                        if(accs.hasData()) {
                            for (Account acc :
                                    accs.getData()) {
                                if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                    acclist.add(acc);
                                    acc_id = acc.getId().toString();
                                    runOnUiThread(()->{
                                        bioView = findViewById(R.id.txt_Bio);
                                        usernameView = findViewById(R.id.txt_username);
                                        usernameView.setText(acc.getUsername());
                                        bioView.setText(acc.getBio());
                                    });
                                }
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });
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