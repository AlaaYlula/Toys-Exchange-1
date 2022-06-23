package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;

import java.util.List;

public class CustomEventAdapter extends RecyclerView.Adapter<CustomEventAdapter.CustomViewHolder>{

    List<Event> eventList;
    CustomClickListener listener;

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

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImage;
        TextView eventName;
        CustomClickListener listener;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventImage = itemView.findViewById(R.id.event_img);

            itemView.setOnClickListener(view -> {
                listener.onTaskClickListener(getAdapterPosition());
            });
        }
    }

    public interface CustomClickListener{
        void onTaskClickListener(int position);
    }
}
