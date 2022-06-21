package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;


public class toysListActivity extends AppCompatActivity {

    private View.OnClickListener mClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            mBack.setText("back");
//            mBack.setAllCaps(true);

            Intent startAllTasksIntent = new Intent(getApplicationContext(), profileActivity.class);
            startActivity(startAllTasksIntent);

        }
    };

    private TextView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toys_list);



//        setToys();


        Button btnBack = findViewById(R.id.back_toys_profile);
        mBack = findViewById(R.id.back_toys_profile);
        btnBack.setOnClickListener(mClickBack);
    }


    private void setToys()
    {
        Toy toyOne = Toy.builder().toyname("ToyOne").toydescription("Barbi").image("AYA").build();
        Toy toyTwo = Toy.builder().toyname("ToyTwo").toydescription("fulla").image("aya").build();
        Toy toyThree = Toy.builder().toyname("ToyThree").toydescription("GTA").image("LOL").build();

        Amplify.API.mutate(
                ModelMutation.create(toyOne),
                item -> Log.i("ToysExchange", "Added : " + item.getData().getId()),
                error -> Log.e("ToysExchange", "Create failed", error)
        );


        Amplify.API.mutate(
                ModelMutation.create(toyTwo),
                item -> Log.i("ToysExchange", "Added : " + item.getData().getId()),
                error -> Log.e("ToysExchange", "Create failed", error)
        );
        Amplify.API.mutate(
                ModelMutation.create(toyThree),
                item -> Log.i("ToysExchange", "Added : " + item.getData().getId()),
                error -> Log.e("ToysExchange", "Create failed", error)
        );

    }

}