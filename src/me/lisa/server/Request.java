package me.lisa.server;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

    private String method;
    private String path;
    private String httpVersion;
    private char[] body;

    private HashMap<String, String> headersHashMap = new HashMap<>();

    public Request(String line) {
        parseStartLine(line);
    }

    private void parseStartLine(String startLine) {
        Pattern pattern = Pattern.compile("([A-Z]*)\\s(.*)\\s(.*)");
        Matcher matcher = pattern.matcher(startLine);
        if (matcher.matches()) {
            method = matcher.group(1);
            path = matcher.group(2);
            httpVersion = matcher.group(3);
        }
    }

    public void setBody(char[] body) {
        this.body = body;
    }

    public void addHeader(String header) {
        Pattern pattern = Pattern.compile("(.*):\\s(.*)");
        Matcher matcher = pattern.matcher(header);
        if (matcher.matches()) {
            headersHashMap.put(matcher.group(1), matcher.group(2));
        }
    }

    public String getMethod() {
        return method;
    }

    private Integer contentLength = null;
    public int getContentLength() {
        if (contentLength == null) {
            contentLength = Integer.parseInt(headersHashMap.get("content-length"));
        }
        return contentLength;
    }

    public String getHeader(String headerName) {
        return headersHashMap.get(headerName);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {

        return String.format("Request: %s %s %s\n", method, path, httpVersion);
    }
}
