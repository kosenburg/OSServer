package commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Kevin on 4/15/2017.
 */
public class RemoveCommand extends FileCommand {

    @Override
    protected void openFile() throws FileNotFoundException {
    }

    @Override
    public void performCommand() throws IOException {
        Files.delete(Paths.get(filePath));
    }

    @Override
    public void returnCommandOutput() throws IOException {
        output.write("File has been deleted");
        output.newLine();
    }

    @Override
    public void closeFiles() throws IOException {
    }
}
