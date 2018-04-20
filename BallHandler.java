
//public class BallHandler {
//	Ball ball = new Ball();
//	
//	public void startBall() {
//		double angle = 360 * Math.random();
//		ball.setLoc(50-(ball.getWidth()/2), 50-(ball.getHeight()/2)); //sets the ball in the center of the screen, taking the size of the ball into account
//		ball.setVel(Math.sin(angle), Math.cos(angle));
//	}
//	
//	
//	 public void bounceBall(player Player) {
//		double xVel = ball.getXVel();
//		double yVel = ball.getYVel();
//	  switch(Player) {
//	   	case 1: Player = player1;//top
//	   		ball.setVel(-xVel, yVel);
//	   		break;
//	   	case 2: Player = player2;//bottom
//	   		ball.setVel(-xVel, yVel);
//	   		break;
//	   	case 3: PLayer = player3;//left
//	   		ball.setVel(xVel, -yVel);
//	   		break;
//	   	case 4: Player = player4; //right
//	   		ball.setVel(xVel, -yVel);
//	  }
//	 lastPlayer = Player; //(sets the lastplayer to the most recent player to hit the ball
//	}
//}
