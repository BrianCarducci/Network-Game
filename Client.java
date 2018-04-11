import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
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
    private JTextArea textArea = new JTextArea();
    private JTextArea chatBox = new JTextArea(3,1);

    public static void main(String[] args) {
        Client chatClient = new Client(args[0], args[1]);
        chatClient.initGUI();        
        chatClient.run();
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

            out.println("ENTER " + clientName);

            while(true){
                String line = in.readLine();
                if(line == null) break;
                textArea.append(line + "\n");
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


    private void initGUI(){
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        chatBox.setLineWrap(true);
        chatBox.setWrapStyleWord(true);
        chatBox.setEditable(true);
        chatBox.setPreferredSize(new Dimension(300, 50));
        chatBox.setCaretPosition(0);
        chatBox.addKeyListener(new ReturnAction());

        JScrollPane scrollPane = new JScrollPane(textArea);
        JScrollPane chatScrollPane = new JScrollPane(chatBox);
        JTextField roomInput = new JTextField();
        roomInput.setPreferredSize(new Dimension(100, 20));
        JLabel roomLabel = new JLabel("Room: 0");
        roomLabel.setPreferredSize(new Dimension(100, 20));

        roomInput.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                if(roomInput.getText().matches("^[a-zA-Z0-9_]+$")){
                    out.println("JOIN " + roomInput.getText());
                    roomLabel.setText("Room: " + roomInput.getText());
                }else{
                    textArea.append("*INVALID : ROOM MUST BE AN ALPHANUMERIC STRING*\n");
                }
               roomInput.setText("");
            }
         }
      });


        JPanel roomPanel = new JPanel();
        roomPanel.add(roomLabel);
        roomPanel.add(roomInput);

        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setSize(300,600);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(chatScrollPane, BorderLayout.SOUTH);
        panel.add(roomPanel, BorderLayout.NORTH);
        panel.setOpaque(true);



        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(300, 600);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - frame.getWidth();
        int y = 0;
        frame.setLocation(x, y);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                try{
                    out.println("EXIT");
                }catch(Exception e){
                    System.out.println(e);
                }
                System.exit(0);
            }
        });

        chatBox.requestFocus();

    }

    private class ReturnAction extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keys = e.getKeyCode();
            if (keys == KeyEvent.VK_ENTER) {
                out.println("TRANSMIT " + chatBox.getText().replace("\n", ""));
                chatBox.setText("");
                chatBox.setCaretPosition(0);
            }
        }
    }
}
