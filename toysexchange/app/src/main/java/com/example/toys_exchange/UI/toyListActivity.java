package com.example.toys_exchange.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomToyAdapter;
import com.example.toys_exchange.adapter.EventDeleteAdapter;
import com.example.toys_exchange.adapter.ToyDeleteAdapter;

import java.util.ArrayList;
import java.util.List;

public class toyListActivity extends AppCompatActivity {

    private static final String TAG = toyListActivity.class.getSimpleName();
    List<Toy> toyList = new ArrayList<>();
    private Handler handler;

    Toy toy;
    Account user;
    String cognitoId;
    Account loginUser;

    public String acc_id;
    Account acc;
    List<Account> acclist = new ArrayList<>();
    public String userId;

    ToyDeleteAdapter toyDeleteAdapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_list);


        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            deleteToyByUser();
            return true;
        });
        getToys();


//        Log.i(TAG, "toyList: " + toyList);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getToys()
    {

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

                                if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                    acclist.add(acc);
                                    acc_id = acc.getId().toString();
                                    Log.i(TAG, "InGetEventsList: " + acc.getId());
                                }
                            }
                        }
                        Amplify.API.query(
                                ModelQuery.list(Toy.class, Toy.ACCOUNT_TOYS_ID.eq(acc_id)),
                                success -> {
                                    Log.i(TAG, "getToys: " + acc_id);
                                    Log.i(TAG, "Saved item API: " + success.getData());
                                    runOnUiThread(() -> {
                                        for (Toy toys :
                                                success.getData()) {
                                            toyList.add(toys);
//                                                acc_id = acc.getId().toString();
//                                                Log.i(TAG, "InEvent: " + acc.getId());
//                                            Log.i(TAG, "InEvent: " + toyList);
                                        }
                                        runOnUiThread(() -> {
                                            handler.sendMessage(new Message());
                                        });
                                    });
                                },
                                error -> Log.e(TAG, "Could not save item to API", error)
                        );
//                        Log.i(TAG, "Account Id" + acId[0]);

                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });

    }



    private void deleteToyByUser() {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        toyDeleteAdapter = new ToyDeleteAdapter(toyList, new ToyDeleteAdapter.CustomClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                Amplify.API.query(
                        ModelQuery.list(Toy.class),
                        toys -> {
                            if(toys.hasData()) {
                                for (Toy toy :
                                        toys.getData()) {
                                    if(toy.getAccountToysId().equals(toyList.get(position).getId())) // Add For comments check
                                    {
                                        Amplify.API.mutate(ModelMutation.delete(toy),
                                                response ->{
                                                    Log.i(TAG, "Toy deleted " + response);
                                                },
                                                error -> Log.e(TAG, "toy failed", error)
                                        );
                                    }
                                }
                            }


                            runOnUiThread(()->{
                                Amplify.API.mutate(ModelMutation.delete(toyList.get(position)),
                                        response ->{
                                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                            Log.i(TAG, "Toy deleted " + response);
                                            toyList.remove(position);
                                            toyDeleteAdapter.notifyItemRemoved(position);
                                        },
                                        error -> Log.e(TAG, "delete failed", error)
                                );
                            });

                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

            }

            @Override
            public void ontItemClickListener(int position) {
                Intent intent = new Intent(getApplicationContext(), ToyDetailActivity.class);
                intent.putExtra("toyName",toyList.get(position).getToyname());
                intent.putExtra("description",toyList.get(position).getToydescription());
                intent.putExtra("image",toyList.get(position).getImage());
                intent.putExtra("price",toyList.get(position).getPrice());
                intent.putExtra("condition",toyList.get(position).getCondition().toString());
                intent.putExtra("contactInfo",toyList.get(position).getContactinfo());
                intent.putExtra("id",toyList.get(position).getAccountToysId());
                intent.putExtra("toyId",toyList.get(position).getId());
                intent.putExtra("toyType",toyList.get(position).getTypetoy().toString());
                startActivity(intent);
            }

            @Override
            public void onUpdateClickListener(int position) {
                Intent intent = new Intent(getApplicationContext(), UpdateToyActivity.class);
                intent.putExtra("toyName",toyList.get(position).getToyname());
                intent.putExtra("description",toyList.get(position).getToydescription());
                intent.putExtra("image",toyList.get(position).getImage());
                intent.putExtra("price",toyList.get(position).getPrice());
                intent.putExtra("condition",toyList.get(position).getCondition().toString());
                intent.putExtra("contactInfo",toyList.get(position).getContactinfo());
                intent.putExtra("id",toyList.get(position).getAccountToysId());
                intent.putExtra("toyId",toyList.get(position).getId());
                intent.putExtra("loginUserID",acc_id);
                intent.putExtra("toyType",toyList.get(position).getTypetoy().toString());
                startActivity(intent);
            }
        });


        // set adapter on recycler view
        recyclerView.setAdapter(toyDeleteAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}