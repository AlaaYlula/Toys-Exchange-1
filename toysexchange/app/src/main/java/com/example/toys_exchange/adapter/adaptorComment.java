package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.EventDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class adaptorComment extends RecyclerView.Adapter<adaptorComment.CustomViewHolder> {
    private static final String TAG = adaptorComment.class.getSimpleName();

    List<Comment> commentsList;
    public String userId;

    public static String loginUserId;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public adaptorComment(List<Comment> commentList ,String loginUserId) {
        this.commentsList = commentList;
        this.loginUserId  = loginUserId;
    }




    @NonNull
    @Override
    public CustomViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.comment_layout, parent, false);
       // return new CustomViewHolder(listItemView).linkAdapter(this);
        return new CustomViewHolder(listItemView , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomViewHolder.loginUserIdSaved = loginUserId;
         userId = commentsList.get(position).getAccountCommentsId();
        Log.i(TAG, "login User Id onBindViewHolder "+loginUserId );

        Amplify.API.query(
                    ModelQuery.get(Account.class,userId),
                    user -> {
                        holder.username.setText(user.getData().getUsername());
                    },
                    error -> Log.e("Adaptor", error.toString(), error)
            );
        holder.text.setText(commentsList.get(position).getText());
        holder.position = position;
        holder.comment = commentsList.get(position);


       // holder.deletebtn.setVisibility(View.VISIBLE);
         if(commentsList.get(position).getAccountCommentsId().equals(loginUserId)) // Add For comments check
                {
                    holder.deletebtn.setVisibility(View.VISIBLE);

               }else{
                      holder.deletebtn.setVisibility(View.GONE);
                          }


    }

    @Override
    public int getItemCount() {
        return commentsList.size();    }


    // CustomViewHolder //
    static class CustomViewHolder extends RecyclerView.ViewHolder {
        public static String loginUserIdSaved ;

        private adaptorComment adapter;
        TextView username;
        TextView text;
        Button deletebtn;

        View rootView;
        int position;
        Comment comment;

        public CustomViewHolder(@NonNull View itemView , OnItemClickListener listener ) {
            super(itemView);

            rootView = itemView;

            username = itemView.findViewById(R.id.username_comment);
            text = itemView.findViewById(R.id.text_comment);
            deletebtn = itemView.findViewById(R.id.btn_deleteComment);

            Log.i(TAG, "login User Id CustomViewHolder "+loginUserIdSaved );


            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }


                    }
                }



            });


        }


    }



}
