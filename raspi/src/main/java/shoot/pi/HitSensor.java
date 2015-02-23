package shoot.pi;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmd4j.Chains;
import cmd4j.Commands;
import cmd4j.ICommand;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
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

	private static final Logger logger = LoggerFactory.getLogger(HitSensor.class);
	private final Collection<ICommand> listeners = Lists.newArrayList();

	private final String id;
	private final ITarget target;
	private final GpioPinDigitalInput pin;

	private long lastHit = 0;
	private EventBus eventbus;


	public HitSensor(final String id, final ITarget target, final GpioPinDigitalInput pin/*, final EventBus eventbus*/) {
		this.id = id;
		this.target = target;
		this.pin = pin;
		//		this.eventbus = eventbus;

		pin.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
				final long current = System.currentTimeMillis();
				if (current - lastHit > 250) {
					logger.trace("hit!");
					final HitInfo hit = new HitInfo(HitSensor.this, System.currentTimeMillis());
					Commands.uncheckedInvoke(Chains.create(listeners), hit);
					eventbus.post(hit);

					lastHit = current;
				}
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


	public void setEventbus(EventBus eventbus) {
		this.eventbus = eventbus;
	}


	@Override
	public String toString() {
		return "Sensor[" + id + "]";
	}
}
