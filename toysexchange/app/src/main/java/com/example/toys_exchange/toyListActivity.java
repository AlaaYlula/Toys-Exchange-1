package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.adapter.ToyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class toyListActivity extends AppCompatActivity {

    private static final String TAG = toyListActivity.class.getSimpleName();
    Handler handler;
    List<Toy> toysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_list);

        handler = new Handler(Looper.getMainLooper(), msg -> {
            getToyByUser();
            return true;
        });

        getToys();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        getEventByUser();
//        getTasks();

    }

    private void getToyByUser()
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // create an Adapter // Custom Adapter
        ToyRecyclerViewAdapter toyRecyclerViewAdapter = new ToyRecyclerViewAdapter(
                toysList, position -> {

        });
        // set adapter on recycler view
        recyclerView.setAdapter(toyRecyclerViewAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getToys() {

        Amplify.API.query(ModelQuery.list(Toy.class),
                toys -> {
                    for (Toy toy : toys.getData()) {
                        toysList.add(toy);
                        Log.i(TAG, "IngetEvents: " + toy.getToyname());
                    }
                    Log.i(TAG, "OutgetEvents: " + toys.getData());
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Event title", events.toString());
                    runOnUiThread(() -> {
                        handler.sendMessage(new Message());
                    });

                },
                error -> Log.e(TAG, "getEvents: ",error)
        );
    }

}