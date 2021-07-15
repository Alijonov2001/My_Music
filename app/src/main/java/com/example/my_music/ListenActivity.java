  package com.example.my_music;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.palette.graphics.Palette;
import androidx.palette.graphics.Target;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.my_music.model.SongsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.jar.Attributes;

import static com.example.my_music.Adapter.SongsAdapter.list;
import static com.example.my_music.AlbumShowActivity.AlbumArraylist;
import static com.example.my_music.MainActivity.arrayList;

public class ListenActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView songTitle, songArtist, nowTime, lengthTime;
    int position = -1;
    Uri uri;
    ImageView backIB, nextIB, btnback;
    FloatingActionButton floatingActionButton;
    ImageView listenImage;
    ArrayList<SongsModel>songList;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Handler handler = new Handler();
    Thread backThread, nextThread, playThread;
    Bitmap bitmap;
    private Object palette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        initViews();
        initVars();
        getIntentData();
        getPathData(uri);
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ListenActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mPosition);
                    nowTime.setText(timeFormat(mPosition));

                }
                handler.postDelayed(this,1000);;
            }
        });

    }

    private void getPathData(Uri mUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mUri.toString());
        int timeLength = Integer.parseInt(songList.get(position).getDuration())/1000;
        lengthTime.setText(timeFormat(timeLength));
        byte [] img = retriever.getEmbeddedPicture();
        if (img != null){
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            AnimationImage(this, bitmap, listenImage);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if(swatch != null) {
                        ConstraintLayout bg = findViewById(R.id.constraintLayout);
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        bg.setBackgroundResource(R.drawable.listen_bg);
                        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), 0x000000});
                        bg.setBackground(drawable);
                        GradientDrawable drawableTool = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{swatch.getRgb(), 0x000000});
                        toolbar.setBackground(drawableTool);
                        songTitle.setTextColor(swatch.getTitleTextColor());
                        songArtist.setTextColor(swatch.getBodyTextColor());
                        nowTime.setTextColor(swatch.getBodyTextColor());
                        lengthTime.setTextColor(swatch.getBodyTextColor());

                    }
                }
            });

        }else {
            Glide.with(this).load(R.drawable.shape).into(listenImage);
        }
    }

    private String timeFormat(int mposition) {
        String showTime = "";
        String seconds = String.valueOf(mposition % 60);
        String minute = String.valueOf(mposition / 60);
        if(seconds.length() == 1){
            showTime = minute + ":" + "0" + seconds;
        }else {
            showTime = minute + ":" + seconds;
        }
        return showTime;
    }

    private void initVars() {
        songList = new ArrayList<>();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListenActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void getIntentData() {
    position = getIntent().getExtras().getInt("position",-1);
    String key = getIntent().getExtras().getString("key");
    if(key != null && key.equals("kalit")){
        songList = AlbumArraylist;
    }else {
        songList = list;
    }
    if(songList != null){
        uri = Uri.parse(songList.get(position).getPath());
       getPathData(uri);
    }
    if(mediaPlayer != null){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
    }else {
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
    }
    songTitle.setText(songList.get(position).getTitle());
    songArtist.setText(songList.get(position).getArtist());
    }

    private void initViews() {
        songTitle = findViewById(R.id.textView3);
        songArtist = findViewById(R.id.textView4);
        backIB = findViewById(R.id.backIB);
        nextIB = findViewById(R.id.nextIB);
        btnback = findViewById(R.id.backActivity);
        lengthTime = findViewById(R.id.durationTime);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        listenImage = findViewById(R.id.listenImage);
        seekBar = findViewById(R.id.seekBar);
        nowTime = findViewById(R.id.nowTime);
    }

    @Override
    protected void onResume() {
        playBtn();
        prevBtn();
        nextBtn();
        super.onResume();
    }

    private void prevBtn() {
       backThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevThreadBtn();

            }
        };backThread.start();
    }

    private void prevThreadBtn() {
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(position == 0){
                        position = songList.size() - 1;
                    }else {
                        position = position - 1;
                    }
                    uri = Uri.parse(songList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    getPathData(uri);
                    songTitle.setText(songList.get(position).getTitle());
                    songArtist.setText(songList.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int mposition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mposition);
                                nowTime.setText(timeFormat(mposition));
                            }
                            handler.postDelayed(this,1000);
                        }
                    });
                    floatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
                else {
                    if(position == 0){
                        position = songList.size() - 1;
                    }else {
                        position = position - 1;
                    }
                    uri = Uri.parse(songList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                    getPathData(uri);
                    songTitle.setText(songList.get(position).getTitle());
                    songArtist.setText(songList.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int mposition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mposition);
                                nowTime.setText(timeFormat(mposition));
                            }
                            handler.postDelayed(this,1000);
                        }
                    });
                    floatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();

                }
            }
        });

    }

    private void nextBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextThreadBtn();
            }
        };nextThread.start();
    }

    private void nextThreadBtn() {
        nextIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtnClicking();
            }
        });
    }

    private void nextBtnClicking() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mposition);
                        nowTime.setText(timeFormat(mposition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            floatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        }else{
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mposition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mposition);
                        nowTime.setText(timeFormat(mposition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            floatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();

        }
    }

    private void playBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                clickPlayBtn();
            }
        };playThread.start();
    }

    private void clickPlayBtn() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    floatingActionButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int mposition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mposition);
                                nowTime.setText(timeFormat(mposition));
                            }
                            handler.postDelayed(this,1000);
                        }
                    });
                }else {
                    floatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int mposition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mposition);
                                nowTime.setText(timeFormat(mposition));

                            }
                            handler.postDelayed(this,1000);
                        }
                    });
                }
            }
        });
    }
    public void AnimationImage(Context context, Bitmap bitmap, ImageView listenImage){
        Animation showOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation showIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        showOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(ListenActivity.this.bitmap).into(ListenActivity.this.listenImage);
                showIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ListenActivity.this.listenImage.startAnimation(showIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.listenImage.startAnimation(showOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicking();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}