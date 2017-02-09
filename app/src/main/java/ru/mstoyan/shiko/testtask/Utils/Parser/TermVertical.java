package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermVertical extends TerminalWord {
    private float y;
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        int next = readFloat(data,offset);
        y = Float.parseFloat(data.substring(offset,next));
        return next-1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        point = new PointF(prev.getPoint().x,y);
        path.moveTo(point.x,point.y);
    }
}
