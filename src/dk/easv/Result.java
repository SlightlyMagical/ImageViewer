package dk.easv;

public class Result {
    private final int imageIndex;
    private final PixelColor pixelColor;

    public Result(int imageIndex, PixelColor pixelColor) {
        this.imageIndex = imageIndex;
        this.pixelColor = pixelColor;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public PixelColor getPixelColor() {
        return pixelColor;
    }
}

