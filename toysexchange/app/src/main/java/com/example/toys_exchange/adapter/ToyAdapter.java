package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class ToyAdapter extends RecyclerView.Adapter<ToyAdapter.CustomViewHolder> {

    private static final String TAG = ToyAdapter.class.getSimpleName();
    List<Toy> toyList;
    CustomClickListener listener;
    String userID;
    private String isliked;
    Context context;


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

    @Override
    public void onBindViewHolder(@NonNull ToyAdapter.CustomViewHolder holder, int position) {

        holder.tvName.setText(toyList.get(position).getToyname());
        holder.tvCondition.setText(toyList.get(position).getCondition().toString());
        holder.tvPrice.setText(toyList.get(position).getPrice().toString());

        String image = toyList.get(position).getImage();

        String toyID = toyList.get(position).getId();

//        isliked = isLiked(toyID);
//
//        if(isliked.equals("like")){
//            holder.ivDislike.setVisibility(View.GONE);
//            holder.ivlike.setVisibility(View.VISIBLE);
//        }else{
//            holder.ivDislike.setVisibility(View.VISIBLE);
//            holder.ivlike.setVisibility(View.GONE);
//        }

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
                                    Log.i(TAG, "liked");
                                    holder.ivDislike.setVisibility(View.GONE);
                                    holder.ivlike.setVisibility(View.VISIBLE);
                                });

                            }

                        }
                    }
                },
                error -> {}
        );
//        Amplify.API.query(
//                ModelQuery.list(UserWishList.class),
//                wishList -> {
//
//                    if(wishList.hasData()){
//                        for (UserWishList wishToy :
//                                wishList.getData()) {
//                            Log.i("TAG => ", wishToy.getAccount().getId() + " userID => " + userID);
//                            if(wishToy.getAccount().getId().equals(userID) && wishToy.getToy().getId().equals(toyID)){
//                                holder.ivDislike.setVisibility(View.GONE);
//                                holder.ivlike.setVisibility(View.VISIBLE);
//                            }else {
//                                holder.ivlike.setVisibility(View.GONE);
//                                holder.ivDislike.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                },
//                error -> Log.e("MyAmplifyApp", "URL generation failure", error)
//        );


        Amplify.Storage.getUrl(
                image,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
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

        TextView tvName;
        TextView tvCondition;
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
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvPrice = itemView.findViewById(R.id.tvPrice);


//            if(isLiked.equals("like")){
//                ivDislike.setVisibility(View.GONE);
//                ivlike.setVisibility(View.VISIBLE);
//            }else {
//                ivlike.setVisibility(View.GONE);
//                ivDislike.setVisibility(View.VISIBLE);
//            }


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
