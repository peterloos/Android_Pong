package de.peterloos.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import de.peterloos.pongshapes.Ball;
import de.peterloos.pongshapes.BallDirection;
import de.peterloos.pongshapes.Paddle;
import de.peterloos.pongshapes.PaddleType;
import de.peterloos.pongshapes.Size;

public class PongView extends View implements View.OnTouchListener {

    private Paddle leftPaddle;      // left paddle
    private Paddle rightPaddle;     // right paddle
    private Ball ball;              // ball
    private TextView textviewScoreboard;   // score board

    private int pointerIds[];
    private int pointerIndexes[];
    private PointF pointerPositions[];

    private Size parent;
    private int fadingOutCount;
    private boolean isActive;
    private int playersTouchDistance;

    private GameType type;

    private int scoresLeftplayer;
    private int scoresRightplayer;

    // c'tor
    public PongView(Context context, AttributeSet set) {
        super(context, set);

        this.setBackgroundColor(Color.BLUE);

        this.pointerIds = new int[2];
        this.pointerIds[0] = -1;  // left pointer
        this.pointerIds[1] = -1;  // right pointer
        this.pointerIndexes = new int[2];

        this.pointerPositions = new PointF[2];
        this.pointerPositions[0] = null;  // left pointer position, if any
        this.pointerPositions[1] = null;  // right pointer position, if any

        this.leftPaddle = new Paddle(PaddleType.LeftPaddle);
        this.rightPaddle = new Paddle(PaddleType.RightPaddle);
        this.ball = new Ball();

        this.setOnTouchListener(this);

        this.isActive = false;
        this.type = GameType.DemoPlayerGame;

        // need to be updated in onSizeChanged
        this.parent = new Size(0, 0);
        this.playersTouchDistance = 0;
    }

    // getter/setter
    public void setGameType(GameType type) {
        this.type = type;
    }

    // public interface
    public void setScoreboardView(TextView scoreboard) {
        this.textviewScoreboard = scoreboard;
    }

    public void Start() {
        if (this.isActive == false) {
            this.resetGame();
            this.isActive = true;
            this.invalidate();
        }
    }

