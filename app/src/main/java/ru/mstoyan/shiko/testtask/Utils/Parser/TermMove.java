package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermMove extends TerminalWord {
    @Override
    public int readFromString(String data, int i) {
        point = new PointF();
        int next = readFloat(data,i);
        point.x = Float.parseFloat(data.substring(i,next));
        checkDelimiter(data,next);
        i = next + 1;


        next = readFloat(data,i);
        point.y = Float.parseFloat(data.substring(i,next));
        return next-1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        path.moveTo(point.x,point.y);
    }
}
