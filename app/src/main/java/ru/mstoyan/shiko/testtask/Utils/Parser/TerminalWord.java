package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public abstract class TerminalWord {
    private static final String numberChars = "0123456789.-+";
    private static final String delimiter = ",";
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

    boolean checkDelimiter(String data, int offset){
        return data.substring(offset,offset+1).equals(delimiter);
    }

    public PointF getPoint(){
        return point;
    }
}
