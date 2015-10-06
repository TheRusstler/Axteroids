package uk.ac.stand.cs.cs5041.axteroids;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

public class Controller implements AttachListener, InputChangeListener, SensorChangeListener {

	static final int IK_SERIAL = 274071, SERVO_SERIAL = 306019;
	static final int JOYSTICK_BUTTON_INDEX = 0;
	static final int X_INDEX = 0, Y_INDEX = 1, ROTATION_INDEX = 2;

	AdvancedServoPhidget servo;
	InterfaceKitPhidget phidget;
	SpaceShip ship;
	private int difficulty = 0;

	public Controller(SpaceShip ship) {
		this.ship = ship;
		registerEvents();
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
		int turn = (int)ship.velocity.magnitude() * 50;
		
		if(turn > 300)
			turn = 300;
		
		try {
			if(servo.isAttached())
			{
				servo.setPosition(0, turn);
			}
			
		} catch (PhidgetException e) {
			e.printStackTrace();
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
			System.out.println("DIFF: " + difficulty);
		}
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
