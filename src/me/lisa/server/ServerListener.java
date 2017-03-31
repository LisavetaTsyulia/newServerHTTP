package me.lisa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {

    private ServerSocket serverSocket;
    private boolean isAlive;

    public ServerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        isAlive = true;
    }

    public void run() {
        while (isAlive) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                new ServerThread(socket).start();
            } catch (IOException ignored) {}
        }
    }

    public void stop() {
        isAlive = false;
    }

}
