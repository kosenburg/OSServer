package commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Kevin on 4/2/2017.
 */
public abstract class Command {
    private static volatile boolean shutdown = false;
    public abstract void execute();
    abstract void returnCommandOutput() throws IOException;
    public abstract void setReturnStream(OutputStream outputStream);
    public abstract void setParameters(String[] parameters);

    synchronized void sendShutdownSignal() {
        shutdown = true;
    }

    public final boolean isShutdown() {
        return shutdown;
    }

    public static Command getCommand(String command) {
        if (command.equals("copy")) {
            return new CopyCommand();
        } else if (command.equals("tail")) { //TODO create tail object
            return new NullCommand();
        } else if (command.equals("shutdown")) { //TODO create shutdown object for server shutdown/cleanup
            return new NullCommand();
        } else  return new NullCommand();
    }

}
