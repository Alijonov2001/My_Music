package com.example.my_music;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_music.Adapter.SongsAdapter;
import com.example.my_music.model.SongsModel;

import java.util.ArrayList;

import static com.example.my_music.MainActivity.arrayList;

public class  SongFragment extends Fragment {
    RecyclerView recyclerView;
    static SongsAdapter adapter;


    public SongFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_song, container, false);
       initView(view);
       if(!(arrayList.size() < 1)){
       adapter  = new SongsAdapter(getContext(),arrayList);
       recyclerView.setAdapter(adapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
       }


        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rec);
    }

}