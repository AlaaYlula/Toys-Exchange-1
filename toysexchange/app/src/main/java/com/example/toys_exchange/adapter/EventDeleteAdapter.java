package com.example.toys_exchange.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        popUp = new PopupWindow();
        holder.eventName.setText(eventsList.get(position).getTitle());

    }
    
    @Override
    public int getItemCount() {
        return eventsList.size();
    }



    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImage;
        TextView eventName;
        boolean click = true;
        Button deleteBtn;
        PopupWindow pw;
        Button updateBtn;
        CustomClickListener listener;
        LayoutInflater inflater;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            eventName = itemView.findViewById(R.id.event_name);
            eventImage = itemView.findViewById(R.id.event_img);
            deleteBtn = itemView.findViewById(R.id.delete_event);
            updateBtn = itemView.findViewById(R.id.update_event);

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

            updateBtn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if(listener!=null){
                                int position = getAdapterPosition();
                                if(position!=RecyclerView.NO_POSITION){
                                    listener.onUpdateClickListener(position);
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
        void onUpdateClickListener(int position);


    }

}












//                            inflater = (LayoutInflater) updateBtn.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                            pw = new PopupWindow(inflater.inflate(R.layout.activity_event, null, false),800,1400, true);
//                    pw.showAtLocation(updateBtn.findViewById(R.id.update_event), Gravity.CENTER, 0, 0);





//                    if (click) {
//
//                            click = false;
//
//                        } if(!click){
//                            pw.dismiss();
//                            click = true;
//                        }

// Initializing the popup menu and giving the reference as current context
//                    PopupMenu popupMenu = new PopupMenu(updateBtn.getContext(), updateBtn);
//
//                    // Inflating popup menu from popup_menu.xml file
//                    popupMenu.getMenuInflater().inflate(R.menu.event_menu, popupMenu.getMenu());
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            // Toast message on menu item clicked
//                            Toast.makeText(updateBtn.getContext(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
//                            return true;
//                        }
//                    });
//                    // Showing the popup menu
//                    popupMenu.show();