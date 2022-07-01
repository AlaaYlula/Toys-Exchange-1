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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.example.toys_exchange.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateCommentActivity extends AppCompatActivity {



    private static final String TAG = UpdateCommentActivity.class.getSimpleName();

    private Handler handler;

    public String acc_id;
    List<Account> acclist = new ArrayList<>();
    public String userId;

    Button updateForm;
    Button cnacelBtn;

    public EditText commentText;
    public String text;
    public String commentId;
    public String eventCommentId;
    public String accCommentId;
    private String eventIdFromMain;
    private String titleText;
    private String descriptionText;
    private String cognitoIdFromMain;
    private String loginUserIdFromMain;
    private String userIdAddedEventFromMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_comment);

        commentText = findViewById(R.id.text_comment_update);
        updateForm =findViewById(R.id.btn_update_Comment);

        updateComment();

        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            return true;
        });

        cnacelBtn = findViewById(R.id.btn_cancelUpdateComment);
        cnacelBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            intent.putExtra("eventTitle",titleText);
            intent.putExtra("description",descriptionText);
            intent.putExtra("userID",userIdAddedEventFromMain);
            intent.putExtra("eventID",eventIdFromMain);
            intent.putExtra("cognitoID",cognitoIdFromMain);
            intent.putExtra("loginUserID",loginUserIdFromMain);

            startActivity(intent);
        });


    }


    public void updateComment()
    {
        Intent intent = getIntent();
        text = intent.getStringExtra("commentText");
        commentId = intent.getStringExtra("commentId");
        accCommentId = intent.getStringExtra("accountsCommentId");
        eventCommentId = intent.getStringExtra("eventCommentsId");

        titleText  = intent.getStringExtra("eventTitle");
        descriptionText  = intent.getStringExtra("description");
        eventIdFromMain = intent.getStringExtra("eventID");
        cognitoIdFromMain = intent.getStringExtra("cognitoID");
        loginUserIdFromMain = intent.getStringExtra("loginUserID");
        userIdAddedEventFromMain = intent.getStringExtra("userID");

        commentText.setText(text);

        updateForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Comment newComment = Comment.builder().text(commentText.getText().toString())
                                .id(commentId).accountCommentsId(accCommentId)
                                .eventCommentsId(eventCommentId).build();
                        Log.i(TAG, "comment id: " + commentId);
                        Log.i(TAG, "comment id: " + text);
                        Amplify.API.mutate(ModelMutation.update(newComment),
                                response -> {
                                    runOnUiThread(() -> {
                                        Log.i(TAG, "comment id: " + response.getData().getText());
                                        Toast.makeText(UpdateCommentActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                    });
                                    // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                    Log.i(TAG, "Event updated " + response);
                                },
                                error -> Log.e(TAG, "update failed", error)
                        );
                    }
                });
    }

}