import chess.*;
import client.*;

public class Main {
    public static void main(String[] args) {
      var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Client(serverUrl).run();
    }
}