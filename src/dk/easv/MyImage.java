package dk.easv;

import javafx.scene.image.Image;

public class MyImage {
    private final String name;
    private final Image image;
    private int redPixels;
    private int greenPixels;
    private int bluePixels;
    private int mixedPixels;

    public MyImage(String name, Image image) {
        this.name = name;
        this.image = image;
        redPixels = 0;
        greenPixels = 0;
        bluePixels = 0;
        mixedPixels = 0;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getRedPixels() {
        return redPixels;
    }

    public void addRedPixel(){
        redPixels++;
    }

    public int getGreenPixels() {
        return greenPixels;
    }

    public void addGreenPixel(){
        greenPixels++;
    }

    public int getBluePixels() {
        return bluePixels;
    }

    public void addBluePixel(){
        bluePixels++;
    }

    public int getMixedPixels() {
        return mixedPixels;
    }

    public void addMixedPixel(){
        mixedPixels++;
    }
}
