package com.example.my_music.Adapter;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.my_music.AlbumFragment;
import com.example.my_music.ArtistFragment;
import com.example.my_music.PlaylistFragment;
import com.example.my_music.SongFragment;


public class ViewPagerAdapter  extends FragmentPagerAdapter {
    int numberTab;  //fragment tartib raqami
    Context context;
    public ViewPagerAdapter(FragmentManager fm, int behavior, Context context) {  //supernini yaratdik
        super(fm, behavior);
        this.numberTab=behavior;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {    //fragment bosilganda otishni taminlaydi
        switch (position){
            case 0:
                return new SongFragment();
            case 1:
                return new ArtistFragment();
            case 2:
                return new PlaylistFragment();
            case 3:
                return new AlbumFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {    //bu yerda fragment sonini sanaydi
        return numberTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {       //tabLayoutni viewpagerga bog'laganda yuzaga keladigan noqulayliklar TabItem title yoqolib qolishini tuzatish uchun ishlatiladi
        switch (position){
            case 0:
                return "Song";
            case 1:
                return "Artist";
            case 2:
                return "Playlist";
            case 3:
                return "Album";
            default: return  null;
        }
    }
}
