package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;

import java.util.List;

public class StoreDeleteAdapter extends RecyclerView.Adapter<StoreDeleteAdapter.CustomViewHolder> {
    private static final String TAG = StoreAdapter.class.getSimpleName();

    List<Store> storeList;

   CustomClickListener listener;

    public StoreDeleteAdapter(List<Store> eventList,CustomClickListener listener) {
        this.storeList = eventList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.activity_user_store,parent,false);
        return new CustomViewHolder(listItemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
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
        ImageView deleteBtn;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView , CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            storeName = itemView.findViewById(R.id.store_title);
            description = itemView.findViewById(R.id.store_description);
            deleteBtn = itemView.findViewById(R.id.delete_store);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClickListener(position);
                        }
                    }
                }
            });
        }
    }
    public interface CustomClickListener{
        void onDeleteClickListener(int position);
    }

}
