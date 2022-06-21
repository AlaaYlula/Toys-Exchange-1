package com.example.toys_exchange.UI.data.model;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.Adaptors.adaptorComment;
import com.example.toys_exchange.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = EventDetailsActivity.class.getSimpleName();
    adaptorComment commentRecyclerViewAdapter;
    List<Comment> commentsListDatabase;

    TextView username;
    TextView title ;
    TextView description ;

    Event event;
    Account user;
    Button addComment;

    Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent passedIntent = getIntent();
        String id = passedIntent.getStringExtra("id");

        handler = new Handler(Looper.getMainLooper(), msg -> {
            recyclerViewWork();
            return true;
        });

        username = findViewById(R.id.username_event);
        title = findViewById(R.id.title_eventDetail);
        description = findViewById(R.id.description_eventDetail);

        // To set the Event Values
        setEventValues();

        // The Add Comment Button
        addComment = findViewById(R.id.btn_addCommentEvent);
        addComment.setOnClickListener(view->{
            EditText comment = findViewById(R.id.comment_text);
            String comment_text = comment.getText().toString();
            Comment commentAPI = Comment.builder()
                  .text(comment_text)
                  .accountCommentsId(user.getId())
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



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setEventValues() {
        Amplify.API.query(
                ModelQuery.get(Event.class, "ba1de473-b108-4a70-8e12-255e7d793afd"),
                events -> {
                    event = events.getData();
                    // Use To do Sync
                    runOnUiThread(() -> {
                        Amplify.API.query(
                                ModelQuery.get(Account.class, event.getAccountEventsaddedId()),
                                users -> {
                                    user = users.getData();
                                    // Use To do Sync
                                    runOnUiThread(() -> {

                                        username.setText(user.getUsername());
                                        title.setText(event.getTitle());
                                        description.setText(event.getEventdescription());
                                        commentsListDatabase = new ArrayList<>();
                                        getCommentsList();
                                    });
                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );

                    });
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCommentsList() {
        Amplify.API.query(
                ModelQuery.list(Comment.class),
                comments -> {
                    for(Comment comment:
                            comments.getData()){
                        commentsListDatabase.add(comment);
                    }
                    // Sort the Created At
                    Collections.sort(commentsListDatabase, new SortByDate());
                    // Use To do Sync
                    runOnUiThread(() -> {
                       // commentRecyclerViewAdapter.notifyDataSetChanged();
                        handler.sendMessage(new Message());
                    });
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
    // Get the List Of the Comments
    // getCommentsList();
    // Recycler View
//        // get the recycler view object
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_Comment);
//        // create an Adapter // Custom Adapter
//        commentRecyclerViewAdapter = new adaptorComment(
//                commentsListDatabase, position -> {
////            Toast.makeText(
////                    MainActivity.this,
////                    "The task clicked => " + commentsListDatabase.get(position).getTitle(), Toast.LENGTH_SHORT).show();
////
////            Intent intent = new Intent(getApplicationContext(), DetailTask.class);
////            //send the Id
////            intent.putExtra("id", commentsListDatabase.get(position).getId());
////            startActivity(intent);
//        });
//        // set adapter on recycler view
//        recyclerView.setAdapter(commentRecyclerViewAdapter);
//
//        // set other important properties
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
}