package server;

/**
 * Created by Kevin on 4/2/2017.
 */
public class ServerStarter {
    public static void main(String[] args) {
        ServerController controller = new ServerController(getPortNumber(args));
        controller.startServer();
    }


    private static int getPortNumber(String[] args) {
        if (args.length == 1) {
            return Integer.parseInt(args[0]);
        } else {
            System.err.println("Invalid number of arguments, please enter  a port number");
            System.exit(1);
            return 1;
        }
    }
}
