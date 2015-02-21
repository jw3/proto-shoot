package shoot.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;

import shoot.api.ISensor;
import shoot.api.IStatusIndicator;
import shoot.api.ITarget;

/**
 *
 * @author wassj
 *
 */
public enum PiShoot {
	/*noinstance*/;

	static final GpioController gpio = GpioFactory.getInstance();


	public static ITarget newTarget(final String id, final Pin pin) {
		return new PiTarget(id, gpio.provisionDigitalOutputPin(pin, id));
	}


	public static ISensor newSensor(final String id, final Pin pin, final ITarget target) {
		return new HitSensor(id, target, gpio.provisionDigitalInputPin(pin, id));
	}


	public static IStatusIndicator newIndicator(final String id, final Pin pin) {
		return new PiStatusLed(id, gpio.provisionDigitalOutputPin(pin, id));
	}
}
