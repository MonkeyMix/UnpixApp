package fr.unpix.com.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewView;
    public ImageView imageView;

    //itemView est la vue correspondante à 1 cellule
    public AlbumViewHolder(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView

        textViewView = itemView.findViewById(R.id.text);
        imageView = itemView.findViewById(R.id.image);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(FbAlbum myAlbum){
        textViewView.setText(myAlbum.getAlbumName());
        //Glide.with(this).load(myAlbum.getAlbumUrl()).asBitmap().centerCrop().into(imageView);
        imageView.setImageBitmap(myAlbum.getAlbumPreview());
    }

}