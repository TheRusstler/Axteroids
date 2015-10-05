import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.phidgets.*;
import com.phidgets.event.*;

public class Axteroids extends Application
		implements AttachListener, DetachListener, InputChangeListener, SensorChangeListener {

	SpaceShip spaceship;
	Scene scene;
	Pane root;

	@Override
	public void start(Stage stage) {
		root = new Pane();
		spaceship = new SpaceShip();
		root.getChildren().add(spaceship.shipPolygon);
		scene = new Scene(root, 800, 600);

		stage.setTitle("Axteroids");
		stage.setScene(scene);
		stage.show();

		startTimer();
		registerEvents();
	}

	private void startTimer() {
		new AnimationTimer() {
			public void handle(long now) {
				loop();
			}
		}.start();
	}

	private void registerEvents() {
		scene.setOnKeyPressed(ke -> {
			processKeyPress(ke, true);
		});

		scene.setOnKeyReleased(ke -> {
			processKeyPress(ke, false);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void loop() {
		spaceship.updateVelocity();
		spaceship.updatePosition(scene.getWidth(), scene.getHeight());
	}

	public void processKeyPress(KeyEvent ke, boolean isPressed) {
		switch (ke.getCode()) {
		case UP:
			spaceship.isAccelerating = isPressed;
			break;
		case DOWN:
			spaceship.isDecelerating = isPressed;
			break;
		case LEFT:
			spaceship.isTurningLeft = isPressed;
			break;
		case RIGHT:
			spaceship.isTurningRight = isPressed;
		default:
			break;
		}
	}

	@Override
	public void sensorChanged(SensorChangeEvent se) {
		System.out.println("Sensor changed!");
	}

	@Override
	public void inputChanged(InputChangeEvent ie) {
		System.out.println("Input changed!");
	}

	@Override
	public void detached(DetachEvent de) {
		System.out.println("Detached!");
	}

	@Override
	public void attached(AttachEvent ae) {
		System.out.println("Attached!");
	}
}
