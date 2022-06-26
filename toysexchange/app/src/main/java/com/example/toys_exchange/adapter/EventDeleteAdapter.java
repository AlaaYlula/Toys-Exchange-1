package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;

import java.util.List;

public class EventDeleteAdapter extends RecyclerView.Adapter<EventDeleteAdapter.CustomViewHolder>{
    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Event> eventsList;

    CustomClickListener listener;

    public EventDeleteAdapter(List<Event> eventlist, CustomClickListener listener) {
        this.eventsList = eventlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.event_delete_layout,parent,false);

        return new CustomViewHolder(listItemView , listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.eventName.setText(eventsList.get(position).getTitle());

    }
    
    @Override
    public int getItemCount() {
        return eventsList.size();
    }



    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImage;
        TextView eventName;
        Button deleteBtn;
        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventImage = itemView.findViewById(R.id.event_img);
            deleteBtn = itemView.findViewById(R.id.delete_event);


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

