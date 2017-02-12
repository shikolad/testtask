package ru.mstoyan.shiko.testtask.Utils.Parser;

import android.graphics.Path;

import java.util.IllegalFormatException;

/**
 * Created by shiko on 10.02.2017.
 */

public class TermEnd extends TerminalWord {
    @Override
    public int readFromString(String data, int offset) throws IllegalFormatException {
        return offset + 1;
    }

    @Override
    public void projectToPath(TerminalWord prev, Path path) {
        path.close();
    }
}
