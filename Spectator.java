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


public class Spectator implements Serializable {
    //private static final long serialVersionUID = 8564362918537326616L;
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static final int port = 1518;
    private String hostname;
    private String specName;
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private GameWindow gameWindow;
    private int specNum = -1;


    public static void main(String[] args) {
        Spectator chatSpec = new Spectator(args[0], args[1]);
        //chatClient.initGUI();
        chatSpec.run();
        System.out.println("after run");


    }

    public Spectator(String hostname, String specName) {
        this.hostname = hostname;
        this.specName = specName;


    }

    public void run() {
        try {

            socket = new Socket(hostname, port); in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(new String("SPECTATOR"));
            gameWindow = new GameWindow();

            while (true) {
                try {
                    Object input = in .readObject();

                    if (input instanceof Integer[][]) {
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