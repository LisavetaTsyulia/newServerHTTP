package me.lisa.server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class RequestHandler extends Thread {

    private Socket socket;
    private Request request;
    private Response response;

    public RequestHandler(Socket socket, Request request) {
        this.socket = socket;
        this.request = request;
        response = new Response();
    }

    @Override
    public void run() {
        try {
            if (request.getMethod().equals("GET") || request.getMethod().equals("HEAD")) {
                File file = new File("./src/files", request.getPath());
                File file2 = new File("./src/files", "second.html");
                if (file.exists() && !file.isDirectory()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String lastModified = sdf.format(file.lastModified());
                    response
                            .setNow()
                            .setCode(200)
                            .setLastModif(lastModified)
                            .setContentType(getFileExtension(file))
                            .setStatus("OK")
                            .setBody(getBody(file));
                } else {
                    response
                            .setNow()
                            .setCode(404)
                            .setStatus("FILE NOT FOUND")
                            .setBody(new char[0]);
                }
            } else if (request.getMethod().equals("POST")) {
                response
                        .setNow()
                        .setCode(200)
                        .setStatus("OK")
                        .setBody(request.getBody());
            }
            OutputStream os = socket.getOutputStream();
            os.write(response.toString().getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    private char[] getBody(File file) {
        char[] content = new char[0];
        try (FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] mybytearray = new byte[(int) file.length()];
            bis.read(mybytearray, 0, mybytearray.length);
            content = new String(mybytearray).toCharArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }
}
