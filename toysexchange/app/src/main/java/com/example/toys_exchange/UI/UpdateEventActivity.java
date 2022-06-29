package com.example.toys_exchange.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateEventActivity extends AppCompatActivity {

    private static final String TAG = UpdateEventActivity.class.getSimpleName();

    private Handler handler;

    public String acc_id;
    List<Account> acclist = new ArrayList<>();
    public String userId;

    public EditText eventTitle;
    public EditText eventBody;

    public String title;
    public String body;
    public String eventId;

    Button updateForm;
    Button cnacelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        eventTitle = findViewById(R.id.title_event_update);
        eventBody = findViewById(R.id.description_event_update);
        updateForm = findViewById(R.id.btn_update_Comment);

        getEventByUser();

        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            return true;
        });

        cnacelBtn = findViewById(R.id.btn_cancelUpdateComment);
        cnacelBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), eventListActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getEventByUser() {

        Intent intent = getIntent();
        title = intent.getStringExtra("eventTitle");
        body = intent.getStringExtra("description");
        eventId = intent.getStringExtra("eventID");

        eventTitle.setText(title);
        eventBody.setText(body);


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
                                    acc_id = acc.getId().toString();
                                    Log.i(TAG, "InGetEventsList: " + acc_id);


                                    updateForm.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//                                Event eventOne = Event.builder().title("aya").eventdescription("good").build();
                                                    Amplify.API.query(ModelQuery.list(Event.class, Event.ID.eq(eventId)),
                                                            events -> {
                                                                if(events.hasData()){
                                                                    for(Event event : events.getData()){
                                                                        if(event.getId().equals(eventId)){
                                                                            Event eventOne = Event.builder().title(eventTitle.getText().toString())
                                                                                    .eventdescription(eventBody.getText().toString())
                                                                                    .id(eventId)
                                                                                    .accountEventsaddedId(acc_id)
                                                                                    .longitude(1.0).latitude(2.0).build();

                                                                            Amplify.API.mutate(ModelMutation.update(eventOne),
                                                                                    response -> {
                                                                                        runOnUiThread(()->{
                                                                                            Toast.makeText(UpdateEventActivity.this, "Updated", Toast.LENGTH_SHORT).show();
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

                                            });
                                }
                            }
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });
}
}