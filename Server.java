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
import java.lang.*;

public class Server implements java.io.Serializable{
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
      while (true) {
        System.out.println("h e l p");
        Socket clientSocket = serverSocket.accept();
        String clientNum = String.valueOf(clients.size());
        Connection c = new Connection(clientSocket, clientNum);
        synchronized(clients){
          clients.add(c);
          System.out.println("Clients connected: " + clients.size());
        }
        c.start();

        //int curTime0 = System.currentTimeMillis();
        pushGameState();
        //int curTime1 = System.currentTimeMillis();
        //Thread.sleep(curTime1 - curTime0);
      }
    } catch (Exception e) {
      System.err.println("Error occured creating server socket: " + e.getMessage());
    }
  }

  private void pushGameState() {
    for(Connection client : clients) {
    //  if(client != null && client.out != null){
        try {
          client.outCoords.writeObject(paddlePos);
          client.outCoords.flush();
          System.out.println("sent");
        } catch (IOException e){
          System.out.println(e);
        }
      //}
    }
  }

  private class Connection extends Thread {
    Socket socket;
    public PrintWriter out;
    public ObjectOutputStream outCoords;
    BufferedReader in;
    String clientNum = "";
    public String username = "";
    public String roomId = "0";
    public int playerNum;

    public Connection(Socket socket, String clientNum) throws IOException{
      this.socket = socket;
      this.outCoords = new ObjectOutputStream(socket.getOutputStream());
      this.clientNum = clientNum;
    }

    public void run() {
      try {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("CLIENTNUM " + clientNum);

        while (true) {
          String line = in.readLine();

          if (line != null) processLine(line);

        }

      } catch (IOException e) {
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


     private void processLine(String line) {
      this.playerNum = Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length()));
      Double[] coordinates = paddlePos[playerNum];
      double x = coordinates[0];
      double y = coordinates[1];
      if(line.startsWith("MV_LEFT")){
        //update position of playerNum
        synchronized(paddlePos){paddlePos[playerNum][0] = x - 3.0;}
        synchronized(paddlePos){paddlePos[playerNum][1] = y;}

      }else if(line.startsWith("MV_RIGHT")){
        //update position of playerNum
        synchronized(paddlePos){paddlePos[playerNum][0] = x + 3.0;}
        synchronized(paddlePos){paddlePos[playerNum][1] = y;}
      }else if(line.startsWith("MV_UP")){
        //update position of playerNum
        synchronized(paddlePos){paddlePos[playerNum][0] = x;}
        synchronized(paddlePos){paddlePos[playerNum][1] = y - 3.0;}
      }else if(line.startsWith("MV_DOWN")){
        //update position of playerNum
        synchronized(paddlePos){paddlePos[playerNum][0] = x;}
        synchronized(paddlePos){paddlePos[playerNum][1] = y + 3.0;}
      }
      for(double lines : paddlePos[0]) {System.out.print(lines + " ");}
    }

  }
}
