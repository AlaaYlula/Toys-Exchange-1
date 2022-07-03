package com.example.toys_exchange.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Event;
import com.example.toys_exchange.R;

import java.util.List;

public class EventDeleteAdapter extends RecyclerView.Adapter<EventDeleteAdapter.CustomViewHolder>{
    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Event> eventsList;
    PopupWindow popUp;
    CustomClickListener listener;

    public interface CustomClickListener{
        void onDeleteClickListener(int position);
        void ontItemClickListener(int position);
        void onUpdateClickListener(int position);

    }
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



    class CustomViewHolder extends RecyclerView.ViewHolder  implements PopupMenu.OnMenuItemClickListener {

        ImageView eventImage;
        TextView eventName;
        boolean click = true;
        Button deleteBtn;
        PopupWindow pw;
        Button updateBtn;
        CustomClickListener listener;
        LayoutInflater inflater;

        ImageView ivEventOption;


        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventImage = itemView.findViewById(R.id.event_img);
            deleteBtn = itemView.findViewById(R.id.delete_event);
            updateBtn = itemView.findViewById(R.id.update_event);

            ivEventOption = itemView.findViewById(R.id.ivEventOption);



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

            ivEventOption.setOnClickListener(view->{
                showPopupMenu(view);
            });
//            updateBtn.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View v) {
//                            if(listener!=null){
//                                int position = getAdapterPosition();
//                                if(position!=RecyclerView.NO_POSITION){
//                                    listener.onUpdateClickListener(position);
//                                }
//                            }
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
                   deleteEvent(getAdapterPosition());
                   return true;
               case R.id.edit:
                   showEditEvent(getAdapterPosition());
                   return true;
               default:
                   return false;
           }
        }
        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.event_option);
            popupMenu.setOnMenuItemClickListener( this);
            popupMenu.show();
        }
   }




    private void showEditEvent(int position){

    //    listener.onUpdateClickListener(position,rlEditComment,ivEditComment,etEditComment);
        listener.onUpdateClickListener(position);
    }

    private void deleteEvent( int position){
        listener.onDeleteClickListener(position);
    }

}
