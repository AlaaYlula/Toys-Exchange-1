package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.CustomViewHolder>{

    List<Toy> toysData;
    CustomClickListener listener;

    public WishListAdapter(List<Toy> dataList, CustomClickListener listener) {
        this.toysData = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.item_wishlist,parent,false);

        return new CustomViewHolder(listItemView , listener);
    }

    // will be called multiple times to inject the data into the view holder object
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.toyName.setText(toysData.get(position).getToyname());
        holder.toyPrice.setText(toysData.get(position).getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return toysData.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView toyImage;
        TextView toyName;
        TextView toyPrice;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.toy_name);
            toyImage = itemView.findViewById(R.id.toy_image);
            toyPrice = itemView.findViewById(R.id.toy_Price);

            itemView.setOnClickListener(view ->
                    listener.onItemClicked(getAdapterPosition()));
        }
    }

    public interface CustomClickListener {
        void onItemClicked(int position);
    }
}
