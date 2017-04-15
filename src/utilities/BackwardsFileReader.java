package utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Stack;

/**
 * Created by Kevin on 4/15/2017.
 */
public class BackwardsFileReader {

    private RandomAccessFile rFile;
    private int numberOfLinesWanted;
    private Stack lineStack;
    private int bytesToRead = 1024;

    public BackwardsFileReader(String filePath) {
        setFilePath(filePath);
    }

    private void setFilePath(String filePath) {
        try {
            rFile = new RandomAccessFile(filePath, "r");
        } catch (FileNotFoundException e) {
            System.err.println("Issue reading file due to: " + e.getMessage());
        }
    }


    public String[] getLinesFromEnd(int numLines) throws IOException {
        numberOfLinesWanted = numLines;
        return returnLines();
    }

    private String[] returnLines() throws IOException {
        lineStack = new Stack<>();
        fillStack(0, rFile.length());
        return makeNewLineArray(unstackToString());
    }

    private void fillStack(int numberOfLines, long lastPosition) throws IOException {
        long seekValue = lastPosition - bytesToRead;

        if (numberOfLines >= numberOfLinesWanted) {
            // recursion bottoms out here
        } else if (seekValue < 0) {
            lineStack.push(getString(0, lastPosition));
        } else {
            String line = getString(seekValue,lastPosition);
            lineStack.push(line);
            numberOfLines += getNumberOfNewLines(line);
            fillStack(numberOfLines, seekValue);
        }
    }

    private String getString(long seekValue, long lastPosition) throws IOException {
        byte[] array = new byte[bytesToRead];

        rFile.seek(seekValue);

        if (seekValue == 0) {
            rFile.read(array,0,(int) lastPosition);
        } else {
            rFile.read(array);
        }

        return new String(array,"UTF-8");
    }

    private int getNumberOfNewLines(String line) {
        String[] lines = line.split("\n");
        return lines.length - 1;
    }

    private String unstackToString() {
        StringBuilder builder = new StringBuilder();
        while (lineStack.size() > 0) {
            builder.append(lineStack.pop());
        }
        return builder.toString();
    }

    private String[] makeNewLineArray(String lines) {
        String[] newLines = lines.split("\n");

        int arraySize = (newLines.length < numberOfLinesWanted) ? newLines.length : numberOfLinesWanted;
        String[] returnArray = new String[arraySize];

        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = newLines[newLines.length - (returnArray.length - i)];
        }
        return returnArray;
    }
}
