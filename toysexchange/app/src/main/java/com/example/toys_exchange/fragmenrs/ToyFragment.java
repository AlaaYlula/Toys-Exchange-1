package com.example.toys_exchange.fragmenrs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.ToyDetailActivity;
import com.example.toys_exchange.adapter.CustomToyAdapter;

import java.util.ArrayList;
import java.util.List;


public class ToyFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Toy> toyList = new ArrayList<>();
    private Handler handler;
    private View mView;

    public ToyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_toy, container, false);

        // Add the following lines to create RecyclerView
//
//        toyList =new ArrayList<>();
//
//        handler = new Handler(Looper.getMainLooper(), msg ->{
//
//            //    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
//            recyclerView = mView.findViewById(R.id.recycler_view);
//
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
//
//            CustomToyAdapter customAdapter = new CustomToyAdapter(toyList, new CustomToyAdapter.CustomClickListener() {
//                @Override
//                public void onTaskClickListener(int position) {
//                    Intent intent = new Intent(getContext(), ToyDetailActivity.class);
//                    intent.putExtra("toyName",toyList.get(position).getToyname());
//                    intent.putExtra("description",toyList.get(position).getToydescription());
//                    intent.putExtra("image",toyList.get(position).getImage());
//                    intent.putExtra("price",toyList.get(position).getPrice());
//                    intent.putExtra("condition",toyList.get(position).getCondition());
//                    startActivity(intent);
//                }
//            });
//
//            recyclerView.setAdapter(customAdapter);
//
//            recyclerView.setHasFixedSize(true);
//
//            recyclerView.setLayoutManager(gridLayoutManager);
////          recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//            recyclerView.setHasFixedSize(true);
//
//            // recyclerView.setLayoutManager(gridLayoutManager);
//            return  true;
//        });
//
//
//        Amplify.API.query(ModelQuery.list(Toy.class), success ->{
//
//                    for(Toy toy: success.getData()){
//                        Log.i("get toy ", toy.toString());
//                        toyList.add(toy);
//                    }
//
//                    Bundle bundle =new Bundle();
//                    bundle.putString("data", "done");
//
//                    Message message = new Message();
//                    message.setData(bundle);
//                    handler.sendMessage(message);
//                },error -> Log.e("error: ","-> ",error)
//        );
        return mView;
    }

    @Override
    public void onResume() {

        toyList =new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), msg ->{

            //    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
            recyclerView = mView.findViewById(R.id.recycler_view);

//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);

            CustomToyAdapter customAdapter = new CustomToyAdapter(toyList, new CustomToyAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getContext(), ToyDetailActivity.class);
                    intent.putExtra("toyName",toyList.get(position).getToyname());
                    intent.putExtra("description",toyList.get(position).getToydescription());
                    intent.putExtra("image",toyList.get(position).getImage());
                    intent.putExtra("price",toyList.get(position).getPrice());
                    intent.putExtra("condition",toyList.get(position).getCondition().toString());
                    intent.putExtra("contactInfo",toyList.get(position).getContactinfo());
                    intent.putExtra("id",toyList.get(position).getAccountToysId());
                    intent.putExtra("toyId",toyList.get(position).getId());
                    startActivity(intent);
                }

                @Override
                public void ontItemClickListener(int position) {

                }
            });

            recyclerView.setAdapter(customAdapter);

            recyclerView.setHasFixedSize(true);

//            recyclerView.setLayoutManager(gridLayoutManager);
          recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
            recyclerView.setHasFixedSize(true);

            // recyclerView.setLayoutManager(gridLayoutManager);
            return  true;
        });


        Amplify.API.query(ModelQuery.list(Toy.class), success ->{

                    for(Toy toy: success.getData()){
                        Log.i("get toy ", toy.toString());
                        toyList.add(toy);
                    }

                    Bundle bundle =new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },error -> Log.e("error: ","-> ",error)
        );
        super.onResume();
    }
}