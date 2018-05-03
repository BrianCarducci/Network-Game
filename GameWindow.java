import javax.swing.JComponent;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.geom.*;
public class GameWindow extends JFrame {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	private ObjectOutputStream out;
	private int clientNumber;

	private Board board;
	private Rectangle2D.Double[] paddles;
	private Ellipse2D.Double ball;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank cursor");

	public GameWindow() {
		paddles = new Rectangle2D.Double[]{new Rectangle2D.Double(16, 200, 30, 200), new Rectangle2D.Double(854, 200, 30, 200),
			new Rectangle2D.Double(200, 5, 200, 30), new Rectangle2D.Double(200, 800, 200, 30)};
			ball = new Ellipse2D.Double(350,350,67,67);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setSize(900,900);

			board = new Board();

			add(board, BorderLayout.CENTER);
			setVisible(true);

			getContentPane().setCursor(blankCursor);
		}

		public GameWindow(ObjectOutputStream out, int clientNumber) {
			paddles = new Rectangle2D.Double[]{new Rectangle2D.Double(16, 200, 30, 200), new Rectangle2D.Double(854, 200, 30, 200),
				new Rectangle2D.Double(200, 5, 200, 30), new Rectangle2D.Double(200, 800, 200, 30)};
				ball = new Ellipse2D.Double(350,350,67,67);

				this.out = out;
				this.clientNumber = clientNumber;

				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setResizable(false);
				setSize(900,900);

				board = new Board();

				add(board, BorderLayout.CENTER);
				setVisible(true);

				getContentPane().setCursor(blankCursor);


				/*addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				try {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				out.writeObject({e.getX(), e.getY()}); //fak
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

}); */

addKeyListener(new KeyListener() {
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		try {
			if (e.getKeyCode() == KeyEvent.VK_R) {
				out.writeObject(new String("RESET"));
				System.out.println("Sent: RESET");
			}
		} catch (IOException err) {
			System.out.println(err);
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
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
			//System.out.println("MouseX: " + e.getX() + "   MouseY: " + e.getY());
			if (clientNumber < 5) {
				if (clientNumber == 1 || clientNumber == 2) {
					out.writeObject(new Integer[]{clientNumber, e.getY()-32});
				}
				if (clientNumber == 3 || clientNumber == 4) {
					out.writeObject(new Integer[]{clientNumber, e.getX()-32});
				}
			}
			//System.out.println("Sent: Updated Y Coords");

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
});
}

public void movePaddle(Integer[][] coords) {
	ball.x = coords[0][0];
	ball.y = coords[0][1];
	for(int i = 1; i<coords.length; i++) {
		int tempClientNum = coords[i][0];
		if (tempClientNum == 0 || tempClientNum == 1) {
			paddles[tempClientNum].y = coords[i][1];
		}
		if (tempClientNum == 2 || tempClientNum == 3) {
			paddles[tempClientNum].x = coords[i][1];
		}
	}


	repaint();
}

private class Board extends JPanel {

	public Board() {
		setBackground(Color.BLACK);
	}

	public void paintComponent(Graphics g) {
		//System.out.println("paint called");
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(Color.white);
		for (Rectangle2D.Double paddle : paddles) {
			g2.fill(paddle);
		}
		g2.fill(ball);

	}

}


}
