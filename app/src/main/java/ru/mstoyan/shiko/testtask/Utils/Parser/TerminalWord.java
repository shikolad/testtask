package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public abstract class TerminalWord {
    public static final String numberChars = "0123456789.-+";
    protected PointF point;

    abstract public int readFromString(String data, int offset) throws IllegalFormatException;
    abstract public void projectToPath(TerminalWord prev, Path path);

    protected int readFloat(String data, int offset){
        final int len = data.length();
        while (offset < len && numberChars.contains(data.substring(offset,offset+1))) {
            offset++;
        }
        return offset;
    }

    protected boolean checkDelimiter(String data, int offset){
        return true;
    }

    public PointF getPoint(){
        return point;
    }
}
