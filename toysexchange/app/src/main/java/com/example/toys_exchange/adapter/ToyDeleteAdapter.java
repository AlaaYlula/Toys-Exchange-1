package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ToyDeleteAdapter extends RecyclerView.Adapter<ToyDeleteAdapter.CustomViewHolder>{
    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Toy> toysList;

    CustomClickListener listener;
    int colorNew = Color.parseColor("#59ba9d");
    int colorUsed = Color.parseColor("#fad170");

    public ToyDeleteAdapter(List<Toy> toysList, CustomClickListener listener) {
        this.toysList = toysList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.activity_user_list,parent,false);

        return new CustomViewHolder(listItemView , listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvName.setText(toysList.get(position).getToyname());
        holder.tvType.setText(toysList.get(position).getTypetoy().toString());
        holder.tvPrice.setText(toysList.get(position).getPrice().toString());


        if(toysList.get(position).getCondition().toString().equals("NEW")){
            holder.ivLabelNew.setVisibility(View.VISIBLE);
            holder.ivLabelNew.setColorFilter(colorNew);
        }else {
            holder.ivLabelUsed.setVisibility(View.VISIBLE);
            holder.ivLabelUsed.setColorFilter(colorUsed);
        }

        String image = toysList.get(position).getImage();
        Amplify.Storage.getUrl(
                image,
                result -> {
                    Log.i(TAG, "Successfully generated: " + result.getUrl());
                    runOnUiThread(()->{
                        Picasso.get().load(result.getUrl().toString()).into(holder.ivImage);
                    });
                },
                error -> Log.e(TAG, "URL generation failure", error)
        );


    }
    
    @Override
    public int getItemCount() {
        return toysList.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder  implements PopupMenu.OnMenuItemClickListener {

        CustomClickListener listener;

        ImageView ivToyOption;

        ImageView ivLabelNew;
        ImageView ivLabelUsed;

        TextView tvName;
        TextView tvType;
        TextView tvPrice;
        ImageView ivImage;




        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;


            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivLabelNew = itemView.findViewById(R.id.ivLabelNew);
            ivLabelUsed = itemView.findViewById(R.id.ivLabelOld);

            ivToyOption = itemView.findViewById(R.id.ivToyOption);

            ivToyOption.setOnClickListener(view->{
                showPopupMenu(view);
            });

            itemView.setOnClickListener(view -> {
                listener.ontItemClickListener(getAdapterPosition());
            });

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
                case R.id.delete:
                    deleteToy(getAdapterPosition());
                    return true;
                case R.id.edit:
                    showEditToy(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.toy_option);
            popupMenu.setOnMenuItemClickListener( this);
            popupMenu.show();
        }
    }



    public interface CustomClickListener{
        void onDeleteClickListener(int position);
        void ontItemClickListener(int position);
        void onUpdateClickListener(int position);

    }

    private void showEditToy(int position){

        //    listener.onUpdateClickListener(position,rlEditComment,ivEditComment,etEditComment);
        listener.onUpdateClickListener(position);
    }

    private void deleteToy( int position){
        listener.onDeleteClickListener(position);
    }


}

