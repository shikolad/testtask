package ru.mstoyan.shiko.testtask.Utils;

import android.graphics.Path;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by shiko on 08.02.2017.
 */

public interface SVGReader {
    public Path getPath() throws IOException, ParseException;
}
