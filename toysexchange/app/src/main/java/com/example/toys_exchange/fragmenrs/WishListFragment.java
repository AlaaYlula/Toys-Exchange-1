package com.example.toys_exchange.fragmenrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.UserWishList;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.ToyDetailActivity;
import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.example.toys_exchange.adapter.CustomToyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListFragment extends Fragment {

    private static final String TAG = WishListFragment.class.getSimpleName();
    private View mView;
    private List<Toy> toyList = new ArrayList<>();
    private Handler handler;
    private List<String> toyIds = new ArrayList<>();
    CustomToyAdapter customAdapter;


    public WishListFragment() {
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

        mView = inflater.inflate(R.layout.activity_wish_list, container, false);
        return mView;

}

    public void getToys(String userId) {
        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {
                    if (wishList.hasData()) {
                        for (UserWishList wishToy : wishList.getData()) {
                            Log.i(TAG , "WishToy object =>>>>>>>>>>>>>>" + wishToy);
                            if (wishToy!= null && wishToy.getAccount().getId().equals(userId)) {
                                toyIds.add(wishToy.getToy().getId());
                            }
                        }
                        getWishList();
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }

    public void getWishList() {
        Amplify.API.query(ModelQuery.list(Toy.class), success -> {
                    for (Toy toy : success.getData()) {
                        for (String toyId : toyIds) {
                            if (toy.getId().equals(toyId)) {
                                toyList.add(toy);
                            }
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }, error -> Log.e("error: ", "-> ", error)
        );
    }

    @Override
    public void onResume() {

        getWishListRecycler();
        super.onResume();
    }

    public void getWishListRecycler(){
        toyList = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());
        String userId =  sharedPreferences.getString(LoginActivity.USERNAME, "");
        getToys(userId);
        handler = new Handler(Looper.getMainLooper(), msg -> {

            RecyclerView recyclerView = mView.findViewById(R.id.recycler_wish_list);

             customAdapter = new CustomToyAdapter(toyList, new CustomToyAdapter.CustomClickListener() {
                @Override
                public void onRemoveClickListener(int position) {
                    removeFromWishList(toyList.get(position).getId(),toyList,position,customAdapter);

                }

                @Override
                public void ontItemClickListener(int position) {
                    Intent intent = new Intent(mView.getContext(), ToyDetailActivity.class);
                    intent.putExtra("toyName", toyList.get(position).getToyname());
                    intent.putExtra("description", toyList.get(position).getToydescription());
                    intent.putExtra("image", toyList.get(position).getImage());
                    intent.putExtra("price", toyList.get(position).getPrice());
                    intent.putExtra("condition", toyList.get(position).getCondition().toString());
                    intent.putExtra("contactInfo", toyList.get(position).getContactinfo());
                    intent.putExtra("id", toyList.get(position).getAccountToysId());
                    intent.putExtra("toyId", toyList.get(position).getId());
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(customAdapter);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    mView.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);


            return true;

        });
    }
    private void removeFromWishList(String toyId, List<Toy> toyList,int position, CustomToyAdapter customToyAdapter){
        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {
                    for (UserWishList wishToy :
                            wishList.getData()) {
                        Amplify.API.query(
                                ModelQuery.list(Account.class),
                                accounts -> {
                                    if(Objects.equals(toyId, wishToy.getToy().getId())){
                                        Log.i(TAG, "removeFromWishList: ***********************"+wishToy.getAccount().getId());

                                        Amplify.API.mutate(ModelMutation.delete(wishToy),
                                                response ->{
                                                    Log.i("MyAmplifyApp", "Todo with id: " + response.getData().getId());
                                                    toyList.remove(position);
                                                    customToyAdapter.notifyItemRemoved(position);
                                                } ,
                                                error -> Log.e("MyAmplifyApp", "Create failed", error)
                                        );
                                    }
                                },
                                error -> Log.e(TAG, error.toString(), error)
                        );
                    }
                },
                error -> Log.e(TAG, error.toString(), error)
        );
    }
}