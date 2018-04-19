import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static final int port = 1518;
    private String hostname;
    private String clientName;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private GameWindow gameWindow;
    private String clientNum = "0";


    public static void main(String[] args) {
        Client chatClient = new Client("localhost", "Brian");    
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
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //out.println("ENTER " + clientName);
//            String line = in.readLine();
//            if (line.startsWith("CLIENTNUM")) {
//            	clientNum = line.substring(line.indexOf(' ') + 1, line.length());
//            	System.out.println(clientNum);
//            }
            gameWindow = new GameWindow(out, clientNum);

            while(true){
                String line2 = in.readLine();
             
                if(line2 == null) break;
                
                
            }

            out.println("EXIT");

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + hostname);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: Error establishing communication with server.");
            System.out.println(e.getMessage());
        }

        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            System.out.println("Error closing the streams.");
        }
    }


//    private void initGUI() {
//    	JFrame frame = new JFrame("testy");
//    	frame.setVisible(true);
//    	frame.addKeyListener(new ReturnAction());
//    	frame.pack();
//
//    }

//    private class ReturnAction extends KeyAdapter {
//        @Override
//        public void keyPressed(KeyEvent e) {
//            int keys = e.getKeyCode();
//            if (keys == KeyEvent.VK_ENTER) {
//                out.println("MV_LEFT 0");
//             
//            }
//        }
//    }
}
