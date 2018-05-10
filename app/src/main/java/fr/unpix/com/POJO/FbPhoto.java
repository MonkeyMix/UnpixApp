package fr.unpix.com.POJO;

public class FbPhoto {

    private String idPhoto;
    private String photoUrl;

    public FbPhoto(){

    }
    public FbPhoto(String idPhoto, String photoUrl) {
        this.idPhoto = idPhoto;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }
}
