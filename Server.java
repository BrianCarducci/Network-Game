import java.io.IOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Arrays;
import java.lang.*;

public class Server {
  private static final int DEFAULT_PORT = 1518;
  private final List<Connection> clients = new ArrayList<>();
  private final List<String> usernames = new ArrayList<>();
  private final Double[][] paddlePos = {{0.0,400.0},{800.0,400.0},{400.0,800.0},{400.0,0.0}};

  public static void main(String[] args) {
    Server chatServer = new Server();
    chatServer.run();
  }

  private void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
      System.out.println("Server Running at localhost:" + DEFAULT_PORT + "\n");

      new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            int curTime0 = (int) System.currentTimeMillis();
            //pushGameState(); //TODO: make this work
            int curTime1 = (int) System.currentTimeMillis();
            try {
              Thread.sleep(curTime1 - curTime0);
            } catch (InterruptedException intexc) {
              System.out.println(intexc);
            }
          }
        }

      }).start();

      while (true) {
        Socket clientSocket = serverSocket.accept();
        String clientNum = String.valueOf(clients.size());
        Connection c = new Connection(clientSocket, clientNum);
        synchronized(clients){
          clients.add(c);
          System.out.println("Clients connected: " + clients.size());
        }
        c.start();
      }

    } catch (Exception e) {
      System.err.println("Error occured creating server socket: " + e.getMessage());
    }
  }

  synchronized private void pushGameState() {
    for(Connection client : clients) {
      if(client != null && client.out != null){
        try {
          client.out.writeObject(paddlePos);
          client.out.flush();
        } catch (IOException e){
          System.out.println(e);
        }
      }
    }
  }

  private class Connection extends Thread {
    Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    String clientNum = "";
    public String username = "";
    public String roomId = "0";
    public int playerNum;

    public Connection(Socket socket, String clientNum) throws IOException{
      this.socket = socket;
      this.clientNum = clientNum;
    }

    public void run() {
      try {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(new String("CLIENTNUM " + clientNum));


        while (true) {
          pushGameState();
          String line = (String) in.readObject();

          processLine(line);

        }

      } catch (Exception e) {
        System.out.println("Error connecting, Terminating. " + e.getMessage());
      }finally{
        closeResources();
      }
    }

    private void closeResources(){
      try{
        synchronized(clients){
          clients.remove(this);
          System.out.println("Clients connected: " + clients.size());
        }
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
      }catch(IOException e){
        System.out.println(e);
      }
    }


    synchronized private void processLine(String line) {
      playerNum = Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length()));
      Double[] coordinates = paddlePos[playerNum];
      double x = coordinates[0];
      double y = coordinates[1];
      if(line.startsWith("MV_LEFT")){
        //update position of playerNum
        paddlePos[playerNum][0] = x - 3.0;
        paddlePos[playerNum][1] = y;

      }else if(line.startsWith("MV_RIGHT")){
        //update position of playerNum
        paddlePos[playerNum][0] = x + 3.0;
        paddlePos[playerNum][1] = y;
      }else if(line.startsWith("MV_UP")){
        //update position of playerNum
        paddlePos[playerNum][0] = x;
        paddlePos[playerNum][1] = y - 3.0;
      }else if(line.startsWith("MV_DOWN")){
        //update position of playerNum
        paddlePos[playerNum][0] = x;
        paddlePos[playerNum][1] = y + 3.0;
      }
      System.out.println(Arrays.deepToString(paddlePos));
    }

  }
}
