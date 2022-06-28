package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.amplifyframework.datastore.generated.model.Condition;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.Typetoy;
import com.example.toys_exchange.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateToyActivity extends AppCompatActivity {


    private static final String TAG = UpdateToyActivity.class.getSimpleName();

    public String acc_id;
    List<Account> acclist = new ArrayList<>();
    public String userId;

    public Handler handler;

    public EditText toyName;
    public EditText toyBody;
    public EditText toyPrice;
    public EditText toyContactInfo;

    public String title;
    public String body;
    public double price;
    public String contactInfo;
    public String toyId;
    public String imageURL;

    Button updateForm;
    Button cnacelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_toy);

        toyName = findViewById(R.id.edit_txt_toy_name_update);
        toyBody = findViewById(R.id.edit_txt_toy_description_update);
        toyPrice = findViewById(R.id.edit_txt_toy_price_update);
        toyContactInfo = findViewById(R.id.edit_txt_contact_info_update);
        updateForm = findViewById(R.id.btn_update_Toy);

        updateToyByUser();

        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            return true;
        });

        cnacelBtn = findViewById(R.id.btn_cancelUpdateToy);
        cnacelBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), toyListActivity.class);
            startActivity(intent);
        });

    }


    private void updateToyByUser() {


        Intent intent = getIntent();
        title = intent.getStringExtra("toyName");
        body = intent.getStringExtra("description");
        price = intent.getDoubleExtra("price",0.0);
        contactInfo = intent.getStringExtra("contactInfo");
        toyId = intent.getStringExtra("toyId");

        toyName.setText(title);
        toyBody.setText(body);
        toyPrice.setText(String.valueOf(price));
        toyContactInfo.setText(contactInfo);

        Log.i(TAG, "title: " + title);
        Log.i(TAG, "body: " + body);

        Log.i(TAG, "acc : " + acc_id);

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();


        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),

                    allUsers -> {
                        if(allUsers.hasData()) {
                            for (Account acc :
                                    allUsers.getData()) {

                                if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                    acclist.add(acc);
                                    acc_id = acc.getId().toString();
                                    Log.i(TAG, "InGetEventsList: " + acc_id);


                                    Amplify.API.query(
                                            ModelQuery.list(Toy.class),
                                            image -> {

                                                if(image.hasData()) {
                                                    for (Toy img :
                                                            image.getData()) {

                                                        if (img.getId().equals(toyId)) { //  id for image =
                                                            imageURL = img.getImage();
                                                            Log.i(TAG, "image URL: " + imageURL);
//


                                    updateForm.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//                                Event eventOne = Event.builder().title("aya").eventdescription("good").build();
                                                    Amplify.API.query(ModelQuery.list(Toy.class),
                                                            toys -> {
                                                                if(toys.hasData()){
                                                                    for(Toy toy : toys.getData()){
                                                                        if(toy.getId().equals(toyId)){
                                                                            Log.i(TAG, "toyId: " + toyId);
                                                                            Log.i(TAG, "toyId: " + toy.getId());

                                                                            Toy toyOne = Toy.builder().toyname(toyName.getText().toString())
                                                                                    .toydescription(toyBody.getText().toString())
                                                                                    .image(imageURL)
                                                                                    .typetoy(Typetoy.SELL)
                                                                                    .price(Double.parseDouble(toyPrice.getText().toString()))
                                                                                    .condition(Condition.USED)
                                                                                    .contactinfo(toyContactInfo.getText().toString())
                                                                                    .accountToysId(acc_id)
                                                                                    .id(toyId)
                                                                                    .build();

                                                                            Amplify.API.mutate(ModelMutation.update(toyOne),
                                                                                    response -> {
                                                                                        runOnUiThread(()->{
                                                                                            Toast.makeText(UpdateToyActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                                                        });
                                                                                        // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                                                                        Log.i(TAG, "Toy updated " + response);
                                                                                    },
                                                                                    error -> Log.e(TAG, "update failed", error)
                                                                            );
                                                                        }
                                                                    }
                                                                }

                                                            },
                                                            error -> Log.e(TAG, "onClick: "));
                                                }

                                            });


                                                        }
                                                    }
                                                }

                                            },
                                            error -> Log.e(TAG, error.toString(), error)
                                    );


                                }
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });
    }
}





// Amplify.API.query(
//         ModelQuery.list(Toy.class, Toy.ACCOUNT_TOYS_ID.eq(logedInUser.getUserId())),
//
//        image -> {
//        if(image.hasData()) {
//        for (Toy img :
//        image.getData()) {
//
//        if (img.getId().equals(toyId)) { //  id for image =
//        imageURL = img.getImage();
//        Log.i(TAG, "image URL: " + imageURL);