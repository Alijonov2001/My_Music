package com.example.my_music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.my_music.Adapter.AlbumSongsAdapter;
import com.example.my_music.model.SongsModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import static com.example.my_music.MainActivity.albums;
import static com.example.my_music.MainActivity.arrayList;

public class AlbumShowActivity extends AppCompatActivity {
    AlbumSongsAdapter adapter;
    RecyclerView recyclerView;
    ImageView albumImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String albumName;
    public static ArrayList<SongsModel> AlbumArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_show);
        initViews();
        albumName = getIntent().getStringExtra("albumName");
        collapsingToolbarLayout.setTitle(albumName);
        String albumImg = getIntent().getStringExtra("albumImage");
        byte [] img = getAlbum(albumImg);
        if(img != null){
            Glide.with(this).asBitmap().load(img).into(albumImage);
        }else {
            Glide.with(this).asBitmap().load(R.drawable.shape).into(albumImage);
        }
       getList();
   }

    private void getList() {
        AlbumArraylist.clear();
        int index = 0;
        for (int i = 0; i<arrayList.size(); i++){
            if(arrayList.get(i).getAlbum().equals(albumName)){
                AlbumArraylist.add(index,arrayList.get(i));
                index++;
            }
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rec1);
        albumImage = findViewById(R.id.imageView2);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new AlbumSongsAdapter(this,AlbumArraylist);
        recyclerView.setAdapter(adapter);
    }
    public byte[] getAlbum(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte [] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }
}