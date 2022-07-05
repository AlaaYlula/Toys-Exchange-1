package com.example.toys_exchange.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomToyAdapter extends RecyclerView.Adapter<CustomToyAdapter.CustomViewHolder> {

    private static final String TAG = CustomToyAdapter.class.getSimpleName();
    List<Toy> toysData;
    private String toyId;

    CustomClickListener listener;

    public CustomToyAdapter(List<Toy> toysData, CustomClickListener listener) {
        this.toysData = toysData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.item_wishlist,parent,false);

        return new CustomViewHolder(listItemView,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.toyName.setText(toysData.get(position).getToyname());
        holder.toyPrice.setText(toysData.get(position).getPrice().toString()+" Jd");
        String image = toysData.get(position).getImage();
        if(image!=null) {
            Amplify.Storage.getUrl(
                    image,
                    result -> {
                        runOnUiThread(() -> {
                            Picasso.get().load(result.getUrl().toString()).into(holder.toyImage);
                        });
                    },
                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
            );
        }

    }

    @Override
    public int getItemCount() {
        return toysData.size();
    }


    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView toyImage;
        TextView toyName;
        TextView toyPrice;
        CustomClickListener listener;

        TextView tvBin;
        LinearLayout AddToCart;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.tvName);
            toyImage = itemView.findViewById(R.id.ivImage);
            toyPrice = itemView.findViewById(R.id.tvPrice);
            tvBin = itemView.findViewById(R.id.tvBin);
            AddToCart = itemView.findViewById(R.id.Remove);

            tvBin.setOnClickListener(view -> {
                listener.onRemoveClickListener(getAdapterPosition());
            });

            AddToCart.setOnClickListener(view -> {
                listener.onBuy(getLayoutPosition());
            });

            itemView.setOnClickListener(view -> {
                listener.ontItemClickListener(getAdapterPosition());
            });

        }
    }


    public interface CustomClickListener{
        void onRemoveClickListener(int position);
        void ontItemClickListener(int position);
        void onBuy(int position);
    }
}
