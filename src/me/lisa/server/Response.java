package me.lisa.server;

import java.util.Date;

public class Response {

    private String HTTPversion = "HTTP/1.1";
    private int code;
    private String status;
    private Date now;
    private String lastModif;
    private String server = "LisaServer";
    private String contentType;
    private long contentLength;
    private String body;

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getHTTPversion() {
        return HTTPversion;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Date getNow() {
        return now;
    }

    public String getLastModif() {
        return lastModif;
    }

    public String getServer() {
        return server;
    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNow() {
        Date date = new Date();
        this.now = date;
    }

    public void setLastModif(String lastModif) {
        this.lastModif = lastModif;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String toString() {
        String result =  getHTTPversion() + " " + String.valueOf(getCode()) + " " + getStatus() + "\r\n" +
                "Date: " + String.valueOf(getNow()) + "\r\n" +
                "Server: " + getServer() + "\r\n" +
                "Last-Modified: " + getLastModif() + "\r\n" +
                "Content-Type: " + getContentType() + "\r\n" +
                "Content-Length: " + String.valueOf(getContentLength()) + "\r\n" +
                "Connection: close\r\n\r\n" +
                getBody() + "\r\n";
        return result;
    }
}
