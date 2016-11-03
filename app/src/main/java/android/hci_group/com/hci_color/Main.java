package android.hci_group.com.hci_color;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;

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

    public static final int CAMERA_REQUEST = 1888;
    public static final int RESULT_LOAD_IMG = 1;

    public static TextView colorText;

    public static ImageView imageView;
    public static ImageView finderIcon;

    private ImageStuff imageStuff;
    private OnTouch pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Permissions(this);
        pressed = new OnTouch(this);
        imageStuff = new ImageStuff(this);

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

    public static void setColorText(int r, int g, int b, String hexCode) {
        colorText.setText("R(" + r + ") G(" + g + ") B(" + b + ")\n " + hexCode);
        colorText.setBackgroundColor(Color.rgb(r,g,b));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When a picture is taken
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                imageStuff.setImage(data.getData());
            }
            // When an Image is picked
            else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                imageStuff.setImage(data.getData());
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
                imageStuff.takePicturewithCamera();
                break;

            // Button the take a picture
            case R.id.loadPicture:
                imageStuff.loadImagefromGallery();
                break;

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {

            pressed.press(event);

        }
        return true;
    }
}