package fr.unpix.com.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.holders.AlbumViewHolder;
import fr.unpix.com.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

    List<FbAlbum> list;

    public AlbumAdapter(List<FbAlbum> list) {
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_album,viewGroup,false);
        return new AlbumViewHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    @Override
    public void onBindViewHolder(AlbumViewHolder myAlbumViewHolder, int position) {
        FbAlbum myObject = list.get(position);
        myAlbumViewHolder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

