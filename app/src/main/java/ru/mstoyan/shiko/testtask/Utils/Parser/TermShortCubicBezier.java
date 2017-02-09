package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermShortCubicBezier extends TerminalWord implements TangentPoint{
    protected PointF tangentPoint = null;
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {

        tangentPoint = new PointF();
        int next = readFloat(data,offset);
        tangentPoint.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        next = readFloat(data,offset);
        tangentPoint.y = Float.parseFloat(data.substring(offset,next));
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
        PointF prevTangent = null;
        if (prev instanceof TangentPoint){
            prevTangent = ((TangentPoint)prev).getTangentPoint();
            prevTangent.x = prev.getPoint().x + prev.getPoint().x - prevTangent.x;
            prevTangent.y = prev.getPoint().y + prev.getPoint().y - prevTangent.y;
        } else {
            prevTangent = tangentPoint;
        }
        path.cubicTo(prevTangent.x,prevTangent.y,
                tangentPoint.x,tangentPoint.y,
                point.x,point.y);
    }

    @Override
    public PointF getTangentPoint() {
        return tangentPoint;
    }
}
