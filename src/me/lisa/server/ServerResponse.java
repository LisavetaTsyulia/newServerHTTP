package me.lisa.server;

import java.net.Socket;

public class ServerResponse implements Runnable{
    private Socket socket;
    private Request request;

    public ServerResponse(Socket socket, Request request) {
        this.socket = socket;
        this.request = request;
    }

    @Override
    public void run() {

    }
}
