package com.example.andre.runnerartist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Path implements Serializable {

    private static final long serialVersionUID = -7906411771852904223L;
    private List<GeoPoint> points;

    public Path() {
        initPath();
    }

    public Double distance() {
        Double sum = 0.0;
        for (int i = 0; i < getPoints().size() - 1; i++) {
            GeoPoint first  = getPoints().get(i),
                    second = getPoints().get(i + 1);

            sum += first.distance(second);
        }
        return sum;
    }

    public Path addPoint(GeoPoint p) {
        List<GeoPoint> path = getPoints();
        path.add(p);
        return withPoints(path);
    }

    private void initPath() {
        if (getPoints() == null) {
            setPoints(new ArrayList<>());
        }
    }
    public List<GeoPoint> getPoints() {
        return points;
    }
    public void setPoints(List<GeoPoint> points) {
        this.points = points;
    }
    public Path withPoints(List<GeoPoint> points) {
        setPoints(points);
        return this;
    }
}
