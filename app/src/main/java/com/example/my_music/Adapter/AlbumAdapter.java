package com.example.my_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_music.AlbumShowActivity;
import com.example.my_music.R;
import com.example.my_music.model.SongsModel;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView .Adapter<AlbumAdapter.MyHolder>{
    ArrayList<SongsModel> albumList;
    Context context;

    public AlbumAdapter(Context context, ArrayList<SongsModel> arrayList) {
        this.context = context;
        this.albumList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recalbum_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).getAlbum());
        byte[] art = getAlbum(albumList.get(position).getPath());
        Glide.with(context).load(art).into(holder.albumImage);
        if(art != null){
            Glide.with(context).load(art).into(holder.albumImage);
        }else {
            Glide.with(context).load(R.drawable.shape).into(holder.albumImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumShowActivity.class);
                intent.putExtra("albumName",albumList.get(position).getAlbum());
                intent.putExtra("albumImage",albumList.get(position).getPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.imageView);
            albumName = itemView.findViewById(R.id.albumName);
        }
    }
    public byte[] getAlbum(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte [] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }

}

