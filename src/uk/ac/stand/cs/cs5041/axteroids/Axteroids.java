package uk.ac.stand.cs.cs5041.axteroids;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
	int rockSpawnDelay = 100;
	int difficulty = 0; // 0-1000

	ArrayList<Rock> rocks = new ArrayList<Rock>();
	ArrayList<Missile> missiles = new ArrayList<Missile>();

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

		if (ke.getCode() == KeyCode.ENTER) {
			spawnRock();
		}

		if (ke.getCode() == KeyCode.SPACE) {
			fireMissile();
		}
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
		updateMissiles();
		update();
		controller.update();
	}

	void fireMissile() {
		Missile m;
		Point2D pos, vel;

		pos = new Point2D(ship.position.getX(), ship.position.getY() + SpaceShip.SHIP_LENGTH / 2);
		vel = new Point2D(Math.cos(ship.direction), -1 * Math.sin(ship.direction)).multiply(4);
		m = new Missile(pos, vel);

		missiles.add(m);
		root.getChildren().add(m.circle);
	}

	private void spawnRock() {
		Rock newRock = Rock.SpawnRock(scene.getWidth(), scene.getHeight());
		rocks.add(newRock);
		root.getChildren().add(newRock.circle);
	}

	void updateMissiles() {
		ArrayList<Missile> exploded = new ArrayList<Missile>();

		for (Missile m : missiles) {
			m.update(scene.getWidth(), scene.getHeight());
			if (m.isExploded) {
				exploded.add(m);
			}
		}

		removeMissiles(exploded);
	}

	void update() {
		ArrayList<Rock> destroyed = new ArrayList<Rock>();
		ArrayList<Missile> missilesHit = new ArrayList<Missile>();

		for (Rock r : rocks) {
			r.update(scene.getWidth(), scene.getHeight());
			if (r.isHit(ship.position, 8)) {
				shipHit();
				return;
			}

			missilesHit = new ArrayList<Missile>();
			for (Missile m : missiles) {
				if (m.isHit(r.position, r.radius)) {
					missilesHit.add(m);
					destroyed.add(r);
				}
			}
		}

		removeMissiles(missilesHit);
		removeRocks(destroyed);

		checkRockSpawn();
	}

	private void checkRockSpawn() {
		if (rockSpawnDelay < 0) {
			spawnRock();
			rockSpawnDelay = controller.getDifficulty() / 5;
		}

		if (rockSpawnDelay > controller.getDifficulty()) {
			rockSpawnDelay = controller.getDifficulty() / 5;
		}

		rockSpawnDelay--;
	}

	@SuppressWarnings("unchecked")
	void shipHit() {
		removeRocks((ArrayList<Rock>) rocks.clone());
	}

	void removeRocks(ArrayList<Rock> destroyed) {
		rocks.removeAll(destroyed);

		for (Rock r : destroyed) {
			root.getChildren().remove(r.circle);
		}
	}

	void removeMissiles(ArrayList<Missile> miss) {
		missiles.removeAll(miss);

		for (Missile m : miss) {
			root.getChildren().remove(m.circle);
		}
	}
}
