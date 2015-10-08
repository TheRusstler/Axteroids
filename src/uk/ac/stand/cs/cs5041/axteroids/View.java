package uk.ac.stand.cs.cs5041.axteroids;

import java.util.Collection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class View {

	public static final int WIDTH = 800, HEIGHT = 600;

	private SpaceShip ship;

	private Scene scene;
	private Label score, soundBombReady, notification;
	private int scoreValue = 0;

	private BorderPane root;

	// ======= Setup =======
	public void start(Stage stage) {
		root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		scene = new Scene(root, WIDTH, HEIGHT);
		stage.setTitle("Axteroids");
		stage.setScene(scene);
		stage.show();
		createLabels();
	}
	
	public void updateColours(int brightness)
	{
		brightness = (int)((255/1000d)*brightness);
		
		final int newColor = brightness;

		Platform.runLater(() -> {
			root.setBackground(new Background(new BackgroundFill(Color.rgb(newColor, newColor, newColor), null, null)));
		});
	}
	
	public void addScore(int points)
	{
		scoreValue += points;
		updateScore();
	}
	
	public void resetScore()
	{
		scoreValue = 0;
		updateScore();
	}
	
	private void updateScore()
	{
		score.setText("Score: " + scoreValue);
	}

	public void addSpaceShip(SpaceShip ship) {
		this.ship = ship;
		root.getChildren().add(ship.polygon);
	}

	// ======= Main game =======
	public void addRock(Rock r) {
		root.getChildren().add(r.circle);
	}

	public void removeAllRocks(Collection<? extends Rock> rocks) {
		ObservableList<Node> rootChildren = root.getChildren();
		for (Rock r : rocks) {
			rootChildren.remove(r.circle);
		}
	}

	// ======= On screen text =======
	public void createLabels() {
		score = createLabel("Score: 0", Color.GREEN, 20, new Insets(20, 0, 0, 20));
		root.setTop(score);

		soundBombReady = createLabel("SOUND BOMB READY!", Color.DARKVIOLET, 20, new Insets(0, 0, 20, 20));
		root.setBottom(soundBombReady);

		notification = createLabel("", Color.RED, 60, new Insets(0, 0, 200, 0));
		notification.setOpacity(0);
		root.setCenter(notification);
	}

	private Label createLabel(String text, Color color, int size, Insets padding) {
		Label l = new Label(text);
		l.setFont(Font.font("Arial", FontWeight.MEDIUM, size));
		l.setPadding(padding);
		l.setTextFill(color);
		return l;
	}

	void notify(String text, Color colour) {
		FadeTransition in = new FadeTransition(Duration.millis(300), notification);
		in.setFromValue(0.0);
		in.setToValue(1.0);

		FadeTransition out = new FadeTransition(Duration.millis(1000), notification);
		out.setFromValue(1.0);
		out.setToValue(0);

		in.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.runLater(() -> out.play());
			}
		});

		Platform.runLater(() -> {
			notification.setText(text);
			notification.setTextFill(colour);
			in.play();
		});
	}
	
	void setSoundBombReadyBinding(BooleanProperty observable)
	{
		soundBombReady.visibleProperty().bind(observable);
	}

	// ======= Getters =======
	public Scene getScene() {
		return scene;
	}

}
