package com.example.toys_exchange.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Toy;
import com.example.toys_exchange.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private static final String TAG = CustomAdapter.class.getSimpleName();
    List<Toy> toysData;

    CustomClickListener listener;

    public CustomAdapter(List<Toy> toysData, CustomClickListener listener) {
        this.toysData = toysData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.toys_items,parent,false);

        return new CustomViewHolder(listItemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.toyName.setText(toysData.get(position).getToyname());

    }

    @Override
    public int getItemCount() {
        return toysData.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView toyImage;
        TextView toyName;
        CustomClickListener listener;
        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            toyName = itemView.findViewById(R.id.toy_name);
            toyImage = itemView.findViewById(R.id.toy_img);

            itemView.setOnClickListener(view -> {
                listener.onTaskClickListener(getAdapterPosition());
            });
        }

//        private void setImage(String imageKey){
//            Amplify.Storage.downloadFile(
//                    imageKey,
//                    new File( getApplicationContext().getFilesDir() + "/" + imageKey),
//                    result -> {
//                        Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
//                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());
//
//                        ImageView image = findViewById(R.id.image);
//                        Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/"+result.getFile().getName());
//                        image.setImageBitmap(bitmap);
//
//
//                    },
//                    error -> Log.e(TAG,  "Download Failure", error)
//            );
//        }
    }


    public interface CustomClickListener{
        void onTaskClickListener(int position);
    }
}
