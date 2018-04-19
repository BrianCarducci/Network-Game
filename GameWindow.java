import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.PrintWriter;

public class GameWindow extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Ball ball;
	private PrintWriter out;
	private String clientNumber;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public GameWindow(PrintWriter out, String clientNumber) {
    	
    	this.out = out;
    	this.clientNumber = clientNumber;
    	
    	setPreferredSize(new Dimension(900,900));
    	setBackground(Color.black);
    	setLayout(null);
    	
    	JFrame frame = new JFrame("Pong boi");
        frame.setVisible(true);
        frame.add(this);

        ball = new Ball();
        ball.height = 50;
        ball.width = 50;
        ball.x = 50;
        ball.y = 50;
       //System.out.println(ball.height);

        frame.pack();
        
        frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					out.println("MV_LEFT " + clientNumber);
					System.out.println("pressed");
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					out.println("MV_UP " + clientNumber);
					System.out.println("pressed");
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					out.println("MV_RIGHT " + clientNumber);
					System.out.println("pressed");
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					out.println("MV_DOWN " + clientNumber);
					System.out.println("pressed");
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
					
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
              try {
                  //sysout.println("EXIT");
              } catch (Exception e) {
                  System.out.println(e);
              }
              System.exit(0);
          }
      });
    }
    
    public void paintComponent(Graphics g) {
//    	Graphics2D g2 = (Graphics2D) g;
//    	System.out.println(ball.getHeight());
//    	g2.fill(ball);
    	
    }

}