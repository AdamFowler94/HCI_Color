package android.hci_group.com.hci_color;

import android.view.MotionEvent;

/**
 * Created by adam on 03/11/16.
 */

public class MoveStuff {

    private int width;
    private int height;

    public MoveStuff() {

    }

    public void move(MotionEvent event) {

        // size of image view
        width = Main.imageView.getWidth();
        height = Main.imageView.getHeight();

        int offset_w = Main.finderIcon.getWidth() / 2;
        int offset_h = Main.finderIcon.getHeight() / 2;

        // X
        float x = event.getX() - offset_w;

        if (x > width - offset_w * 2) {
            x = width - offset_w * 2;
        }
        else if (x < 0) {
            x = 0;
        }

        // Y
        float y = event.getY() - offset_h;

        if (y > height - offset_h * 2) {
            y = height - offset_h * 2;
        }
        else if (y < 0) {
            y = 0;
        }

        Main.finderIcon.setX(x);
        Main.finderIcon.setY(y);

        Main.finderIcon.bringToFront();

    }

}
