package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomEventAdapter extends RecyclerView.Adapter<CustomEventAdapter.CustomViewHolder>{

    private static final String TAG = CustomEventAdapter.class.getSimpleName();

    public static String acc_id;
    List<Event> eventList;
    CustomClickListener listener;

    public static void getUserId()
    {
        List<Account> acclist = new ArrayList<>();

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
                    },
                    error -> Log.e(TAG, error.toString(), error)
            );
        });

    }
    public CustomEventAdapter(List<Event> eventList, CustomClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.event_items,parent,false);
        return new CustomViewHolder(listItemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.eventName.setText(eventList.get(position).getTitle());
        if(eventList.get(position).getEventdescription().length()>100){
            holder.eventDescription.setText(eventList.get(position).getEventdescription().substring(0,100)+"... ");
        }else {
            holder.eventDescription.setText(eventList.get(position).getEventdescription());
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

       // ImageView eventImage;
        TextView eventName;
        TextView eventDescription;
        CustomClickListener listener;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);

            getUserId();

            itemView.setOnClickListener(view -> {
                listener.onTaskClickListener(getAdapterPosition());
            });
        }
    }

    public interface CustomClickListener{
        void onTaskClickListener(int position);
    }
}
