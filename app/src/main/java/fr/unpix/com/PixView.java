package fr.unpix.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.widget.LoginButton;
import com.jgabrielfreitas.core.BlurImageView;

public class PixView extends AppCompatActivity {
    private ImageView photoImageView;
    private Button blurButton;
    private BlurImageView blurImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pix_view);
        blurButton = findViewById(R.id.flouter);
        blurImage = (BlurImageView) findViewById(R.id.imageView);
    }

    public void blur(View view) {
        blurImage.setBlur(7);
    }
}
