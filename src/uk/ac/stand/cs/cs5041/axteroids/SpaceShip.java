package uk.ac.stand.cs.cs5041.axteroids;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.geometry.Point2D;

class SpaceShip {

	final static double SHIP_WIDTH = 10, SHIP_LENGTH = 15, LINE_WIDTH_EXTERNAL = 2;
	final static double JOYSTICK_DAMPING = 3000d;

	Color color;
	Polygon polygon;
	int acceleration, turn;
	
	Point2D position;
	private Point2D velocity;
	private double direction = 0;

	public SpaceShip() {
		polygon = new Polygon();
		polygon.getPoints().addAll(new Double[] { -SHIP_WIDTH / 2, 0.0, +SHIP_WIDTH / 2, 0.0, 0.0, SHIP_LENGTH });
		color = Color.RED;
		polygon.setStroke(Color.WHITE);
		polygon.setFill(color);
		polygon.setStrokeWidth(LINE_WIDTH_EXTERNAL);
		center();
		stop();
	}

	public void updatePosition(double sizeSceneX, double sizeSceneY) {
		position = position.add(velocity.getX(), velocity.getY());
		jumpBounderies(sizeSceneX, sizeSceneY);
		
		updateDirection();
		
		polygon.setTranslateX(position.getX());
		polygon.setTranslateY(position.getY());
	}

	private void updateDirection() {
		if (direction > Math.PI * 2) {
			direction = 0;
		}
		if (direction < 0) {
			direction = Math.PI * 2;
		}

		polygon.setRotate(-90 - direction * 180 / Math.PI); // setRotate
																// works in
																// degrees
	}

	private void jumpBounderies(double sizeSceneX, double sizeSceneY) {
		if (position.getX() > sizeSceneX) {
			position = position.subtract(position.getX(), 0);
		}
		if (position.getY() > sizeSceneY) {
			position = position.subtract(0, position.getY());
		}
		if(position.getX() < 0)
		{
			position = position.add(sizeSceneX, 0);
		}
		if(position.getY() < 0)
		{
			position = position.add(0, sizeSceneY);
		}
	}

	public void updateVelocity() {
		double accel = acceleration / JOYSTICK_DAMPING;
		velocity = velocity.add(Math.cos(-direction) * accel, Math.sin(-direction) * accel);
		turn(turn / JOYSTICK_DAMPING);
	}

	public void turn(double d) {
		direction = direction + d;
		if (direction > Math.PI * 2)
			direction = 0;
		if (direction < 0)
			direction = Math.PI * 2;
	}
	
	public void center()
	{
		position = new Point2D(400, 300);
	}
	
	public void stop()
	{
		velocity = new Point2D(0, 0);
	}
}
