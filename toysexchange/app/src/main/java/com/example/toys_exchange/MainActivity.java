package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.data.model.EventDetailsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        Button addEventbtn = findViewById(R.id.addEvent);
//        addEventbtn.setOnClickListener(view->{
//            startActivity(new Intent(this, EventActivity.class));
//        });
//
//
//        Button detailEventbtn = findViewById(R.id.DetailEvent);
//        detailEventbtn.setOnClickListener(view->{
//            startActivity(new Intent(this, EventDetailsActivity.class));
//        });
    }
}