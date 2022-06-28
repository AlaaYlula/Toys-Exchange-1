package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Store;
import com.example.toys_exchange.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.CustomViewHolder> {

    private static final String TAG = StoreAdapter.class.getSimpleName();

    List<Store> storeList;

    public StoreAdapter(List<Store> eventList) {
        this.storeList = eventList;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.store_items,parent,false);
        return new StoreAdapter.CustomViewHolder(listItemView);
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
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_title);
            description = itemView.findViewById(R.id.store_description);


        }
    }
}
