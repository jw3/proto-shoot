package shoot.pi;

import java.util.concurrent.Future;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import shoot.api.IStatusIndicator;

/**
 *
 * @author wassj
 *
 */
public class PiStatusLed
	implements IStatusIndicator {

	private final String id;
	private final GpioPinDigitalOutput pin;


	public PiStatusLed(final String id, final GpioPinDigitalOutput pin) {
		this.id = id;
		this.pin = pin;
	}


	public String id() {
		return id;
	}


	public Future<Void> alert() {
		return (Future<Void>)pin.blink(100, 3000);
	}


	@Override
	public String toString() {
		return "Status[" + id + "]";
	}
}
