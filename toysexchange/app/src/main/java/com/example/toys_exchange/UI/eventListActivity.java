package com.example.toys_exchange.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.EventRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class eventListActivity extends AppCompatActivity {
    private static final String TAG = eventListActivity.class.getSimpleName();
    List<Event> eventList = new ArrayList<>();
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        handler = new Handler(Looper.getMainLooper(), msg -> {
            getEventByUser();
        return true;
        });

        getEvents();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        getEventByUser();
//        getTasks();

    }

    private void getEventByUser()
    {
//        handler = new Handler(Looper.getMainLooper(), msg -> {
////            RecyclerView recyclerView = findViewById(R.id.recycler_view);
//
//            EventRecyclerViewAdapter eventRecyclerViewAdapter = new EventRecyclerViewAdapter(
//                    eventList, position -> {
//                Toast.makeText(
//                        eventListActivity.this,
//                        "The Task clicked => " + eventList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//
//
//                Intent intent = new Intent(getApplicationContext(), EventDetails.class);
//                intent.putExtra("Title", eventList.get(position).getTitle());
//                startActivity(intent);
//
//            });

        //        handler = new Handler(Looper.getMainLooper(), msg -> {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
            // create an Adapter // Custom Adapter
            EventRecyclerViewAdapter eventRecyclerViewAdapter = new EventRecyclerViewAdapter(
                 eventList, position -> {

            });
            // set adapter on recycler view
            recyclerView.setAdapter(eventRecyclerViewAdapter);
            // set other important properties
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getEvents() {

        Amplify.API.query(ModelQuery.list(Event.class),
                events -> {
                        for (Event event : events.getData()) {
                            eventList.add(event);
                            Log.i(TAG, "IngetEvents: " + event.getTitle());
                    }
                    Log.i(TAG, "OutgetEvents: " + events.getData());
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