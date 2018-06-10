package fr.unpix.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import fr.unpix.com.adapters.AlbumAdapter;
import fr.unpix.com.POJO.FbAlbum;

public class AlbumPickerActivity extends AppCompatActivity {

    ListView mListView;
    private List<FbAlbum> fbAlbums;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_picker);
        recyclerView = findViewById(R.id.recyclerView);


        fbAlbums = new ArrayList<FbAlbum>();

        if(fbAlbums.size() == 0) {
            try {
                getAlbums();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new AlbumAdapter(fbAlbums));

    }

    private void getAlbums() throws InterruptedException {
        final GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(object!=null) {
                            try {
                                JSONObject obj = object.getJSONObject("albums");
                                Log.i("test",object.getJSONObject("albums").toString());
                                JSONArray jArray = obj.getJSONArray("data");
                                for(int i =0;i<jArray.length();i++){
                                    FbAlbum album = new FbAlbum();
                                    JSONObject dataObj = jArray.getJSONObject(i);
                                    album.setIdAlbum(dataObj.getString("id"));
                                    album.setAlbumName(dataObj.getString("name"));

                                    Log.i("INFO",dataObj.toString());

                                    Bitmap mIcon11 = null;
                                    try {
                                        InputStream in = new java.net.URL("https://graph.facebook.com/" + album.getIdAlbum() + "/picture?/picture?type=normal"
                                                + "&access_token=" + AccessToken.getCurrentAccessToken().getToken()).openStream();
                                        mIcon11 = BitmapFactory.decodeStream(in);
                                    } catch (Exception e) {
                                        //Log.e("Error", e.getMessage());
                                        //e.printStackTrace();
                                    }
                                    //album.setAlbumPreview(Glide.with(getActivity().getApplicationContext()).load(myAlbum.getAlbumUrl()).asBitmap());
                                    album.setAlbumUrl("https://graph.facebook.com/" + album.getIdAlbum() + "/picture?type=small"
                                            + "&access_token=" + AccessToken.getCurrentAccessToken().getToken());
                                    album.setAlbumPreview(mIcon11);

                                    fbAlbums.add(album);
                                }
                                //lv.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else
                            System.out.println("Impossible to get albums");
                    }
                });
        // Run facebook graphRequest.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "albums{id,name,message}");
        request.setParameters(parameters);
        request.executeAsync();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GraphResponse gResponse = request.executeAndWait();
            }
        });
        t.start();
        t.join();
    }

}
