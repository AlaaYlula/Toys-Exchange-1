package com.example.toys_exchange.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.UserWishList;
import com.example.toys_exchange.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ToyDetailActivity extends AppCompatActivity {

    private static final String TAG = ToyDetailActivity.class.getSimpleName();
    private TextView toyName;
    private TextView toyDescription;
    private TextView toyUser;
    private TextView toyCondition;
    private TextView contactMe;
    private TextView toyPrice;

    private ImageView addToWishList;
    private ImageView toyImage;



    private Handler handler;
    private Handler handler1;
    private Handler handler2;


    private String userId;
    private String toyId;

    private String loggedAccountId;
    private String idCognito;

    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_product_detail);


        CollapsingToolbarLayout toolbar = findViewById(R.id.toolbar_layout);
        toyUser=findViewById(R.id.txt_view_user_name);


//        toyName=findViewById(R.id.txt_view_name);
//        toyDescription=findViewById(R.id.txt_view_description);
//        toyCondition=findViewById(R.id.txt_view_condition);
//        contactMe=findViewById(R.id.txt_view_contact);
        toyPrice=findViewById(R.id.tvPrice);

        addToWishList=findViewById(R.id.image_view_fav);
        toyImage=findViewById(R.id.image_view_toy);

        Intent toyIntent=getIntent();
        String name=toyIntent.getStringExtra("toyName");
        String description=toyIntent.getStringExtra("description");
        Double price=toyIntent.getDoubleExtra("price",0.0);
        String condition=toyIntent.getStringExtra("condition");
        String image=toyIntent.getStringExtra("image");
        String contactInfo=toyIntent.getStringExtra("contactInfo");
        userId =toyIntent.getStringExtra("id");
        toyId =toyIntent.getStringExtra("toyId");

        toolbar.setTitle(toyIntent.getStringExtra("toyName"));

//        toyName.setText(name);
//        toyDescription.setText(description);
//        toyCondition.setText(condition);
//        contactMe.setText(contactInfo);
        toyPrice.setText(String.valueOf(price) + "JD");


//        Amplify.Storage.getUrl(
//                image,
//                result -> {
//                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
//                    runOnUiThread(()->{
//                        Picasso.get().load(result.getUrl().toString()).into(toyImage);
//                    });
//                },
//                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
//        );


//        handler1=new Handler(Looper.getMainLooper(), msg->{
//         //   Log.i(TAG, "onCreate: --------------------->"+msg.getData().get("username").toString());
//            toyUser.setText(msg.getData().get("username").toString());
//            return true;
//        });
//
//        handler=new Handler(Looper.getMainLooper(), msg->{
//              //Log.i(TAG, "onCreate: --------------------->"+msg.getData().get("idCognito").toString());
//             // Log.i(TAG, "onCreate: --------------------->"+msg.getData().get("loggedUser").toString());
//              loggedAccountId=msg.getData().get("loggedUser").toString();
//              idCognito=msg.getData().get("idCognito").toString();
//            return true;
//        });


//        getUserName();
//        getLoggedInAccount();
//        identify();


