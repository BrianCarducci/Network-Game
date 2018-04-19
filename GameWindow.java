import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.io.PrintWriter;

public class GameWindow extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Ball ball;
	private PrintWriter out;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public GameWindow(PrintWriter out) {
    	
    	this.out = out;
    	
    	setPreferredSize(new Dimension(900,900));
    	setBackground(Color.black);
    	setLayout(null);
    	
    	JFrame frame = new JFrame("Pong boi");
        frame.setVisible(true);
        frame.add(this);

        ball = new Ball(0,0,0);

        frame.pack();
        
        frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					out.println("MV_LEFT 0");
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
    	super.paintComponent(g);
    	g.setColor(Color.WHITE);
    	g.fillArc(20, 20, 250, 250, 0, 360);
    }

}