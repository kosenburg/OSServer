package commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kevin on 4/15/2017.
 */
public class ListCommand extends FileCommand {
    private Process process;
    private BufferedReader input;

    @Override
    protected void openFile() throws FileNotFoundException {

    }

    @Override
    public void performCommand() throws IOException {
        System.out.println("Executing list");
        process = Runtime.getRuntime().exec("cmd.exe /C dir \"" + filePath + "\"");
        setInputStream();
        returnCommandOutput();
    }

    private void setInputStream() {
        input = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    @Override
    public void returnCommandOutput() throws IOException {
        String line;
        while((line = input.readLine()) != null) {
            output.write(line);
            output.newLine();
        }
    }

    @Override
    public void closeFiles() throws IOException {
        input.close();
    }
}
