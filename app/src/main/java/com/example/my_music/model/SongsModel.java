package com.example.my_music.model;

public class SongsModel {
    String title;
    String artist;
    String duration;
    String path;
    String album;

    public SongsModel(String title, String artist, String duration, String path, String album) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public SongsModel() {

    }
}
