package com.example.my_music.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my_music.ListenActivity;
import com.example.my_music.R;
import com.example.my_music.model.SongsModel;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    Context context;
  public static ArrayList<SongsModel> list ;


    public SongsAdapter(Context context, ArrayList<SongsModel> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songTitle.setText(list.get(position).getTitle());
        holder.songArtist.setText(list.get(position).getArtist());
        byte[] art = getAlbum(list.get(position).getPath());
        Glide.with(context).load(art).into(holder.songImage);
        if(art != null){
            Glide.with(context).load(art).into(holder.songImage);
        }else {
            Glide.with(context).load(R.drawable.shape).into(holder.songImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListenActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle, songArtist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.songImage);
            songTitle = itemView.findViewById(R.id.textView);
            songArtist = itemView.findViewById(R.id.textView2);
        }
    }
    public byte[] getAlbum(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte [] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }
   public void SearchList(ArrayList<SongsModel> songsModelArrayList){
        list = new ArrayList<>();
        list.addAll(songsModelArrayList);
        notifyDataSetChanged();
    }
}
