package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.amplifyframework.datastore.generated.model.UserWishList;
import com.example.toys_exchange.R;
import com.example.toys_exchange.UI.data.model.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ToyAdapter extends RecyclerView.Adapter<ToyAdapter.CustomViewHolder> {

    private static final String TAG = ToyAdapter.class.getSimpleName();
    List<Toy> toyList;
    CustomClickListener listener;
    String userID;
    private String isliked;
    Context context;

    int colorNew = Color.parseColor("#59ba9d");
    int colorUsed = Color.parseColor("#fad170");

    public ToyAdapter(List<Toy> toyList , CustomClickListener listener) {
        this.toyList = toyList;

        this.listener = listener;
    }

    @NonNull
    @Override
    public ToyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shophop_item_newest_product,parent,false);

        context = parent.getContext();
        return new CustomViewHolder(view, isliked ,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ToyAdapter.CustomViewHolder holder, int position) {

        holder.tvName.setText(toyList.get(position).getToyname());
        holder.tvType.setText(toyList.get(position).getTypetoy().toString());
        holder.tvPrice.setText(toyList.get(position).getPrice().toString() + " JD");


        if(toyList.get(position).getCondition().toString().equals("NEW")){
            holder.ivLabelNew.setVisibility(View.VISIBLE);
            holder.ivLabelNew.setColorFilter(colorNew);
        }else {
            holder.ivLabelUsed.setVisibility(View.VISIBLE);
            holder.ivLabelUsed.setColorFilter(colorUsed);
        }

        String image = toyList.get(position).getImage();
        String toyID = toyList.get(position).getId();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userID =  sharedPreferences.getString(LoginActivity.USERNAME, "No Team setting");

        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {

                    if(wishList.hasData()){
                        for (UserWishList wishToy :
                                wishList.getData()) {
                            if(wishToy.getAccount().getId().equals(userID) && wishToy.getToy().getId().equals(toyList.get(position).getId())) {
                                runOnUiThread(() -> {
                                    holder.ivDislike.setVisibility(View.GONE);
                                    holder.ivlike.setVisibility(View.VISIBLE);
                                });

                            }

                        }
                    }
                },
                error -> {}
        );

        Amplify.Storage.getUrl(
                image,
                result -> {
                    runOnUiThread(()->{
                        Picasso.get().load(result.getUrl().toString()).into(holder.ivImage);
                    });
                },
                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
        );

    }
    private String isLiked(String  toyId){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userID =  sharedPreferences.getString(LoginActivity.USERNAME, "No Team setting");

        isliked = "unLike";
        Amplify.API.query(
                ModelQuery.list(UserWishList.class),
                wishList -> {

                    if(wishList.hasData()){
                        for (UserWishList wishToy :
                                wishList.getData()) {
                            if(wishToy.getAccount().getId().equals(userID) && wishToy.getToy().getId().equals(toyId)){
                                isliked = "like";
                            }

                        }
                    }
                },
                error -> {}
        );
        return isliked;
    }

    @Override
    public int getItemCount() {
        return toyList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;
        ImageView ivDislike;
        ImageView ivlike;
        ImageView ivLabelNew;
        ImageView ivLabelUsed;

        TextView tvName;
//        TextView tvCondition;
        TextView tvType;
        TextView tvPrice;

        CustomClickListener listener;
        private String toyId;

        public CustomViewHolder(@NonNull View itemView,String isLiked ,CustomClickListener listener) {
            super(itemView);
            this.listener = listener;



            ivImage = itemView.findViewById(R.id.ivImage);
            ivDislike = itemView.findViewById(R.id.ivDislike);
            ivlike = itemView.findViewById(R.id.ivlike);

            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivLabelNew = itemView.findViewById(R.id.ivLabelNew);
            ivLabelUsed = itemView.findViewById(R.id.ivLabelOld);

            ivDislike.setOnClickListener(view -> {
                if(ivDislike.getVisibility() == View.VISIBLE){
                    ivDislike.setVisibility(View.GONE);
                    ivlike.setVisibility(View.VISIBLE);
                    listener.onFavClickListener(getAdapterPosition(),false);
                }
            });
//
            ivlike.setOnClickListener(view -> {
                if(ivlike.getVisibility() == View.VISIBLE){
                    ivlike.setVisibility(View.GONE);
                    ivDislike.setVisibility(View.VISIBLE);
                    listener.onFavClickListener(getAdapterPosition(), true);
                }
            });

            itemView.setOnClickListener(view -> {
                listener.ontItemClickListener(getAdapterPosition());
            });

        }


    }

    public interface CustomClickListener{
        void onFavClickListener(int position, Boolean like);
        void ontItemClickListener(int position);
    }
}
