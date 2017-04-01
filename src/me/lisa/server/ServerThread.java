package me.lisa.server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private InputStream inputStream;
    private boolean isAlive;

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            socket.setSoTimeout(2000);
            this.inputStream = socket.getInputStream();
            isAlive = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while (isAlive) {
                try {
                    String line = br.readLine();
                    if (line != null) {
                        Request request = new Request(line);
                        String header;
                        while (!(header = br.readLine()).equals("")) {
                            request.addHeader(header);
                        }
                        // TODO remove if
                        if (request.getMethod().equals("POST")) {
                            char[] body = new char[request.getContentLength()];
                            br.read(body, 0, body.length);
                            request.setBody(body);
                        }
                        System.out.println(request);
                        new RequestHandler(socket, request).start();
                    }
                } catch (IOException e) {
                    isAlive = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
