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
    private final List < Connection > players = new ArrayList < > ();
    private final List < Connection > spectators = new ArrayList < > ();
    private final List < String > usernames = new ArrayList < > ();
    //first obj is the ball, the rest are the paddles in order
    private final Integer[][] gameObjs = {{350,350},{0,200},{1,200},{2,200},{3,200}};
    private final Double[] ballPos = new Double[] {Double.valueOf(gameObjs[0][0]), Double.valueOf(gameObjs[0][1])};
    private final Random random = new Random();
    private Double ballVelY = -2.0 * Math.random() + 1;
    private Double ballVelX = random.nextBoolean() ? Math.sqrt(1 - ballVelY * ballVelY) : -1 * Math.sqrt(1 - ballVelY * ballVelY);
    private String lastPlayerToHit = "0";
    
    // main
    public static void main(String[] args) {
        Server chatServer = new Server();
        chatServer.run();
    }

    // 1. initialize the server socket, 
    // 2. create a thread for the game loop (will not run if no players)
    // 3. create an infinite loop that accepts clients and adds them to the game
    private void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Server Running at localhost:" + DEFAULT_PORT + "\n");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        synchronized(players) {
                            if (players.size() > 0) {
                                int curTime0 = (int) System.currentTimeMillis();
                                pushGameState();
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
            // process clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientNum = String.valueOf(players.size() + 1);
                Connection c = new Connection(clientSocket, clientNum);

                synchronized(players) {
                    players.add(c);
                    System.out.println("Clients connected: " + players.size());
                }
                c.start();
            }
        } catch (Exception e) {
            System.err.println("Error occured creating server socket: " + e.getMessage());
        }
    }

    synchronized private void pushGameState() {
        // advance the balls position
        moveBall();

        // add new ball position to the object we are going to transmit to client
        gameObjs[0] = new Integer[] {
            (int) Math.round(ballPos[0]), (int) Math.round(ballPos[1])
        };

        // Transmit game state to the players
        for (Connection player: players) {
            if (player != null && player.out != null) {
                try {
                    player.writeObject(gameObjs, true);

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        // Transmit game state to the spectators
        for (Connection spectator: spectators) {
            if (spectator != null && spectator.out != null) {
                try {
                    spectator.writeObject(gameObjs, true);

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    // 1. increment the balls position, and then check for collisions
    // 2. if the ball goes off screen, the person who last hit the ball gets a point
    // 3. if nobody hit the ball last, nobody gets points
    // 4. if the ball is still in bounds, check for paddle collisions
    // 5. if paddle collisions change directions otherwise keep moving in same direction
    private synchronized void moveBall() {
        ballPos[0] += ballVelX;
        ballPos[1] += ballVelY;

        if (ballPos[0] <= -15 || ballPos[0] >= 915 || ballPos[1] <= -15 || ballPos[1] >= 915) {
            ballPos[0] = 350.0;
            ballPos[1] = 350.0;

            if (lastPlayerToHit.equals("0")) {
                System.out.println("No Winner");
            } else {
                synchronized(players) {
                    String score = "";
                    // increment the winners score
                    for (Connection player: players) {
                        if (player != null && player.out != null) {
                            if (player.clientNum.equals(lastPlayerToHit)) player.score++;
                            if (!score.equals("")) score += ",";
                            score = score + player.username + " " + player.score;
                        }
                    }
                    // transmit updated scores
                    for (Connection player: players) {
                        if (player != null && player.out != null) {
                            try {
                                player.writeObject(new String("SCORE " + score), true);
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        }
                    }
                    // transmit game state to the spectators
                    synchronized(spectators){
                        for (Connection spectator: spectators) {
                            if (spectator != null && spectator.out != null) {
                                try {
                                    spectator.writeObject(new String("SCORE " + score), true);
                                } catch (IOException e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }
                }

            }
            lastPlayerToHit = "0";
            ballVelY = -2.0 * Math.random() + 1;
            ballVelX = random.nextBoolean() ? Math.sqrt(1 - ballVelY * ballVelY) : -1 * Math.sqrt(1 - ballVelY * ballVelY);
        }
        // first paddle collision
        if (ballPos[0] <= 35 && ((ballPos[1] > gameObjs[1][1] - 26) && (ballPos[1] < gameObjs[1][1] + 200))) {
            ballVelX = -ballVelX;
            lastPlayerToHit = "1";
        }
        // second paddle collision
        if (ballPos[0] >= 803 && ((ballPos[1] > gameObjs[2][1] - 26) && (ballPos[1] < gameObjs[2][1] + 200))) {
            ballVelX = -ballVelX;
            lastPlayerToHit = "2";
        }
        // third paddle collision
        if (ballPos[1] <= 35 && ((ballPos[0] > gameObjs[3][1]) && (ballPos[0] < gameObjs[3][1] + 200))) {
            ballVelY = -ballVelY;
            lastPlayerToHit = "3";
        }
        // fourth paddle collision
        if (ballPos[1] >= 750 && ((ballPos[0] > gameObjs[4][1]) && (ballPos[0] < gameObjs[4][1] + 200))) {
            ballVelY = -ballVelY;
            lastPlayerToHit = "4";
        }
    }

    synchronized private void movePaddle(Integer[] line) {
        int playerNum = line[0];
        int y = line[1];

        if (y <= 662) { //Check if offscreen on the bottom
            gameObjs[playerNum][1] = y;
        }
    }

    // private class to represent game players and spectator connections
    private class Connection extends Thread {
        Socket socket;
        public ObjectInputStream in;
        private ObjectOutputStream out;
        String clientNum = "";
        public String username = "";
        public int score = 0;

        public Connection(Socket socket, String clientNum) throws IOException {
            this.socket = socket;
            this.clientNum = clientNum;
        }

        public synchronized void writeObject(Object obj, boolean reset) throws IOException {
            if (reset) out.reset();
            out.writeObject(obj);
        }
        // run all connections on their own thread
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream()); in = new ObjectInputStream(socket.getInputStream());
                writeObject(new String("CLIENTNUM " + clientNum), true);
                // infinite loop to wait for paddle movements
                while (true) {
                    Object input = in .readObject();
                    // check if the input is the new coordinates
                    if (input instanceof Integer[]) {
                        Integer[] newCoord = (Integer[]) input;
                        movePaddle(newCoord);
                    }
                    // check if string so we can handle protocols
                    if (input instanceof String) {
                        String inputString = (String) input;
                        // reset ball
                        if (inputString.startsWith("RESET")) {
                            ballPos[0] = 350.0;
                            ballPos[1] = 350.0;
                            ballVelY = -2.0 * Math.random() + 1;
                            ballVelX = random.nextBoolean() ? Math.sqrt(1 - ballVelY * ballVelY) : -1 * Math.sqrt(1 - ballVelY * ballVelY);
                        }
                        // initialize players username
                        if (inputString.startsWith("NAME")) {
                            this.username = inputString.substring(inputString.indexOf(' ') + 1, inputString.length());
                        }
                        // handle spectators
                        if (inputString.startsWith("SPECTATOR")) {
                          synchronized(spectators) {
                            spectators.add(this);
                        }
                          synchronized(players) {
                            players.remove(this);
                          }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error connecting, Terminating. " + e.getMessage());
            } finally {
                closeResources();
            }
        }
        // close all the resrouces we opened
        private void closeResources() {
            try {
                synchronized(players) {
                    players.remove(this);
                    System.out.println("players connected: " + players.size());
                }
                if ( in != null) in .close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
