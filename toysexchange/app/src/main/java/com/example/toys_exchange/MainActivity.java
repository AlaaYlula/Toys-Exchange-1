package com.example.toys_exchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.Firebase.FcnNotificationSender;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.EventAttendList;
import com.example.toys_exchange.UI.EventDetailsActivity;
import com.example.toys_exchange.UI.StoreAddActivity;
import com.example.toys_exchange.UI.StoreListActivity;
import com.example.toys_exchange.UI.ToyActivity;

import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.UI.eventListActivity;
import com.example.toys_exchange.UI.toyListActivity;
import com.example.toys_exchange.adapter.TabAdapter;
import com.example.toys_exchange.fragmenrs.EventFragment;
import com.example.toys_exchange.fragmenrs.StoreFragment;
import com.example.toys_exchange.fragmenrs.ToyFragment;
import com.example.toys_exchange.fragmenrs.WishListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.FirebaseApp;



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

            Intent startAllTasksIntent = new Intent(getApplicationContext(), WishListActivity.class);
            startActivity(startAllTasksIntent);

        }
    };

    private String username;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    LinearLayout llHome;
    LinearLayout llEvent;
    LinearLayout llWish;
    LinearLayout llRecommendation;

    ImageView ivHome;
    ImageView ivEventList;
    ImageView ivWishList;
    ImageView ivRecommendation;

    private String acc_id;
    private ToyFragment toyFragment;
    private EventFragment eventFragment;
    private WishListFragment wishListFragment;
    private StoreFragment storeFragment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);



        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // send it to API

                        //


                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TOKEN", token);
                        Log.i(TAG, "TOKEN: " + token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        FcnNotificationSender notificationSender = new FcnNotificationSender("Hello", "your Toy is sold", getApplicationContext(), MainActivity.this,"dz3rZETJS6evPSjWTLynSU:APA91bHg3EPti8H_CiKGlR7p9ETJcvoK8yXJKEWDj_Idimn73TxKhTcu6_O2SqPhwFv8f8qpbsGPi2xVd66hiyvz_Z7jOOAWKNa-lr1h0PV9Oi9DNlSSmXQhcKk5qMgk1iLbF9FQxZYz");
        notificationSender.SendNotifications();


//
//        // https://droidbyme.medium.com/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff
//        viewPager = findViewById(R.id.viewPager);
//        tabLayout = findViewById(R.id.tabLayout);
//        adapter = new TabAdapter(getSupportFragmentManager());
//        adapter.addFragment(new EventFragment(), "Event");
//        adapter.addFragment(new ToyFragment(), "Toy");
//        adapter.addFragment(new StoreFragment(), "store");
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//
//        Button addStore = findViewById(R.id.addStore);
//        addStore.setOnClickListener(view ->{
//            startActivity(new Intent(this, StoreListActivity.class));
//        });
//
//       // Button btnDetailEvent = findViewById(R.id.DetailEvent);
//
////        btnDetailEvent.setOnClickListener(view -> {
////            startActivity(new Intent(this, WishListActivity.class));
////        });
//
//       // Button btnDetailToy = findViewById(R.id.detailToy);
//
////        btnDetailToy.setOnClickListener(view -> {
////            startActivity(new Intent(this, MainActivity2.class));
////        });
        getLoginUserId();

        ivHome = findViewById(R.id.ivHome);
        ivEventList = findViewById(R.id.ivEventList);
        ivWishList = findViewById(R.id.ivWishList);
        ivRecommendation = findViewById(R.id.ivRecommendation);

        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId =  logedInUser.getUserId();


        enable(ivHome);
        toyFragment = new ToyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,toyFragment).commit();

        llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(view -> {
            enable(ivHome);
            toyFragment = new ToyFragment();
            changeFragment(toyFragment);
        });

        llEvent = findViewById(R.id.llEvent);
        llEvent.setOnClickListener(view -> {
            enable(ivEventList);
            eventFragment = new EventFragment();
            changeFragment(eventFragment);
        });

        llWish = findViewById(R.id.llWish);
        llWish.setOnClickListener(view -> {
            enable(ivWishList);
            wishListFragment = new WishListFragment();
            changeFragment(wishListFragment);
        });

        // add for store
        llRecommendation = findViewById(R.id.llRecommendation);
        llRecommendation.setOnClickListener(view -> {
            enable(ivRecommendation);
            storeFragment = new StoreFragment();
            changeFragment(storeFragment);
        });


        TextView tvAccount = findViewById(R.id.tvAccount);
        tvAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, profileActivity.class);
            startActivity(intent);
        });

        TextView eventAttended = findViewById(R.id.attended_event);
        eventAttended.setOnClickListener(view -> {
            Intent intent = new Intent(this, EventAttendList.class);
            intent.putExtra("loginUserId",acc_id);
            intent.putExtra("cognitoId",cognitoId);
            startActivity(intent);
        });

        TextView myEvent = findViewById(R.id.my_event);
        myEvent.setOnClickListener(view -> {
            Intent intent = new Intent(this, eventListActivity.class);
            intent.putExtra("userId", acc_id);
            intent.putExtra("cognitoId",cognitoId);
            startActivity(intent);
        });
        TextView myToys = findViewById(R.id.my_toys);
        myToys.setOnClickListener(view -> {
            Intent intent = new Intent(this, toyListActivity.class);
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

        TextView addStore = findViewById(R.id.addStore);
        addStore.setOnClickListener(view -> {
            Intent intent = new Intent(this, StoreAddActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId =  sharedPreferences.getString(LoginActivity.USERNAME, "No Team setting");
        Log.i(TAG, "SharedPreferences => " + userId);


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
            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
            sharedPref.edit().remove("userId");
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void enable(ImageView imageView){
        disableAll();
        imageView.setBackground(getDrawable(R.drawable.shophop_bg_circle_primary_light));
        imageView.setColorFilter(getColor(R.color.ShopHop_colorPrimary));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void disableAll(){
        disable(ivHome);
        disable(ivEventList);
        disable(ivWishList);
        disable(ivRecommendation);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void disable(ImageView imageView){
        imageView.setColorFilter(getColor(R.color.ShopHop_textColorSecondary));
        imageView.setBackground(null);
    }


    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}