//        addToWishList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  addToWishList.setBackgroundColor(Color.RED);
//
//
//                if(count==0){
//                    addToWish();
//                    addToWishList.setColorFilter(getResources().getColor(R.color.purple_500));
//                    count++;
//                    Log.i(TAG, "onClick: in addd "+count);
//                }else if(count==1){
//                    removeFromWishList();
//                    addToWishList.setColorFilter(getResources().getColor(R.color.black));
//                    count--;
//                    Log.i(TAG, "onClick: in removed "+count);
//                }
//
//                Log.i(TAG, "onClick: in out "+count);
//
//            }
//        });

    }

    public void getUserName(){
        Amplify.API.query(
                ModelQuery.list(Account.class),
                accounts -> {
                  //  Log.i(TAG, "getUserName: -----------------------------------<>"+accounts.getData());
                    for (Account user :
                            accounts.getData()) {
                        if (user.getId().equals(userId)) {
                            Bundle bundle=new Bundle();
                            bundle.putString("username",user.getUsername());

                            Message message = new Message();
                            message.setData(bundle);

                            handler1.sendMessage(message);
                        }
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    public void addToWish(){
        Amplify.API.query(
                ModelQuery.list(Toy.class,Toy.ID.eq(toyId)),
                toys -> {
                 //   Log.i(TAG, "getToys addToWish: ************************"+toys.getData());
                    for (Toy toy :
                            toys.getData()) {
                              Amplify.Auth.fetchUserAttributes(
                                 attributes -> {
                                   Log.i(TAG, "User attributes = addToWish " + attributes.get(0).getValue());
                                   String id=attributes.get(0).getValue();
                                       Amplify.API.query(
                                            ModelQuery.list(Account.class),
                                                       accounts -> {
                                                              for (Account user :
                                                                  accounts.getData()) {
                                                          if (user.getIdcognito().equals(id)) {
                                                              runOnUiThread(()->{
                                                                  UserWishList wishList=UserWishList.builder().toy(toy)
                                                                          .account(user).build();
//
//                                                                  Amplify.DataStore.save(wishList,
//                                                                          success -> Log.i(TAG, "Saved item DataStore addToWish: " + success),
//                                                                          error -> Log.e(TAG, "Could not save item to DataStore addToWish", error)
//                                                                  );
                                                                  // API save to backend
                                                                  Amplify.API.mutate(
                                                                          ModelMutation.create(wishList),
                                                                          success -> {
                                                                              Log.i(TAG, "Saved item API: addToWish " + success.getData());
                                                                          },
                                                                          error -> Log.e(TAG, "Could not save item to API addToWish", error)
                                                                  );
                                                              });


                        }

                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );

    },
    error -> Log.e(TAG, "Failed to fetch user attributes.", error)
            );


                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    public void removeFromWishList(){
        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {
//                    Log.i(TAG, "getToys addToWish: ************************"+wishList.getData());
                    for (UserWishList wishToy :
                            wishList.getData()) {
                        Amplify.Auth.fetchUserAttributes(
                                attributes -> {
                                    Log.i(TAG, "User attributes = addToWish " + attributes.get(0).getValue());
                                    String id=attributes.get(0).getValue();

                                    Amplify.API.query(
                                            ModelQuery.list(Account.class),
                                            accounts -> {
                                                if(Objects.equals(toyId, wishToy.getToy().getId())){
                                                    Log.i(TAG, "removeFromWishList: ***********************"+wishToy.getAccount().getId());
//                                                    Amplify.DataStore.delete(wishToy,
//                                                            deleted -> Log.i(TAG, "UserAttendEvent deleted from Datastore " ),
//                                                            error -> Log.e(TAG, "delete failed", error));

                                                    Amplify.API.mutate(ModelMutation.delete(wishToy),
                                                            response -> Log.i("MyAmplifyApp", "Todo with id: " + response.getData().getId()),
                                                            error -> Log.e("MyAmplifyApp", "Create failed", error)
                                                    );
                                                }
                                            },
                                            error -> Log.e(TAG, error.toString(), error)
                                    );

                                },
                                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
                        );


                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }

    public void identify(){
        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {
                    Log.i(TAG, "identify: id-----------------------------------> " + loggedAccountId);
                    if(wishList.hasData()){
                        for (UserWishList wishToy :
                                wishList.getData()) {
                            if(wishToy.getAccount().getId().equals(loggedAccountId) && wishToy.getToy().getId().equals(toyId)){
                                    addToWishList.setColorFilter(getResources().getColor(R.color.purple_500));
                                    count=1;
                                    Log.i(TAG, "identify: in fav "+count);

                            }

                        }
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );


    }


    public void getLoggedInAccount(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.get(0).getValue());

                    Amplify.API.query(
                            ModelQuery.list(Account.class),
                            accounts -> {
                                //  Log.i(TAG, "getUserName: -----------------------------------<>"+accounts.getData());
                                for (Account user :
                                        accounts.getData()) {
                                    if (user.getIdcognito().equals(attributes.get(0).getValue())) {

                                        Amplify.API.query(
                                                ModelQuery.list(UserWishList.class),
                                                wishList -> {
                                                    Log.i(TAG, "identify: id-----------------------------------> " + loggedAccountId);
                                                    if(wishList.hasData()){
                                                        for (UserWishList wishToy :
                                                                wishList.getData()) {
                                                            if(wishToy.getAccount().getId().equals(user.getId()) && wishToy.getToy().getId().equals(toyId)){
                                                                addToWishList.setColorFilter(getResources().getColor(R.color.purple_500));
                                                                count=1;
                                                                Log.i(TAG, "identify: in fav "+count);

                                                            }

                                                        }
                                                    }
                                                },
                                                error -> Log.e(TAG, error.toString(), error)
                                        );
                                        Bundle bundle=new Bundle();

                                        bundle.putString("loggedUser",user.getId());
                                        bundle.putString("idCognito",  attributes.get(0).getValue());

                                        Message message = new Message();
                                        message.setData(bundle);

                                        handler.sendMessage(message);
                                    }
                                }
                            },
                            error -> Log.e(TAG, error.toString(), error)
                    );
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }


}