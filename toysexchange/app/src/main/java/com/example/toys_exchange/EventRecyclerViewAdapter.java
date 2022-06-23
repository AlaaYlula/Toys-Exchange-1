package com.example.toys_exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Event;

import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder>{

    List<Event> eventsList;
    EventClickListener listener;


    public EventRecyclerViewAdapter( List<Event> eventsList, EventClickListener listener) {
        this.eventsList = eventsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.event_layout, parent, false);
        return new EventViewHolder(listItemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.EventViewHolder holder, int position) {
        holder.title.setText(eventsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public interface EventClickListener {
        void onEventClicked(int position);
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        EventClickListener listener;

        public EventViewHolder(@NonNull View itemView, EventClickListener listener) {
            super(itemView);

            this.listener = listener;

            title = itemView.findViewById(R.id.title_txt);

            itemView.setOnClickListener(view -> listener.onEventClicked(getAdapterPosition()));
        }
    }
}
