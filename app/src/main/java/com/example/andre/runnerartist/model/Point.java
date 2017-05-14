package com.example.andre.runnerartist.model;

public class Point {
    private Double x, y;
    public Point() {
        setX(0.0);
        setY(0.0);
    }
    public Point(Double x, Double y) {
        setX(x);
        setY(y);
    }

    public static Double distance(Point p1, Point p2) {
        return Math.sqrt(p1.getX() * p2.getX() + p1.getY() * p2.getY());
    }

    public Double distance(Point p) {
        return distance(this, p);
    }

    public Double getX() {
        return x;
    }
    public Double getY() {
        return y;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public Point withX(Double x) {
        this.x = x;
        return this;
    }
    public Point withY(Double y) {
        this.y = y;
        return this;
    }
}
