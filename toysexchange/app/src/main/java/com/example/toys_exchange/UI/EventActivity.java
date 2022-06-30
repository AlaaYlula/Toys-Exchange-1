package com.example.toys_exchange.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = EventActivity.class.getSimpleName() ;

    Button addEvent;
    Button cancelAdd;

    String userId;

    private Handler handler;

    private final int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        enableLocation();


        handler = new Handler(Looper.getMainLooper(), msg -> {
            // get the username
            String user = msg.getData().getString("name");
            //get the user Id
            userId =  msg.getData().getString("Id");
            return true;

        });
        authAttribute(); //get the username and userID

        addEvent = findViewById(R.id.btn_addEvent);
        cancelAdd = findViewById(R.id.btn_cancelAddEvent);

        addBtnListener(); // Listeners
        
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

        addEvent.setOnClickListener(view -> {

            EditText eventDescription = findViewById(R.id.description_event);
            EditText eventTitle = findViewById(R.id.title_event);

            String eventDescriptionText = eventDescription.getText().toString();
            String eventTitleText = eventTitle.getText().toString();

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
                                    Event event = Event.builder()
                                            .title(eventTitleText)
                                            .eventdescription(eventDescriptionText)
                                            .accountEventsaddedId(user.getId())
                                            .build();
                                    // API save to backend
                                    Amplify.API.mutate(
                                            ModelMutation.create(event),
                                            success -> {
                                                Log.i(TAG, "Saved item API: " + success.getData());
                                            },
                                            error -> Log.e(TAG, "Could not save item to API", error)
                                    );
                                }
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );

            Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();
            addEvent.setBackgroundColor(Color.RED);
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
    }
}