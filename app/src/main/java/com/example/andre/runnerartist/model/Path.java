package com.example.andre.runnerartist.model;

import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Point> points;

    public Path() {
        initPath();
    }
    public Path(List<Point> points) {
        setPoints(points);
    }

    public Double distance() {
        Double sum = 0.0;
        for (int i = 0; i < getPoints().size() - 1; i++) {
            Point first  = getPoints().get(i),
                  second = getPoints().get(i + 1);

            sum += first.distance(second);
        }
        return sum;
    }

    public Path addPoint(Point p) {
        List<Point> path = getPoints();
        path.add(p);
        return withPoints(path);
    }

    private void initPath() {
        if (getPoints() == null) {
            setPoints(new ArrayList<>());
        }
    }
    public List<Point> getPoints() {
        return points;
    }
    public void setPoints(List<Point> points) {
        this.points = points;
    }
    public Path withPoints(List<Point> points) {
        this.points = points;
        return this;
    }
}
