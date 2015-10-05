package uk.ac.stand.cs.cs5041.axteroids;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

public class Controller  implements InputChangeListener, SensorChangeListener {
	static final int IK_SERIAL = 274071, ROTATION_SENSOR_INDEX = 0;
	static final int X_INDEX = 0, Y_INDEX = 1;
	
	InterfaceKitPhidget phidget;
	SpaceShip ship;
	
	public Controller(SpaceShip ship)
	{
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
		}
	}

	boolean isSensorNearCentre(SensorChangeEvent se) {
		return se.getValue() > 400 && se.getValue() < 600;
	}

	@Override
	public void inputChanged(InputChangeEvent ie) {
		if (ie.getIndex() == ROTATION_SENSOR_INDEX) {
			if (ie.getState() == true) {
				ship.stop();
			}
		}
	}
}
