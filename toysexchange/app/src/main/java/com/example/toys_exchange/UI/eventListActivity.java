package com.example.toys_exchange.UI;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.UserAttendEvent;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.CustomEventAdapter;
import com.example.toys_exchange.adapter.CustomToyAdapter;
import com.example.toys_exchange.adapter.EventDeleteAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class eventListActivity extends AppCompatActivity {


    private static final String TAG = eventListActivity.class.getSimpleName();
    List<Event> eventList = new ArrayList<>();
    private Handler handler;


    public String acc_id;
    Account acc;
    List<Account> acclist = new ArrayList<>();
    public String userId;
    Event eventOne;

    String cognitoId;
    private String loginUserId;
    private String loginUserName;

    EventDeleteAdapter customEventAdapter;

    Dialog myDialog;

    public String title;
    public String body;
    public String eventId;

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
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();
        getEvents();

        myDialog = new Dialog(this);

    }

    @Override
    protected void onResume() {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();

        super.onResume();
    }

    private void getEvents() {

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String dima =  logedInUser.getUserId();
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
                                            Log.i(TAG, "InEvent: " + eventList);
                                        }
                                        runOnUiThread(() -> {
                                            // Sort the Created At
                                            Collections.sort(eventList,new SortByDate());
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
    // Class to sort the comments by date
    // https://www.delftstack.com/howto/java/how-to-sort-objects-in-arraylist-by-date-in-java/
    static class SortByDate implements Comparator<Event> {
        @Override
        public int compare(Event a, Event b) {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

    private void getEventByUser() {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        customEventAdapter = new EventDeleteAdapter(eventList, new EventDeleteAdapter.CustomClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                Amplify.API.query(
                        ModelQuery.list(Comment.class),
                        comments -> {
                            if(comments.hasData()) {
                                for (Comment comment :
                                        comments.getData()) {
                                    if(comment.getEventCommentsId().equals(eventList.get(position).getId())) // Add For comments check
                                    {
                                        Amplify.API.mutate(ModelMutation.delete(comment),
                                                response ->{
                                                    Log.i(TAG, "Comment deleted " + response);
                                                },
                                                error -> Log.e(TAG, "delete failed", error)
                                        );
                                    }
                                }
                            }
                            runOnUiThread(()->{
                                Amplify.API.query(
                                        ModelQuery.list(UserAttendEvent.class),
                                        userAttendEvent -> {
                                            if(userAttendEvent.hasData()) {
                                                for (UserAttendEvent userAttendEvent1 :
                                                        userAttendEvent.getData()) {
                                                    if(userAttendEvent1.getEvent().getId().equals(eventList.get(position).getId())) // Add For comments check
                                                    {
                                                        Amplify.API.mutate(ModelMutation.delete(userAttendEvent1),
                                                                response ->{
                                                                    Log.i(TAG, "UserAttendEvent deleted " + response);
                                                                },
                                                                error -> Log.e(TAG, "delete failed", error)
                                                        );
                                                    }
                                                }
                                            }
                                            runOnUiThread(()->{
                                                Amplify.API.mutate(ModelMutation.delete(eventList.get(position)),
                                                        response ->{
                                                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                                            Log.i(TAG, "Event deleted " + response);
                                                            eventList.remove(position);
                                                            customEventAdapter.notifyItemRemoved(position);
                                                        },
                                                        error -> Log.e(TAG, "delete failed", error)
                                                );
                                            });

                                        },
                                        error -> Log.e(TAG, error.toString(), error)
                                );
//
                            });

                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

            }

            @Override
            public void ontItemClickListener(int position) {
                Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                intent.putExtra("eventTitle",eventList.get(position).getTitle());
                intent.putExtra("description",eventList.get(position).getEventdescription());
                intent.putExtra("userID",eventList.get(position).getAccountEventsaddedId());
                intent.putExtra("eventID",eventList.get(position).getId());
                intent.putExtra("cognitoID",cognitoId);
                intent.putExtra("loginUserID",acc_id);
                intent.putExtra("loginUserName",loginUserName);
                startActivity(intent);
            }

            @Override
            public void onUpdateClickListener(int position) {
                title = eventList.get(position).getTitle();
                body = eventList.get(position).getEventdescription();
                eventId = eventList.get(position).getId();
                View layout = getLayoutInflater().inflate(R.layout.popup_update_event, null);
                ShowPopup(layout,position);
            }

        });


        // set adapter on recycler view
        recyclerView.setAdapter(customEventAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getLoginUserId() {
        Amplify.API.query(
                ModelQuery.list(Account.class),
                allUsers -> {
                    for (Account userAc:
                            allUsers.getData()) {
                        if(userAc.getIdcognito().equals(cognitoId)){
                            loginUserId = userAc.getId();
                            loginUserName = userAc.getUsername();
                        }
                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }

    public void ShowPopup(View v,int position) {
        TextView txtclose;
        MaterialButton btn_update;
        myDialog.setContentView(R.layout.popup_update_event);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btn_update =  myDialog.findViewById(R.id.btn_update);
        //Set defualt values
        EditText eventTitle;
        EditText eventDescription;

        eventTitle = myDialog.findViewById(R.id.etTitle);
        eventDescription = myDialog.findViewById(R.id.etDescription);

        eventTitle.setText(title);
        eventDescription.setText(body);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();


            }
        });
        btn_update.setOnClickListener(view->{


            String title = eventTitle.getText().toString();
            String description = eventDescription.getText().toString();
            updateEvent(title,description,position);

        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void updateEvent(String title , String description,int position) {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();

        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),
                    allUsers -> {
                        if(allUsers.hasData()) {
                            for (Account acc :
                                    allUsers.getData()) {
                                if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                    acclist.add(acc);
                                    acc_id = acc.getId();
                                    Amplify.API.query(ModelQuery.list(Event.class, Event.ID.eq(eventId)),
                                            events -> {
                                                if(events.hasData()){
                                                    for(Event event : events.getData()){
                                                        if(event.getId().equals(eventId)){
                                                            eventOne = Event.builder().title(title)
                                                                    .eventdescription(description)
                                                                    .id(eventId)
                                                                    .accountEventsaddedId(acc_id)
                                                                    .longitude(event.getLongitude()).latitude(event.getLatitude()).build();

                                                            Amplify.API.mutate(ModelMutation.update(eventOne),
                                                                    response -> {
                                                                        runOnUiThread(()->{
                                                                            Toast.makeText(eventListActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                                                eventList.remove(position);
                                                                                eventList.add(position,eventOne);
                                                                                customEventAdapter.notifyItemChanged(position);

                                                                        });
                                                                        // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                                                        Log.i(TAG, "Event updated " + response);
                                                                    },
                                                                    error -> Log.e(TAG, "update failed", error)
                                                            );
                                                        }
                                                    }
                                                }

                                            },
                                            error -> Log.e(TAG, "onClick: "));
                                }
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });
          }


}



