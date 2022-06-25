package com.example.toys_exchange.fragmenrs;

import android.content.Intent;
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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.EventActivity;
import com.example.toys_exchange.UI.EventDetailsActivity;
import com.example.toys_exchange.UI.ToyDetailActivity;
import com.example.toys_exchange.adapter.CustomEventAdapter;
import com.example.toys_exchange.adapter.CustomToyAdapter;


import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Event> eventList = new ArrayList<>();
    private Handler handler;
    private View mView;

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

        return mView;
    }

    @Override
    public void onResume() {

        eventList =new ArrayList<>();

        handler = new Handler(Looper.getMainLooper(), msg ->{

            //    GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
            recyclerView = mView.findViewById(R.id.recycler_view);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);

            CustomEventAdapter customAdapter = new CustomEventAdapter(eventList, new CustomEventAdapter.CustomClickListener() {
                @Override
                public void onTaskClickListener(int position) {
                    Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                    intent.putExtra("eventTitle",eventList.get(position).getTitle());
                    intent.putExtra("description",eventList.get(position).getEventdescription());
                    intent.putExtra("userID",eventList.get(position).getAccountEventsaddedId());
                    intent.putExtra("eventID",eventList.get(position).getId());
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(customAdapter);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(gridLayoutManager);
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