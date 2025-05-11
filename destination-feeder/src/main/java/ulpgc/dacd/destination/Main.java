package ulpgc.dacd.destination;

import ulpgc.dacd.destination.control.DestinationCoordinator;

public class Main {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("usage: java -jar destination-feeder.jar <api_key> <db_path> <lat> <lon>");
            return;
        }

        new DestinationCoordinator().run(args);
    }
}
