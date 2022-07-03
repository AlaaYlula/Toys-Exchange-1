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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.toys_exchange.UI.UpdateCommentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class adaptorComment extends RecyclerView.Adapter<adaptorComment.CustomViewHolder> {
    private static final String TAG = adaptorComment.class.getSimpleName();

    List<Comment> commentsList;
    public String userId;
    public static String loginUserId;
    private OnItemClickListener mListener;

    String commentId;
    String eventCommentId;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onUpdateClick(int position,ConstraintLayout rlEditComment, ImageView ivEditComment
              ,  EditText etEditComment);
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
        View listItemView = layoutInflater.inflate(R.layout.toyexchange_comment_item, parent, false);
       // return new CustomViewHolder(listItemView).linkAdapter(this);
        return new CustomViewHolder(listItemView,commentsList , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomViewHolder.loginUserIdSaved = loginUserId;
         userId = commentsList.get(position).getAccountCommentsId();
         commentId = commentsList.get(position).getId();
        eventCommentId = commentsList.get(position).getEventCommentsId();

        Amplify.API.query(
                    ModelQuery.get(Account.class,userId),
                    user -> {
                       runOnUiThread(()->{
                           holder.username.setText(user.getData().getUsername());
                       });
                    },
                    error -> Log.e("Adaptor", error.toString(), error)
            );
        holder.text.setText(commentsList.get(position).getText());
        holder.position = position;
        holder.comment = commentsList.get(position);

        holder.ivCommentOption.setVisibility(View.VISIBLE);
         if(commentsList.get(position).getAccountCommentsId().equals(loginUserId)) // Add For comments check
                {
                    holder.ivCommentOption.setVisibility(View.VISIBLE);
                    holder.etEditComment.setText(holder.text.getText());


               }else{
                      holder.ivCommentOption.setVisibility(View.GONE);
                          }


    }

    @Override
    public int getItemCount() {
        return commentsList.size();    }


    // CustomViewHolder //
    static class CustomViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        public static String loginUserIdSaved ;

        private adaptorComment adapter;
        TextView username;
        TextView text;

        ImageView ivCommentOption;
        ImageView ivEditComment;

        EditText etEditComment;
        ConstraintLayout rlEditComment;

        View rootView;
        int position;

        Comment comment;
        List<Comment> commentsList;
        OnItemClickListener listener;





        public CustomViewHolder(@NonNull View itemView ,
                                List<Comment> commentsList,
                                OnItemClickListener listener ) {
            super(itemView);

            rootView = itemView;
            username = itemView.findViewById(R.id.tvName);
            text = itemView.findViewById(R.id.tvComment);
            etEditComment = itemView.findViewById(R.id.etEditComment);
            ivEditComment = itemView.findViewById(R.id.ivEditComment);

            this.listener = listener;
            this.commentsList = commentsList;

            ivCommentOption = itemView.findViewById(R.id.ivCommentOption);
            ivCommentOption.setOnClickListener(view -> {
                showPopupMenu(view);
            });

            rlEditComment = itemView.findViewById(R.id.rlEditComment);

        }
        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.commint_option);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch(item.getItemId()){
                case R.id.delete:
                    deleteComment(getAdapterPosition());
                    return true;
                case R.id.edit:
                    showEditComment(getAdapterPosition());

                    return true;
                default:
                    return false;
            }

        }

        private void showEditComment(int position){

            listener.onUpdateClick(position,rlEditComment,ivEditComment,etEditComment);
        }

        private void deleteComment( int position){
        listener.onDeleteClick(position);
        }

    }
}
