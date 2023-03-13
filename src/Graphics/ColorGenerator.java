package Graphics;

import java.awt.Color;
import java.util.Random;

public class ColorGenerator {

    private static Random rand;

    public ColorGenerator() {

    }

    public static Color getRandomColor() {
        rand = new Random();
        // Generate random integers in range 0 to 255
        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);

        // Create a new Color object
        Color color = new Color(red, green, blue);

        return color;
    }
}