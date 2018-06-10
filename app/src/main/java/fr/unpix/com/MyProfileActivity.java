package fr.unpix.com;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unpix.com.POJO.FbAlbum;
import fr.unpix.com.POJO.FbPhoto;
import fr.unpix.com.adapters.ImageAdapter;

public class MyProfileActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextInputEditText nameTextView;
    private ImageView photoImageView;
    private ImageView photoImageView2;
    private ImageView photoImageView3;
    private ImageView photoImageView4;
    private ImageView photoImageView5;
    private List<FbAlbum> fbAlbums;
    private List<FbPhoto> fbPhotos;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    private Map<Integer,ImageView> userImageViewList;
    private Map<Integer,Integer> userImageViewCodeList;
    private File directory;
    private ImageView selectedImageView;
    private FileOutputStream fOut;
    private final static String filename = "image_";
    FileInputStream fin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.textNameInputEditText);
        photoImageView = findViewById(R.id.photoImageView);
        photoImageView2 = findViewById(R.id.photoImageView2);
        photoImageView3 = findViewById(R.id.photoImageView3);
        photoImageView4 = findViewById(R.id.photoImageView4);
        photoImageView5 = findViewById(R.id.photoImageView5);
        userImageViewList = new HashMap<Integer,ImageView>(5);
        userImageViewList.put(1,photoImageView);
        userImageViewList.put(2,photoImageView2);
        userImageViewList.put(3,photoImageView3);
        userImageViewList.put(4,photoImageView4);
        userImageViewList.put(5,photoImageView5);
        userImageViewCodeList = new HashMap<Integer,Integer>(5);
        userImageViewCodeList.put(R.id.photoImageView,1);
        userImageViewCodeList.put(R.id.photoImageView2,2);
        userImageViewCodeList.put(R.id.photoImageView3,3);
        userImageViewCodeList.put(R.id.photoImageView4,4);
        userImageViewCodeList.put(R.id.photoImageView5,5);

        user = FirebaseAuth.getInstance().getCurrentUser();
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

            //getAlbums();
            if(fbAlbums != null) {
                for (FbAlbum fbAlbum : fbAlbums) {
                    //getPhotosFromAlbumId(fbAlbum.getIdAlbum());
                }
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                int i=0;
                for ( i = 0 ; i<=5; i++){
                    Bitmap b = null;
                    try {
                        FileInputStream fin = openFileInput(filename+i);
                        b = BitmapFactory.decodeStream(fin);
                        StorageReference storageReference = storageRef.child("images/"+user.getUid()+"/image_"+i);
                        putPictureInImageView(storageReference,i,b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                //Glide.with(this).load(fbPhotos.get(0).getPhotoUrl()).asBitmap().centerCrop().into(cardview);
                //Glide.with(this).load(fbPhotos.get(8).getPhotoUrl()).asBitmap().override(1000, 1000).centerCrop().into(photoImageView2);
                // Glide.with(this).load(fbPhotos.get(1).getPhotoUrl()).asBitmap().override(1000, 1000).centerCrop().into(photoImageView3);
                //Glide.with(this).load(fbPhotos.get(2).getPhotoUrl()).asBitmap().override(1000, 1000).centerCrop().into(photoImageView4);
            }

            //PREVIEW SMALL USER IMAGES
            FloatingActionButton fab1 = findViewById(R.id.FAB1);
            fab1.setOnTouchListener(this);
            FloatingActionButton fab2 = findViewById(R.id.FAB2);
            fab2.setOnTouchListener(this);
            FloatingActionButton fab3 = findViewById(R.id.FAB3);
            fab3.setOnTouchListener(this);
            FloatingActionButton fab4 = findViewById(R.id.FAB4);
            fab4.setOnTouchListener(this);
            FloatingActionButton fab5 = findViewById(R.id.FAB5);
            fab5.setOnTouchListener(this);

            nameTextView.setText(name.substring(0, name.lastIndexOf(' ')));
            nameTextView.setKeyListener(null);
            //emailTextView.setText(email);
            //uidTextView.setText(uid);
            //Glide.with(this).load(photoUrl).asBitmap().centerCrop().into(photoImageView);
        } else {
            goLoginScreen();
        }
    }

    private void putPictureInImageView(StorageReference storageReference,final int i, Bitmap btmp) {
        final ImageView imgView = userImageViewList.get(i);
        if (btmp != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            btmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(MyProfileActivity.this).load(stream.toByteArray()).asBitmap().centerCrop().into(imgView);
        }
        else{
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MyProfileActivity.this).load(uri.toString()).asBitmap().centerCrop().into(imgView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // failed
                }
            });
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
                                photo.setPhotoUrl("https://graph.facebook.com/" + photo.getIdPhoto() + "/picture?access_token=" + AccessToken.getCurrentAccessToken().getToken());
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
        Intent intent = new Intent(this, ChatListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void uploadPictures(final byte[] pictureByteArray, final String tag) throws IOException, URISyntaxException {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+user.getUid()+"/"+filename+tag);
        imagesRef.putBytes(pictureByteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        try {
                            fOut = openFileOutput(filename+tag,Context.MODE_PRIVATE);
                            fOut.write(pictureByteArray);
                            fOut.close();
                        } catch (IOException e ) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //Toast.makeText( exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("erreur"+ exception);
                    }
                });
    }

    public void pickPicture(View view) {
        selectedImageView = (ImageView)view;
        showPictureDialog();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera",
                "Select from Facebook"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                            case 2 :
                                choosePhotoFromFacebook();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    public void choosePhotoFromFacebook(){
        Intent intent = new Intent(this, AlbumPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(this).load(stream.toByteArray()).asBitmap().centerCrop().into(selectedImageView);
                    uploadPictures(stream.toByteArray(),userImageViewCodeList.get(selectedImageView.getId()).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MyProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Toast.makeText(MyProfileActivity.this, "Failed to save Picture", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Boolean returnValue =true;
        PixView pixView = new PixView();
        ViewSwitcher viewSwitcher = null;
        ImageView tempImageView = new ImageView(this);
        ImageView picImageView = new ImageView(this);
        Bitmap bitmap = null;
        GridView grid = null;
        switch (v.getId()){
            case R.id.FAB1:
                viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
                bitmap = ((BitmapDrawable)photoImageView.getDrawable()).getBitmap();
                grid = (GridView) findViewById(R.id.gridView);
                break;
            case R.id.FAB2:
                viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher2);
                bitmap = ((BitmapDrawable)photoImageView2.getDrawable()).getBitmap();
                grid = (GridView) findViewById(R.id.gridView2);
                break;
            case R.id.FAB3:
                viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher3);
                bitmap = ((BitmapDrawable)photoImageView3.getDrawable()).getBitmap();
                grid = (GridView) findViewById(R.id.gridView3);
                break;
            case R.id.FAB4:
                viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher4);
                bitmap = ((BitmapDrawable)photoImageView4.getDrawable()).getBitmap();
                grid = (GridView) findViewById(R.id.gridView4);
                break;
            case R.id.FAB5:
                viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher5);
                bitmap = ((BitmapDrawable)photoImageView5.getDrawable()).getBitmap();
                grid = (GridView) findViewById(R.id.gridView5);
                break;
        }
        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
            viewSwitcher.showPrevious();
            returnValue= false;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN){
            picImageView.setImageBitmap(bitmap);
            ArrayList<Bitmap> images = pixView.crop(picImageView,MyProfileActivity.this);
            //Getting the grid view and setting an adapter to it
            grid.setAdapter(new ImageAdapter(MyProfileActivity.this, images));
            grid.setNumColumns((int) Math.sqrt(images.size()));
            viewSwitcher.showNext();
            returnValue= true;
        }
        return returnValue;
    }
}