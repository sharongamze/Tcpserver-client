package Main;

import Client.Client;
import TcpServer.*;

public class Main {

    public static void main(String[] args) throws Exception {
        TcpServer myServer = new TcpServer(8010);
        myServer.run(new MatrixIHandler());
        Client.main(args);
        myServer.stop();
        System.exit(0);
    }

}
