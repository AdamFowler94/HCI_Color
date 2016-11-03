package android.hci_group.com.hci_color;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by adam on 25/10/16.
 */

public class Main extends Activity implements View.OnClickListener, View.OnTouchListener {

    public static final int CAMERA_REQUEST = 1888;
    public static final int RESULT_LOAD_IMG = 1;

    public static TextView colorText;

    public static ImageView imageView;
    public static ImageView finderIcon;
    private float scaleFactor;

    public static ImageStuff imageStuff;
    private OnTouch pressed;
    private MoveStuff moveStuff;

    private ScaleGestureDetector mScaleDetector;

    private Button menuButton;
    private boolean inScale;
    private int hasImage;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Permissions(this);

        // text to show the color name
        colorText = (TextView) findViewById(R.id.colorText);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);

        finderIcon = (ImageView) findViewById(R.id.finderIcon);

        scaleFactor = 1;
        inScale = false;

        hasImage = 0;

        menuButton = (Button) findViewById(R.id.menu);
        menuButton.setOnClickListener(this);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                inScale = false;
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                inScale = true;
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                if (scaleFactor > 20) {
                    scaleFactor = 20;
                }
                finderIcon.getLayoutParams().width = 20 + (int) (10 * scaleFactor);
                finderIcon.getLayoutParams().height = 20 + (int) (10 * scaleFactor);
                finderIcon.requestLayout();
                return true;
            }
        });

        pressed = new OnTouch(this);
        imageStuff = new ImageStuff(this);
        moveStuff = new MoveStuff();

    }

    public static void setColorText(int r, int g, int b, String hexCode) {
        colorText.setText("R(" + r + ") G(" + g + ") B(" + b + ")\n " + hexCode);
        colorText.setBackgroundColor(Color.rgb(r,g,b));
        //colorText.setTextColor(Color.rgb());
        colorText.bringToFront();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When a picture is taken
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                photoUri = data.getData();
                imageStuff.setImage(photoUri);
                hasImage = 1;
            }
            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                photoUri = data.getData();
                imageStuff.setImage(photoUri);
                hasImage = 1;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void makeMenu() {
        PopupMenu menu = new PopupMenu(this, menuButton);

        menu.getMenuInflater().inflate(R.menu.activity_menu, menu.getMenu());

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // Menu Buttons

                    case R.id.takePicture:
                        imageStuff.takePicturewithCamera();
                        break;

                    case R.id.loadPicture:
                        imageStuff.loadImagefromGallery();
                        break;

                    /*
                    case R.id.options:
                        // open the options
                        break;
                    */
                }
                return true;
            }
        });

        menu.show();

    }

    // onClick listner callback function
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // Menu button
            case R.id.menu:
                makeMenu();
                break;

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mScaleDetector.onTouchEvent(event);

        if ((event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) && (!inScale)) {

            moveStuff.move(event);

            if (hasImage == 1) {
                pressed.press(event);
            }

        }
        return true;
    }
}