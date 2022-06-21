package com.example.toys_exchange.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Condition;
import com.amplifyframework.datastore.generated.model.Todo;
import com.amplifyframework.datastore.generated.model.Toy;

import com.amplifyframework.predictions.aws.AWSPredictionsPlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.example.toys_exchange.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ToyActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;
    private static final String TAG = ToyActivity.class.getSimpleName();
    private String URL;

    private Spinner mSpinnerCondition;
    private EditText toyPrice;
    private EditText toyDescription;
    private EditText toyName;

    Button uploadImage;
    Button addToy;

    Handler handler;

    String userId;

    String[] conditions=new String[]{"NEW","USED","FREE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy);


        handler=new Handler(Looper.getMainLooper(),msg->{
            userId=msg.getData().get("id").toString();
            return true;
        });

         toyName=findViewById(R.id.edit_txt_toy_name);
         toyDescription=findViewById(R.id.edit_txt_toy_description);
         toyPrice=findViewById(R.id.edit_txt_toy_price);

         uploadImage=findViewById(R.id.btn_upload);
         addToy=findViewById(R.id.btn_add_toy);

        setSpinner();
        authAttribute();


        addToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=toyName.getText().toString();
                String description=toyDescription.getText().toString();
                String price=toyPrice.getText().toString();
                String toyCondition=mSpinnerCondition.getSelectedItem().toString();

                saveToCloud(name,description,price,toyCondition);

            }
        });

        uploadImage.setOnClickListener(view -> {
            pictureUpload();

        });


    }

    public void setSpinner(){
        ArrayAdapter<String> conditionAdapter=new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                conditions);
        mSpinnerCondition=findViewById(R.id.spinner_condition);
        mSpinnerCondition.setAdapter(conditionAdapter);
    }

    public void saveToCloud(String name,String description ,String price ,String condition){

        Log.i(TAG, "saveToCloud: "+ URL);
            Toy oneToy=Toy.builder()
                    .toyname(name)
                    .toydescription(description)
                    .image(URL)
                    .condition(Enum.valueOf(Condition.class, condition))
                    .price(Double.parseDouble(price))
                    //.usersToysId("5523b0e3-162d-4ddd-95b7-e664bf330ed1")
                    .build();

            Log.i(TAG,"-------------------------------------"+oneToy);

            // save the data
            Amplify.DataStore.save(oneToy,
                    successful->  Log.i(TAG, "test: saved to data store" +successful),
                    fail->Log.e(TAG, "test: fail to save " ));

            // save to backend
            Amplify.API.mutate(
                    ModelMutation.create(oneToy),
                    success ->  Log.i(TAG, "Saved item: to api " + success.getData()),
                    error ->  Log.e(TAG, "Could not save item to API", error)
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

                    File file = new File(getApplicationContext().getFilesDir(), name+".jpg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            name+".jpg",
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

    private void getImgUrl() {
        String name=toyName.getText().toString();

        Amplify.Storage.getUrl(
                name+".jpg",
                result -> {
                    Log.i(TAG, "Successfully generated: " + result.getUrl());
//                    URL=result.getUrl().toString();
                },
                error -> Log.e(TAG, "URL generation failure", error)
        );
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
                error -> Log.e(TAG, "Failed to fetch user attributes.", error)
        );
    }



}