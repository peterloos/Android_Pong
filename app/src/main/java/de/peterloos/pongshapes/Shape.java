package de.peterloos.pongshapes;

import android.graphics.Canvas;
import android.graphics.Point;

public interface Shape {

    /**
     * center of shape
     * paddle: upper left corner
     * ball:   center point
     */
    Point getOrigin();
    void setOrigin(Point p);
    int getOriginX();
    int getOriginY();
    void moveOrigin(int dx, int dy);

    /* size of surrounding view */
    void setParentSize(Size size);
    int getParentWidth();
    int getParentHeight();

    /* background color of shape */
    int getColor();
    void setColor(int color);

    /* drawing method */
    void draw(Canvas canvas);
}
