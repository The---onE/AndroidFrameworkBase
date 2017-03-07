package com.xmx.androidframeworkbase.common.floatwindow;

/**
 * Created by The_onE on 2016/8/30.
 */
public class Coordinate {
    public float x;
    public float y;

    public Coordinate() {
        x = 0;
        y = 0;
    }

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate other) {
        this.x = other.x;
        this.y = other.y;
    }

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
