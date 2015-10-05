package uk.ac.stand.cs.cs5041.axteroids;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.geometry.Point2D;

class SpaceShip {

	final static double SHIP_WIDTH = 10, SHIP_LENGTH = 15, LINE_WIDTH_EXTERNAL = 2;
	final static double JOYSTICK_DAMPING = 3000d;

	private Point2D position, velocity = new Point2D(0, 0);
	private double direction = 0;
	
	Polygon polygon;
	public Color color;

	public int acceleration, turn;

	public SpaceShip() {
		polygon = new Polygon();
		polygon.getPoints().addAll(new Double[] { -SHIP_WIDTH / 2, 0.0, +SHIP_WIDTH / 2, 0.0, 0.0, SHIP_LENGTH });
		color = Color.RED;
		polygon.setStroke(Color.BLACK);
		polygon.setFill(color);
		polygon.setStrokeWidth(LINE_WIDTH_EXTERNAL);
		center();
	}

	public void updatePosition(double sizeSceneX, double sizeSceneY) {
		position = position.add(velocity.getX(), velocity.getY());

		if (position.getX() > sizeSceneX || position.getX() < 0) {
			position = position.subtract(position.getX(), 0);
		}
		if (position.getY() > sizeSceneY || position.getY() < 0) {
			position = position.subtract(0, position.getY());
		}

		if (direction > Math.PI * 2) {
			direction = 0;
		}
		if (direction < 0) {
			direction = Math.PI * 2;
		}

		polygon.setRotate(-90 - direction * 180 / Math.PI); // setRotate
																// works in
																// degrees
		polygon.setTranslateX(position.getX());
		polygon.setTranslateY(position.getY());
	}

	public void updateVelocity() {
		increaseSpeed(acceleration / JOYSTICK_DAMPING);
		turn(turn / JOYSTICK_DAMPING);
	}
	
	public void center()
	{
		position = new Point2D(400, 300);
	}
	
	public void stop()
	{
		velocity = new Point2D(0, 0);
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public void increaseSpeed(double accel) {
		velocity = velocity.add(Math.cos(-direction) * accel, Math.sin(-direction) * accel);
		System.out.println(velocity);
	}

	public void turn(double d) {
		direction = direction + d;
		if (direction > Math.PI * 2)
			direction = 0;
		if (direction < 0)
			direction = Math.PI * 2;
	}

}
