package me.lisa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerListener extends Thread {

    private ServerSocket serverSocket;
    private boolean isAlive;
    private boolean run;

    public ServerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        isAlive = true;
        run = false;
    }

    @Override
    public void run() {
        while (isAlive) {
            if (run) {
                try {
                    Socket socket = serverSocket.accept();
                    new ServerThread(socket).start();
                } catch (SocketTimeoutException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    public void runListener() {
        run = true;
    }

    public void pauseListener() {
        run = false;
    }

    public void stopListener() {
        isAlive = false;
    }

    @Override
    protected void finalize() throws Throwable {
        serverSocket.close();
        super.finalize();
    }
}
