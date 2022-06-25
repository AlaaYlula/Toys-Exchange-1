package com.example.toys_exchange.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.squareup.picasso.Picasso;

public class ToyDetailActivity extends AppCompatActivity {

    private static final String TAG = ToyDetailActivity.class.getSimpleName();
    private TextView toyName;
    private TextView toyDescription;
    private TextView toyUser;
    private TextView toyCondition;

    private ImageView addToWishList;
    private ImageView toyImage;

    private Handler handler;
    private Handler handler1;
    private Handler handler2;

    private String userId;
    private String imageKey;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_detail);


        toyUser=findViewById(R.id.txt_view_user_name);
        toyName=findViewById(R.id.txt_view_name);
        toyDescription=findViewById(R.id.txt_view_description);
        toyCondition=findViewById(R.id.txt_view_condition);

        addToWishList=findViewById(R.id.image_view_fav);
        toyImage=findViewById(R.id.image_view_toy);


        handler=new Handler(Looper.getMainLooper(), msg->{
            toyName.setText(msg.getData().get("name").toString());
            userId=msg.getData().get("id").toString();
            toyDescription.setText(msg.getData().get("desc").toString());
            toyCondition.setText(msg.getData().get("cond").toString());
            Log.i(TAG, "onCreate: -------------------------->" + msg.getData().get("url"));


//            imageKey=msg.getData().get("image").toString();
//            Log.i(TAG, "onCreate: ------------------------------->"+ imageKey);
            return true;
        });

        handler1=new Handler(Looper.getMainLooper(), msg->{
            toyUser.setText(msg.getData().get("username").toString());
            return true;
        });

        handler2=new Handler(Looper.getMainLooper(), msg->{
            Log.i(TAG, " handler 2: -------------------------->" + msg.getData().get("url"));
            Picasso.get().load(msg.getData().get("url").toString()).into(toyImage);
            return true;
        });

        getToys();
        getUserName();


        addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  addToWishList.setBackgroundColor(Color.RED);
                addToWishList.setColorFilter(getResources().getColor(R.color.purple_500));
            }
        });

    }


    public void getToys(){
        Amplify.API.query(
                ModelQuery.list(Toy.class,Toy.ID.eq("1455dcd3-d2f0-4044-93d1-88b41b762185")),
                toys -> {
                    Log.i(TAG, "getToys: ************************"+toys.getData());
                    for (Toy toy :
                            toys.getData()) {
                        Bundle bundle=new Bundle();
                        bundle.putString("name",toy.getToyname());
                        bundle.putString("cond",toy.getCondition().toString());
                        bundle.putString("id",toy.getAccountToysId());
                        bundle.putString("desc",toy.getToydescription());
                        bundle.putString("image",toy.getImage());

                        Message message = new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);


                        Amplify.Storage.getUrl(
                           toy.getImage(),
                           result -> {
                              Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                              Bundle bundle1=new Bundle();
                              bundle1.putString("url",result.getUrl().toString());

                              Message message1=new Message();
                              message1.setData(bundle1);
                              handler2.sendMessage(message1);

                              },
                           error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                           );


                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }

    public void getUserName(){
        Amplify.API.query(
                ModelQuery.list(Account.class),
                accounts -> {
                    Log.i(TAG, "getUserName: -----------------------------------<>"+accounts.getData());
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
}