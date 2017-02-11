package ru.mstoyan.shiko.testtask.Utils;

import java.util.ArrayList;

/**
 * Created by shiko on 11.02.2017.
 */

public  class ArcToBezier {


    static double TAU = Math.PI * 2;

    static double unit_vector_angle(double ux, double uy, double vx, double vy) {
        double sign = (ux * vy - uy * vx < 0) ? -1 : 1;
        double dot  = ux * vx + uy * vy;

        // Add this to work with arbitrary vectors:
        // dot /= Math.sqrt(ux * ux + uy * uy) * Math.sqrt(vx * vx + vy * vy);

        // rounding errors, e.g. -1.0000000000000002 can screw up this
        if (dot >  1.0) { dot =  1.0; }
        if (dot < -1.0) { dot = -1.0; }

        return sign * Math.acos(dot);
    }


    // Convert from endpoint to center parameterization,
// see http://www.w3.org/TR/SVG11/implnote.html#ArcImplementationNotes
//
// Return [cx, cy, theta1, delta_theta]
//
    static ArcInfo get_arc_center(double x1, double y1, double x2, double y2, double fa, double fs, double rx, double ry, double sin_phi, double cos_phi) {
        // Step 1.
        //
        // Moving an ellipse so origin will be the middlepoint between our two
        // points. After that, rotate it to line up ellipse axes with coordinate
        // axes.
        //
        ArcInfo result = new ArcInfo();
        double x1p =  cos_phi*(x1-x2)/2 + sin_phi*(y1-y2)/2;
        double y1p = -sin_phi*(x1-x2)/2 + cos_phi*(y1-y2)/2;

        double rx_sq  =  rx * rx;
        double ry_sq  =  ry * ry;
        double x1p_sq = x1p * x1p;
        double y1p_sq = y1p * y1p;

        // Step 2.
        //
        // Compute coordinates of the centre of this ellipse (cx', cy')
        // in the new coordinate system.
        //
        double radicant = (rx_sq * ry_sq) - (rx_sq * y1p_sq) - (ry_sq * x1p_sq);

        if (radicant < 0) {
            // due to rounding errors it might be e.g. -1.3877787807814457e-17
            radicant = 0;
        }

        radicant /=   (rx_sq * y1p_sq) + (ry_sq * x1p_sq);
        radicant = Math.sqrt(radicant) * (fa == fs ? -1 : 1);

        double cxp = radicant *  rx/ry * y1p;
        double cyp = radicant * -ry/rx * x1p;

        // Step 3.
        //
        // Transform back to get centre coordinates (cx, cy) in the original
        // coordinate system.
        //
        result.cx = cos_phi*cxp - sin_phi*cyp + (x1+x2)/2;
        result.cy = sin_phi*cxp + cos_phi*cyp + (y1+y2)/2;

        // Step 4.
        //
        // Compute angles (theta1, delta_theta).
        //
        double v1x =  (x1p - cxp) / rx;
        double v1y =  (y1p - cyp) / ry;
        double v2x = (-x1p - cxp) / rx;
        double v2y = (-y1p - cyp) / ry;

        result.theta1 = unit_vector_angle(1, 0, v1x, v1y);
        result.delta_theta = unit_vector_angle(v1x, v1y, v2x, v2y);

        if (fs == 0 && result.delta_theta > 0) {
            result.delta_theta -= TAU;
        }
        if (fs == 1 && result.delta_theta < 0) {
            result.delta_theta += TAU;
        }

        return result;
    }

    //
// Approximate one unit arc segment with bézier curves,
// see http://math.stackexchange.com/questions/873224
//
    static BezierInfo approximate_unit_arc(double theta1, double delta_theta) {
        double alpha = 4/3 * Math.tan(delta_theta/4);

        BezierInfo result = new BezierInfo();
        result.x1 = Math.cos(theta1);
        result.y1 = Math.sin(theta1);
        result.x2 = Math.cos(theta1 + delta_theta);
        result.y2 = Math.sin(theta1 + delta_theta);
        result.x11 = result.x1 - result.y1*alpha;
        result.y11 = result.y1 + result.x1*alpha;
        result.x22 = result.x2 + result.y2*alpha;
        result.y22 = result.y2 - result.x2*alpha;
        return result;
    }

