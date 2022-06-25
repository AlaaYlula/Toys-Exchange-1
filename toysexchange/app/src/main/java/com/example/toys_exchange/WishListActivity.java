package com.example.toys_exchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.adapter.CustomToyAdapter;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity {

    private List<Toy> toyList = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
    }

    @Override
    protected void onResume() {

        toyList = new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), msg -> {
            RecyclerView recyclerView = findViewById(R.id.grid_recycler);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    2, LinearLayoutManager.VERTICAL,
                    false);

            CustomToyAdapter customAdapter = new CustomToyAdapter(toyList, new CustomToyAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                    intent.putExtra("toyName", toyList.get(position).getToyname());
                    intent.putExtra("description", toyList.get(position).getToydescription());
                    intent.putExtra("image", toyList.get(position).getImage());
                    intent.putExtra("price", toyList.get(position).getPrice());
                    intent.putExtra("condition", toyList.get(position).getCondition());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(customAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(gridLayoutManager);
            return true;
        });

        Amplify.API.query(ModelQuery.list(Toy.class), success -> {

                    for (Toy toy : success.getData()) {
                        Log.i("get toy ", toy.toString());
                        toyList.add(toy);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }, error -> Log.e("error: ", "-> ", error)
        );
        super.onResume();
    }
}