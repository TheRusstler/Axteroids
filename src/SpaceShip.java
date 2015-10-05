import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

class SpaceShip {

	final static double SHIP_WIDTH = 10, SHIP_LENGTH = 15, LINE_WIDTH_EXTERNAL = 2;
	final static double JOYSTICK_DAMPING = 3000d;

	private double posX = 0, posY = 0, speedX = 0, speedY = 0, direction = 0;
	Polygon shipPolygon;
	public Color color;

	public int acceleration, turn;
	public boolean isAccelerating, isDecelerating, isTurningLeft, isTurningRight;

	public SpaceShip() {
		shipPolygon = new Polygon();
		shipPolygon.getPoints().addAll(new Double[] { -SHIP_WIDTH / 2, 0.0, +SHIP_WIDTH / 2, 0.0, 0.0, SHIP_LENGTH });
		color = Color.RED;
		shipPolygon.setStroke(Color.BLACK);
		shipPolygon.setFill(color);
		shipPolygon.setStrokeWidth(LINE_WIDTH_EXTERNAL);
		center();
	}

	public void updatePosition(double sizeSceneX, double sizeSceneY) {
		posX = posX + speedX;
		posY = posY + speedY;

		if (posX > sizeSceneX) {
			posX = posX % sizeSceneX;
		}
		if (posY > sizeSceneY) {
			posY = posY % sizeSceneY;
		}
		if (posX < 0) {
			posX = sizeSceneX;
		}
		if (posY < 0) {
			posY = sizeSceneY;
		}
		if (direction > Math.PI * 2) {
			direction = 0;
		}
		if (direction < 0) {
			direction = Math.PI * 2;
		}

		shipPolygon.setRotate(-90 - direction * 180 / Math.PI); // setRotate
																// works in
																// degrees
		shipPolygon.setTranslateX(posX);
		shipPolygon.setTranslateY(posY);
	}

	public void updateVelocity() {
		increaseSpeed(acceleration / JOYSTICK_DAMPING);
		turn(turn / JOYSTICK_DAMPING);
	}
	
	public void center()
	{
		posX = 400;
		posY = 300;
	}
	
	public void stop()
	{
		speedX = 0;
		speedY = 0;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public void increaseSpeed(double accel) {
		speedX = speedX + Math.cos(-direction) * accel;
		speedY = speedY + Math.sin(-direction) * accel;
	}

	public void turn(double d) {
		direction = direction + d;
		if (direction > Math.PI * 2)
			direction = 0;
		if (direction < 0)
			direction = Math.PI * 2;
	}

}
