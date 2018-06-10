package fr.unpix.com.holders;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.PrivateKey;

import fr.unpix.com.POJO.ChatHeader;
import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.R;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    private GridView picMatch;
    private TextView nameMatch;
    private TextView nbPix;

    public ChatListViewHolder(View itemView) {
        super(itemView);
        nameMatch = itemView.findViewById(R.id.name);
        picMatch = itemView.findViewById(R.id.gridView);
        nbPix = itemView.findViewById(R.id.text);
    }

    public void bind(ChatHeader aChat){
        nameMatch.setText(aChat.getName());
        nbPix.setText(aChat.getNbPix());
        //Glide.with(this).load(myAlbum.getAlbumUrl()).asBitmap().centerCrop().into(imageView);
        picMatch.setAdapter(aChat.getPicMatch().getAdapter());
        picMatch.setNumColumns(aChat.getPicMatch().getNumColumns());
    }

}
