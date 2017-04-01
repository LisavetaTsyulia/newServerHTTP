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
            if (request.getMethod().equals("GET") || request.getMethod().equals("HEAD")){
                File file = new File("./src/files", request.getPath());
                if (file.exists() && !file.isDirectory()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String lastModified = sdf.format(file.lastModified());
                    response.setNow();
                    response.setCode(200);
                    response.setLastModif(lastModified);
                    response.setContentLength(file.length());
                    response.setContentType(getFileExtension(file));
                    response.setStatus("OK");
                    response.setBody(getBody(file));
                } else {
                    response.setNow();
                    response.setCode(404);
                    response.setLastModif("");
                    response.setContentLength(0);
                    response.setContentType("");
                    response.setStatus("FILE NOT FOUND");
                }
            }
            OutputStream os = socket.getOutputStream();
            os.write(response.toString().getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
    private String getBody(File file) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] mybytearray = new byte[(int) file.length()];
            bis.read(mybytearray, 0, mybytearray.length);
            content = new String(mybytearray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }
}
