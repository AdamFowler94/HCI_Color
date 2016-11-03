package android.hci_group.com.hci_color;
import java.util.HashMap;
//import java.util.Scanner;
import java.io.*;
//import java.lang.*;
import java.util.Set;
import android.graphics.Color;
public class ColorCompare {
    private HashMap<String, String> colorCode;
    private String closest_color;

    public ColorCompare(String hex) {
        colorCode = new HashMap<String, String>();

        colorCode.put("#FFFFFF", "white");
        colorCode.put("#FFFF00", "yellow");
        colorCode.put("#FF0000", "red");
        colorCode.put("#C0C0C0", "silver");
        colorCode.put("#808080", "gray");
        colorCode.put("#800080", "purple");
        colorCode.put("#00FFFF", "aqua");
        colorCode.put("#008000", "green");
        colorCode.put("#0000FF", "blue");
        colorCode.put("#000000", "black");


        Set<String> hexKeys = colorCode.keySet();

        int hexValues[] = getRGB(hex);

        int i = 0;
        String min_key = "";
        double min_dist = 1000.0;
        for (String key : hexKeys){

            // convert key to rgb
            int rgbValues[] = getRGB(key);

            double cur_dist = Math.sqrt(Math.pow(rgbValues[0] * 1.0 - hexValues[0] * 1.0, 2) +
                    Math.pow(rgbValues[1] *1.0 - hexValues[1] * 1.0, 2)
                    + Math.pow(rgbValues[2] *1.0 - hexValues[2]*1.0, 2));

            if (cur_dist < min_dist) {
                min_dist = cur_dist;
                min_key = key;
            }

            System.out.println(key);
            i++;
        }
        closest_color = min_key;
    }

    public int[] getRGB(final String hex) {
        int[] ret = new int[3];

        int color = Color.parseColor(hex);

        ret[0] = Color.red(color);
        ret[1] = Color.green(color);
        ret[2] = Color.blue(color);

        return ret;
    }

    public String getCloseColor() {
        return colorCode.get(closest_color);
    }
    public static void main(String[] args) {
        ColorCompare m = new ColorCompare("#FFFFFF");
        System.out.println("Closest Color: " + m.getCloseColor());
        // m.p();
    }
}