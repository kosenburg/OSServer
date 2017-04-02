package server;

import commands.CommandExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Kevin on 4/2/2017.
 */
public class ServerController {
    public static boolean shutdown = false; //TODO two for notify all and notify local?
    private static ServerSocket serverSocket;
    private final static int socketTimeoutSec = 10;
    private final static int pauseTime = 3;
    private int portNumber;

    public ServerController(int portNumber) {
        this.portNumber = portNumber;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(socketTimeoutSec * 1000);
            System.out.println("Server successfully started, listening..");
            listen();
        } catch (IOException e) {
            System.err.println("A problem occurred while starting the server due to: " + e.getMessage());
        }
    }

    private void listen() throws IOException {
        while (serverSocket.isBound()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Connection accepted. IP: " + client.getRemoteSocketAddress().toString());

                CommandExecutor executor = new CommandExecutor(client);
                executor.start();
            } catch (SocketTimeoutException e) {
                if (isShutdown()) {
                    performCleanUp();
                }
            }
        }
    }

    private void performCleanUp() throws IOException {
        System.out.println("Shutdown request detected, allowing threads to clean up..");
        pause();
        System.out.println("Exiting...");
        serverSocket.close();
        System.exit(0);
    }

    private void pause() {
        try {
            Thread.sleep(pauseTime * 1000);
        } catch (InterruptedException e) {}
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void sendShutdownMessage() {
        System.out.println("Shutdown message received!");
        synchronized (this) {
            shutdown = true;
        }
    }

    public synchronized static void sendShutdownMessageToAllServers() {
        shutdown = true;
    }
}
