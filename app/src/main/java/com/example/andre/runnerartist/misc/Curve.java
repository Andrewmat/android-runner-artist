package com.example.andre.runnerartist.misc;

import com.example.andre.runnerartist.model.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Curve {
    public static List<GeoPoint> bezierCurve(GeoPoint p1, GeoPoint p2, GeoPoint p3) {
        Double distance = p1.distance(p2) + p2.distance(p3);
        final Integer MAX = ((Double) Math.ceil(distance * 300)).intValue();
        List<GeoPoint> curve = new ArrayList<>();
        for (Integer i = 0; i < MAX; i++) {
            Double ratio = i.doubleValue() / MAX;
            GeoPoint pb1 = p1.pointBetween(p2, ratio);
            GeoPoint pb2 = p2.pointBetween(p3, ratio);
            curve.add(pb1.pointBetween(pb2, ratio));
        }
        return curve;
    }
}