    static public ArrayList<BezierInfo> a2c(double x1, double y1, double x2, double y2, double fa, double fs, double rx, double ry,
                              double phi) {
        double sin_phi = Math.sin(phi * TAU / 360);
        double cos_phi = Math.cos(phi * TAU / 360);

        // Make sure radii are valid
        //
        double x1p =  cos_phi*(x1-x2)/2 + sin_phi*(y1-y2)/2;
        double y1p = -sin_phi*(x1-x2)/2 + cos_phi*(y1-y2)/2;

        if (x1p == 0 && y1p == 0) {
            // we're asked to draw line to itself
            return null;
        }

        if (rx == 0 || ry == 0) {
            // one of the radii is zero
            return null;
        }


        // Compensate out-of-range radii
        //
        rx = Math.abs(rx);
        ry = Math.abs(ry);

        double lambda = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry);
        if (lambda > 1) {
            rx *= Math.sqrt(lambda);
            ry *= Math.sqrt(lambda);
        }


        // Get center parameters (cx, cy, theta1, delta_theta)
        //
        ArcInfo cc = get_arc_center(x1, y1, x2, y2, fa, fs, rx, ry, sin_phi, cos_phi);

        ArrayList<BezierInfo> result = new ArrayList<>(4);
        double theta1 = cc.theta1;
        double delta_theta = cc.delta_theta;

        // Split an arc to multiple segments, so each segment
        // will be less than τ/4 (= 90°)
        //
        double segments = Math.max(Math.ceil(Math.abs(delta_theta) / (TAU / 4)), 1);
        delta_theta /= segments;

        for (int i = 0; i < segments; i++) {
            result.add(approximate_unit_arc(theta1, delta_theta));
            theta1 += delta_theta;
        }

        // We have a bezier approximation of a unit circle,
        // now need to transform back to the original ellipse
        //
        for (BezierInfo curve:
             result) {
            PointD point = new PointD();
            translatePoint(point.setVals(curve.x1,curve.y1),rx,ry,cos_phi,sin_phi,cc.cx,cc.cy);
            curve.x1 = point.x;
            curve.y1 = point.y;

            translatePoint(point.setVals(curve.x2,curve.y2),rx,ry,cos_phi,sin_phi,cc.cx,cc.cy);
            curve.x2 = point.x;
            curve.y2 = point.y;

            translatePoint(point.setVals(curve.x11,curve.y11),rx,ry,cos_phi,sin_phi,cc.cx,cc.cy);
            curve.x11 = point.x;
            curve.y11 = point.y;

            translatePoint(point.setVals(curve.x22,curve.y22),rx,ry,cos_phi,sin_phi,cc.cx,cc.cy);
            curve.x22 = point.x;
            curve.y22 = point.y;
        }
        return result;
    }

    static void translatePoint(PointD point, double rx, double ry, double cos_phi, double sin_phi, double cx, double cy){
        point.x *= rx;
        point.y *= ry;

        double xp = cos_phi * point.x - sin_phi * point.y;
        double yp = sin_phi * point.x + cos_phi * point.y;

        point.x = xp + cx;
        point.y = yp + cy;
    }

    static private class ArcInfo {
        double cx, cy, theta1, delta_theta;
    }

    static public class BezierInfo {
        public double x1, y1, x11, y11, x22, y22, x2, y2;
    }

    static private class PointD{
        double x;
        double y;

        PointD setVals(double newX, double newY){
            x = newX;
            y = newY;
            return this;
        }
    }
}
