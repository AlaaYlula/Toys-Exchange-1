package com.example.toys_exchange.UI;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
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
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Condition;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.Typetoy;
import com.amplifyframework.datastore.generated.model.UserWishList;
import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.adapter.CustomToyAdapter;
import com.example.toys_exchange.adapter.EventDeleteAdapter;
import com.example.toys_exchange.adapter.ToyDeleteAdapter;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class toyListActivity extends AppCompatActivity {

    private static final String TAG = toyListActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 123;

    List<Toy> toyList = new ArrayList<>();
    private Handler handler;
    Toy toyOne;

    String[] conditions=new String[]{"NEW","USED"};
    String[] types=new String[]{ "SELL","REQUEST", "DONATION" };
    private Spinner mSpinnerCondition;
    private Spinner mSpinnerType;

    Toy toy;
    Account user;
    String cognitoId;
    Account loginUser;

    public String acc_id;
    Account acc;
    List<Account> acclist = new ArrayList<>();
    public String userId;
    private String username;

    ToyDeleteAdapter toyDeleteAdapter;

    Dialog myDialog;
    String toyId;
    private String title;
    private String contact;
    private double price;
    private String body;
    private String imageURL;
    private String type;
    private String condition;

    private String URL;
    CircleImageView imageView;
    CircleImageView imageUpdate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy_list);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Toys List");

        handler = new Handler(Looper.getMainLooper(), msg -> {
            userId = msg.getData().getString("id");
            toyOptionsByUser();
            return true;
        });
        getToys();
        myDialog = new Dialog(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId =  sharedPreferences.getString(LoginActivity.USERNAME, "No Team setting");
        username =  sharedPreferences.getString(LoginActivity.NAMEUSERNAME, "No Team setting");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getToys() {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        String dima =  logedInUser.getUserId();
        final String[] acId = new String[1];
        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),
                    accs -> {
                        if(accs.hasData()) {
                            for (Account acc :
                                    accs.getData()) {

                                if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                    acclist.add(acc);
                                    acc_id = acc.getId().toString();
                                }
                            }
                        }
                        Amplify.API.query(
                                ModelQuery.list(Toy.class, Toy.ACCOUNT_TOYS_ID.eq(acc_id)),
                                success -> {
                                    runOnUiThread(() -> {
                                        for (Toy toys :
                                                success.getData()) {
                                            toyList.add(toys);
                                        }
                                        runOnUiThread(() -> {
                                            // Sort the Created At
                                            Collections.sort(toyList,new SortByDate());
                                            handler.sendMessage(new Message());
                                        });
                                    });
                                },
                                error -> Log.e(TAG, "Could not save item to API", error)
                        );

                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });

    }

    // Class to sort the comments by date
    // https://www.delftstack.com/howto/java/how-to-sort-objects-in-arraylist-by-date-in-java/
    static class SortByDate implements Comparator<Toy> {
        @Override
        public int compare(Toy a, Toy b) {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

    private void toyOptionsByUser() {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        toyDeleteAdapter = new ToyDeleteAdapter(toyList, new ToyDeleteAdapter.CustomClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                Amplify.API.query(
                        ModelQuery.list(UserWishList.class),
                        toys -> {
                            if(toys.hasData()) {
                                for (UserWishList toy :
                                        toys.getData()) {
                                    if(toy.getToy().getId().equals(toyList.get(position).getId()))                                     {
                                        Amplify.API.mutate(ModelMutation.delete(toy),
                                                response ->{
                                                    Log.i(TAG, "Toy wishList deleted " + response);
                                                },
                                                error -> Log.e(TAG, "toy failed", error)
                                        );
                                    }
                                }
                            }

                            runOnUiThread(()->{
                                Amplify.API.mutate(ModelMutation.delete(toyList.get(position)),
                                        response ->{
                                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                            Log.i(TAG, "Toy deleted " + response);
                                            toyList.remove(position);
                                            toyDeleteAdapter.notifyItemRemoved(position);
                                        },
                                        error -> Log.e(TAG, "delete failed", error)
                                );
                            });

                        },
                        error -> Log.e(TAG, error.toString(), error)
                );

            }

            @Override
            public void ontItemClickListener(int position) {
                Intent intent = new Intent(getApplicationContext(), ToyDetailActivity.class);
                intent.putExtra("toyName",toyList.get(position).getToyname());
                intent.putExtra("description",toyList.get(position).getToydescription());
                intent.putExtra("image",toyList.get(position).getImage());
                intent.putExtra("price",toyList.get(position).getPrice());
                intent.putExtra("condition",toyList.get(position).getCondition().toString());
                intent.putExtra("contactInfo",toyList.get(position).getContactinfo());
                intent.putExtra("id",toyList.get(position).getAccountToysId());
                intent.putExtra("toyId",toyList.get(position).getId());
                intent.putExtra("toyType",toyList.get(position).getTypetoy().toString());
                startActivity(intent);
            }

            @Override
            public void onUpdateClickListener(int position) {
//
                title = toyList.get(position).getToyname();
                body = toyList.get(position).getToydescription();
                contact = toyList.get(position).getContactinfo();
                price = toyList.get(position).getPrice();
                type = toyList.get(position).getTypetoy().toString();
                condition = toyList.get(position).getCondition().toString();
               // URL = toyList.get(position).getImage();

                toyId = toyList.get(position).getId();


                View layout = getLayoutInflater().inflate(R.layout.popup_update_toy, null);
                ShowPopupToy(layout,position,toyList.get(position).getImage());
            }
        });


        // set adapter on recycler view
        recyclerView.setAdapter(toyDeleteAdapter);
        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void ShowPopupToy(View v,int position,String urlBase) {
        TextView txtclose;
        MaterialButton btn_update;
        myDialog.setContentView(R.layout.popup_update_toy);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btn_update =  myDialog.findViewById(R.id.btn_update);
        //Set defualt values
        imageUpdate = myDialog.findViewById(R.id.editImage);
       // getUrl(URL,imageUpdate);
        setToyImageInpopUp(imageUpdate);

        EditText toyTitle;
        EditText toyDescription;
        EditText toyprice;
        EditText toyContact;

        toyTitle = myDialog.findViewById(R.id.etTitle);
        toyDescription = myDialog.findViewById(R.id.etDescription);
        toyprice = myDialog.findViewById(R.id.etPrice);
        toyContact = myDialog.findViewById(R.id.etContact);

        setSpinner(myDialog,type,condition);

        toyTitle.setText(title);
        toyDescription.setText(body);
        toyprice.setText(String.valueOf(price));
        toyContact.setText(contact);
         ////////

        imageUpdate.setOnClickListener(view->{
            pictureUpload();
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btn_update.setOnClickListener(view->{

            String title = toyTitle.getText().toString();
            String description = toyDescription.getText().toString();
            String price = toyprice.getText().toString();
            String contact = toyContact.getText().toString();
            String toyCondition=mSpinnerCondition.getSelectedItem().toString();
            String toyType=mSpinnerType.getSelectedItem().toString();
            String url = URL;
            updateToy(title,description,contact,price,toyCondition,toyType,url,position);
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void setSpinner(Dialog myDialog,String typeToy , String conditionToy){
        ArrayAdapter<String> conditionAdapter=new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                conditions);
        mSpinnerCondition=myDialog.findViewById(R.id.spinner_condition);
        mSpinnerCondition.setAdapter(conditionAdapter);
        selectSpinnerValue(mSpinnerCondition,conditionToy);

        ArrayAdapter<String> typeAdapter=new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                types);
        mSpinnerType=myDialog.findViewById(R.id.spinner_type);
        mSpinnerType.setAdapter(typeAdapter);
        selectSpinnerValue(mSpinnerType,typeToy);

        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type=mSpinnerType.getSelectedItem().toString();
                if(type.equals("REQUEST")){
                    //  uploadImage.setEnabled(false);
                   mSpinnerCondition.setVisibility(View.GONE);
                }
                if(type.equals("DONATION")){
                    mSpinnerCondition.setVisibility(View.GONE);
                    // uploadImage.setEnabled(true);
                }
                if(type.equals("SELL")){
                    mSpinnerCondition.setVisibility(View.VISIBLE);
                    //uploadImage.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void updateToy(String titleToy, String descriptionToy,String contactToy , String priceToy , String conditionToy,
                           String typeToy,String url , int position) {
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        runOnUiThread(() -> {
            Amplify.API.query(
                ModelQuery.list(Account.class, Account.IDCOGNITO.eq(logedInUser.getUserId())),

                allUsers -> {
                    if(allUsers.hasData()) {
                        for (Account acc :
                                allUsers.getData()) {

                            if (acc.getIdcognito().equals(logedInUser.getUserId())) { //
                                acclist.add(acc);
                                acc_id = acc.getId();
                                Amplify.API.query(
                                        ModelQuery.list(Toy.class),
                                        image -> {
                                            if(image.hasData()) {
                                                for (Toy img :
                                                        image.getData()) {
                                                    if (img.getId().equals(toyId)) { //  id for image =
                                                        Amplify.API.query(ModelQuery.list(Toy.class),
                                                                toys -> {
                                                                    if(toys.hasData()){
                                                                        for(Toy toy : toys.getData()){
                                                                            if(toy.getId().equals(toyId)){
                                                                            if(url!=null){
                                                                                toyOne = Toy.builder().toyname(titleToy)
                                                                                        .toydescription(descriptionToy)
                                                                                        .image(url)
                                                                                        .typetoy(Enum.valueOf(Typetoy.class, typeToy))
                                                                                        .price(Double.parseDouble(priceToy))
                                                                                        .condition(Enum.valueOf(Condition.class, conditionToy))
                                                                                        .contactinfo(contactToy)
                                                                                        .accountToysId(acc_id)
                                                                                        .id(toyId)
                                                                                        .build();
                                                                            }else{
                                                                                 toyOne = Toy.builder().toyname(titleToy)
                                                                                        .toydescription(descriptionToy)
                                                                                        .image(toy.getImage())
                                                                                        .typetoy(Enum.valueOf(Typetoy.class, typeToy))
                                                                                        .price(Double.parseDouble(priceToy))
                                                                                        .condition(Enum.valueOf(Condition.class, conditionToy))
                                                                                        .contactinfo(contactToy)
                                                                                        .accountToysId(acc_id)
                                                                                        .id(toyId)
                                                                                        .build();
                                                                            }

                                                                                Amplify.API.mutate(ModelMutation.update(toyOne),
                                                                                        response -> {
                                                                                            runOnUiThread(()->{
                                                                                                Toast.makeText(toyListActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                                                                    toyList.remove(position);
                                                                                                    toyList.add(position,toyOne);
                                                                                                    toyDeleteAdapter.notifyItemChanged(position);

                                                                                            });
                                                                                            // https://www.youtube.com/watch?v=LQmGU3UCOPQ
                                                                                            Log.i(TAG, "Toy updated " + response);
                                                                                        },
                                                                                        error -> Log.e(TAG, "update failed", error)
                                                                                );
                                                                            }
                                                                        }
                                                                    }

                                                                },
                                                                error -> Log.e(TAG, "onClick: "));

                                                    }
                                                }
                                            }

                                        },
                                        error -> Log.e(TAG, error.toString(), error)
                                );


                            }
                        }
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
        });

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

                    File file = new File(getApplicationContext().getFilesDir(), title+username+userIdForImageName+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            title+username+userIdForImageName+".jpg",
                            file,
                            result -> {
                                Toast.makeText(toyListActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Successfully uploaded: " + result.getKey());
                                URL=result.getKey();
                                runOnUiThread(()->{
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
    public void getUrl(String image, CircleImageView imageView){
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

    private void setToyImageInpopUp(CircleImageView imageView) {
        runOnUiThread(() -> {
            Amplify.API.query(
                    ModelQuery.get(Toy.class, toyId),
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