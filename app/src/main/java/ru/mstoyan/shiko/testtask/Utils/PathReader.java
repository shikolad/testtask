package ru.mstoyan.shiko.testtask.Utils;

import android.graphics.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import ru.mstoyan.shiko.testtask.Utils.Parser.TermArc;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermCubicBezier;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermEnd;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermHorizontal;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermLine;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermMove;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermQuadBezier;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermShortCubicBezier;
import ru.mstoyan.shiko.testtask.Utils.Parser.TermVertical;
import ru.mstoyan.shiko.testtask.Utils.Parser.TerminalWord;

/**
 * Created by shiko on 10.02.2017.
 */

public class PathReader implements SVGReader {

    private String filePath = null;

    private PathReader(){}

    public PathReader(String filePath){
        this.filePath = filePath;
    }

    @Override
    public Path getPath() throws IOException, ParseException, NumberFormatException {
        Path result = new Path();

        String data = getFileContent();
        if (!data.substring(0,1).toUpperCase().equals("M")){
            throw new ParseException("Wrong file beginning",0);
        }
        final int length = data.length();
        String currentChar;
        TerminalWord prevWord = null;
        TerminalWord currentWord = null;

        for (int i = 0; i < length; i++){
            currentChar = data.substring(i,i+1).toUpperCase();
            i++;
            switch (currentChar){
                case "M":
                    currentWord = new TermMove();
                    break;
                case "L":
                    currentWord = new TermLine();
                    break;
                case "H":
                    currentWord = new TermHorizontal();
                    break;
                case "V":
                    currentWord = new TermVertical();
                    break;
                case "C":
                    currentWord = new TermCubicBezier();
                    break;
                case "S":
                    currentWord = new TermShortCubicBezier();
                    break;
                case "Q":
                    currentWord = new TermQuadBezier();
                    break;
                case "T":
                    break;
                case "A":
                    currentWord = new TermArc();
                    break;
                case "Z":
                    currentWord = new TermEnd();
                    break;
                default:
                    throw new ParseException("Wrong char!",i);
            }

            i = currentWord.readFromString(data,i);
            currentWord.projectToPath(prevWord,result);
            prevWord = currentWord;
        }
        if (!(prevWord instanceof TermEnd)){
            throw new ParseException("Unexpected end of data!!",length);
        }

        return result;
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
