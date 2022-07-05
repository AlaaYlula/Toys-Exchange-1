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
        View listItemView = layoutInflater.inflate(R.layout.activity_user_event,parent,false);

        return new CustomViewHolder(listItemView , listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.eventName.setText(eventsList.get(position).getTitle());
        String desc = eventsList.get(position).getEventdescription();
        if(desc.length() > 100){
            holder.eventDescription.setText(desc.substring(0,100)+"...");
        }else {
            holder.eventDescription.setText(desc);
        }

    }
    
    @Override
    public int getItemCount() {
        return eventsList.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder  implements PopupMenu.OnMenuItemClickListener {

        TextView eventName;
        TextView eventDescription;
        CustomClickListener listener;

        ImageView ivEventOption;


        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_description);

            ivEventOption = itemView.findViewById(R.id.ivEventOption);


            ivEventOption.setOnClickListener(view->{
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
        listener.onUpdateClickListener(position);
    }

    private void deleteEvent( int position){
        listener.onDeleteClickListener(position);
    }

}
