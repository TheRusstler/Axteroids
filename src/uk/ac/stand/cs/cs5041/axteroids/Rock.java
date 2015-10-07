package uk.ac.stand.cs.cs5041.axteroids;

import javafx.scene.paint.Color;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class Rock {
	static Random rand = new Random();

	Point2D position, velocity;
	Circle circle;
	int radius;

	public Rock(Point2D position, Point2D velocity, int radius, Color color) {
		this.position = position;
		this.velocity = velocity;
		this.radius = radius;
		circle = new Circle(radius, color);
	}

	public void update(double sizeSceneX, double sizeSceneY) {
		position = position.add(velocity.getX(), velocity.getY());
		circle.setTranslateX(position.getX());
		circle.setTranslateY(position.getY());
		jumpBounderies(sizeSceneX, sizeSceneY);
	}
	
	public boolean isHit(Point2D point, int rad)
	{
		return point.distance(this.position) < (rad + this.radius);
	}

	private void jumpBounderies(double sizeSceneX, double sizeSceneY) {
		if (position.getX() - radius > sizeSceneX) {
			position = position.subtract(position.getX(), 0);
		}
		if (position.getY() - radius > sizeSceneY) {
			position = position.subtract(0, position.getY());
		}
		if (position.getX() + radius < 0) {
			position = position.add(sizeSceneX, 0);
		}
		if (position.getY() + radius < 0) {
			position = position.add(0, sizeSceneY);
		}
	}

	public static Rock SpawnRock(double sizeSceneX, double sizeSceneY) {
		Rock newRock;
		Point2D pos, vel;
		Color color;
		
		color = Color.rgb(100 + rand.nextInt(100), 100 + rand.nextInt(100), 255);

		pos = randomSpawnPosition(sizeSceneX, sizeSceneY);
		vel = randomVelocity(pos, sizeSceneX, sizeSceneY);

		newRock = new Rock(pos, vel, 10 + rand.nextInt(3), color);
		newRock.update(sizeSceneX, sizeSceneY);
		
		return newRock;
	}

	private static Point2D randomSpawnPosition(double sizeSceneX, double sizeSceneY) {
		Point2D pos;
		switch (rand.nextInt(3)) {
		case 0:
			pos = new Point2D(0, rand.nextInt((int) sizeSceneY)); // LEFT
			break;
		case 1:
			pos = new Point2D(rand.nextInt((int) sizeSceneX), 0); // TOP
			break;
		case 2:
			pos = new Point2D(sizeSceneX, rand.nextInt((int) sizeSceneY)); // RIGHT
			break;
		case 3:
			pos = new Point2D(rand.nextInt((int) sizeSceneX), sizeSceneY); // BOTTOM
			break;
		default:
			pos = new Point2D(0, 0);
		}
		return pos;
	}

	private static Point2D randomVelocity(Point2D pos, double sizeSceneX, double sizeSceneY) {
		double velX, velY;

		velX = rand.nextDouble() * 2;
		velY = rand.nextDouble() * 2;

		if (pos.getX() == sizeSceneX || (pos.getX() != 0 && rand.nextBoolean())) {
			velX = velX * -1;
		}

		if (pos.getY() == sizeSceneY || (pos.getY() != 0 && rand.nextBoolean())) {
			velY = velY * -1;
		}

		return new Point2D(velX, velY);
	}
}
