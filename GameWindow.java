import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;

public class GameWindow extends JFrame {

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
	
	private Board board;
	private Rectangle2D.Double paddle;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public GameWindow(ObjectOutputStream out, String clientNumber) {
		
		paddle = new Rectangle2D.Double(200, 200, 200, 200);

		this.out = out;
		this.clientNumber = clientNumber;
		MV_LEFT += clientNumber;
		MV_RIGHT += clientNumber;
		MV_UP += clientNumber;
		MV_DOWN += clientNumber;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,900);
		
		board = new Board();
		
		add(board, BorderLayout.CENTER);
		setVisible(true);
	

		addKeyListener(new KeyListener() {

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
						paddle.x += 20;
						board.repaint();
						
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
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					
				if (paddle.y < e.getY()) {
						out.writeObject(MV_UP);
				}
				if (paddle.y > e.getY()) {
					out.writeObject(MV_DOWN);
				}
				
				
				paddle.x = e.getX();
				paddle.y = e.getY();
				repaint();
			
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			}
		});
	}
	
	private class Board extends JPanel {
		
		public Board() {
			setBackground(Color.BLACK);
		}
		
		public void paintComponent(Graphics g) {
			System.out.println("paint called");
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setPaint(Color.white);
			g2.fill(paddle);
			
		}
		
	}


}


