package shoot.api.events;

import shoot.api.ITargetSensor;

/**
 *
 * @author wassj
 *
 */
public class HitInfo {
	private final ITargetSensor sensor;
	private final long when;


	public HitInfo(final ITargetSensor sensor, final long when) {
		this.sensor = sensor;
		this.when = when;
	}


	public ITargetSensor sensor() {
		return sensor;
	}


	public long getWhen() {
		return when;
	}


	@Override
	public String toString() {
		return "Hit[ " + sensor.id() + "] " + when;
	}
}