    public void Stop() {
        this.isActive = false;
        this.textviewScoreboard.setText("0:0");
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        this.parent = new Size(xNew, yNew);

        this.playersTouchDistance = xNew / 3;

        this.leftPaddle.setParentSize(this.parent);
        this.rightPaddle.setParentSize(this.parent);
        this.ball.setParentSize(this.parent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (this.isActive) {
            this.drawShapes(canvas);
            this.moveShapes();
            boolean successful = this.handlePaddleHits();
            if (!successful) {
                this.updateScores();
                this.resetPlay();
            }
            this.invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // View is now detached, and about to be destroyed
        this.isActive = false;
    }

    // private helper methods
    private void drawShapes(Canvas canvas) {
        this.leftPaddle.draw(canvas);
        this.rightPaddle.draw(canvas);
        this.ball.draw(canvas);
    }

    private void moveShapes() {

        // ball
        this.ball.move();

        // left paddle
        if (this.type == GameType.DemoPlayerGame) {
            // set left paddle programmatically
            if (this.ball.getDirection() == BallDirection.Leftwards) {
                if (this.ball.getOriginX() < this.parent.getWidth() / 2) {
                    this.leftPaddle.move(this.ball.getOriginY());
                }
            } else {
                int y = this.parent.getHeight() / 2;
                this.leftPaddle.move(y);
            }
        } else {
            // set left paddle according to user's interaction
            if (this.pointerPositions[0] != null) {
                this.leftPaddle.move((int) this.pointerPositions[0].y);
            }
        }

        // right paddle
        if (this.type == GameType.MultiPlayerGame) {
            // set right paddle according to user's interaction
            if (this.pointerPositions[1] != null) {
                this.rightPaddle.move((int) this.pointerPositions[1].y);
            }
        } else {
            // set right paddle programmatically
            if (this.ball.getDirection() == BallDirection.Rightwards) {
                if (this.ball.getOriginX() > this.parent.getWidth() / 2) {
                    this.rightPaddle.move(this.ball.getOriginY());
                }
            } else {
                int y = this.parent.getHeight() / 2;
                this.rightPaddle.move(y);
            }
        }
    }

    /**
     * Returns true, if either the ball is far away from the paddles
     * or the player succeeded to hit the ball.
     * If the player failed to hit the ball, the method will return false
     * after a delay of 'fading out' invocations.
     * In this case a 'game over' scenario should be handled and a
     * new game could be started.
     */
    private boolean handlePaddleHits() {

        if (this.fadingOutCount > 0) {
            this.fadingOutCount--;
            return (this.fadingOutCount == 1) ? false : true;
        }

        Point center = this.ball.getOrigin();

        if (!this.ball.ballIsInCriticalArea(center.x))
            return true;

        // check collision of ball with left paddle
        if (this.leftPaddle.hitsBall(center)) {
            this.ball.changeDirection();
            return true;
        }

        // check collision of ball with right paddle
        if (this.rightPaddle.hitsBall(center)) {
            this.ball.changeDirection();
            return true;
        }

        // paddle didn't hit ball, return game over (delayed)
        this.fadingOutCount = Globals.BallFadingOutMaxSteps;
        return true;
    }

    private void updateScores() {

        if (this.ball.getDirection() == BallDirection.Leftwards) {
            this.scoresRightplayer++;
        } else if (this.ball.getDirection() == BallDirection.Rightwards) {
            this.scoresLeftplayer++;
        }

        String text = String.format(
            Locale.getDefault(), "%d:%d", this.scoresLeftplayer, this.scoresRightplayer);
        this.textviewScoreboard.setText(text);

        if (this.scoresLeftplayer >= Globals.MaxPlays) {
            this.isActive = false;
            Context context = this.getContext();
            Toast.makeText(context, "Left Player has won !", Toast.LENGTH_LONG).show();
        } else if (this.scoresRightplayer >= Globals.MaxPlays) {
            this.isActive = false;
            Context context = this.getContext();
            Toast.makeText(context, "Right Player has won !", Toast.LENGTH_LONG).show();
        }
    }

    private void resetPlay() {
        this.ball.init();
        this.fadingOutCount = 0;
    }

    private void resetGame() {
        this.leftPaddle.init();
        this.rightPaddle.init();
        this.ball.init();

        this.scoresLeftplayer = 0;
        this.scoresRightplayer = 0;

        this.textviewScoreboard.setText("0:0");
        this.fadingOutCount = 0;
    }

    // implementation of interface 'OnTouchListener'
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int index = MotionEventCompat.getActionIndex(event);
                int id = MotionEventCompat.getPointerId(event, index);
                PointF point = new PointF(event.getX(index), event.getY(index));

                if (point.x < this.playersTouchDistance) {

                    // should be left paddle
                    if (this.pointerIds[0] == -1) {

                        // left paddle gets active
                        this.pointerIndexes[0] = index;
                        this.pointerIds[0] = id;
                        this.pointerPositions[0] = point;
                    } else {
                        Log.v("PONG", "Ignoring second left touch event ...");
                    }
                } else if (point.x >= this.parent.getWidth() - this.playersTouchDistance) {

                    // should be right paddle
                    if (this.pointerIds[1] == -1) {

                        // right paddle gets active
                        this.pointerIndexes[1] = index;
                        this.pointerIds[1] = id;
                        this.pointerPositions[1] = point;
                    } else {
                        Log.v("PONG", "Ignoring second right touch event ...");
                    }

                } else {
                    Log.v("PONG", "Ignoring middle touch event ...");
                }
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                // update left paddle position, if data is available
                if (this.pointerIds[0] != -1) {
                    int leftIndex = event.findPointerIndex(this.pointerIds[0]);
                    if (leftIndex != -1) {
                        // update current touch position of left player
                        this.pointerPositions[0] =
                            new PointF(event.getX(leftIndex), event.getY(leftIndex));
                    }
                }

                // update right paddle position, if data is available
                if (this.pointerIds[1] != -1) {
                    int rightIndex = event.findPointerIndex(this.pointerIds[1]);
                    if (rightIndex != -1) {
                        // update current touch position of right player
                        this.pointerPositions[1] =
                            new PointF(event.getX(rightIndex), event.getY(rightIndex));
                    }
                }
            }
            break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int index = MotionEventCompat.getActionIndex(event);
                int id = MotionEventCompat.getPointerId(event, index);

                if (this.pointerIds[0] == id) {
                    // releasing left paddle
                    this.pointerIds[0] = -1;
                    this.pointerPositions[0] = null;

                } else if (this.pointerIds[1] == id) {
                    // releasing right paddle
                    this.pointerIds[1] = -1;
                    this.pointerPositions[1] = null;
                } else {
                    Log.v("PONG", "releasing unused pointer !");
                }
            }
            break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.v("PONG", "ACTION_OUTSIDE");
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.v("PONG", "ACTION_CANCEL");
                break;
        }

        return true;
    }
}
