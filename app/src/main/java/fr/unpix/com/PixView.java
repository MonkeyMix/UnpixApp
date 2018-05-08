package fr.unpix.com;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.login.widget.LoginButton;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;

public class PixView extends AppCompatActivity {
    private ImageView photoImageView;
    private Button blurButton;
    private BlurImageView blurImage;
    private ArrayList<Bitmap> pieces;

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

    public void crop(View view) {
        int piecesNumber = 12;
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<Bitmap> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        //int croppedImageWidth = scaledBitmapWidth - 2 * Math.abs(scaledBitmapLeft);
        //int croppedImageHeight = scaledBitmapHeight - 2 * Math.abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        //Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, Math.abs(scaledBitmapLeft), Math.abs(scaledBitmapTop), scaledBitmapWidth, scaledBitmapHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = scaledBitmapWidth/cols;
        int pieceHeight = scaledBitmapHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        int x =0;
        int y=0;
        int deltaY = 0;
        int constantex = 270;
        int constantey = scaledBitmapHeight;
        for (int row = 0; row < rows; row++) {
            int deltaX = 0;
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                ImageView iv = new ImageView(getApplicationContext());
                iv.setX(xCoord + constantex + deltaX);
                iv.setY(yCoord + constantey + deltaY);
                iv.setImageBitmap(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, pieceWidth, pieceHeight));
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.pixview);
                layout.addView(iv);
                xCoord += pieceWidth;
                deltaX = deltaX + 3;
            }
            deltaY = deltaY + 3;
            yCoord += pieceHeight;
        }
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }
}
