package com.example.toys_exchange.adapter;

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



    class CustomViewHolder extends RecyclerView.ViewHolder  implements PopupMenu.OnMenuItemClickListener {

        ImageView toyImage;
        TextView toyName;
        Button deleteBtn;
        Button updateBtn;
        CustomClickListener listener;

        ImageView ivToyOption;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.tvName);
            toyImage = itemView.findViewById(R.id.toy_img);
            deleteBtn = itemView.findViewById(R.id.delete_toy);
            updateBtn = itemView.findViewById(R.id.update_toy);
            ivToyOption = itemView.findViewById(R.id.ivToyOption);


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

            ivToyOption.setOnClickListener(view->{
                showPopupMenu(view);
            });

//            updateBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    if(listener!=null){
//                        int position = getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            listener.onUpdateClickListener(position);
//                        }
//                    }
//                }
//
//            });



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

