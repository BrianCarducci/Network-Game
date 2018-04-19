import java.awt.geom.*;

public class Ball extends Ellipse2D.Double {
    private int xLoc = 0;
    private int yLoc = 0;
    private int direction = 0;

    public Ball(int xLoc, int yLoc, int direction) {
    	super(50,50,50,50);
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.direction = direction;
    }

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFrame(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		
	}
}