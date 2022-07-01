package com.example.toys_exchange.UI;


import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.UserAttendEvent;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.adaptorComment;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = EventDetailsActivity.class.getSimpleName();

    adaptorComment commentRecyclerViewAdapter;
    List<Comment> commentsListDatabase = new ArrayList<>();

//    TextView username;
//    TextView title ;
    TextView description ;
    CollapsingToolbarLayout title;

    Event event;

    Account userWhoAttend;
    UserAttendEvent userAttendEvent;


    Button addComment;
    Button btnAttend;
    Button deleteComment;
    Handler handler;
    Button updateBtn;

    Double longitude;
    Double latitude;

    String eventIdFromMain;
    String cognitoIdFromMain;
    String loginUserIdFromMain;
    String userIdAddedEventFromMain;
    String titEvent;

    Button updateForm;

    Button showLocation;

    Intent passedIntent;
    private String titleText;
    private String descriptionText;

    ConstraintLayout rlEditComment;
    ImageView ivEditComment;
    EditText etEditComment;


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_event_detail);
        getUserAttend();

        passedIntent = getIntent();
        eventIdFromMain = passedIntent.getStringExtra("eventID");
        cognitoIdFromMain = passedIntent.getStringExtra("cognitoID");
        loginUserIdFromMain = passedIntent.getStringExtra("loginUserID");
        userIdAddedEventFromMain = passedIntent.getStringExtra("userID");
        longitude=passedIntent.getDoubleExtra("longitude",0.0);
        latitude=passedIntent.getDoubleExtra("latitude",0.0);

        updateForm = findViewById(R.id.btn_updateComment);

//        titEvent = passedIntent.getStringExtra("title");




        handler = new Handler(Looper.getMainLooper(), msg -> {
            if(commentsListDatabase.size()!=0) {
                recyclerViewWork();

            }
            return true;
        });

//        username = findViewById(R.id.username_event);
//        title = findViewById(R.id.title_eventDetail);
//        titleText = passedIntent.getStringExtra("eventTitle");
//        title.setText(titleText);
//        description = findViewById(R.id.description_eventDetail);
//        descriptionText = passedIntent.getStringExtra("description");
//        description.setText(descriptionText);
//        username = findViewById(R.id.username_event);
//        title = findViewById(R.id.title_eventDetail);
//        title.setText(passedIntent.getStringExtra("eventTitle"));

        title = findViewById(R.id.toolbar_layout);
        title.setTitle(passedIntent.getStringExtra("eventTitle"));


        description = findViewById(R.id.tvEventDescription);
        description.setText(passedIntent.getStringExtra("description"));


//        updateBtn = findViewById(R.id.btn_update_EventDetails);
//
//        updateBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), UpdateEventActivity.class);
//            intent.putExtra("eventTitle",passedIntent.getStringExtra("eventTitle"));
//            intent.putExtra("description",passedIntent.getStringExtra("description"));
//            intent.putExtra("userID",passedIntent.getStringExtra("userID"));
//            intent.putExtra("eventID",passedIntent.getStringExtra("eventID"));
//            intent.putExtra("cognitoID",passedIntent.getStringExtra("cognitoID"));
//            intent.putExtra("loginUserID",passedIntent.getStringExtra("loginUserID"));
//            intent.putExtra("loginUserName",passedIntent.getStringExtra("loginUserName"));
//            startActivity(intent);
//        });




        setEventValues();

        // The Add Comment Button
        addComment = findViewById(R.id.btnComment);
