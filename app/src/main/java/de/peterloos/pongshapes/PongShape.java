package de.peterloos.pongshapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

abstract public class PongShape implements Shape {

    private Point origin;
    private int color;
    private Size size;

    public PongShape () {
        this.origin = new Point();
        this.color = Color.GRAY;
        this.size = new Size(0, 0);
    }

    @Override
    public Point getOrigin() {
        return this.origin;
    }

    @Override
    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    @Override
    public int getOriginX() {
        return this.origin.x;
    }

    @Override
    public int getOriginY() {
        return this.origin.y;
    }

    @Override
    public void moveOrigin (int dx, int dy) {
        this.origin.offset(dx, dy);
    }

    @Override
    public void setParentSize(Size size) {
        this.size = size;
    }

    @Override
    public int getParentWidth() {
        return this.size.getWidth();
    }

    @Override
    public int getParentHeight() {
        return this.size.getHeight();
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public abstract void draw(Canvas canvas);
}
