package fr.unpix.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.unpix.com.POJO.ChatHeader;
import fr.unpix.com.adapters.AlbumAdapter;
import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.adapters.ChatListAdapter;
import fr.unpix.com.adapters.ImageAdapter;

public class ChatListActivity extends AppCompatActivity {

    ListView mListView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        recyclerView = findViewById(R.id.chatListRecyclerView);
        ImageView imageView1 = new ImageView(this);
        List<ChatHeader> chatList = new ArrayList<ChatHeader>(8);
        GridView gridView1 = new GridView(this);

        PixView pixView = new PixView();

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        Bitmap fille1 = BitmapFactory.decodeResource(getResources(), R.drawable.fille1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView1.setImageBitmap(fille1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ArrayList<Bitmap> imagesPic1 = pixView.crop(imageView1,this);
        gridView1.setAdapter(new ImageAdapter(this, imagesPic1));
        gridView1.setNumColumns((int) Math.sqrt(imagesPic1.size()));

        ChatHeader chat1 = new ChatHeader("Jessica",gridView1,"15");
        chatList.add(chat1);

        /*Bitmap fille2 = BitmapFactory.decodeResource(getResources(), R.drawable.fille2);
        ChatHeader chat2 = new ChatHeader("Lucie",fille2,"5");
        chatList.add(chat2);

        Bitmap fille3 = BitmapFactory.decodeResource(getResources(), R.drawable.fille3);
        ChatHeader chat3 = new ChatHeader("Jennifer",fille3,"0");
        chatList.add(chat3);

        Bitmap fille4 = BitmapFactory.decodeResource(getResources(), R.drawable.fille4);
        ChatHeader chat4 = new ChatHeader("Kadera",fille4,"3");
        chatList.add(chat4);

        chatList.add(chat2);
        chatList.add(chat1);
        chatList.add(chat3);
        chatList.add(chat4);*/
        recyclerView.setAdapter(new ChatListAdapter(chatList));
    }

}
