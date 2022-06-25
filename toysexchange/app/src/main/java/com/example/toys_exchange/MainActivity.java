package com.example.toys_exchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.UI.EventActivity;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://droidbyme.medium.com/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventFragment(), "Event");
        adapter.addFragment(new ToyFragment(), "Toy");
//        adapter.addFragment(new Tab3Fragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Button btnDetailEvent = findViewById(R.id.DetailEvent);







//        Button btnProfile = findViewById(R.id.btn_profilei);
//        mProfile = findViewById(R.id.btn_profilei);

//        btnProfile.setOnClickListener(mClickprofile);

//        Button button = findViewById(R.id.add_event);
//
//        button.setOnClickListener(view -> {

        btnDetailEvent.setOnClickListener(view -> {
            startActivity(new Intent(this, EventDetailsActivity.class));
        });

        Button btnDetailToy = findViewById(R.id.detailToy);

        btnDetailToy.setOnClickListener(view -> {
            startActivity(new Intent(this, ToyDetailActivity.class));
        });

        mAdd = findViewById(R.id.add_fab);
        mAddEvent = findViewById(R.id.add_event);
        mAddToy = findViewById(R.id.add_toy);



        mAdd.setOnClickListener(view -> {
            if(mAddToy.getVisibility() != View.VISIBLE){
                mAddToy.setVisibility(View.VISIBLE);
                mAddEvent.setVisibility(View.VISIBLE);
            }else {
                mAddToy.setVisibility(View.GONE);
                mAddEvent.setVisibility(View.GONE);
            }
        });


        mAddEvent.setOnClickListener(view -> {
            startActivity(new Intent(this, EventActivity.class));
        });
        mAddToy.setOnClickListener(view -> {
            startActivity(new Intent(this, ToyActivity.class));
        });

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
//                goToSetting();
                return true;
            case R.id.action_profile:
                Toast.makeText(this, "Copyrig ht 2022 ", Toast.LENGTH_SHORT).show();
//                goToProfile();
                startActivity(new Intent(this, profileActivity.class));

                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSetting(){}

    private void goToProfile(){
//        Intent profileIntent = new Intent(this,)
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




    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "Attributes => "+ attributes);
                    //  Send message to the handler to get the user Id >>
                    username =attributes.get(2).getValue();
                    userId = attributes.get(0).getValue();

                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }

}

