package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.UserAttendEvent;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventAttendList extends AppCompatActivity {

     List<Event> eventList = new ArrayList<>();
     Handler handler;

     String loginUserIdFromProfile;
     String cognitoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attend_list);
        Intent passedIntent = getIntent();
        loginUserIdFromProfile = passedIntent.getStringExtra("loginUserId");
        cognitoId = passedIntent.getStringExtra("cognitoId");

        handler = new Handler(Looper.getMainLooper(), msg -> {
            setRecyclerView();
            return true;
        });



    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // create an Adapter // Custom Adapter
        CustomEventAdapter customEventAdapter = new CustomEventAdapter(
                eventList, position -> {
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            intent.putExtra("eventTitle",eventList.get(position).getTitle());
            intent.putExtra("description",eventList.get(position).getEventdescription());
            intent.putExtra("userID",eventList.get(position).getAccountEventsaddedId());
            intent.putExtra("eventID",eventList.get(position).getId());
            intent.putExtra("cognitoID",cognitoId);
            intent.putExtra("loginUserID",loginUserIdFromProfile);
            startActivity(intent);
        });
        // set adapter on recycler view
        recyclerView.setAdapter(customEventAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        eventList = new ArrayList<>();
        Amplify.API.query(ModelQuery.list(UserAttendEvent.class), success ->{

                    if(success.hasData()) {
                        for (UserAttendEvent userAttendEvent : success.getData()) {

                            if(userAttendEvent.getAccount().getId().equals(loginUserIdFromProfile)){
                                eventList.add(userAttendEvent.getEvent());
                            }
                        }

                    }
                    Bundle bundle =new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },error -> Log.e("error: ","-> ",error)
        );
        super.onResume();
    }
}