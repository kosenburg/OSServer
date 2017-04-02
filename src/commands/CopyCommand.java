package commands;

import java.io.*;

/**
 * Created by Kevin on 4/2/2017.
 */
public class CopyCommand extends FileCommand {
    private BufferedReader file = null;
    private long fileLength;

    private void setCurrentFileLength() {
        File file = new File(filePath);
        fileLength = file.length();
    }

    @Override
    public void returnCommandOutput() throws IOException {
        String line;
        long bytesWritten = 0;

        while(((line = file.readLine()) != null) && (bytesWritten <= fileLength) && (!isShutdown())) {
            output.write(line);
            output.newLine();
            bytesWritten += line.getBytes().length;
        }
        output.flush();
    }

    @Override
    public void closeFiles() throws IOException {
        file.close();
    }

    @Override
    protected void openFile() throws FileNotFoundException {
        file = new BufferedReader(new FileReader(filePath));
        setCurrentFileLength();
    }

    @Override
    public void performCommand() throws IOException {
        System.out.println("Executing copy command on " + filePath);
        openFile();
        returnCommandOutput();
    }

}
