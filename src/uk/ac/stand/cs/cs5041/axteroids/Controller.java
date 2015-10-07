package uk.ac.stand.cs.cs5041.axteroids;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Controller implements AttachListener, InputChangeListener, SensorChangeListener {

	static final int IK_SERIAL = 274071, SERVO_SERIAL = 306019;
	static final int JOYSTICK_BUTTON_INDEX = 0;
	static final int X_INDEX = 0, Y_INDEX = 1;
	static final int ROTATION_INDEX = 2;
	static final int SOUND_INDEX = 3;
	static final int LIGHT_INDEX = 4;
	
	static final int SOUND_BOMB_RESPAWN = 5000;

	private AdvancedServoPhidget servo;
	private InterfaceKitPhidget phidget;
	private SpaceShip ship;

	private int difficulty = 0;
	private long lastSoundBomb = 0;

	private Runnable soundBomb;

	public Controller(SpaceShip ship, Runnable soundBomb) {
		this.ship = ship;
		this.soundBomb = soundBomb;
		registerEvents();
	}

	// Bindings
	public SimpleBooleanProperty isSoundBombReady = new SimpleBooleanProperty();

	public final boolean getIsSoundBombReady() {
		return isSoundBombReady.get();
	}

	public final void setIsSoundBombReady(boolean value) {
		isSoundBombReady.set(value);
	}

	public BooleanProperty isSoundBombReadyProperty() {
		return isSoundBombReady;
	}

	private void registerEvents() {
		try {
			phidget = new InterfaceKitPhidget();
			phidget.addSensorChangeListener(this);
			phidget.addInputChangeListener(this);
			phidget.open(IK_SERIAL);

		} catch (PhidgetException e) {
			e.printStackTrace();
		}

		try {
			servo = new AdvancedServoPhidget();
			servo.open(SERVO_SERIAL);
			servo.addAttachListener(this);

		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}

	void update() {

		try {
			if (servo.isAttached()) {
				int turn = (int) ship.velocity.magnitude() * 20;
				int max = (int) servo.getPositionMax(0);

				if (turn > max)
					turn = max;

				servo.setPosition(0, turn);
			}

		} catch (PhidgetException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void sensorChanged(SensorChangeEvent se) {
		switch (se.getIndex()) {
		case X_INDEX:
			if (isSensorNearCentre(se) == false)
				ship.turn = se.getValue() - 500;
			else
				ship.turn = 0;
			break;

		case Y_INDEX:
			if (isSensorNearCentre(se) == false)
				ship.acceleration = se.getValue() - 500;
			else
				ship.acceleration = 0;
			break;

		case ROTATION_INDEX:
			difficulty = se.getValue();
			break;

		case SOUND_INDEX:
			setIsSoundBombReady(isSoundBombReady());
			if (se.getValue() > 100 && getIsSoundBombReady()) {
				soundBomb.run();
				lastSoundBomb = System.nanoTime();
			}
			break;
			
		case LIGHT_INDEX:
			break;
		}
	}

	boolean isSoundBombReady() {
		double difference = (System.nanoTime() - lastSoundBomb) / 1e6;
		return difference > SOUND_BOMB_RESPAWN;
	}

	boolean isSensorNearCentre(SensorChangeEvent se) {
		return se.getValue() > 400 && se.getValue() < 600;
	}

	@Override
	public void inputChanged(InputChangeEvent ie) {
		if (ie.getIndex() == JOYSTICK_BUTTON_INDEX) {
			if (ie.getState() == true) {
				ship.stop();
			}
		}
	}

	public int getDifficulty() {
		return difficulty;
	}

	@Override
	public void attached(AttachEvent e) {
		try {
			System.out.println("Servo attached!");
			servo.setEngaged(0, true);
		} catch (PhidgetException e1) {
			e1.printStackTrace();
		}
	}
}
