package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermHorizontal extends TerminalWord {
    private float x;
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        int next = readFloat(data,offset);
        x = Float.parseFloat(data.substring(offset,next));
        return next-1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        point = new PointF(x,prev.getPoint().y);
        path.lineTo(point.x,point.y);
    }
}
