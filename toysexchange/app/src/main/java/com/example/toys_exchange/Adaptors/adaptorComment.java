package com.example.toys_exchange.Adaptors;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Account;
import com.amplifyframework.datastore.generated.model.Comment;
import com.example.toys_exchange.R;

import java.util.List;

public class adaptorComment extends RecyclerView.Adapter<adaptorComment.CustomViewHolder> {
    List<Comment> commentsList;
    CustomClickListener listener;

    public adaptorComment(List<Comment> commentList, CustomClickListener listener) {
        this.commentsList = commentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // task Layout has the three elements
        View listItemView = layoutInflater.inflate(R.layout.comment_layout, parent, false);
        return new CustomViewHolder(listItemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        String userId = commentsList.get(position).getAccountCommentsId();
        Log.i("Adapter", "User Id = > "+ userId);
        Amplify.API.query(
                ModelQuery.get(Account.class,userId),
                user -> {
                    Log.i("Adapter", "User Id = > "+ user.getData().getUsername());
                    holder.username.setText(user.getData().getUsername());
                },
                error -> Log.e("Adaptor", error.toString(), error)
        );
        holder.text.setText(commentsList.get(position).getText());
    }


    @Override
    public int getItemCount() {
        return commentsList.size();    }

    public interface CustomClickListener {
        void onCommentClicked(int position);
    }

    // CustomViewHolder //
    static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView text;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            username = itemView.findViewById(R.id.username_comment);
            text = itemView.findViewById(R.id.text_comment);

            itemView.setOnClickListener(view -> listener.onCommentClicked(getAdapterPosition()));
        }
    }
}