//        btnAttend = findViewById(R.id.btn_attendEvent);
//        deleteComment = findViewById(R.id.btn_deleteComment);
        addBtnListner();
        showLocation=findViewById(R.id.bt_event_location);

        showLocation.setOnClickListener(view -> {

            Log.i(TAG, "onCreate:lan "+longitude);
            Log.i(TAG, "onCreate:lat "+latitude);
            if(latitude!=0 && longitude!=0){
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude));
                startActivity(intent);
            }else {
                Toast.makeText(this, "no location provide", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addBtnListner() {
        addComment.setOnClickListener(view->{
                EditText comment = findViewById(R.id.etComment);
                String comment_text = comment.getText().toString();
                Comment commentAPI = Comment.builder()
                        .text(comment_text)
                        .accountCommentsId(loginUserIdFromMain) // User loginUser From function setEventValues
                        .eventCommentsId(event.getId())
                        .build();

                // API save to backend
                Amplify.API.mutate(
                        ModelMutation.create(commentAPI),
                        success -> {
                            Log.i(TAG, "Saved item API: " + success.getData());
                            runOnUiThread(() -> {
                                comment.setText("");
                                Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_SHORT).show();
                                commentsListDatabase.add(commentAPI);
                                //commentRecyclerViewAdapter.notifyDataSetChanged();

                                Bundle bundle = new Bundle();
                                bundle.putString("commentsListUpdate", "commentsListUpdate");

                                Message message = new Message();
                                message.setData(bundle);

                                handler.sendMessage(message);

                            });
                        },
                        error -> Log.e(TAG, "Could not save item to API", error)
                );

            });

//        btnAttend.setOnClickListener(view->{
//            // If the User Not Attend the Event
//            if(btnAttend.getText().equals("Attend")) {
//                                Amplify.API.query(
//                                        ModelQuery.list(Account.class),
//                                        users -> {
//                                            for (Account userAccount :
//                                                    users.getData()) {
//                                                if (userAccount.getIdcognito().equals(cognitoIdFromMain)) {
//                                                    userWhoAttend = userAccount;
//                                                    userAttendEvent = UserAttendEvent.builder()
//                                                            .account(userAccount)
//                                                            .event(event)
//                                                            .build();
//                                                }
//                                            }
//
//                                            runOnUiThread(() -> {
//
//                                                Amplify.API.mutate(
//                                                        ModelMutation.create(userAttendEvent),
//                                                        success -> {
//                                                            Log.i(TAG, "Saved item API: " + success.getData());
//                                                            runOnUiThread(() -> {
//                                                                btnAttend.setText("Un Attend");
//                                                                Toast.makeText(getApplicationContext(), "user Attend", Toast.LENGTH_SHORT).show();
//                                                            });
//                                                        },
//                                                        error -> Log.e(TAG, "Could not save item to API", error)
//                                                );
//
//
//                                            });
//
//                                        },
//                                        error -> Log.e(TAG, error.toString(), error)
//                                );
//
//            }else{ // User want Un Attend // Delete From tables
//                // https://docs.amplify.aws/lib/graphqlapi/mutate-data/q/platform/android/#run-a-mutation
//                Amplify.API.query(ModelQuery.list(UserAttendEvent.class),
//                        usersAttend -> {
//                            for (UserAttendEvent user:
//                                    usersAttend.getData()) {
//                                if(user.getAccount().getId().equals(loginUserIdFromMain)
//                                        && user.getEvent().getId().equals(eventIdFromMain)){
//                                    runOnUiThread(() -> {
//                                        Amplify.API.mutate(ModelMutation.delete(user),
//                                                response ->{
//                                                    Log.i(TAG, "UserAttendEvent deleted " + response);
//
//                                                    runOnUiThread(() -> {
//                                                        btnAttend.setText("Attend");
//
//                                                    });
//
//                                            },
//                                                error -> Log.e(TAG, "delete failed", error)
//                                        );
//                                    });
//                                    break;
//                                }
//                            }
//
//                        },
//                        error -> Log.e(TAG, error.toString(), error)
//                );
//
//            }
//        });

    }

    private void getUserAttend(){
        Amplify.API.query(ModelQuery.list(UserAttendEvent.class),
           usersAttend -> {
            if(usersAttend.hasData()) {
                for (UserAttendEvent user :
                        usersAttend.getData()) {
                    if (user.getAccount().getId().equals(loginUserIdFromMain)
                            && user.getEvent().getId().equals(eventIdFromMain)) {
                        runOnUiThread(() -> {
                            btnAttend.setText("Un Attend");
                        });
                        break;
                    }
                }
            }

          },
        error -> Log.e(TAG, error.toString(), error)
        );
    }

    // Updated From the Main
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setEventValues() {
        Amplify.API.query(
                ModelQuery.get(Event.class, eventIdFromMain),

                events -> {
                    if(events.hasData()) {
                    event = events.getData();
                    // Use To do Sync
                    runOnUiThread(() -> {
                        Amplify.API.query(ModelQuery.get(Account.class,event.getAccountEventsaddedId()),
                                useradd -> {
                               runOnUiThread(()->{
                                   if(event.getAccountEventsaddedId().equals(loginUserIdFromMain)){
                                       btnAttend.setVisibility(View.INVISIBLE);
                                   }
//                                   username.setText(useradd.getData().getUsername());
                               });

                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );
                    });

                        event = events.getData();
                        // Use To do Sync
                        runOnUiThread(() -> {
                            Amplify.API.query(ModelQuery.get(Account.class, event.getAccountEventsaddedId()),
                                    useradd -> {
                                        if (useradd.hasData()) {
                                            runOnUiThread(() -> {
                                                if (event.getAccountEventsaddedId().equals(loginUserIdFromMain)) {
                                                    btnAttend.setVisibility(View.INVISIBLE);
                                                }
                                              //  username.setText(useradd.getData().getUsername());
                                            });
                                        }
                                    },
                                    error -> Log.e(TAG, error.toString(), error)
                            );
                        });
                    }

                                                },
           error -> Log.e(TAG, error.toString(), error)
                                        );

    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCommentsList() {
        commentsListDatabase = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Comment.class),
                comments -> {
                    if(comments.hasData()) {
                        for (Comment comment :
                                comments.getData()) {
                            if(comment.getEventCommentsId().equals(eventIdFromMain)) // Add For comments check
                                  commentsListDatabase.add(comment);
                        }
                        // Sort the Created At
                        Collections.sort(commentsListDatabase, new SortByDate());
                    }

//                    // Use To do Sync
                  //  runOnUiThread(() -> {
//                        commentsListDatabase.addAll(commentsListDatabase);
//                        commentRecyclerViewAdapter.notifyDataSetChanged();
                        Bundle bundle = new Bundle();
                        bundle.putString("commentsListUpdate", "commentsListUpdate");

                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);
                  //  });
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    // Class to sort the comments by date
    // https://www.delftstack.com/howto/java/how-to-sort-objects-in-arraylist-by-date-in-java/ 
    static class SortByDate implements Comparator<Comment> {
        @Override
        public int compare(Comment a, Comment b) {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }
    // Recycler View
    private void recyclerViewWork() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // create an Adapter // Custom Adapter
        commentRecyclerViewAdapter = new adaptorComment(commentsListDatabase, loginUserIdFromMain );

        // https://gist.github.com/codinginflow/7b9dd1c12ba015f2955bdd15b09b1cb1
        commentRecyclerViewAdapter.setOnItemClickListener(new adaptorComment.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

                Amplify.API.mutate(ModelMutation.delete(commentsListDatabase.get(position)),
                        response -> {
                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                            Log.i(TAG, "comment deleted " + response);
                            commentsListDatabase.remove(position);
                            commentRecyclerViewAdapter.notifyItemRemoved(position);
                        },
                        error -> Log.e(TAG, "delete failed", error)
                );
            }

            @Override
            public void onUpdateClick(int position,ConstraintLayout rlEditComment
                    ,    ImageView ivEditComment, EditText etEditComment) {
                rlEditComment.setVisibility(View.VISIBLE);
                ivEditComment.setOnClickListener(view -> {
                    Comment newComment = Comment.builder().text(etEditComment.getText().toString())
                            .id(commentsListDatabase.get(position).getId()).accountCommentsId(commentsListDatabase.get(position).getAccountCommentsId())
                            .eventCommentsId(commentsListDatabase.get(position).getEventCommentsId()).build();

                    Amplify.API.mutate(ModelMutation.update(newComment),
                            response -> {

                                runOnUiThread(() -> {
                                    Log.i(TAG, "comment id: " + response.getData().getText());
                                    rlEditComment.setVisibility(View.GONE);
                                    commentsListDatabase.remove(position);
                                    commentsListDatabase.add(position,newComment);
                                    commentRecyclerViewAdapter.notifyItemChanged(position);
                                });
                                // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                Log.i(TAG, "Event updated " + response);
                            },
                            error -> Log.e(TAG, "update failed", error)
                    );

                });

            }
        });

        // set adapter on recycler view
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override

    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");

        handler = new Handler(Looper.getMainLooper(), msg -> {
            if(commentsListDatabase.size()!=0) {
                recyclerViewWork();

            }
            return true;
        });

        //getUserAttend();
        getCommentsList();
       // setEventValues();
        super.onResume();
    }

}