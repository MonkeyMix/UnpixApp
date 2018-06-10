package fr.unpix.com.POJO;

import android.graphics.Bitmap;

public class FbAlbum {
    private String idAlbum;
    private String albumName;
    private String albumUrl;
    Bitmap albumPreview = null;

    public FbAlbum(){

    }

    public FbAlbum(String idAlbum, String albumName, String albumUrl, Bitmap icon11) {
        this.idAlbum = idAlbum;
        this.albumName = albumName;
        this.albumUrl = albumUrl;
        albumPreview = icon11;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public Bitmap getAlbumPreview() {
        return albumPreview;
    }

    public void setAlbumPreview(Bitmap albumPreview) {
        this.albumPreview = albumPreview;
    }
}
