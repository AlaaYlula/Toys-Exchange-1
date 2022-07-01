package com.example.toys_exchange.UI;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

    String eventIdFromMain;
    String cognitoIdFromMain;
    String loginUserIdFromMain;
    String userIdAddedEventFromMain;
    String titEvent;

    Button updateForm;

    Intent passedIntent;
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
//        title.setText(passedIntent.getStringExtra("eventTitle"));

        title = findViewById(R.id.toolbar_layout);
        title.setTitle(passedIntent.getStringExtra("eventTitle"));


        description = findViewById(R.id.tvEventDescription);
        description.setText(passedIntent.getStringExtra("description"));

        setEventValues();

        // The Add Comment Button
        addComment = findViewById(R.id.btnComment);
//        btnAttend = findViewById(R.id.btn_attendEvent);
//        deleteComment = findViewById(R.id.btn_deleteComment);
        addBtnListner();

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
                    event = events.getData();
                    // Use To do Sync
                    runOnUiThread(() -> {
                        Amplify.API.query(ModelQuery.get(Account.class,event.getAccountEventsaddedId()),
                                useradd -> {
                               runOnUiThread(()->{
                                   if(event.getAccountEventsaddedId().equals(loginUserIdFromMain)){
//                                       btnAttend.setVisibility(View.INVISIBLE);
                                   }
//                                   username.setText(useradd.getData().getUsername());
                               });

                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );
                    });


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
            public void onUpdateClick(int position) {
                commentRecyclerViewAdapter.notifyItemRemoved(position);
//                Intent intent = new Intent(getApplicationContext(), UpdateCommentActivity.class);
//                intent.putExtra("commentText",commentsListDatabase.get(position).getText());
//                intent.putExtra("commentId",commentsListDatabase.get(position).getId());
//                intent.putExtra("accountsCommentId",commentsListDatabase.get(position).getAccountCommentsId());
//                intent.putExtra("eventCommentsId",commentsListDatabase.get(position).getEventCommentsId());
//                startActivity(intent);

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

//        title.setText(passedIntent.getStringExtra("eventTitle"));
//        description.setText(passedIntent.getStringExtra("description"));


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