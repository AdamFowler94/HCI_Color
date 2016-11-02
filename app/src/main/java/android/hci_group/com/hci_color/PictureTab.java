package android.hci_group.com.hci_color;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PictureTab extends Fragment implements View.OnClickListener {

    private View PictureView;
    private Bitmap bitmap;
    private ImageView imageView;
    private ImageView finderIcon;

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PictureView = inflater.inflate(R.layout.activity_pic_tab,
                container, false);

        Button loadButton = (Button) PictureView.findViewById(R.id.load);

        finderIcon = (ImageView) PictureView.findViewById(R.id.finderIcon);

        loadButton.setOnClickListener(this);

        return PictureView;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void loadImagefromGallery(View view) {

        verifyStoragePermissions(Main.activity);

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = Main.context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Log.d("CHECK OUT THIS LOG",filePathColumn[0].toString());
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imageView = (ImageView) PictureView.findViewById(R.id.imageView);

                // Set the Image in ImageView after decoding the String
                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache(true);

                finderIcon.bringToFront();


                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                            motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                            bitmap = imageView.getDrawingCache();
                            int pixel = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());

                            int offset_w = finderIcon.getWidth() / 2;
                            int offset_h = finderIcon.getHeight() / 2;

                            finderIcon.setX(motionEvent.getX() - offset_w);
                            finderIcon.setY(motionEvent.getY() - offset_h);

                            int r = Color.red(pixel);
                            int g = Color.green(pixel);
                            int b = Color.blue(pixel);


                            // neighborhood
                            int count = 0;
                            int cur_pix;

                            int tot_r = 0;
                            int tot_g = 0;
                            int tot_b = 0;

                            Log.d("X:", String.valueOf(Math.round(motionEvent.getX())));
                            Log.d("Y:", String.valueOf(Math.round(motionEvent.getY())));

                            Log.d("X1", String.valueOf(Math.round(motionEvent.getX() + finderIcon.getWidth())));
                            Log.d("Y1", String.valueOf(Math.round(motionEvent.getY() + finderIcon.getHeight())));

                            Log.d("im w:", String.valueOf(imageView.getWidth()));
                            Log.d("im h:", String.valueOf(imageView.getHeight()));


//                            for (int i = Math.round(motionEvent.getX()); i < motionEvent.getX() + finderIcon.getWidth(); i++ ) {
//                                for (int j = Math.round(motionEvent.getY()); i < motionEvent.getY() + finderIcon.getHeight(); j++ ) {
//                                    cur_pix = bitmap.getPixel(i, j);
//                                    tot_r += Color.red(pixel);
//                                    tot_g += Color.green(pixel);
//                                    tot_b += Color.blue(pixel);
//                                    count++;
//                                }
//                            }
//
//                            int r = tot_r / count;
//                            int g = tot_g / count;
//                            int b = tot_b / count;


                            String hexCode = String.format("#%02x%02x%02x", r, g, b);

                            // Kelvins code
                            // hexCode -> english

                            Main.colorText.setText("R(" + r + ") G(" + g + ") B(" + b + ")\n " + hexCode);
                            Main.colorText.setBackgroundColor(Color.rgb(r,g,b));


                        }
                        return false;
                    }
                });

            } else {
                Toast.makeText(Main.activity, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("CHECK OUT THIS LOG", e.getMessage());
            Toast.makeText(Main.activity, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.load:
                loadImagefromGallery(v);
                break;
        }

    }
}
