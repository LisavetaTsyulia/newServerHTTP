package me.lisa.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

public class HTTPServer {
    private JFrame frm;
    private JPanel pan;
    private JTextArea textArea;
    private JButton btnStart;
    private JButton btnStop;
    private ServerSocket serverSocket;
    private ServerListener currentServerListener;

    public static void main(String[] args) {
        HTTPServer httpServer = new HTTPServer();
        httpServer.setGUI();
    }

    private void setGUI() {
        frm = new JFrame();
        pan = new JPanel();
        textArea = new JTextArea(20, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        btnStart.addActionListener(new StartActionListener());
        btnStop.addActionListener(new StopActionListener());

        JScrollPane scr = new JScrollPane(textArea);
        scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pan.add(scr);
        pan.add(btnStart);
        pan.add(btnStop);

        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setContentPane(pan);
        frm.setBounds(10, 10, 10, 10);
        frm.setSize(600, 400);
        frm.setVisible(true);
    }

    private class StartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                serverSocket = new ServerSocket(8080);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ServerListener serverListener = new ServerListener(serverSocket);
            serverListener.run();
            currentServerListener = serverListener;
        }
    }

    private class StopActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentServerListener.stop();
        }
    }
}
