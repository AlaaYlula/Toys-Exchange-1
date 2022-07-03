package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.StoreDeleteAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoreListActivity extends AppCompatActivity {

    private static final String TAG = StoreListActivity.class.getSimpleName();
    private String cognitoId;
    private String loginUserId;

    List<Store> storeList = new ArrayList<>();
    private Handler handler;

    StoreDeleteAdapter storeDeleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        handler = new Handler(Looper.getMainLooper(), msg -> {
            setUserStore();
            return true;
        });

    }

    @Override
    protected void onResume() {
        storeList = new ArrayList<>();
        getUserStore();
        super.onResume();
    }

    private void setUserStore() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        storeDeleteAdapter = new StoreDeleteAdapter(storeList, new StoreDeleteAdapter.CustomClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                Amplify.API.mutate(ModelMutation.delete(storeList.get(position)),
                        response ->{
                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                            Log.i(TAG, "store deleted " + response);
                            storeList.remove(position);
                            storeDeleteAdapter.notifyItemRemoved(position);
                        },
                        error -> Log.e(TAG, "delete failed", error)
                );
            }
        });

        // set adapter on recycler view
        recyclerView.setAdapter(storeDeleteAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void getUserStore() {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();
        Amplify.API.query(
                ModelQuery.list(Account.class),
                allUsers -> {
                    for (Account userAc:
                            allUsers.getData()) {
                        if(userAc.getIdcognito().equals(cognitoId)){
                            loginUserId = userAc.getId();
                        }
                    }
                    runOnUiThread(()->{
                        Amplify.API.query(
                                ModelQuery.list(Store.class),
                                stores -> {
                                    if(stores.hasData()) {
                                        for (Store store :
                                                stores.getData()) {
                                            Log.i(TAG, "store not  added " + store.getStorename());
                                            if (store.getAccountStoresId().equals(loginUserId)) {
                                                Log.i(TAG, "store added " + store.getStorename());
                                                storeList.add(store);
                                            }
                                        }
                                        // Sort the Created At
                                        Collections.sort(storeList,new SortByDate());
                                    }
                                    handler.sendMessage(new Message());

                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );
                    });

                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
    // Class to sort the comments by date
    // https://www.delftstack.com/howto/java/how-to-sort-objects-in-arraylist-by-date-in-java/
    static class SortByDate implements Comparator<Store> {
        @Override
        public int compare(Store a, Store b) {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

    private void getLoginUserId() {
        Amplify.API.query(
                ModelQuery.list(Account.class),
                allUsers -> {
                    for (Account userAc:
                            allUsers.getData()) {
                        if(userAc.getIdcognito().equals(cognitoId)){
                            loginUserId = userAc.getId();
                        }
                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }
}