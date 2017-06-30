package com.example.andre.runnerartist.model;

import android.graphics.Path;

import com.example.andre.runnerartist.misc.Curve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DrawingPath implements Serializable {

    private static final long serialVersionUID = -7906411771852904223L;
    private List<GeoPoint> points;

    public DrawingPath() {
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

    public Path asDrawablePath(Double size) {
        if (getPoints().size() > 1) {
            List<GeoPoint> absPoints = absolutePoints(size);
            Path path = new Path();
            GeoPoint initial = absPoints.get(0);
            path.moveTo(initial.getLng().floatValue(), initial.getLat().floatValue());
            for (GeoPoint p : absPoints.subList(1, absPoints.size())) {
                path.lineTo(p.getLng().floatValue(), p.getLat().floatValue());
            }
            return path;
        } else {
            return null;
        }
    }

    private List<GeoPoint> absolutePoints(Double scale) {
        GeoPoint pMin = getPoints().get(0).clone(),
                pMax = getPoints().get(0).clone();
        for (GeoPoint p : getPoints()) {
            pMin.setLat(Math.min(pMin.getLat(), p.getLat()));
            pMin.setLng(Math.min(pMin.getLng(), p.getLng()));

            pMax.setLat(Math.max(pMax.getLat(), p.getLat()));
            pMax.setLng(Math.max(pMax.getLng(), p.getLng()));
        }
        List<GeoPoint> absPoints = new ArrayList<>(getPoints().size());
        for (GeoPoint p : getPoints()) {
            Double ratioLat = (p.getLat() - pMin.getLat()) / (pMax.getLat() - pMin.getLat());
            Double ratioLng = (p.getLng() - pMin.getLng()) / (pMax.getLng() - pMin.getLng());
//            assert ratioLat >= 0 && ratioLat <= 1;
//            assert ratioLng >= 0 && ratioLng <= 1;
            GeoPoint abs = p.clone()
                    .withLat(ratioLat * scale)
                    .withLng(ratioLng * scale);
            absPoints.add(abs);
        }
        return absPoints;
    }

    public DrawingPath curved() {
        DrawingPath curvedPath =  new DrawingPath();
        curvedPath.addPoint(getPoints().get(0));
        for (Integer i = 1; i < getPoints().size() - 2; i++) {
            GeoPoint p1 = curvedPath.getPoints().get(curvedPath.getPoints().size() - 1);
            GeoPoint p2 = getPoints().get(i);
            GeoPoint p3 = getPoints().get(i + 1);
            List<GeoPoint> curve = Curve.bezierCurve(p1, p2, p3);
            curvedPath.getPoints().addAll(curve.subList(0, curve.size() / 2));
        }
        GeoPoint p1 = getPoints().get(getPoints().size() - 3);
        GeoPoint p2 = getPoints().get(getPoints().size() - 2);
        GeoPoint p3 = getPoints().get(getPoints().size() - 1);
        curvedPath.getPoints().addAll(Curve.bezierCurve(p1, p2, p3));
        return curvedPath;
    }

    public DrawingPath addPoint(GeoPoint p) {
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
    public DrawingPath withPoints(List<GeoPoint> points) {
        setPoints(points);
        return this;
    }
}
