package me.lisa.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class HTTPServer {

    private ServerListener serverListener;

    public static void main(String[] args) {
        try {
            HTTPServer httpServer = new HTTPServer();
            httpServer.setGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HTTPServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        serverSocket.setSoTimeout(100);
        serverListener = new ServerListener(serverSocket);
        serverListener.start();
    }

    private void setGUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextArea logArea = new JTextArea(20, 50);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setEditable(false);

        JButton btnStart = new JButton("Start");
        JButton btnStop = new JButton("Stop");
        btnStop.setEnabled(false);
        btnStart.addActionListener(e -> {
            try {
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                serverListener.runListener();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnStop.addActionListener(e -> {
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            serverListener.pauseListener();
        });

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane);
        panel.add(btnStart);
        panel.add(btnStop);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setBounds(10, 10, 10, 10);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

}
