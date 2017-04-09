package commands;

import java.io.*;

/**
 * Created by Kevin on 4/2/2017.
 */
public abstract class FileCommand extends Command {

    String filePath;
    BufferedWriter output;

    protected abstract void openFile() throws FileNotFoundException;
    public abstract void performCommand() throws IOException;
    public abstract void returnCommandOutput() throws IOException;
    public abstract void closeFiles() throws IOException;

    public void execute() {
        try {
            if (!isShutdown()) {
                sendMessage();
                performCommand();
            }
        } catch (IOException e) {
            System.err.println("An error occurred due to: " + e.getMessage());
        } finally {
            cleanUp();
        }
    }

    private void sendMessage() throws IOException {
        output.write("Command executing.");
        output.newLine();
        output.flush();
    }

    private void cleanUp() {
        try {
            closeFiles();
        } catch (IOException e) {
            System.err.println("Issue closing open files due to: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("files were never opened. See: " + e.getMessage());
        }
    }

    public final void setReturnStream(OutputStream outputStream) {
        output = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public final void setParameters(String[] parameters) {
        filePath = parameters[0];
    }

}
