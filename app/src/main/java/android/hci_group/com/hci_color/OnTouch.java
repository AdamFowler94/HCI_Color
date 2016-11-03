package android.hci_group.com.hci_color;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by adam on 03/11/16.
 */

public class OnTouch{

    private final Activity current;

    public OnTouch(Activity main) {
        current = main;
    }

    public void press(MotionEvent event) {
        Bitmap bitmap = ((BitmapDrawable)Main.imageView.getDrawable()).getBitmap();
        int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

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
        String hexCode1 = getNeighborColorHex(event, bitmap, Main.finderIcon);


        // Kelvins code
        // hexCode -> english
        Main.setColorText(r,g,b, hexCode1);
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
