package de.peterloos.pong;

import android.graphics.Color;

public class Globals {

    // paddle constants
    public static final int PaddleTop = 20;
    public static final int PaddleLeft = 10;
    public static final int PaddleHeight = 100;
    public static final int PaddleWidth = 30;
    public static final int PaddleColor = Color.YELLOW;

    // ball constants
    public static final int BallRadius = 15;
    public static final int BallSpeed = 5;
    public static final int BallColor = Color.BLACK;

    // putExtra and getStringExtra support
    public static final String GameType = "de.peterloos.anotherpong.GAMETYPE";

    // miscellaneous constants
    public static final int ToleranceDistance = 5;  // should be similar to Ball.Speed
    public static final int PaddleScrollDistance = 5;
    public static final int BallFadingOutMaxSteps = 30;
    public static final int MaxPlays = 5;
}
