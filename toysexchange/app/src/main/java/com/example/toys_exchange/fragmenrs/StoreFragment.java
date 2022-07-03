package com.example.toys_exchange.fragmenrs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.EventDetailsActivity;
import com.example.toys_exchange.adapter.CustomEventAdapter;
import com.example.toys_exchange.adapter.StoreAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoreFragment extends Fragment {


    private static final String TAG = StoreFragment.class.getSimpleName() ;
    String cognitoId;
    private RecyclerView recyclerView;
    private List<Store> storeList = new ArrayList<>();
    private Handler handler;
    private View mView;
    private String loginUserId;
    private String loginUserName;

    public StoreFragment() {
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
        mView = inflater.inflate(R.layout.fragment_store, container, false);
        // get the loginUser cognitoId
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();
        getLoginUserId();
        return mView;
    }

    private void getLoginUserId() {
        Amplify.API.query(
                ModelQuery.list(Account.class),
                allUsers -> {
                    for (Account userAc:
                            allUsers.getData()) {
                        if(userAc.getIdcognito().equals(cognitoId)){
                            loginUserId = userAc.getId();
                            loginUserName = userAc.getUsername();
                        }
                    }

                },
                error -> Log.e(TAG, error.toString(), error)
        );

    }

    @Override
    public void onResume() {
        // get the loginUser cognitoId
        AuthUser logedInUser = Amplify.Auth.getCurrentUser();
        cognitoId = logedInUser.getUserId();
        getLoginUserId();

        storeList =new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), msg ->{

            recyclerView = mView.findViewById(R.id.recycler_view);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);

            StoreAdapter customAdapter = new StoreAdapter(storeList, new StoreAdapter.CustomClickListener() {
                @Override
                public void onClickListener(int position) {
                    Double longitude=storeList.get(position).getLongitude();
                    Double latitude=storeList.get(position).getLatitude();
                    if(longitude!=null && latitude!=null){
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitude+","+longitude));
                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), "no location provider", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            recyclerView.setAdapter(customAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
            return  true;
        });


        Amplify.API.query(ModelQuery.list(Store.class), success ->{

                    for(Store store: success.getData()){
                        Log.i("get store ", store.toString());
                        storeList.add(store);
                    }
            // Sort the Created At
            Collections.sort(storeList,new SortByDate());

                    Bundle bundle =new Bundle();
                    bundle.putString("data", "done");

                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                },error -> Log.e("error: ","-> ",error)
        );
        super.onResume();
    }
    // Class to sort the comments by date
    // https://www.delftstack.com/howto/java/how-to-sort-objects-in-arraylist-by-date-in-java/
    static class SortByDate implements Comparator<Store> {
        @Override
        public int compare(Store a, Store b) {
            return a.getCreatedAt().compareTo(b.getCreatedAt());
        }
    }

}