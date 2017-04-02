package commands;

import server.ServerController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Kevin on 4/2/2017.
 */
public class CommandExecutor extends Thread{
    private Socket clientSocket;
    private String commandName;
    private String[] parameters = null;
    private Command command;

    public CommandExecutor(Socket client) {
        clientSocket = client;
    }

    public void run() {
        try {
            readSentCommandString();
            setCommand(Command.getCommand(commandName));
            setUpCommand();
            performCommand();
            checkForShutdown();
        } catch (IOException e) {
            System.err.println("Issue executing " + commandName + " due to: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void readSentCommandString() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String[] line = reader.readLine().split(",");

        setCommandName(line[0]);

        if (line.length > 1) {
            setParameters(Arrays.copyOfRange(line, 1, line.length));
        }
    }

    private void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    private void checkForShutdown() {
        if (commandName.equals("shutdown")) {
            ServerController.sendShutdownMessageToAllServers();
        }
    }

    private void setUpCommand() throws IOException {
        command.setReturnStream(clientSocket.getOutputStream());
        command.setParameters(parameters);
    }

    private void setCommand(Command command) {
        this.command = command;
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Issue closing connection due to: " + e.getMessage());
        } catch (NullPointerException e) {}
    }

    private void performCommand() throws IOException {
        command.execute();
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}
