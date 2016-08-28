package de.peterloos.pongshapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import de.peterloos.pong.Globals;

public class Ball extends PongShape {

    protected Paint ballPaint;
    protected BallDirection direction;
    protected float xOfs;
    protected float yOfs;

    // c'tor
    public Ball() {

        this.setColor(Globals.BallColor);

        this.ballPaint = new Paint();
        this.ballPaint.setColor(this.getColor());

        this.setOrigin(new Point(0, 0));

        this.xOfs = Globals.BallSpeed;
        this.yOfs = -Globals.BallSpeed;
        this.direction = BallDirection.Rightwards;
    }

    // getter/setter
    public BallDirection getDirection() {
        return this.direction;
    }

    // public interface
    public void init() {

        /* position ball in center of view */
        this.setOrigin(new Point(this.getParentWidth() / 2, this.getParentHeight() / 2));
    }

    public void move() {

        if (this.getOriginY() <= Globals.BallRadius) {
            this.yOfs *= -1;
        } else if (this.getOriginY() >= this.getParentHeight() - Globals.BallRadius) {
            this.yOfs *= -1;
        }

        // this.origin.offset((int) this.xOfs, (int) this.yOfs);
        this.moveOrigin((int) this.xOfs, (int) this.yOfs);
    }

    public void changeDirection() {
        this.xOfs *= -1;
        this.direction = (this.direction == BallDirection.Leftwards) ?
                BallDirection.Rightwards : BallDirection.Leftwards;
    }

    public boolean ballIsInCriticalArea(int x) {

        if (x < Globals.PaddleLeft + Globals.PaddleWidth
                + Globals.BallRadius + Globals.ToleranceDistance) {
            return true;
        } else if (x > this.getParentWidth() - Globals.PaddleLeft - Globals.PaddleWidth
                - Globals.BallRadius - Globals.ToleranceDistance) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(
            this.getOriginX(),
            this.getOriginY(),
            Globals.BallRadius,
            this.ballPaint);
    }
}
