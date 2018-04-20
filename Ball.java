import java.awt.geom.*;
import java.math.*;

public class Ball extends Ellipse2D.Double {	
	
    final private int width = 0;
    final private int height = 0;
    private int xLoc = 0;
    private int yLoc = 0;
    private double xVel = 0;
    private double yVel = 0;
    private double angle;

    public Ball() {
    	
    }

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return xLoc;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return yLoc;
	}
	
	public double getXVel() {
		return xVel;
	}
	
	public double getYVel() {
		return yVel;
	}
	
	public void setLoc(int newX, int newY) {
		xLoc = newX;
		yLoc = newY;
	}
	
	public void setVel(double newxVel, double newyVel) {
		xVel = newxVel;
		yVel = newyVel;
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