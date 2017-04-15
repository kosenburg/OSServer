package commands;

import utilities.BackwardsFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kevin on 4/9/2017.
 */
public class TailCommand extends FileCommand {
    private static final int SLEEPTIMESEC = 1;
    private RandomAccessFile file;

    @Override
    protected void openFile() throws FileNotFoundException {
        file = new RandomAccessFile(filePath,"r");
    }

    @Override
    public void performCommand() throws IOException {
        System.out.println("Executing tail on " + filePath);
        openFile();
        returnCommandOutput();
    }

    @Override
    public void returnCommandOutput() throws IOException {
        returnLastTenLines();
        try {
            returnTail();
        } catch (InterruptedException e) {
            System.err.println("Issue occured!");
        }
    }

    @Override
    public void closeFiles() throws IOException {
        file.close();
    }

    private void returnLastTenLines() throws IOException {
        BackwardsFileReader backwardsFileReader = new BackwardsFileReader(filePath);
        String[] lines = backwardsFileReader.getLinesFromEnd(10);

        for (String line: lines) {
            sendLine(line);
        }
    }

    private void returnTail() throws IOException, InterruptedException {
        movePointerToEOF();
        while (!isShutdown()) {
            readBytes();
        }
    }

    private void movePointerToEOF() throws IOException {
        long offSet = file.length();
        file.seek(offSet);
    }

    private void readBytes() throws IOException {
        int bytesToRead = 1024;
        byte[] fileChunks = new byte[bytesToRead];
        int length = 0;

        if ((length = file.read(fileChunks)) != -1) {
            sendLine(new String(fileChunks, "UTF-8"));
        } else {
            pause();
        }
    }

    private void sendLine(String line) throws IOException {
        output.write(line);
        output.flush();
    }

    private void pause() {
        try {
            Thread.sleep(SLEEPTIMESEC * 1000);
        } catch (InterruptedException e) {}
    }
}
