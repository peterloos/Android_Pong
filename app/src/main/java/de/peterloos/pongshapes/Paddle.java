package de.peterloos.pongshapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import de.peterloos.pong.Globals;

public class Paddle extends PongShape {

    private PaddleType type;
    private Rect paddleRect;
    private Rect hitBallRect;
    private Paint paddlePaint;

    public Paddle(PaddleType type) {

        this.type = type;
        this.setColor(Globals.PaddleColor);

        this.paddleRect = new Rect(
                Globals.PaddleLeft,
                Globals.PaddleTop,
                Globals.PaddleLeft + Globals.PaddleWidth,
                Globals.PaddleTop + Globals.PaddleHeight
        );
        this.hitBallRect = new Rect();
        this.paddlePaint = new Paint();
        this.paddlePaint.setColor(this.getColor());
    }

    public void init() {

        // compute new position of paddle
        int x = (this.type == PaddleType.LeftPaddle) ?
                Globals.PaddleLeft :
                this.getParentWidth() - Globals.PaddleLeft - Globals.PaddleWidth;
        int y = (this.getParentHeight() - Globals.PaddleHeight) / 2;

        // adjust paddle's origin
        this.setOrigin(new Point(x, y));

        // adjust paddle's rectangle values
        this.paddleRect.left = this.getOriginX();
        this.paddleRect.right = this.getOriginX() + Globals.PaddleWidth;
        this.paddleRect.top = this.getOriginY();
        this.paddleRect.bottom = this.paddleRect.top + Globals.PaddleHeight;
    }

    public void move(int y) {

        // is distance between touch event and paddle small
        if (Math.abs(this.paddleRect.centerY() - y) < Globals.PaddleScrollDistance) {
            // small distance
            this.paddleRect.offset(0, y - this.paddleRect.centerY());
        } else {
            // large distance
            int dy = (this.paddleRect.centerY() < y) ?
                    Globals.PaddleScrollDistance :
                    -Globals.PaddleScrollDistance;

            this.paddleRect.offset(0, dy);
        }

        // take care of upper and lower border of surrounding container
        int left = (this.type == PaddleType.LeftPaddle) ?
                Globals.PaddleLeft :
                this.getParentWidth() - Globals.PaddleLeft - Globals.PaddleWidth;

        int maxTop = this.getParentHeight() - Globals.PaddleTop - Globals.PaddleHeight;

        if (this.paddleRect.top < Globals.PaddleTop) {
            this.paddleRect.offsetTo(left, Globals.PaddleTop);
        }

        if (this.paddleRect.top > maxTop) {
            this.paddleRect.offsetTo(left, maxTop);
        }
    }

    public boolean hitsBall(Point point) {

        // hitting rectangle should be a bit smaller than the ball
        int offset = Globals.BallRadius - 2;

        if (this.type == PaddleType.LeftPaddle) {

            if (point.x < Globals.PaddleLeft + Globals.PaddleWidth
                    + Globals.BallRadius + Globals.ToleranceDistance) {

                this.hitBallRect.left = 0;
                this.hitBallRect.right = point.x + offset;
                this.hitBallRect.top = point.y - offset;
                this.hitBallRect.bottom = point.y + offset;

                if (Rect.intersects(this.hitBallRect, this.paddleRect))
                    return true;
            }
        } else if (this.type == PaddleType.RightPaddle) {

            if (point.x > this.getParentWidth() - Globals.PaddleLeft
                    - Globals.PaddleWidth - Globals.BallRadius - Globals.ToleranceDistance) {

                this.hitBallRect.left = point.x - offset;
                this.hitBallRect.right = Integer.MAX_VALUE;
                this.hitBallRect.top = point.y - offset;
                this.hitBallRect.bottom = point.y + offset;

                if (Rect.intersects(this.hitBallRect, this.paddleRect))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(this.paddleRect, this.paddlePaint);
    }
}
