package ru.mstoyan.shiko.testtask.Utils;

import android.graphics.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by shiko on 09.02.2017.
 */

public class PathReader implements SVGReader {

    private String filePath = null;
    private static final String namespace = null;
    private static final String commandsLarge = "MLHVCSQTAZ";
    private static final String delimiter = ",";
    private static final String numberChars = "0123456789.-+";

    private PathReader(){}

    public PathReader(String filePath){
        this.filePath = filePath;
    }

    @Override
    public Path getPath() throws IOException, ParseException, NumberFormatException {
        Path result = new Path();

        String data = getFileContent();
        final int length = data.length();
        String currentChar;
        String prevChar = "";

        float x1 = 0,x2 = 0,x3 = 0,y1 = 0,y2 = 0,y3 = 0;
        boolean a, b;
        int next;

        for (int i = 0; i < length; i++){
            currentChar = data.substring(i,i+1).toUpperCase();
            i++;
            switch (currentChar){
                case "M":
                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.moveTo(x1,y1);
                    i = next-1;
                    break;
                case "L":
                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.lineTo(x1,y1);
                    i = next-1;
                    break;
                case "H":
                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    result.lineTo(x1,y1);
                    i = next-1;
                    break;
                case "V":
                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.lineTo(x1,y1);
                    i = next-1;
                    break;
                case "C":
                    next = readFloat(data,i);
                    x2 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y2 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    x3 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y3 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.cubicTo(x2,y2,x3,y3,x1,y1);
                    i = next-1;
                    break;
                case "S":
                    if (prevChar.equals("C") || prevChar.equals("S")){
                        x2 = x1 + x1 - x3;
                        y2 = y1 + y1 - y3;
                    }

                    next = readFloat(data,i);
                    x3 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y3 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    if (!prevChar.equals("C") && !prevChar.equals("S")){
                        x2 = x1 + x1 - x3;
                        y2 = y1 + y1 - y3;
                    }

                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.cubicTo(x2,y2,x3,y3,x1,y1);
                    i = next-1;
                    break;
                case "Q":
                    next = readFloat(data,i);
                    x2 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y2 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.quadTo(x2,y2,x1,y2);
                    i = next-1;
                    break;
                case "T":
                    x2 = x1 + x1 - x2;
                    y2 = y1 + y1 - y2;

                    next = readFloat(data,i);
                    x1 = Float.parseFloat(data.substring(i,next));
                    checkDelimiter(data,next);
                    i = next + 1;


                    next = readFloat(data,i);
                    y1 = Float.parseFloat(data.substring(i,next));
                    result.quadTo(x2,y2,x1,y2);
                    i = next-1;
                    break;
                case "A":
                    break;
                case "Z":
                    break;
                default:
                    throw new ParseException("Wrong char!",i);
            }
            prevChar = currentChar;
        }

        return result;
    }

    private int readFloat(String str, int beginPos){
        final int len = str.length();
        while (beginPos < len && numberChars.contains(str.substring(beginPos,beginPos+1))) {
            String currChar = str.substring(beginPos,beginPos+1);
            beginPos++;
        }
        return beginPos;
    }

    private boolean checkDelimiter(String str, int pos){
        return true;
    }

    private boolean checkBoolean(String str, int pos){
        return true;
    }

    private String getFileContent() throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        File file = new File (filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine())!=null){
            strBuilder.append(line);
            strBuilder.append('\n');
        }
        return strBuilder.toString();
    }
}
