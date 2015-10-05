package uk.ac.stand.cs.cs5041.axteroids;

import javafx.scene.paint.Color;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class Rock {
	static Random rand = new Random();
	
	Point2D position, velocity;
	Circle circle;
	
	public Rock(Point2D position, Point2D velocity)
	{
		this.position = position;
		this.velocity = velocity;
		circle = new Circle(5, Color.BLACK);
	}
	
	public void update()
	{
		position = position.add(velocity.getX(), velocity.getY());
		circle.setTranslateX(position.getX());
		circle.setTranslateY(position.getY());
	}
	
	public static Rock SpawnRock(double sizeSceneX, double sizeSceneY)
	{
		Point2D pos, vel;

		//pos = new Point2D(rand.nextInt((int)sizeSceneX), rand.nextInt((int)sizeSceneY));
		pos = new Point2D(200,200);
		vel = new Point2D(rand.nextDouble(), rand.nextDouble());
		
		// Either x, or y must be negative to spawn rock off screen
//		if(rand.nextBoolean())
//		{
//			pos = new Point2D(pos.getX() * -1, pos.getY());
//			vel = new Point2D(vel.getX(), vel.getY());
//		}
//		else
//		{
//			pos = new Point2D(pos.getX(), pos.getY() * -1);
//		}
		
		// TODO: BE SMARTER!
		
		return new Rock(pos, vel);
	}
}
