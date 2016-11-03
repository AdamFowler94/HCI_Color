package android.hci_group.com.hci_color;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;

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

    private ScaleGestureDetector mScaleDetector;

    private Button menuButton;
    private boolean inScale;
    private boolean hasImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Permissions(this);
        pressed = new OnTouch(this);
        imageStuff = new ImageStuff(this);

        // text to show the color name
        colorText = (TextView) findViewById(R.id.colorText);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);

        finderIcon = (ImageView) findViewById(R.id.finderIcon);

        scaleFactor = 1;
        inScale = false;

        hasImage = false;

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
                finderIcon.setScaleX(scaleFactor);
                finderIcon.setScaleY(scaleFactor);
                return true;
            }
        });

    }

    public static void setColorText(int r, int g, int b, String hexCode) {
        colorText.setText("R(" + r + ") G(" + g + ") B(" + b + ")\n " + hexCode);
        colorText.setBackgroundColor(Color.rgb(r,g,b));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When a picture is taken
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                imageStuff.setImage(data.getData());
                hasImage = true;
            }
            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                imageStuff.setImage(data.getData());
                hasImage = true;
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

                    case R.id.options:
                        // open the options
                        break;

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

            moveSquare(event);

            if (hasImage) {
                pressed.press(event);
            }

        }
        return true;
    }

    private void moveSquare(MotionEvent event) {


        int offset_w = finderIcon.getWidth() / 2;
        int offset_h = finderIcon.getHeight() / 2;

        // size of image view
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        // X
        float x = event.getX() - offset_w;

        if (x > width - offset_w) {
            x = width - offset_w;
        }
        else if (x < offset_w) {
            x = offset_w;
        }

        // Y
        float y = event.getY() - offset_h;

        if (y > height - offset_h) {
            y = height - offset_h;
        }
        else if (y < offset_h) {
            y = offset_h;
        }

        Main.finderIcon.setX(x);
        Main.finderIcon.setY(y);


    }
}