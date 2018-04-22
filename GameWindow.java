import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.ObjectOutputStream;
import java.io.*;

public class GameWindow extends JPanel {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	private Ball ball;
	private ObjectOutputStream out;
	private String clientNumber;

	private String MV_LEFT = "MV_LEFT ";
	private String MV_RIGHT = "MV_RIGHT ";
	private String MV_UP = "MV_UP ";
	private String MV_DOWN = "MV_DOWN ";

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public GameWindow(ObjectOutputStream out, String clientNumber) {

		this.out = out;
		this.clientNumber = clientNumber;
		MV_LEFT += clientNumber;
		MV_RIGHT += clientNumber;
		MV_UP += clientNumber;
		MV_DOWN += clientNumber;

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
				try {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						out.writeObject(MV_LEFT);
						System.out.println("Sent: " + MV_LEFT);
					}
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						out.writeObject(MV_UP);
						System.out.println("Sent: " + MV_UP);
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						out.writeObject(MV_RIGHT);
						System.out.println("Sent: " + MV_RIGHT);
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						out.writeObject(MV_DOWN);
						System.out.println("Sent: " + MV_DOWN);
					}
				} catch (IOException err) {
					System.out.println(err);
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
