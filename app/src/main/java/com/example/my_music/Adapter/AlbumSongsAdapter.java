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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_music.AlbumShowActivity;
import com.example.my_music.ListenActivity;
import com.example.my_music.R;
import com.example.my_music.model.SongsModel;

import java.util.ArrayList;

import static com.example.my_music.MainActivity.arrayList;

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.MyViewHolder> {
    ArrayList<SongsModel> songAlbums;
    int i;
    Context context;

    public AlbumSongsAdapter(Context albumShowActivity, ArrayList<SongsModel> songAlbums) {
        this.context = albumShowActivity;
        this.songAlbums = songAlbums;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songTitleAlbum.setText(songAlbums.get(position).getTitle());
        holder.songArtistAlbum.setText(songAlbums.get(position).getAlbum());
        byte[] img = getAlbum(songAlbums.get(position).getPath());
        if(img != null){
            Glide.with(context).asBitmap().load(img).into(holder.songImageAlbum);
        }else {
            Glide.with(context).asBitmap().load(R.drawable.shape).into(holder.songImageAlbum);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListenActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("key","kalit");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return songAlbums.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView songImageAlbum;
        TextView songTitleAlbum, songArtistAlbum;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songImageAlbum = itemView.findViewById(R.id.songImageAlbum);
            songTitleAlbum = itemView.findViewById(R.id.textViewAlbum);
            songArtistAlbum = itemView.findViewById(R.id.textView2Album);
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
