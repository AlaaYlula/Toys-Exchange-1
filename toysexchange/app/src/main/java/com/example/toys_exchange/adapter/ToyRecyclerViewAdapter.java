package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;

import java.util.List;

public class ToyRecyclerViewAdapter extends RecyclerView.Adapter<ToyRecyclerViewAdapter.ToyViewHolder>{

    List<Toy> toysList;
    ToyClickListener listener;


    public ToyRecyclerViewAdapter( List<Toy> toysList, ToyRecyclerViewAdapter.ToyClickListener listener) {
        this.toysList = toysList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToyRecyclerViewAdapter.ToyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.toy_layout, parent, false);
        return new ToyRecyclerViewAdapter.ToyViewHolder(listItemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ToyRecyclerViewAdapter.ToyViewHolder holder, int position) {
        holder.name.setText(toysList.get(position).getToyname());
    }

    @Override
    public int getItemCount() {
        return toysList.size();
    }

    public interface ToyClickListener {
        void onToyClicked(int position);
    }

    static class ToyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        ToyRecyclerViewAdapter.ToyClickListener listener;

        public ToyViewHolder(@NonNull View itemView, ToyRecyclerViewAdapter.ToyClickListener listener) {
            super(itemView);

            this.listener = listener;

            name = itemView.findViewById(R.id.toy_txt);

            itemView.setOnClickListener(view -> listener.onToyClicked(getAdapterPosition()));
        }
    }
}
