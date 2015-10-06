package uk.ac.stand.cs.cs5041.axteroids;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Missile extends Rock {

	public boolean isExploded = false;
	private int range = 100;
	
	public Missile(Point2D position, Point2D velocity) {
		super(position, velocity, 2, Color.RED);
	}
	
	public void update(double sizeSceneX, double sizeSceneY) {
		super.update(sizeSceneX, sizeSceneY);
		range--;
		
		if(range < 0)
		{
			isExploded = true;
		}
	}
}
