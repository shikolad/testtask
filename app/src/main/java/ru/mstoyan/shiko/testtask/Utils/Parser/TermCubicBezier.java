package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermCubicBezier extends TerminalWord implements TangentPoint{
    private PointF prevTangentPoint;
    private PointF tangentPoint = null;
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        point = new PointF();
        tangentPoint = new PointF();
        prevTangentPoint = new PointF();

        int next = readFloat(data,offset);
        prevTangentPoint.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        prevTangentPoint.y = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        tangentPoint.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        tangentPoint.y = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        point.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        point.y = Float.parseFloat(data.substring(offset,next));
        return next-1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        path.cubicTo(prevTangentPoint.x,prevTangentPoint.y,
                tangentPoint.x,tangentPoint.y,
                point.x,point.y);
    }

    @Override
    public PointF getTangentPoint() {
        return tangentPoint;
    }
}
