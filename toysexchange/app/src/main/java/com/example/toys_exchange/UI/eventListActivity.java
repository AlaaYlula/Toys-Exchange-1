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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class eventListActivity extends AppCompatActivity {


    private static final String TAG = eventListActivity.class.getSimpleName();
    List<Event> eventList = new ArrayList<>();
    private Handler handler;

    Event event;
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
        setContentView(R.layout.activity_event_list);


        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            getEventByUser();
        return true;
        });
        getEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getEvents()
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
                                ModelQuery.list(Event.class, Event.ACCOUNT_EVENTSADDED_ID.eq(acc_id)),
                                success -> {
                                    Log.i(TAG, "Saved item API: " + success.getData());
                                    runOnUiThread(() -> {
                                        for (Event events :
                                                success.getData()) {
                                            eventList.add(events);
//                                                acc_id = acc.getId().toString();
//                                                Log.i(TAG, "InEvent: " + acc.getId());
                                            Log.i(TAG, "InEvent: " + eventList);
                                        }
                                        runOnUiThread(() -> {
                                            handler.sendMessage(new Message());
                                        });
                                    });
                                },
                                error -> Log.e(TAG, "Could not save item to API", error)
                        );
                        Log.i(TAG, "Account Id" + acId[0]);

                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });

    }



    private void getEventByUser()
    {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
            // create an Adapter // Custom Adapter
        CustomEventAdapter customEventAdapter = new CustomEventAdapter(
                 eventList, position -> {
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            startActivity(intent);
            });
            // set adapter on recycler view
            recyclerView.setAdapter(customEventAdapter);
            // set other important properties
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void authAttribute() {
        Amplify.Auth.fetchUserAttributes(
                attribute -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", attribute.get(2).getValue());
                    bundle.putString("id", attribute.get(0).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(0).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(2).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(1).getValue());
                    Log.i(TAG, "userIdFun: " + attribute.get(3).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> Log.e(TAG, "authAttribute: ", error)
        );
    }

}