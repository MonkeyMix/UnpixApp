package fr.unpix.com.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.unpix.com.POJO.ChatHeader;
import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.R;
import fr.unpix.com.holders.AlbumViewHolder;
import fr.unpix.com.holders.ChatListViewHolder;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder>  {
    List<ChatHeader> chatList;

    public ChatListAdapter (List<ChatHeader> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list,parent,false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        ChatHeader myObject = chatList.get(position);
        holder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
