package com.example.my_music;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ArtistFragment extends Fragment {
    public ArtistFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();;
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }
}