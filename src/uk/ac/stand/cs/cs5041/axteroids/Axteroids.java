package uk.ac.stand.cs.cs5041.axteroids;

import java.util.ArrayList;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Axteroids extends Application {

	private View view;

	SpaceShip ship;
	Controller controller;

	AnimationTimer timer;

	int rockSpawnDelay = 100;
	int difficulty = 0, score = 0;
	boolean firing = false;

	ArrayList<Rock> rocks = new ArrayList<Rock>();
	ArrayList<Missile> missiles = new ArrayList<Missile>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		view = new View();
		view.start(stage);

		ship = new SpaceShip();
		view.addSpaceShip(ship);

		controller = new Controller(ship, () -> soundBomb(), () -> brightnessChanged());

		view.getScene().setOnKeyPressed(ke -> {
			if (ke.getCode() == KeyCode.SPACE)
				firing = true;
		});
		view.getScene().setOnKeyReleased(ke -> {
			if (ke.getCode() == KeyCode.SPACE)
				firing = false;
		});

		view.setSoundBombReadyBinding(controller.isSoundBombReadyProperty());

		view.notify("BEGIN", Color.GREEN);
		startTimer();
	}

	private void brightnessChanged() {
		view.updateColours(controller.getBrightness());
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
		ship.updatePosition(View.WIDTH, View.HEIGHT);
		updateMissiles();
		update();
		controller.update();

		if (firing) {
			fireMissile();
		}
	}

	void soundBomb() {
		view.notify("SOUND BOMB!", Color.BLUEVIOLET);
		Platform.runLater(() -> {
			clearAllRocks();
			view.addScore(100);
		});
	}

	void fireMissile() {
		Missile m;
		Point2D pos, vel;

		pos = new Point2D(ship.position.getX(), ship.position.getY() + SpaceShip.SHIP_LENGTH / 2);
		vel = new Point2D(Math.cos(ship.direction), -1 * Math.sin(ship.direction)).multiply(4);
		m = new Missile(pos, vel);

		missiles.add(m);
		view.addRock(m);
	}

	void shipHit() {
		clearAllRocks();
		ship.stop();
		ship.center();
		view.notify("FAIL", Color.RED);
	}

	private void spawnRock() {
		Rock newRock = Rock.SpawnRock(View.WIDTH, View.HEIGHT);
		rocks.add(newRock);
		view.addRock(newRock);
	}

	void updateMissiles() {
		ArrayList<Missile> exploded = new ArrayList<Missile>();

		for (Missile m : missiles) {
			m.update(View.WIDTH, View.HEIGHT);
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
			r.update(View.WIDTH, View.HEIGHT);
			if (r.isHit(ship.position, 8)) {
				shipHit();
				view.resetScore();
				return;
			}

			missilesHit = new ArrayList<Missile>();
			for (Missile m : missiles) {
				if (m.isHit(r.position, r.radius)) {
					missilesHit.add(m);
					destroyed.add(r);
					view.addScore(1);
				}
			}
			removeMissiles(missilesHit);
		}
		removeRocks(destroyed);

		checkRockSpawn();
	}

	private void checkRockSpawn() {
		int diff = difficulty();
		if (rockSpawnDelay < 0) {
			spawnRock();
			rockSpawnDelay = diff;
		}

		if (rockSpawnDelay > diff) {
			rockSpawnDelay = diff;
		}

		rockSpawnDelay--;
	}

	int difficulty() {
		return (int) ((controller.getDifficulty()) / 50);
	}

	@SuppressWarnings("unchecked")
	void clearAllRocks() {
		removeRocks((ArrayList<Rock>) rocks.clone());
	}

	void removeRocks(ArrayList<Rock> destroyed) {
		view.removeAllRocks(destroyed);
		rocks.removeAll(destroyed);
	}

	void removeMissiles(ArrayList<Missile> miss) {
		view.removeAllRocks(miss);
		missiles.removeAll(miss);
	}
}
