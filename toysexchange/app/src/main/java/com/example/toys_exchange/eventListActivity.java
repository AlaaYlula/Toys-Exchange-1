package com.example.toys_exchange;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Todo;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.data.model.EventDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class eventListActivity extends AppCompatActivity {
    private static final String TAG = eventListActivity.class.getSimpleName();
    List<Event> eventList = new ArrayList<>();
    private Handler handler;

    Event event;
    Account user;
    String cognitoId;
    Account loginUser;

    private String userId = "id";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        authAttribute();
//        authSession("logout");
//        getUserCogID();

        getEventsList();
//        handler = new Handler(Looper.getMainLooper(), msg -> {
//            userId = msg.getData().getString("id");
////            getEventByUserId();
//        return true;
//        });

        getEventByUserId();
//        getEvents();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        getEventByUserId();
//        getEvents();

    }


    private void getEventByUserId()
    {
        handler = new Handler(Looper.getMainLooper(), msg -> {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
//            // create an Adapter // Custom Adapter
//            EventRecyclerViewAdapter eventRecyclerViewAdapter = new EventRecyclerViewAdapter(
//                    eventList, position -> {
//
//            });

            // create an Adapter // Custom Adapter
            EventRecyclerViewAdapter eventRecyclerViewAdapter = new EventRecyclerViewAdapter(
                    eventList, position -> {

                Toast.makeText(
                        eventListActivity.this,
                        "The Task clicked => " + eventList.get(position).getTitle(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("id", eventList.get(position).getId());
                intent.putExtra("Title", eventList.get(position).getTitle());
                intent.putExtra("Description", eventList.get(position).getEventdescription());
                startActivity(intent);

            });

            recyclerView.setAdapter(eventRecyclerViewAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            return true;
        });
    }
//

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getEventsList() {
        Amplify.API.query(
                ModelQuery.list(Event.class),
                events -> {
                    if(events.hasData()) {
                        for (Event event :
                                events.getData()) {
                            if(event.getAccountEventsaddedId().equals("")) // Add For comments check
                                eventList.add(event);
                        }
                        // Sort the Created At
//                        Collections.sort(eventList, new EventDetailsActivity.SortByDate());
                    }
//                    // Use To do Sync
//                    runOnUiThread(() -> {
                    // commentRecyclerViewAdapter.notifyDataSetChanged();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventsListUpdate", "eventsListUpdate");

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                    //   });
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }



//    private void getEvents() {
//
//        String[] eventId = new String[1];
//
////        String userEventId = Event.ACCOUNT_EVENTSADDED_ID;
////        Amplify.API.query(
////                ModelQuery.get(Event.class, userId),
////                response -> {
////                    response.getData()).getAccountEventsaddedId();
////                }
////                error -> Log.e("MyAmplifyApp", error.toString(), error)
////        );
//
//        Amplify.API.query(ModelQuery.list(Event.class, Event.ACCOUNT_EVENTSADDED_ID.eq(userId)),  //Event.ID.eq(eventId) Event.ACCOUNT_EVENTSADDED_ID.eq()   Event.ACCOUNT_EVENTSADDED_ID.eq(userId)
//                events -> {
//                    Log.i(TAG, "UserId: " + userId);
//                    for (Event event : events.getData()) {
//                        if(event.getAccountEventsaddedId().equals("da464963-9a6f-4ef8-a1d7-b2ab28f3a070")) {
////                        eventId[0] = event.getId();
////                        if(events.equals(event.getAccountEventsaddedId())){
//                            Log.i(TAG, "getAccountEventsaddedId: " + event.getAccountEventsaddedId());
//                            eventList.add(event);
//                        }
//
//                        Log.i(TAG, "IngetEvents: " + event.getTitle());
//                    }
//
//                    Log.i(TAG, "OutgetEvents: " + events.getData());
//
//                    runOnUiThread(() -> {
//                        handler.sendMessage(new Message());
//                    });
//
//                },
//                error -> Log.e(TAG, "getEvents: ",error)
//        );
//
//    }


    private void getEventByUser()
    {

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

    private void authSession(String method){
        Amplify.Auth.fetchAuthSession(result ->{
            Log.i(TAG, "Auth Session => " + method + result.toString()) ;

            AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

            switch(cognitoAuthSession.getIdentityId().getType()) {
                case SUCCESS:
                {
                    Log.i("AuthQuickStart", "IdentityId: " + cognitoAuthSession.getIdentityId().getValue());
                    userId = cognitoAuthSession.getIdentityId().getValue();
                    break;
                }

                case FAILURE:
                    Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
            }
        },error -> Log.e(TAG, error.toString()));
    }

    private void authAttribute() {
        Amplify.Auth.fetchUserAttributes(
                attribute -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", attribute.get(0).getValue());
                    bundle.putString("id", attribute.get(0).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> Log.e(TAG, "authAttribute: ", error)
        );
    }

}