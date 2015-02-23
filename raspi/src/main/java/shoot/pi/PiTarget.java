package shoot.pi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import shoot.api.ITarget;

/**
 *
 * @author wassj
 *
 */
public class PiTarget
	implements ITarget {

	private static final Logger logger = LoggerFactory.getLogger(ITarget.class);
	private final String id;
	private final GpioPinDigitalOutput pin;

	private boolean active;


	public PiTarget(final String id, final GpioPinDigitalOutput pin) {
		this.id = id;
		this.pin = pin;
	}


	public String id() {
		return id;
	}


	public boolean isActive() {
		return active;
	}


	public ITarget activate() {
		if (!active) {
			logger.trace("activate " + id);
			pin.setState(true);
			active = true;
		}
		return this;
	}


	public ITarget deactivate() {
		if (active) {
			logger.trace("deactivate " + id);
			pin.setState(false);
			active = false;
		}
		return this;
	}


	@Override
	public String toString() {
		return "PiTarget[" + id + "]";
	}
}
