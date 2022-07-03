package com.example.toys_exchange.fragmenrs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.EventDetailsActivity;
import com.example.toys_exchange.adapter.CustomEventAdapter;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {


    private static final String TAG = EventFragment.class.getSimpleName() ;
    String cognitoId;
    private RecyclerView recyclerView;
    private List<Event> eventList = new ArrayList<>();
    private Handler handler;
    private View mView;
    private String loginUserId;
    private String loginUserName;

    public EventFragment() {
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
        mView = inflater.inflate(R.layout.fragment_event, container, false);
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

        eventList =new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), msg ->{

            //    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
            recyclerView = mView.findViewById(R.id.recycler_view);

          //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);

            CustomEventAdapter customAdapter = new CustomEventAdapter(eventList, new CustomEventAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                    intent.putExtra("eventTitle",eventList.get(position).getTitle());
                    intent.putExtra("description",eventList.get(position).getEventdescription());
                    intent.putExtra("userID",eventList.get(position).getAccountEventsaddedId());
                    intent.putExtra("eventID",eventList.get(position).getId());
                    intent.putExtra("cognitoID",cognitoId);
                    intent.putExtra("loginUserID",loginUserId);
                    intent.putExtra("loginUserName",loginUserName);
                    intent.putExtra("longitude",eventList.get(position).getLongitude());
                    intent.putExtra("latitude",eventList.get(position).getLatitude());
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(customAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
            recyclerView.setHasFixedSize(true);
            return  true;
        });


        Amplify.API.query(ModelQuery.list(Event.class), success ->{

                    for(Event event: success.getData()){
                        Log.i("get toy ", event.toString());
                        eventList.add(event);
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