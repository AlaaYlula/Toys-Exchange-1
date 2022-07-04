package com.example.toys_exchange.UI;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Condition;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.Typetoy;
import com.example.toys_exchange.MainActivity;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.data.model.LoginActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class ToyActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;
    private static final String TAG = ToyActivity.class.getSimpleName();
    private String URL;

    private Spinner mSpinnerCondition;
    private Spinner mSpinnerType;
    private EditText toyPrice;
    private EditText toyDescription;
    private EditText toyName;
    private EditText contactInfo;

    Button uploadImage;
    Button addToy;
    Button cancelAdd;

    Handler handler;

    String userId;

    String[] conditions=new String[]{"NEW","USED"};
    String[] types=new String[]{ "SELL","REQUEST", "DONATION" };

    boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Toy");


        handler=new Handler(Looper.getMainLooper(),msg->{
            userId=msg.getData().get("id").toString();
            return true;
        });

         toyName=findViewById(R.id.etToyName);
         toyDescription=findViewById(R.id.etDescription);
         toyPrice=findViewById(R.id.etPrice);
         contactInfo=findViewById(R.id.etContactInfo);

         uploadImage=findViewById(R.id.btn_upload);
         addToy=findViewById(R.id.btn_add_toy);
        cancelAdd = findViewById(R.id.btnCancel);

        mSpinnerType=findViewById(R.id.spinner_type);


        setSpinner();
        authAttribute();

        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String type=mSpinnerType.getSelectedItem().toString();
               if(type.equals("REQUEST")){
                 //  uploadImage.setEnabled(false);
                   toyPrice.setEnabled(false);
               }
                if(type.equals("DONATION")){
                    toyPrice.setEnabled(false);
                   // uploadImage.setEnabled(true);
                }
                if(type.equals("SELL")){
                    toyPrice.setEnabled(true);
                    //uploadImage.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sharedImg();
        if(flag){
            uploadImage.setEnabled(false);
        }



        addToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=toyName.getText().toString();
                String description=toyDescription.getText().toString();
                String toyCondition=mSpinnerCondition.getSelectedItem().toString();
                String toyType=mSpinnerType.getSelectedItem().toString();
                String contact=contactInfo.getText().toString();

                String price=toyPrice.getText().toString();

//                if(toyType.equals("REQUEST")){
//                    toyPrice.setEnabled(false);
//                    uploadImage.setEnabled(false);
//                }

                saveToCloud(name,description,price,toyCondition,contact,toyType);


            }
        });

        uploadImage.setOnClickListener(view -> {
            pictureUpload();
        });

        cancelAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

    }

    public void setSpinner(){
        ArrayAdapter<String> conditionAdapter=new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                conditions);
        mSpinnerCondition=findViewById(R.id.spinner_condition);
        mSpinnerCondition.setAdapter(conditionAdapter);


        ArrayAdapter<String> typeAdapter=new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                types);

        mSpinnerType.setAdapter(typeAdapter);
    }

    public void saveToCloud(String name,String description ,String price ,String condition, String contact, String type){

        if(URL==null){
            URL="default 9afe5c16-d479-4598-b6d7-b9aed9737f32.jpg";
        }
        Amplify.API.query(
                ModelQuery.list(Account.class),
                users -> {
                    Log.i(TAG, "Users => "+ users.getData());
                    if(users.hasData()) {
                        for (Account user :
                                users.getData()) {
                            Log.i(TAG, "User add this Event" + user);
                            if (user.getIdcognito().equals(userId)) {
//                                if(Objects.equals(type, "SELL")){
                                    Toy oneToy=Toy.builder()
                                            .toyname(name)
                                            .toydescription(description)
                                            .image(URL)
                                            .condition(Enum.valueOf(Condition.class, condition))
                                            .accountToysId(user.getId())
                                            .contactinfo(contact)
                                            .price(Double.parseDouble(price))
                                            .typetoy(Enum.valueOf(Typetoy.class, type))
                                            .build();

                                    // API save to backend
                                    Amplify.API.mutate(
                                            ModelMutation.create(oneToy),
                                            success -> {
                                                runOnUiThread(()->{
                                                    Toast.makeText(ToyActivity.this, "Toy Added", Toast.LENGTH_SHORT).show();

                                                });

                                                Log.i(TAG, "Saved item API: " + success.getData());
                                            },
                                            error -> Log.e(TAG, "Could not save item to API", error)
                                    );
                                //}
//                                else if(Objects.equals(type, "DONATION")) {
//                                    Toy oneToy=Toy.builder()
//                                            .toyname(name)
//                                            .toydescription(description)
//                                            .image(URL)
//                                            .condition(Enum.valueOf(Condition.class, condition))
//                                            .accountToysId(user.getId())
//                                            .contactinfo(contact)
//                                            .price(Double.parseDouble(price))
//                                            .typetoy(Enum.valueOf(Typetoy.class, type))
//                                            .build();
//
////                                    Amplify.DataStore.save(oneToy,
////                                            success -> Log.i(TAG, "Saved item DataStore: " + success),
////                                            error -> Log.e(TAG, "Could not save item to DataStore", error)
////                                    );
//                                    // API save to backend
//                                    Amplify.API.mutate(
//                                            ModelMutation.create(oneToy),
//                                            success -> {
//                                                Log.i(TAG, "Saved item API: " + success.getData());
//                                            },
//                                            error -> Log.e(TAG, "Could not save item to API", error)
//                                    );
//                                }else if(Objects.equals(type, "REQUEST")){
//                                    Toy oneToy=Toy.builder()
//                                            .toyname(name)
//                                            .toydescription(description)
//                                            .image(URL)
//                                            .condition(Enum.valueOf(Condition.class, condition))
//                                            .accountToysId(user.getId())
//                                            .contactinfo(contact)
//                                            .price(Double.parseDouble(price))
//                                            .typetoy(Enum.valueOf(Typetoy.class, type))
//                                            .build();
//
////                                    Amplify.DataStore.save(oneToy,
////                                            success -> Log.i(TAG, "Saved item DataStore: " + success),
////                                            error -> Log.e(TAG, "Could not save item to DataStore", error)
////                                    );
//                                    // API save to backend
//                                    Amplify.API.mutate(
//                                            ModelMutation.create(oneToy),
//                                            success -> {
//                                                Log.i(TAG, "Saved item API: " + success.getData());
//                                            },
//                                            error -> Log.e(TAG, "Could not save item to API", error)
//                                    );
//                                }

                            }
                        }
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

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
        String name=toyName.getText().toString();
        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);

                    File file = new File(getApplicationContext().getFilesDir(), name+userId+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            name+userId+".jpg",
                            file,
                            result -> {
                                Log.i(TAG, "Successfully uploaded: " + result.getKey());
                                Toast.makeText(ToyActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();

                                URL=result.getKey();
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

    // Get Auth Attribute
    private void authAttribute(){
        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.get(0).getValue());
                    //  Send message to the handler to show the User name >>
                    Bundle bundle = new Bundle();
                    bundle.putString("id",  attributes.get(0).getValue());

                    Message message = new Message();
                    message.setData(bundle);

                    handler.sendMessage(message);
                },
                error -> {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Log.e(TAG, "Failed to fetch user attributes.", error);
                }
        );
    }


    public void sharedImg(){
        Intent intent=getIntent();

        Uri imgUri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(imgUri!=null){
            flag=true;;
        }

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    if(imgUri!=null) {
                        try {
                            Bitmap bitmap = getBitmapFromUri(imgUri);

                            Log.i(TAG, "sharedImg: id"+userId);

                            File file = new File(getApplicationContext().getFilesDir(), "imgUri"+attributes.get(0).getValue()+".jpg");
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.close();

                            // upload to s3
                            // uploads the file
                            Amplify.Storage.uploadFile(
                                    "imgUri1"+attributes.get(0).getValue()+".jpg",
                                    file,
                                    result -> {
                                        Log.i(TAG, "Successfully uploaded: " + result.getKey());
                                        URL=result.getKey();
                                    },
                                    storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                            );

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)

        );

        Log.i(TAG, "sharedImg: id"+userId);

    }
}




