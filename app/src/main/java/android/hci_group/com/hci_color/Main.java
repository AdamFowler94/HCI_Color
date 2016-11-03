package android.hci_group.com.hci_color;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by adam on 25/10/16.
 */

public class Main extends Activity implements View.OnClickListener, View.OnTouchListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMG = 1;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView colorText;

    private ImageView imageView;
    private ImageView finderIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyPermissions(this);

        // text to show the color name
        colorText = (TextView) findViewById(R.id.colorText);

        // button to take a picture with
        Button takePicture = (Button) findViewById(R.id.takePicture);
        takePicture.setOnClickListener(this);

        // button to load a picture with
        Button loadPicture = (Button) findViewById(R.id.loadPicture);
        loadPicture.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);

        finderIcon = (ImageView) findViewById(R.id.finderIcon);

    }

    public void setColorText(int r, int g, int b, String hexCode) {
        colorText.setText("R(" + r + ") G(" + g + ") B(" + b + ")\n " + hexCode);
        colorText.setBackgroundColor(Color.rgb(r,g,b));
    }

    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST);
        }

        // Check if we have write permission
        permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void takePicturewithCamera() {

        Intent cameraIntent= new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    private void loadImagefromGallery() {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    private void setImage(Bitmap photo) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Toast.makeText(this,Integer.toString(size.x)+" : "+Integer.toString(size.y),
                Toast.LENGTH_SHORT).show();

        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = height;

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(photo,height,width,true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        imageView.setImageBitmap(rotatedBitmap);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        finderIcon.bringToFront();
        colorText.bringToFront();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When a picture is taken
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                setImage(photo);
            }
            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Log.d("CHECK OUT THIS LOG",filePathColumn[0].toString());
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                Bitmap photo = BitmapFactory.decodeFile(imgDecodableString);
                setImage(photo);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // onClick listner callback function
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // Button to take a picture
            case R.id.takePicture:
                takePicturewithCamera();
                break;

            // Button the take a picture
            case R.id.loadPicture:
                loadImagefromGallery();
                break;

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {

            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

            int offset_w = finderIcon.getWidth() / 2;
            int offset_h = finderIcon.getHeight() / 2;

            finderIcon.setX(event.getX() - offset_w);
            finderIcon.setY(event.getY() - offset_h);

            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);

            // neighborhood
            int count = 0;
            int cur_pix;

            int tot_r = 0;
            int tot_g = 0;
            int tot_b = 0;

            // single pix
            String hexCode = String.format("#%02x%02x%02x", r, g, b);

            // neighbourhood
            String hexCode1 = getNeighborColorHex(event, bitmap, finderIcon);


            // Kelvins code
            // hexCode -> english
            setColorText(r,g,b, hexCode1);

        }
        return false;
    }
    // helper function to get neighborhood of pixels
    private String getNeighborColorHex(MotionEvent motionEvent, Bitmap bitmap, ImageView finderIcon) {
        String hex;

        // neighborhood
        int count = 0;
        int cur_pix;
        int tot_r = 0;
        int tot_g = 0;
        int tot_b = 0;



        Log.d("X:", String.valueOf(Math.round(motionEvent.getX())));
        Log.d("Y:", String.valueOf(Math.round(motionEvent.getY())));

        Log.d("iconX:", String.valueOf(finderIcon.getX()));
        Log.d("iconY:", String.valueOf(finderIcon.getY()));

        Log.d("X1", String.valueOf(Math.round(finderIcon.getX() + finderIcon.getWidth())));
        Log.d("Y1", String.valueOf(Math.round(finderIcon.getY() + finderIcon.getHeight())));

        Log.d("im w:", String.valueOf(bitmap.getWidth()));
        Log.d("im h:", String.valueOf(bitmap.getHeight()));


        // make sure view doesn't go outside of bitmap
        int end_X = 0;
        if (finderIcon.getX() + finderIcon.getWidth() > bitmap.getWidth())
            end_X = bitmap.getWidth();
        else
            end_X = Math.round(finderIcon.getX() + finderIcon.getWidth());

        int end_Y = 0;
        if (finderIcon.getY() + finderIcon.getHeight() > bitmap.getHeight())
            end_Y = bitmap.getHeight();
        else
            end_Y = Math.round(finderIcon.getY() + finderIcon.getHeight());


        // loop through all pixels in neighbourhood
        for (int i = Math.round(finderIcon.getX()); i <  end_X; i++ ) {
            for (int j = Math.round(finderIcon.getY()); j < end_Y; j++ ) {
                cur_pix = bitmap.getPixel(i, j);
                tot_r += Color.red(cur_pix);
                tot_g += Color.green(cur_pix);
                tot_b += Color.blue(cur_pix);
                count++;
            }
        }

        // AVG RGB
        int r = tot_r / count;
        int g = tot_g / count;
        int b = tot_b / count;

        // Convert and return to hex
        hex = String.format("#%02x%02x%02x", r, g, b);

        return hex;
    }
}