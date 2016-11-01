package android.hci_group.com.hci_color;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by adam on 25/10/16.
 */

public class Main extends FragmentActivity implements View.OnClickListener {

    public static Context context;
    public static Activity activity;

    public static final String CAMERA_VIEW = "Camera";
    public static final String PICTURE_VIEW = "Picture";

    private Fragment view;

    private CameraTab cameraTab;
    private PictureTab pictureTab;

    public static TextView colorText;

    private Button contextSwitch;

    private String display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        activity = this;

        cameraTab = new CameraTab();
        cameraTab.setArguments(getIntent().getExtras());

        pictureTab = new PictureTab();
        pictureTab.setArguments(getIntent().getExtras());

        // text to show the color name
        colorText = (TextView) findViewById(R.id.colorText);

        // button that switched from camera to picture mode
        contextSwitch = (Button) findViewById(R.id.contextSwitch);
        contextSwitch.setOnClickListener(this);

        // sets the current contect to camera view when the app is opened
        display = CAMERA_VIEW;
        showContext();

    }
    public void setColorText(String color) {
        colorText.setText(color);
    }

    // Switched the context to what the button is currently displaying
    private void switchContext() {
        display = (String) contextSwitch.getText();
        showContext();
    }
    // shows the context appripriate to the current one
    private void showContext() {
        switch (display) {

            case CAMERA_VIEW:
                view = new CameraTab();
                contextSwitch.setText("Picture");
                break;

            case PICTURE_VIEW:
                view = new PictureTab();
                contextSwitch.setText("Camera");
                break;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.tabView, view);
        transaction.commit();

    }

    // onClick listner callback function
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // Button the switch the context
            case R.id.contextSwitch:
                switchContext();
                break;

        }

    }

}