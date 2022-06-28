package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;

import java.util.List;

public class ToyDeleteAdapter extends RecyclerView.Adapter<ToyDeleteAdapter.CustomViewHolder>{
    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Toy> toysList;

    CustomClickListener listener;

    public ToyDeleteAdapter(List<Toy> toysList, CustomClickListener listener) {
        this.toysList = toysList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.toy_delete_layout,parent,false);

        return new CustomViewHolder(listItemView , listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.toyName.setText(toysList.get(position).getToyname());

    }
    
    @Override
    public int getItemCount() {
        return toysList.size();
    }



    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView toyImage;
        TextView toyName;
        Button deleteBtn;
        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.tvName);
            toyImage = itemView.findViewById(R.id.toy_img);
            deleteBtn = itemView.findViewById(R.id.delete_toy);


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

            itemView.setOnClickListener(view -> {
                listener.ontItemClickListener(getAdapterPosition());
            });

        }
    }



    public interface CustomClickListener{
        void onDeleteClickListener(int position);
        void ontItemClickListener(int position);
    }

}

