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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.CustomViewHolder> {

    private static final String TAG = StoreAdapter.class.getSimpleName();

    List<Store> storeList;
    private String userId;

    CustomClickListener listener;

    public StoreAdapter(List<Store> eventList,CustomClickListener listener) {
        this.storeList = eventList;
        this.listener=listener;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.store_items,parent,false);
        return new StoreAdapter.CustomViewHolder(listItemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        userId = storeList.get(position).getAccountStoresId();
        Amplify.API.query(
                ModelQuery.get(Account.class,userId),
                user -> {
                    runOnUiThread(()->{
                        holder.username.setText(user.getData().getUsername());
                    });

                },
                error -> Log.e("Adaptor", error.toString(), error)
        );
        holder.storeName.setText(storeList.get(position).getStorename());
        holder.description.setText(storeList.get(position).getStoredescription());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView storeName;
        TextView description;
        ImageView storeLocation;
        TextView username;


        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView , CustomClickListener listener) {

            super(itemView);

            this.listener=listener;

            storeName = itemView.findViewById(R.id.store_title);
            description = itemView.findViewById(R.id.store_description);

            storeLocation=itemView.findViewById(R.id.txt_view_store_location);

            storeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onClickListener(position);
                        }
                    }
                }
            });

            username = itemView.findViewById(R.id.username_store);



        }
    }

    public interface CustomClickListener{
        void onClickListener(int position);
    }
}
