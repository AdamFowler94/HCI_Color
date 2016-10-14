package android.hci_group.com.hci_color;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Main extends Activity {

    ViewGroup mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (ViewGroup) findViewById(R.id.activity_main);

        showReticule();

    }

    private void showReticule() {

        View reticule = new View(this);

        reticule.setBackgroundResource(R.drawable.reticule_style);

        RelativeLayout.LayoutParams reticuleLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        reticuleLayout.width = 100;
        reticuleLayout.height = 100;

        reticuleLayout.addRule(RelativeLayout.CENTER_IN_PARENT);

        reticule.setLayoutParams(reticuleLayout);

        mainView.addView(reticule);

    }
}
