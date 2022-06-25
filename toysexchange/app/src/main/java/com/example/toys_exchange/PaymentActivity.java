package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private TextView toyName;
    private TextView toyCost;
    private Button btn;

    Toy toy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();

        toyName = findViewById(R.id.item_name);
        toyName.setText(toy.getToyname());

        toyCost = findViewById(R.id.item_cost);
        toyCost.setText(toy.getPrice().toString());


        btn = findViewById(R.id.buy_toy);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteToyFromAPI();
            }
        });
    }

    public void onClickRadioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        switch (view.getId()) {
            case R.id.creditCard:
                if(checked)
                    relativeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.cash:
                if(checked)
                    relativeLayout.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void deleteToyFromAPI() {
        Amplify.API.mutate(ModelMutation.delete(toy),
                response -> {
                Log.i(TAG, "toy deleted from API:");
                    Toast toast = Toast.makeText(getApplicationContext(), "the item deleted", Toast.LENGTH_SHORT);
                    toast.show();
                },
                error -> Log.e(TAG, "Delete failed", error)
        );
    }
}