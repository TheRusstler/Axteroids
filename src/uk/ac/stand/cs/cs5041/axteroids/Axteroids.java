package uk.ac.stand.cs.cs5041.axteroids;

import java.util.ArrayList;
import javafx.animation.*;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Axteroids extends Application {

	Label soundBombLabel;
	SpaceShip ship;
	Scene scene;
	BorderPane root;
	Controller controller;
	AnimationTimer timer;
	int rockSpawnDelay = 100;
	int difficulty = 0;

	ArrayList<Rock> rocks = new ArrayList<Rock>();
	ArrayList<Missile> missiles = new ArrayList<Missile>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root = new BorderPane();
		ship = new SpaceShip();

		root.getChildren().add(ship.polygon);
		soundBombLabel = addNotificationLabel("SOUND BOMB!", Color.BLUEVIOLET);

		root.setCenter(soundBombLabel);
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		scene = new Scene(root, 800, 600);

		stage.setTitle("Axteroids");
		stage.setScene(scene);
		stage.show();

		startTimer();
		controller = new Controller(ship, () -> soundBomb());

		scene.setOnKeyPressed(ke -> {
			processKeyPress(ke, true);
		});
	}
	
	Label addNotificationLabel(String text, Color colour)
	{
		Label l = new Label(text);
		l.setFont(Font.font(50));
		l.setOpacity(0);
		l.setAlignment(Pos.CENTER);
		l.setTextFill(colour);
		root.setCenter(l);
		return l;
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

	void soundBomb() {
		textNotification(soundBombLabel);
		Platform.runLater(() -> clearAllRocks());
	}
	
	void textNotification(Label l)
	{
		FadeTransition in = new FadeTransition(Duration.millis(300), l);
		in.setFromValue(0.0);
		in.setToValue(1.0);
		
		FadeTransition out = new FadeTransition(Duration.millis(1000), l);
		out.setFromValue(1.0);
		out.setToValue(0);
		
		in.setOnFinished(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent event) {
		    	Platform.runLater(() -> out.play());
		    }
		});
		
		Platform.runLater(() -> in.play());
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
				clearAllRocks();
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
	
	int difficulty()
	{
		return (int) ((controller.getDifficulty())/50);
	}

	@SuppressWarnings("unchecked")
	void clearAllRocks() {
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
