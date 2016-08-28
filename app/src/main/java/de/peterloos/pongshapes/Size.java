package de.peterloos.pongshapes;

/**
 * helper class for describing width and height dimensions in pixels
 */
public class Size {

    private int width;
    private int height;

    public Size(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = (width >= 0) ? width : 0;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = (height >= 0) ? height : 0;
    }
}
