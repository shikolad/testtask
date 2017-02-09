package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermQuadBezier extends TerminalWord implements TangentPoint {
    PointF tangentPoint = null;

    @Override
    public PointF getTangentPoint() {
        return tangentPoint;
    }

    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        tangentPoint = new PointF();
        int next = readFloat(data,offset);
        point.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        point.y = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        point = new PointF();
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
        path.quadTo(tangentPoint.x,tangentPoint.y,
                point.x, point.y);
    }
}
