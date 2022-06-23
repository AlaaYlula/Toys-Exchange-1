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

public class CustomToyAdapter extends RecyclerView.Adapter<CustomToyAdapter.CustomViewHolder> {

    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Toy> toysData;

    CustomClickListener listener;

    public CustomToyAdapter(List<Toy> toysData, CustomClickListener listener) {
        this.toysData = toysData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.toys_items,parent,false);

        return new CustomViewHolder(listItemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.toyName.setText(toysData.get(position).getToyname());

    }

    @Override
    public int getItemCount() {
        return toysData.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView toyImage;
        TextView toyName;
        CustomClickListener listener;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.event_name);
            toyImage = itemView.findViewById(R.id.event_img);

            itemView.setOnClickListener(view -> {
                listener.onTaskClickListener(getAdapterPosition());
            });
        }
    }


    public interface CustomClickListener{
        void onTaskClickListener(int position);
    }
}
