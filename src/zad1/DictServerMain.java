package zad1;

import java.net.ServerSocket;

public class DictServerMain {
    public final static int port = 2628;
    private String host;
    private ServerSocket serverSocket;

    public DictServerMain(String host) {

        this.host = host;
    }
}
