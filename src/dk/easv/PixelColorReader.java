package dk.easv;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.awt.*;
import java.util.concurrent.Callable;

public class PixelColorReader implements Callable<Result> {
    private final int x;
    private final int y;
    private final Image image;
    private final int imageIndex;

    public PixelColorReader(int x, int y, Image image, int imageIndex) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.imageIndex = imageIndex;
    }

    @Override
    public Result call() throws Exception {
        PixelReader pixelReader = image.getPixelReader();
        Color color = new Color(pixelReader.getArgb(x, y));
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        PixelColor pixelColor;

        if (red > green && red > blue)
            pixelColor = PixelColor.RED;
        else if (green > blue && green > red)
            pixelColor = PixelColor.GREEN;
        else if (blue > red && blue > green)
            pixelColor = PixelColor.BLUE;
        else
            pixelColor = PixelColor.MIXED;

        return new Result(imageIndex, pixelColor);
    }
}
