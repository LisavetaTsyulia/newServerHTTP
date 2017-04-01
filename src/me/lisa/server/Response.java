package me.lisa.server;

import java.util.Date;

public class Response {

    private String httpVersion = "HTTP/1.1";
    private int code;
    private String status;
    private Date now;
    private String lastModif = "";
    private String server = "LisaServer";
    private String contentType = "";
    private long contentLength;
    private char[] body;

    public Response setBody(char[] body) {
        this.body = body;
        contentLength = body.length;
        return this;
    }

    public char[] getBody() {
        return body;
    }

    public String getHttpVersion() {
        return httpVersion;
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

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public Response setStatus(String status) {
        this.status = status;
        return this;
    }

    public Response setNow() {
        this.now = new Date();
        return this;
    }

    public Response setLastModif(String lastModif) {
        this.lastModif = lastModif;
        return this;
    }

    public Response setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String toString() {
        return getHttpVersion() + " " + String.valueOf(getCode()) + " " + getStatus() + "\r\n" +
                "Date: " + String.valueOf(getNow()) + "\r\n" +
                "Server: " + getServer() + "\r\n" +
                "Last-Modified: " + getLastModif() + "\r\n" +
                "Content-Type: " + getContentType() + "\r\n" +
                "Content-Length: " + String.valueOf(getContentLength()) + "\r\n\r\n" +
                new String(getBody()) + "\r\n";
    }
}
