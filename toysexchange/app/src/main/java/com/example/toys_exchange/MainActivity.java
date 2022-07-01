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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
    private String image;
    private String usernameDisplay;
    private String userBio;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);

//        // https://droidbyme.medium.com/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff

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
            intent.putExtra("username",usernameDisplay);
            intent.putExtra("bio",userBio);
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



        TextView tvSetting = findViewById(R.id.tvSetting);
        tvSetting.setOnClickListener(view -> {
            startActivity(new Intent(this,MainActivity2.class));
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
                            image = userAc.getImage();
                            usernameDisplay = userAc.getUsername();
                            userBio = userAc.getBio();
                        }
                    }
                    runOnUiThread(()->{
                        // For Set the Image
                        CircleImageView imageView = findViewById(R.id.civProfile);
                        getUrl(image,imageView);

                        TextView txtDisplayName = findViewById(R.id.txtDisplayName);
                        txtDisplayName.setText(usernameDisplay);
                    });

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

    private void getUrl(String image , CircleImageView imageView){
        Amplify.Storage.getUrl(
                image,
                result -> {
                    runOnUiThread(()->{
                        Picasso.get().load(result.getUrl().toString()).into(imageView);
                    });
                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );
    }
}

