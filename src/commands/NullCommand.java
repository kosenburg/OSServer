package commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Kevin on 4/2/2017.
 */
public class NullCommand extends Command {
    @Override
    public void execute() {

    }

    @Override
    void returnCommandOutput() throws IOException {

    }

    @Override
    public void setReturnStream(OutputStream outputStream) {

    }

    @Override
    public void setParameters(String[] parameters) {

    }
}
