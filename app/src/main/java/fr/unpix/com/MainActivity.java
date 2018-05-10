package fr.unpix.com;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.POJO.FbPhoto;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private ImageView photoImageView;
    private ImageView photoImageView2;
    private ImageView photoImageView3;
    private ImageView photoImageView4;
    private List<FbAlbum> fbAlbums;
    private List<FbPhoto> fbPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        uidTextView = (TextView) findViewById(R.id.idTextView);
        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        photoImageView2 = (ImageView) findViewById(R.id.photoImageView2);
        photoImageView3 = (ImageView) findViewById(R.id.photoImageView3);
        photoImageView4 = (ImageView) findViewById(R.id.photoImageView4);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String facebookUserId ="";
        fbAlbums = new ArrayList<FbAlbum>();
        fbPhotos = new ArrayList<FbPhoto>();

        if (user != null) {
            for(UserInfo profile : user.getProviderData()) {
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())){
                    facebookUserId = profile.getUid();
                }
            }
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
            System.out.println(photoUrl);
            String uid = user.getUid();

            try {
                getAlbums();
                if(fbAlbums != null) {
                    for (FbAlbum fbAlbum : fbAlbums) {
                        getPhotosFromAlbumId(fbAlbum.getIdAlbum());
                        System.out.println("id album :" +fbAlbum.getIdAlbum());
                    }
                    Glide.with(this).load(fbPhotos.get(0).getPhotoUrl()).into(photoImageView2);
                    Glide.with(this).load(fbPhotos.get(1).getPhotoUrl()).into(photoImageView3);
                    Glide.with(this).load(fbPhotos.get(2).getPhotoUrl()).into(photoImageView4);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nameTextView.setText(name);
            emailTextView.setText(email);
            uidTextView.setText(uid);
            Glide.with(this).load(photoUrl).into(photoImageView);
        } else {
            goLoginScreen();
        }
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
                                JSONArray jArray = obj.getJSONArray("data");
                                for(int i =0;i<jArray.length();i++){
                                    FbAlbum album = new FbAlbum();
                                    JSONObject dataObj = jArray.getJSONObject(i);
                                    album.setIdAlbum(dataObj.getString("id"));
                                    album.setAlbumName(dataObj.getString("name"));
                                    /*if(dataObj.getString("cover_photo") != null){
                                        album.setAlbumUrl("https://graph.facebook.com/" + dataObj.getString("cover_photo") + "/picture?type=normal"
                                                + "&access_token=" + AccessToken.getCurrentAccessToken().getToken());
                                    }*/
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
        parameters.putString("fields", "albums{id,name}");
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

    private void getPhotosFromAlbumId(String albumId ) throws InterruptedException {
        final GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+albumId+"/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try{
                        JSONObject object = response.getJSONObject();
                        JSONArray jArray = object.getJSONArray("data");
                        for(int i =0;i<jArray.length();i++){
                            FbPhoto photo = new FbPhoto();
                            JSONObject dataObj = jArray.getJSONObject(i);
                            photo.setIdPhoto(dataObj.getString("id"));
                            photo.setPhotoUrl("https://graph.facebook.com/" + photo.getIdPhoto() + "/picture?height=500&access_token=" + AccessToken.getCurrentAccessToken().getToken());
                            fbPhotos.add(photo);
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }});
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
    private String makeFacebookPhotoURL(String id, String accessToken ) {
        return "https://graph.facebook.com/" + id + "/picture?access_token=" + accessToken;
    }
    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    public void goPixView(View view) {
        Intent intent = new Intent(this, PixView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}