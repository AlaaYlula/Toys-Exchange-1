package com.example.toys_exchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.EventAttendList;
import com.example.toys_exchange.UI.EventDetailsActivity;
import com.example.toys_exchange.UI.ToyActivity;
import com.example.toys_exchange.UI.ToyDetailActivity;

import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.adapter.TabAdapter;
import com.example.toys_exchange.fragmenrs.EventFragment;
import com.example.toys_exchange.fragmenrs.ToyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TOY_ID = "toyId";
    private FloatingActionButton mAdd;
    private TextView mProfile;
    private FloatingActionButton mAddEvent;
    private FloatingActionButton mAddToy;

    private List<Toy> toyList = new ArrayList<>();
    private Handler handler;
    private Handler userHandler;

    private String userId;



    private View.OnClickListener mClickprofile = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mProfile.setText("profile");
            mProfile.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), profileActivity.class);
            startActivity(startAllTasksIntent);

        }
    };

    private String username;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    LinearLayout llHome;
    private String acc_id;
    private ToyFragment toyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);

        getLoginUserId();

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId =  logedInUser.getUserId();

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Toast.makeText(this, "Setting ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                Toast.makeText(this, "Copyrig ht 2022 ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, profileActivity.class));

                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        Amplify.Auth.signOut(()->{
            Log.i(TAG, "Signed out successfully");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            authSession("logout");
            finish();
        },error -> Log.e(TAG, error.toString())
        );
    }

    private void authSession(String method){
        Amplify.Auth.fetchAuthSession(result ->{
            Log.i(TAG, "Auth Session => " + method + result.toString()) ;

            AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

            switch(cognitoAuthSession.getIdentityId().getType()) {
                case SUCCESS:
                {
                    Log.i("AuthQuickStart", "IdentityId: " + cognitoAuthSession.getIdentityId().getValue());
                    userId = cognitoAuthSession.getIdentityId().getValue();
                    break;
                }

                case FAILURE:
                    Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
            }
        },error -> Log.e(TAG, error.toString()));
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

