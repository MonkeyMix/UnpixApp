package fr.unpix.com.POJO;

public class FbAlbum {
    private String idAlbum;
    private String albumName;
    private String albumUrl;

    public FbAlbum(){

    }
    public FbAlbum(String idAlbum, String albumName, String albumUrl) {
        this.idAlbum = idAlbum;
        this.albumName = albumName;
        this.albumUrl = albumUrl;
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
}
