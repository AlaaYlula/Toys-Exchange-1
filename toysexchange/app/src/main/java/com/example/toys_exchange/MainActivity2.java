package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.EventAttendList;
import com.example.toys_exchange.UI.ToyActivity;
import com.example.toys_exchange.fragmenrs.ToyFragment;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();
    LinearLayout llHome;
    private String acc_id;
    private ToyFragment toyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);
        llHome = findViewById(R.id.llHome);

        getLoginUserId();

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId =  logedInUser.getUserId();

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


        TextView eventAttended = findViewById(R.id.attended_event);
        eventAttended.setOnClickListener(view -> {
            Intent intent = new Intent(this, EventAttendList.class);
            intent.putExtra("loginUserId",acc_id);
            intent.putExtra("cognitoId",cognitoId);
            startActivity(intent);
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


    public  void getLoginUserId() {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId = logedInUser.getUserId();
        Amplify.API.query(
                ModelQuery.list(Account.class),
                allUsers -> {
                    for (Account userAc:
                            allUsers.getData()) {
                        if(userAc.getIdcognito().equals(cognitoId)){
                            acc_id = userAc.getId();
                        }
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
}