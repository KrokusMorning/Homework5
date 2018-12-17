package se.kth.id1212.hangman.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements Runnable {
    private static final int TIMEOUT_QUARTER = 900000;
    private static final int TIMEOUT_20S = 20000;
    private Socket socket;
    private PrintWriter writeToServer;
    private BufferedReader readFromServer;
    private final String HOST = ""; //Give server ip here
    private final int PORT = 8080;
    private OutputHandler outputHandler;


    public Connection(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public void run() {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT_20S);
            socket.setSoTimeout(TIMEOUT_QUARTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean autoFlush = true;
        try {
            writeToServer = new PrintWriter(socket.getOutputStream(), autoFlush);
            readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Listener(outputHandler)).start();
        sendMessage("START");
    }

    public void sendMessage(String... message) {
        StringBuilder sb = new StringBuilder();
        for (String aMessage : message) {
            sb.append(aMessage + " ");
        }
        if(writeToServer != null){
            writeToServer.println(sb.toString());
        }

    }

    private class Listener implements Runnable {
        private final OutputHandler outputHandler;

        private Listener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            for (;;) {
                try {
                    outputHandler.handleMsg(readFromServer.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}