package com.example.toys_exchange.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Notification;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.Firebase.FcnNotificationSender;

import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = EventActivity.class.getSimpleName() ;

    Button btnSubmit;
    Button cancelAdd;

    TextView location;

    String userId;

    Double longitude;
    Double latitude;
    String acc_id;

    private Handler handler;

    private final int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toyexchange_activity_add_event);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Event");
        enableLocation();

        location=findViewById(R.id.tvLocation);

        cancelAdd = findViewById(R.id.btnCancel);

        handler = new Handler(Looper.getMainLooper(), msg -> {
            // get the username
            String user = msg.getData().getString("name");
            //get the user Id
            userId =  msg.getData().getString("Id");
            return true;

        });
        authAttribute(); //get the username and userID

        btnSubmit = findViewById(R.id.btnSubmit);

        addBtnListener(); // Listeners

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MapActivity.class);
                intent.putExtra("type","event");
                startActivity(intent);
            }
        });
        
    }
    ///////////////////////////////////////////////////// Location //////////////////////////////////////////////
    @SuppressLint("MissingPermission")
    private void enableLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                Toast.makeText(this, "Your Location Enabled... ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }


    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "Attributes => "+ attributes);
                    //  Send message to the handler to get the user Id >>
                    Bundle bundle = new Bundle();
                    bundle.putString("name",  attributes.get(2).getValue());
                    bundle.putString("Id",  attributes.get(0).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }

    private void addBtnListener() {

        btnSubmit.setOnClickListener(view -> {

            EditText eventDescription = findViewById(R.id.etDescription);
            EditText eventTitle = findViewById(R.id.etTitle);

            String eventDescriptionText = eventDescription.getText().toString();
            String eventTitleText = eventTitle.getText().toString();

            if(eventDescriptionText.length()>0 && eventTitleText.length()>0 && longitude!=0.0 & latitude!=0.0){
                Log.i(TAG, "ID Cognito => "+ userId);
                Amplify.API.query(
                        ModelQuery.list(Account.class),
                        users -> {
                            Log.i(TAG, "Users => "+ users.getData());
                            if(users.hasData()) {
                                for (Account user :
                                        users.getData()) {
                                    Log.i(TAG, "User add this Event" + user);
                                    if (user.getIdcognito().equals(userId)) {
                                        Event event;
                                        if(longitude!=null && latitude!=null){
                                            event = Event.builder()
                                                    .title(eventTitleText)
                                                    .eventdescription(eventDescriptionText)
                                                    .latitude(latitude)
                                                    .longitude(longitude)
                                                    .accountEventsaddedId(user.getId())
                                                    .build();
                                        }else {
                                            event = Event.builder()
                                                    .title(eventTitleText)
                                                    .eventdescription(eventDescriptionText)
                                                    .accountEventsaddedId(user.getId())
                                                    .build();
                                            // API save to backend

                                        }

                                        Amplify.API.mutate(
                                                ModelMutation.create(event),
                                                success -> {
                                                    Log.i(TAG, "Saved item API: " + success.getData());
                                                },
                                                error -> Log.e(TAG, "Could not save item to API", error)
                                        );
//                                    firebaseAction();
                                    }
                                }
                            }
                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

                Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();
                btnSubmit.setBackgroundColor(Color.RED);
            }else {
                if (!isFinishing()){
                    new AlertDialog.Builder(EventActivity.this)
                            .setTitle("Error")
                            .setMessage("you should add title and description and location ")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                    dialog.cancel();
                                }
                            }).show();
                }
            }

        });

        cancelAdd.setOnClickListener(view -> {
           startActivity(new Intent(this, MainActivity.class));
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            enableLocation();
        }

        Intent locationIntent=getIntent();
        longitude= locationIntent.getDoubleExtra("longitude",0.0);
        latitude= locationIntent.getDoubleExtra("latitude",0.0);

        Log.i(TAG, "onCreate: long   "+longitude);
        Log.i(TAG, "onCreate: lat   "+latitude);
    }


    public void firebaseAction()
    {

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId = logedInUser.getUserId();

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // send it to API
                        Amplify.API.query(
                                ModelQuery.list(Account.class),
                                allUsers -> {
                                    for (Account userAc:
                                            allUsers.getData()) {
                                        if(userAc.getIdcognito().equals(cognitoId)){
                                            acc_id = userAc.getId();
                                            Log.i(TAG, "ayahh: " + acc_id);

                                            Notification notification = Notification.builder()
                                                    .tokenid(token)
                                                    .accountid(acc_id)
                                                    .build();
                                            Amplify.API.query(
                                                    ModelQuery.list(Notification.class),
                                                    notify -> {
                                                        for (Notification noti:
                                                                notify.getData()) {
                                                            if(!noti.getAccountid().equals(acc_id)){
                                                                Amplify.API.mutate(
                                                                        ModelMutation.create(notification),
                                                                        success -> {
                                                                            Log.i(TAG, "Saved item API: " + success.getData());
                                                                        },
                                                                        error -> Log.e(TAG, "Could not save item to API", error)
                                                                );

                                                            }

                                                        }

                                                    },
                                                    error -> Log.e(TAG, error.toString(), error)
                                            );
                                        }
                                    }
                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );

//                        id: ID!tokenid: Stringaccountid:


                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TOKEN", token);
                        Log.i(TAG, "TOKEN: " + token);
                        Toast.makeText(EventActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        FcnNotificationSender notificationSender = new FcnNotificationSender("Hello", "your Toy is sold", getApplicationContext(), EventActivity.this,"dz3rZETJS6evPSjWTLynSU:APA91bHg3EPti8H_CiKGlR7p9ETJcvoK8yXJKEWDj_Idimn73TxKhTcu6_O2SqPhwFv8f8qpbsGPi2xVd66hiyvz_Z7jOOAWKNa-lr1h0PV9Oi9DNlSSmXQhcKk5qMgk1iLbF9FQxZYz");
        notificationSender.SendNotifications();
    }

}