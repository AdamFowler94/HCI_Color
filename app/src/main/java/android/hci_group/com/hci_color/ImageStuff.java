package android.hci_group.com.hci_color;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Display;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adam on 03/11/16.
 */

public class ImageStuff {

    private final Activity current;

    public ImageStuff(Activity main) {
        current = main;
    }

    public void takePicturewithCamera() {

        Intent cameraIntent= new Intent("android.media.action.IMAGE_CAPTURE");
        current.startActivityForResult(cameraIntent, Main.CAMERA_REQUEST);
    }

    public void loadImagefromGallery() {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        current.startActivityForResult(galleryIntent, Main.RESULT_LOAD_IMG);

    }

    public void setImage(Uri data) throws IOException {

        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = current.getContentResolver().query(data,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

        Display display = current.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Main.imageView.getLayoutParams().width = width;
        Main.imageView.getLayoutParams().height = height;

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,height,width,true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(),
                scaledBitmap.getHeight(), matrix, true);

        Main.imageView.setImageBitmap(rotatedBitmap);

        Main.imageView.setDrawingCacheEnabled(true);
        Main.imageView.buildDrawingCache(true);

    }

}
