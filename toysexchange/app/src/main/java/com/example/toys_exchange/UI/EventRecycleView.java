package com.example.toys_exchange.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventRecycleView extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_recycle_view);


    }

    @Override
    protected void onResume() {

        eventList = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper(),msg ->{
            RecyclerView recyclerView = findViewById(R.id.recycler_view);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
            CustomEventAdapter customEventAdapter = new CustomEventAdapter(eventList, new CustomEventAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);

                    intent.putExtra("eventTitle", eventList.get(position).getTitle());

                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(customEventAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(gridLayoutManager);
            return true;
        });

        Amplify.API.query(ModelQuery.list(Event.class),
                success ->{
            for(Event event : success.getData()){
                eventList.add(event);
            }
            Bundle bundle = new Bundle();
            bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error ->{
                    Log.e("error: ","-> ",error);
                });


        super.onResume();
    }
}