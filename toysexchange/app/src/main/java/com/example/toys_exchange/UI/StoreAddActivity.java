package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;

public class StoreAddActivity extends AppCompatActivity {
    private static final String TAG = StoreAddActivity.class.getSimpleName() ;

    Button addStore;
    String cognitoId;

    Button addLocation;


    Double longitude;
    Double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add);
       // getSupportActionBar().setTitle("Add Store");



        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();

        addStore = findViewById(R.id.btn_addStore);
        addLocation=findViewById(R.id.btn_add_location);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),MapActivity.class);
                intent.putExtra("type","store");
                startActivity(intent);
            }
        });

        Log.i(TAG, "onCreate: long   "+longitude);
        Log.i(TAG, "onCreate: lat   "+latitude);

        addBtnListener(); // Listeners
    }

    private void addBtnListener() {

        addStore.setOnClickListener(view -> {

            EditText storeDescription = findViewById(R.id.description_store);
            EditText storeTitle = findViewById(R.id.title_store);

            String storeDescriptionText = storeDescription.getText().toString();
            String storeTitleText = storeTitle.getText().toString();

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
        });

    }

    @Override
    protected void onResume() {
        Intent locationIntent=getIntent();
        longitude= locationIntent.getDoubleExtra("longitude",0.0);
        latitude= locationIntent.getDoubleExtra("latitude",0.0);

        Log.i(TAG, "onCreate: long   "+longitude);
        Log.i(TAG, "onCreate: lat   "+latitude);
        super.onResume();
    }
}