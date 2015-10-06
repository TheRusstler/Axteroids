package uk.ac.stand.cs.cs5041.axteroids;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Axteroids extends Application {

	SpaceShip ship;
	Scene scene;
	Pane root;
	Controller controller;
	AnimationTimer timer;
	
	ArrayList<Rock> rocks = new ArrayList<Rock>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root = new Pane();
		ship = new SpaceShip();

		root.getChildren().add(ship.polygon);
		scene = new Scene(root, 800, 600);
		scene.setFill(Color.BLACK);

		stage.setTitle("Axteroids");
		stage.setScene(scene);
		stage.show();

		startTimer();
		controller = new Controller(ship);
		
		scene.setOnKeyPressed(ke -> {
			processKeyPress(ke, true);
		});
	}
	
	public void processKeyPress(KeyEvent ke, boolean isPressed) {
		Rock newRock = Rock.SpawnRock(scene.getWidth(), scene.getHeight());
		rocks.add(newRock);
		root.getChildren().add(newRock.circle);
		System.out.println("new rock. count: " + rocks.size());
	}

	private void startTimer() {
		new AnimationTimer() {
			public void handle(long now) {
				loop();
			}
		}.start();
	}

	public void loop() {
		ship.updateVelocity();
		ship.updatePosition(scene.getWidth(), scene.getHeight());
		
		for(Rock r : rocks)
		{
			r.update(scene.getWidth(), scene.getHeight());
		}
	}
}
