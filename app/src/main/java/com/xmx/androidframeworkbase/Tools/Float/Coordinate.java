package com.xmx.androidframeworkbase.Tools.Float;

/**
 * Created by The_onE on 2016/8/30.
 */
public class Coordinate {
    public float x;
    public float y;

    public Coordinate add(Coordinate other) {
        Coordinate newCoordinate = new Coordinate();
        newCoordinate.x = x + other.x;
        newCoordinate.y = y + other.y;
        return newCoordinate;
    }

    public Coordinate sub(Coordinate other) {
        Coordinate newCoordinate = new Coordinate();
        newCoordinate.x = x - other.x;
        newCoordinate.y = y - other.y;
        return newCoordinate;
    }

    public double distance() {
        return Math.sqrt(x * x + y * y);
    }
}
