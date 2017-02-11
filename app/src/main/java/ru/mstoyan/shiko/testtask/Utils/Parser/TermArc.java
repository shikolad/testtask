package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.IllegalFormatException;

import ru.mstoyan.shiko.testtask.Utils.ArcToBezier;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermArc extends TerminalWord {
    float rx, ry, rotation;
    float largeFlag, sweepFlag;
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        int next = readFloat(data,offset);
        rx = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        next = readFloat(data,offset);
        ry = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;


        next = readFloat(data,offset);
        rotation = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        next = readFloat(data,offset);
//        largeFlag = Float.parseFloat(data.substring(offset,next)) != 0;
        largeFlag = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        next = readFloat(data,offset);
//        sweepFlag = Float.parseFloat(data.substring(offset,next)) != 0;
        sweepFlag = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        point = new PointF();
        next = readFloat(data,offset);
        point.x = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        offset = next + 1;

        next = readFloat(data,offset);
        point.y = Float.parseFloat(data.substring(offset,next));
        checkDelimiter(data,next);
        return next - 1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        ArrayList<ArcToBezier.BezierInfo> bezierInfos = ArcToBezier.a2c(prev.getPoint().x,prev.getPoint().y,
                point.x,point.y,
                largeFlag, sweepFlag,
                rx,ry,rotation);
        for (ArcToBezier.BezierInfo info :
                bezierInfos) {
            path.cubicTo((float)info.x11, (float)info.y11,
                    (float)info.x22, (float)info.y22,
                    (float)info.x2, (float)info.y2);
        }
    }
}
