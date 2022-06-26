package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.ToyActivity;
import com.example.toys_exchange.fragmenrs.ToyFragment;

public class MainActivity2 extends AppCompatActivity {

    LinearLayout llHome;



    FragmentTransaction fragmentTransaction;
    private ToyFragment toyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);
        llHome = findViewById(R.id.llHome);

        toyFragment = new ToyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,toyFragment).commit();

        TextView tvAccount = findViewById(R.id.tvAccount);
        tvAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, profileActivity.class);
            startActivity(intent);
        });

        llHome.setOnClickListener(view -> {
            toyFragment = new ToyFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, toyFragment).commit();

        });


        TextView addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(view -> {
           Intent intent =  new Intent(getApplicationContext(), EventActivity.class);
           startActivity(intent);
        });

        TextView addToy = findViewById(R.id.addToy);
        addToy.setOnClickListener(view -> {
           Intent intent = new Intent(this, ToyActivity.class);
           startActivity(intent);
        });
    }
}