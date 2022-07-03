package com.example.toys_exchange;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Notification;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.FirebaseApp;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 123;
    private String URL;
    CircleImageView imageView;

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
    private Notification notification;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private String image;
    private String usernameDisplay;
    private String userBio;

    Dialog myDialog;
    CircleImageView imageUpdate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shophop_activity_dashboard_shop);

//        getLoginUserId();
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String cognitoId = logedInUser.getUserId();

//        // https://droidbyme.medium.com/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff

        getLoginUserId();

        ivHome = findViewById(R.id.ivHome);
        ivEventList = findViewById(R.id.ivEventList);
        ivWishList = findViewById(R.id.ivWishList);
        ivRecommendation = findViewById(R.id.ivRecommendation);

//        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
//        String cognitoId =  logedInUser.getUserId();


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



//        TextView tvSetting = findViewById(R.id.tvSetting);
//        tvSetting.setOnClickListener(view -> {
//            startActivity(new Intent(this,MainActivity2.class));
//        });
        myDialog = new Dialog(this);

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

    public void getLoginUserId() {
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
                         imageView = findViewById(R.id.civProfile);
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


    /////////////////////////////////////////////////////// Image ..............................
    private void pictureUpload() {
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }
        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String userIdForImageName =  sharedPreferences.getString(LoginActivity.USERNAME, "No User Id");

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);

                    File file = new File(getApplicationContext().getFilesDir(), username+userIdForImageName+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            username+userIdForImageName+".jpg",
                            file,
                            result -> {
                                Toast.makeText(MainActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Successfully uploaded: " + result.getKey());
                                URL=result.getKey();
                                runOnUiThread(()->{
                                    //getUrl(URL,imageView);
                                    getUrl(URL,imageUpdate);
                                });
                            },
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void ShowPopup(View v) {
        TextView txtclose;
        MaterialButton btn_update;
        myDialog.setContentView(R.layout.popup_update_window);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btn_update =  myDialog.findViewById(R.id.btn_update);
        //Set defualt values
        EditText textUsername;
        EditText textBio;

        textUsername = myDialog.findViewById(R.id.tvNameText);
        textBio = myDialog.findViewById(R.id.tvBioText);
        imageUpdate = myDialog.findViewById(R.id.editImage);

        setUserImageInpopUp(imageUpdate);
        textUsername.setText(usernameDisplay);
        textBio.setText(userBio);
        ////////
        imageUpdate.setOnClickListener(view->{
            pictureUpload();
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                getLoginUserId();
            }
        });
        btn_update.setOnClickListener(view->{


            String username = textUsername.getText().toString();
            String bio = textBio.getText().toString();
            String url = URL;

            updateUserBio(username,bio,url);

        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void updateUserBio(String username , String bio, String url) {
        Amplify.API.query(ModelQuery.get(Account.class,userId),
                user -> {
                    if(user.hasData()) {
                        runOnUiThread(()->{
                            Account userInfo;
                            if(URL!=null){
                                userInfo = Account.builder()
                                        .username(username)
                                        .idcognito(user.getData().getIdcognito())
                                        .image(url)
                                        .bio(bio)
                                        .id(user.getData().getId())
                                        .build();
                            }else{
                                userInfo = Account.builder()
                                        .username(username)
                                        .idcognito(user.getData().getIdcognito())
                                        .image(user.getData().getImage())
                                        .bio(bio)
                                        .id(user.getData().getId())
                                        .build();
                            }


                            Amplify.API.mutate(ModelMutation.update(userInfo),
                                    response -> {
                                        runOnUiThread(() -> {
                                            Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                        });
                                        // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                        Log.i(TAG, "Bio updated " + response);
                                    },
                                    error -> Log.e(TAG, "update failed", error)
                            );
                        });

                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    public void getUrl(String image,CircleImageView imageView){
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

    private void setUserImageInpopUp(CircleImageView imageView) {
        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.get(Account.class, userId),
                    accs -> {
                        if(accs.hasData()) {
                            getUrl(accs.getData().getImage(),imageView);
                        }
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });
    }
}

