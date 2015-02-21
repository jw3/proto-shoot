package shoot.pi;

import java.util.Collection;

import cmd4j.Chains;
import cmd4j.Commands;
import cmd4j.ICommand;

import com.google.common.collect.Lists;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import shoot.api.ISensor;
import shoot.api.ITarget;
import shoot.api.ITargetSensor;
import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public class HitSensor
	implements ITargetSensor {

	private final Collection<ICommand> listeners = Lists.newArrayList();

	private final String id;
	private final ITarget target;
	private final GpioPinDigitalInput pin;


	public HitSensor(final String id, final ITarget target, final GpioPinDigitalInput pin) {
		this.id = id;
		this.target = target;
		this.pin = pin;

		pin.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				final HitInfo hit = new HitInfo(HitSensor.this, System.currentTimeMillis());
				Commands.uncheckedInvoke(Chains.create(listeners), hit);
			}
		});
	}


	public String id() {
		return id;
	}


	public ITarget target() {
		return target;
	}


	public ISensor addListener(final ICommand command) {
		listeners.add(command);
		return null;
	}


	@Override
	public String toString() {
		return "Sensor[" + id + "]";
	}
}
