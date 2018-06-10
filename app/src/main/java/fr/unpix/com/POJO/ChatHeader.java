package fr.unpix.com.POJO;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

public class ChatHeader{
    String name;
    String nbPix;
    GridView picMatch;

    public ChatHeader(String name, GridView picMatch, String nbPix){
        this.name = name;
        this.picMatch = picMatch;
        this.nbPix = nbPix;
    }

    public String getName() {
        return name;
    }

    public String getNbPix() {
        return nbPix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GridView getPicMatch() {
        return picMatch;
    }

    public void setPicMatch(GridView picMatch){
        this.picMatch = picMatch;
    }
}
