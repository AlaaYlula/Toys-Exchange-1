package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class eventListActivity extends AppCompatActivity {

    private View.OnClickListener mClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mBack.setText("back");
            mBack.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), profileActivity.class);
            startActivity(startAllTasksIntent);

        }
    };

    private TextView mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Button btnBack = findViewById(R.id.back_events_profile);
        mBack = findViewById(R.id.back_events_profile);

        btnBack.setOnClickListener(mClickBack);

    }
}