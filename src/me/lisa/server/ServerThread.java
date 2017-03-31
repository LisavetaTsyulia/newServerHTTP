package me.lisa.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private boolean isAlive;

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isAlive = true;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(2000);
        } catch (SocketException e) { }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while (isAlive) {
                try {
                    String line = br.readLine();
                    if (line != null){
                        Request request = new Request(line);
                        String header;
                        while ((header = br.readLine()) != null){
                            request.addHeader(header);
                        }
                        char[] buffer = new char[request.getContentLength()];
                        br.read(buffer, 0, buffer.length);
                        request.addBody(buffer);
                        new Thread(new ServerResponse(socket, request)).start();
                    }
                } catch (Exception ex) {
                    isAlive = false;
                }
            }
        } catch (Exception ex) { }
    }
}
