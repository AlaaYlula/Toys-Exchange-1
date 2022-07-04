package com.example.toys_exchange.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.data.model.LoginActivity;

public class StoreAddActivity extends AppCompatActivity {
    private static final String TAG = StoreAddActivity.class.getSimpleName() ;

    Button addStore;
    Button cancelAdd;
    String cognitoId;

    TextView addLocation;

    EditText storeDescription;
    EditText storeTitle;


    Double longitude;
    Double latitude;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Store");


         storeDescription = findViewById(R.id.etDescription);
         storeTitle = findViewById(R.id.etTitle);

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();

        addStore = findViewById(R.id.btn_addStore);
        addLocation=findViewById(R.id.tvLocation);
        cancelAdd = findViewById(R.id.btnCancel);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MapActivity.class);
                intent.putExtra("type","store");
                intent.putExtra("title",storeTitle.getText().toString());
                intent.putExtra("desc",storeDescription.getText().toString());
                startActivity(intent);
            }
        });

        Log.i(TAG, "onCreate: long   "+longitude);
        Log.i(TAG, "onCreate: lat   "+latitude);

        addBtnListener(); // Listeners
    }

    private void addBtnListener() {

        addStore.setOnClickListener(view -> {



            String storeDescriptionText = storeDescription.getText().toString();
            String storeTitleText = storeTitle.getText().toString();

            if(storeDescriptionText.length()>0 && storeTitleText.length()>0 && longitude!=0.0 & latitude!=0.0){
                Log.i(TAG, "ID Cognito => "+ cognitoId);
                Amplify.API.query(
                        ModelQuery.list(Account.class),
                        users -> {
                            Log.i(TAG, "Users => "+ users.getData());
                            if(users.hasData()) {
                                for (Account user :
                                        users.getData()) {
                                    Log.i(TAG, "User add this Event" + user);
                                    if (user.getIdcognito().equals(cognitoId)) {

                                        Store store;
                                        if(longitude!=null && latitude!=null){
                                            store = Store.builder()
                                                    .storename(storeTitleText)
                                                    .storedescription(storeDescriptionText)
                                                    .accountStoresId(user.getId())
                                                    .latitude(latitude)
                                                    .longitude(longitude)
                                                    .build();
                                        }else {
                                            store = Store.builder()
                                                    .storename(storeTitleText)
                                                    .storedescription(storeDescriptionText)
                                                    .accountStoresId(user.getId())
                                                    .build();
                                        }

                                        // API save to backend
                                        Amplify.API.mutate(
                                                ModelMutation.create(store),
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

                Toast.makeText(getApplicationContext(), "Store Added", Toast.LENGTH_SHORT).show();
                addStore.setBackgroundColor(Color.RED);
            }else {
                if (!isFinishing()){
                    new AlertDialog.Builder(StoreAddActivity.this)
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
    protected void onResume() {
        Intent locationIntent=getIntent();
        longitude= locationIntent.getDoubleExtra("longitude",0.0);
        latitude= locationIntent.getDoubleExtra("latitude",0.0);
        storeTitle.setText(locationIntent.getStringExtra("title"));
        storeDescription.setText(locationIntent.getStringExtra("desc"));

        Log.i(TAG, "onCreate: long   "+longitude);
        Log.i(TAG, "onCreate: lat   "+latitude);
        super.onResume();
    }

}