package uk.ac.stand.cs.cs5041.axteroids;

public class Rock {
	private double posX = 0, posY = 0, speedX = 0, speedY = 0, direction = 0;
	
	public static Rock SpawnRock()
	{
		return new Rock();
	}
}
