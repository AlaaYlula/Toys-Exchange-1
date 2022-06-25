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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomToyAdapter;

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



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_list);


        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            getToyByUser();
            return true;
        });
        getToys();
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



    private void getToyByUser()
    {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // create an Adapter // Custom Adapter
        CustomToyAdapter customAdapter = new CustomToyAdapter(toyList, new CustomToyAdapter.CustomClickListener() {
            @Override
            public void onTaskClickListener(int position) {
                Intent intent = new Intent(getApplicationContext(), ToyDetailActivity.class);
                intent.putExtra("id", toyList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void ontItemClickListener(int position) {

            }
        });

        // set adapter on recycler view
        recyclerView.setAdapter(customAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}