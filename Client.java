import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Client implements Serializable {
    //private static final long serialVersionUID = 8564362918537326616L;
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static final int port = 1518;
    private String hostname;
    private String clientName;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private GameWindow gameWindow;
    private int clientNum = -1;


    public static void main(String[] args) {
        Client chatClient = new Client(args[0], args[1]);
        //chatClient.initGUI();
        chatClient.run();
        System.out.println("after run");


    }

    public Client(String hostname, String clientName) {
        this.hostname = hostname;
        this.clientName = clientName;


    }

    public void run() {
        try {

            socket = new Socket(hostname, port); in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());


            while (true) {
                try {
                    Object input = in .readObject();
                    if (input instanceof String) {
                        String line = (String) input;
                        if (line.startsWith("CLIENTNUM")) {
                            clientNum = Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length()));
                            gameWindow = new GameWindow(out, clientNum);
                            out.writeObject(new String("NAME " + clientName));
                        }
                        if (line.startsWith("SCORE")) {
                            gameWindow.setScores(line.substring(line.indexOf(' ') + 1, line.length()).split(","));
                        }
                    }

                    if (input instanceof Integer[][] && clientNum != -1) {
                        Integer[][] coords = (Integer[][]) input;
                        gameWindow.movePaddle(coords);
                        //System.out.println(Arrays.deepToString(coords));
                    }

                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            //out.println("EXIT");

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + hostname);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: Error establishing communication with server.");
            System.out.println(e.getMessage());
        }

        try {
            if (out != null) out.close();
            if ( in != null) in .close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            System.out.println("Error closing the streams.");
        }
    }
}