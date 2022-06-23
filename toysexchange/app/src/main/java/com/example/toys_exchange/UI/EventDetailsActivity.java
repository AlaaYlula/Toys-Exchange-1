package com.example.toys_exchange.UI;

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
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.UserAttendEvent;
import com.example.toys_exchange.R;
import com.example.toys_exchange.adapter.adaptorComment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = EventDetailsActivity.class.getSimpleName();
    adaptorComment commentRecyclerViewAdapter;
    List<Comment> commentsListDatabase = new ArrayList<>();

    String cognitoId;

    TextView username;
    TextView title ;
    TextView description ;

    Event event;
    Account user;

    Account userWhoAttend;
    UserAttendEvent userAttendEvent;

    Account loginUser;

    Button addComment;
    Button btnAttend;

    Handler handler;
    Handler handler2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent passedIntent = getIntent();
        String id = passedIntent.getStringExtra("id");



        handler = new Handler(Looper.getMainLooper(), msg -> {
            if(commentsListDatabase.size()!=0)
                recyclerViewWork();

            return true;
        });
        handler2 = new Handler(Looper.getMainLooper(), msg -> {
            cognitoId =  msg.getData().getString("Id");
            return true;
        });
        // To get the cognito Id
        authAttribute();

        username = findViewById(R.id.username_event);
        title = findViewById(R.id.title_eventDetail);
        description = findViewById(R.id.description_eventDetail);
        // To set the Event Values
        setEventValues();


        // The Add Comment Button
        addComment = findViewById(R.id.btn_addCommentEvent);
        btnAttend = findViewById(R.id.btn_attendEvent);
        addBtnListner();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addBtnListner() {
        addComment.setOnClickListener(view->{
                EditText comment = findViewById(R.id.comment_text);
                String comment_text = comment.getText().toString();
                Comment commentAPI = Comment.builder()
                        .text(comment_text)
                        .accountCommentsId(loginUser.getId()) // User loginUser From function setEventValues
                        .eventCommentsId(event.getId())
                        .build();

                Amplify.DataStore.save(commentAPI,
                        success -> Log.i(TAG, "Saved item DataStore: " + success.item()),
                        error -> Log.e(TAG, "Could not save item to DataStore", error)
                );
                // API save to backend
                Amplify.API.mutate(
                        ModelMutation.create(commentAPI),
                        success -> {
                            Log.i(TAG, "Saved item API: " + success.getData());
                            runOnUiThread(() -> {
                                comment.setText("");
                                Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_SHORT).show();
                                setEventValues();

                            });
                        },
                        error -> Log.e(TAG, "Could not save item to API", error)
                );

            });

        btnAttend.setOnClickListener(view->{
            // If the User Not Attend the Event
            if(btnAttend.getText().equals("Attend")) {
                Amplify.Auth.fetchUserAttributes(
                        attributes -> {
                            Log.i(TAG, "Attributes => " + attributes.get(0).getValue());
                            runOnUiThread(() -> {
                                Amplify.API.query(
                                        ModelQuery.list(Account.class),
                                        users -> {
                                            for (Account userAccount :
                                                    users.getData()) {
                                                if (userAccount.getIdcognito().equals(attributes.get(0).getValue())) {
                                                    userWhoAttend = userAccount;
                                                    userAttendEvent = UserAttendEvent.builder()
                                                            .account(userAccount)
                                                            .event(event)
                                                            .build();
                                                }
                                            }

                                            runOnUiThread(() -> {
                                                // Use To do Sync
                                                Amplify.DataStore.save(event,
                                                        savedEvent -> {
                                                            Log.i(TAG, "Event saved.");
                                                            Amplify.DataStore.save(userWhoAttend,
                                                                    savedUserAttend -> {
                                                                        Log.i(TAG, "User saved.");
                                                                        Amplify.DataStore.save(userAttendEvent,
                                                                                saved -> Log.i(TAG, "userAttendEvent saved."),
                                                                                failure -> Log.e(TAG, "userAttendEvent not saved.", failure)
                                                                        );
                                                                    },
                                                                    failure -> Log.e(TAG, "userAttendEvent not saved.", failure)
                                                            );
                                                        },
                                                        failure -> Log.e(TAG, "Post not saved.", failure)
                                                );
                                                Amplify.API.mutate(
                                                        ModelMutation.create(userAttendEvent),
                                                        success -> {
                                                            Log.i(TAG, "Saved item API: " + success.getData());
                                                            runOnUiThread(() -> {
                                                                btnAttend.setText("Un Attend");
                                                                Toast.makeText(getApplicationContext(), "user Attend", Toast.LENGTH_SHORT).show();
                                                            });
                                                        },
                                                        error -> Log.e(TAG, "Could not save item to API", error)
                                                );


                                            });

                                        },
                                        error -> Log.e(TAG, error.toString(), error)
                                );
                            });
                        },
                        error -> Log.e(TAG, "Failed to fetch user attributes.", error)
                );
            }else{ // User want Un Attend // Delete From tables
                // https://docs.amplify.aws/lib/graphqlapi/mutate-data/q/platform/android/#run-a-mutation
                Amplify.API.query(ModelQuery.list(UserAttendEvent.class),
                        usersAttend -> {
                            for (UserAttendEvent user:
                                    usersAttend.getData()) {
                                if(user.getAccount().getId().equals(loginUser.getId())
                                        && user.getEvent().getId().equals("6c8bae84-0053-4309-9b1e-9ff23c2ef691")){
                                    runOnUiThread(() -> {
                                        Amplify.DataStore.query(UserAttendEvent.class ,
                                                Where.id(user.getId()), matches -> {
                                            if(matches.hasNext()){
                                                UserAttendEvent userAttend = matches.next();
                                                Amplify.DataStore.delete(userAttend,
                                                        deleted -> Log.i(TAG, "UserAttendEvent deleted from Datastore " ),
                                                        error -> Log.e(TAG, "delete failed", error));
                                            }
                                        },
                                                error -> Log.e(TAG, "delete failed", error)
                                        );

                                        Amplify.API.mutate(ModelMutation.delete(user),
                                                response ->{
                                                    Log.i(TAG, "UserAttendEvent deleted " + response.getData().getId());

                                                    runOnUiThread(() -> {
                                                        btnAttend.setText("Attend");

                                                    });

                                            },
                                                error -> Log.e(TAG, "delete failed", error)
                                        );
                                    });
                                    break;
                                }
                            }

                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

            }
        });

    }

    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
//                    //  Send message to the handler to get the user Id >>
                    Bundle bundle = new Bundle();
                    bundle.putString("Id",  attributes.get(0).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler2.sendMessage(message);
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }

    private void getUserAttend(){
        // This Not Work for Attend
        Amplify.API.query(ModelQuery.list(UserAttendEvent.class),
           usersAttend -> {
            for (UserAttendEvent user:
               usersAttend.getData()) {
                   if(user.getAccount().getId().equals(loginUser.getId())
                     && user.getEvent().getId().equals("6c8bae84-0053-4309-9b1e-9ff23c2ef691")){
                       runOnUiThread(() -> {
                           btnAttend.setText("Un Attend");

                       });

                          break;
                    }
            }

          },
        error -> Log.e(TAG, error.toString(), error)
        );
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setEventValues() {
        Amplify.API.query(
                ModelQuery.get(Event.class, "6c8bae84-0053-4309-9b1e-9ff23c2ef691"),

                events -> {
                    event = events.getData();
                    // Use To do Sync
                    runOnUiThread(() -> {
                        Amplify.API.query(
                                ModelQuery.get(Account.class, event.getAccountEventsaddedId()),
                                users -> {
                                    // User who add the Event
                                    user = users.getData();
                                    // Use To do Sync
                                    runOnUiThread(() -> {
                                        Amplify.API.query(
                                                ModelQuery.list(Account.class),
                                                allUsers -> {
                                                    for (Account userAc:
                                                         allUsers.getData()) {
                                                        if(userAc.getIdcognito().equals(cognitoId)){
                                                            loginUser = userAc;
                                                        }
                                                    }

                                                    runOnUiThread(() -> {
                                                        if(event.getAccountEventsaddedId().equals(loginUser.getId())){
                                                            btnAttend.setVisibility(View.INVISIBLE);
                                                        }

                                                        username.setText(user.getUsername());
                                                        title.setText(event.getTitle());
                                                        description.setText(event.getEventdescription());
                                                        commentsListDatabase = new ArrayList<>();
                                                        getCommentsList();
                                                        getUserAttend();

                                                    });
                                                            },
                                                error -> Log.e(TAG, error.toString(), error)
                                        );

                                    });
                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );

                    });
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
  //  @RequiresApi(api = Build.VERSION_CODES.N)
//    private void setEventValues() {
//        Amplify.API.query(
//                ModelQuery.get(Event.class, "b6f7e655-a712-4810-a2bd-16ee827e8d61"),
//                events -> {
//                    event = events.getData();
//                    // Use To do Sync
//                    runOnUiThread(() -> {
//                        Amplify.API.query(
//                                ModelQuery.get(Account.class, event.getAccountEventsaddedId()),
//                                users -> {
//                                    // User who add the Event
//                                    user = users.getData();
//                                    // Use To do Sync
//                                    runOnUiThread(() -> {
//                                        Amplify.API.query(
//                                                ModelQuery.list(Account.class),
//                                                allUsers -> {
//                                                    for (Account userAc:
//                                                            allUsers.getData()) {
//                                                        if(userAc.getIdcognito().equals(cognitoId)){
//                                                            loginUser = userAc;
//                                                        }
//                                                    }
//                                                    runOnUiThread(() -> {
//                                                        if(event.getAccountEventsaddedId().equals(loginUser.getId())){
//                                                            btnAttend.setVisibility(View.INVISIBLE);
//                                                        }
//
//                                                        username.setText(user.getUsername());
//                                                        title.setText(event.getTitle());
//                                                        description.setText(event.getEventdescription());
//                                                        commentsListDatabase = new ArrayList<>();
//                                                        getCommentsList();
//
//                                                        runOnUiThread(() -> { // For PUT the Attend Button text value
//                                                            Amplify.API.query(ModelQuery.list(UserAttendEvent.class),
//                                                                    usersAttend -> {
//                                                                        for (UserAttendEvent user:
//                                                                                usersAttend.getData()) {
//                                                                            if(user.getAccount().equals(loginUser.getId())){
//                                                                                btnAttend.setText("Un Attend");
//                                                                            }
//                                                                        }
//
//                                                                    },
//                                                                    error -> Log.e(TAG, error.toString(), error)
//                                                            );
//                                                        });
//                                                    });
//                                                },
//                                                error -> Log.e(TAG, error.toString(), error)
//                                        );
//
//                                    });
//                                },
//                                error -> Log.e(TAG, error.toString(), error)
//                        );
//
//                    });
//                },
//                error -> Log.e(TAG, error.toString(), error)
//        );
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCommentsList() {
        Amplify.API.query(
                ModelQuery.list(Comment.class),
                comments -> {
                    if(comments.hasData()) {
                        for (Comment comment :
                                comments.getData()) {
                            if(comment.getEventCommentsId().equals("6c8bae84-0053-4309-9b1e-9ff23c2ef691")) // Add For comments check
                                  commentsListDatabase.add(comment);
                        }
                        // Sort the Created At
                        Collections.sort(commentsListDatabase, new SortByDate());
                    }
//                    // Use To do Sync
//                    runOnUiThread(() -> {
                       // commentRecyclerViewAdapter.notifyDataSetChanged();
                        Bundle bundle = new Bundle();
                        bundle.putString("commentsListUpdate", "commentsListUpdate");

                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);
                 //   });
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
        // commentRecyclerViewAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_Comment);
        // create an Adapter // Custom Adapter
        commentRecyclerViewAdapter = new adaptorComment(
                commentsListDatabase, position -> {
        });
        // set adapter on recycler view
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: called - The App is VISIBLE");
        super.onResume();
    }

}