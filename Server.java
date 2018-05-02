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
import java.util.Random;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Arrays;
import java.lang.*;

public class Server {
  private static final int DEFAULT_PORT = 1518;
  private final List<Connection> clients = new ArrayList<>();
  private final List<String> usernames = new ArrayList<>();
  //private final Integer[][] paddlePos = {{0,200}, {1,200}, {2,400}, {3, 400}}; //Each entry is [clientNum, <X or Y>] , where x or y depends on which client. 0,1 = y, 2,3 = x
  private final Integer[][] paddlePos = {{350,350}, {0,200}, {1,200}}; //Each entry is [clientNum, <X or Y>] , where x or y depends on which client. 0,1 = y, 2,3 = x
  private final Double[] ballPos = new Double[]{Double.valueOf(paddlePos[0][0]), Double.valueOf(paddlePos[0][1])};
  private Double ballVelY = Math.sqrt(2.0)*Math.random() - Math.sqrt(2.0)/2.0;
  private Double ballVelX = Math.sqrt(1-ballVelY*ballVelY);

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
            /*try {
            Thread.sleep(100);
          } catch (Exception e) {
          System.out.println(e);
        }*/

        //Connection[] conns = clients.toArray(new Connection[clients.size()]);
        //System.out.println(conns.length);
        synchronized(clients) {

          if (clients.size() > 0) {
            int curTime0 = (int) System.currentTimeMillis();
            //                  translateBall();
            //                  checkCollision();
            pushGameState(); //TODO: make this work
            int curTime1 = (int) System.currentTimeMillis();
            try {
              Thread.sleep(1);
            } catch (InterruptedException intexc) {
              System.out.println(intexc);
            }
          }
        }
      }
    }

  }).start();

  while (true) {
    Socket clientSocket = serverSocket.accept();
    String clientNum = String.valueOf(clients.size()+1);
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
  // Update game state
  moveBall();

  paddlePos[0] = new Integer[]{(int) Math.round(ballPos[0]), (int) Math.round(ballPos[1])};

  // Transmit game state to the clients
  for(Connection client : clients) {
    if(client != null && client.out != null){
      try {
        client.writeObject(paddlePos, true);

      } catch (IOException e){
        System.out.println(e);
      }
    }
  }
}

private synchronized void moveBall() {
  ballPos[0] += ballVelX;
  ballPos[1] += ballVelY;

  if (ballPos[0] <= 30 && ((ballPos[1] > paddlePos[1][1]) && (ballPos[1] < paddlePos[1][1] + 200))) {
    ballVelX = -ballVelX;
    //System.out.println("Offscreen UP");
  }

  if (ballPos[0] >= 803 && ((ballPos[1] > paddlePos[2][1]) && (ballPos[1] < paddlePos[2][1] + 200))) {
    ballVelX = -ballVelX;
    //System.out.println("Offscreen UP");
  }

  /*
  if (ballPos[0] <= 0) {
  ballVelX = -ballVelX;
}
if (ballPos[0] >= 833) {
ballVelX = -ballVelX;
}
*/

if (ballPos[1] <= 0) {
  ballVelY = -ballVelY;
  //System.out.println("Offscreen UP");
}
if (ballPos[1] >= 777) {
  ballVelY = -ballVelY;
  //System.out.println("Offscreen DOWN");
}


}

synchronized private void movePaddle(Integer[] line) {
  int playerNum = line[0];
  int y = line[1];

  if(y <= 662) { //Check if offscreen on the bottom
    paddlePos[playerNum][1] = y;
  }
}

private class Connection extends Thread {
  Socket socket;
  public ObjectInputStream in;
  private ObjectOutputStream out;
  String clientNum = "";
  public String username = "";
  public String roomId = "0";
  public int playerNum;

  public Connection(Socket socket, String clientNum) throws IOException{
    this.socket = socket;
    this.clientNum = clientNum;


  }

  public synchronized void writeObject(Object obj, boolean reset) throws IOException {
    if (reset) out.reset();
    out.writeObject(obj);
  }

  public void run() {
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());

      writeObject(new String("CLIENTNUM " + clientNum), false);


      while (true) {
        Object input = in.readObject();

        if (input instanceof Integer[]) {
          Integer[] newCoord = (Integer[]) input;
          movePaddle(newCoord);
        }

        if (input instanceof String) {
          String inputString = (String) input;
          if (inputString.startsWith("RESET")) {
            ballPos[0] = 350.0;
            ballPos[1] = 350.0;
          }
        }
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
}
}
