package shoot.pi;

import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import shoot.api.IGallery;
import shoot.api.IGame;
import shoot.api.ISensor;
import shoot.api.IStatusIndicator;
import shoot.api.ITarget;
import shoot.games.KnockAllDown;

/**
 *
 * @author wassj
 *
 */
public class PiGallery
	implements IGallery {

	private final List<ITarget> targets = Lists.newArrayList();


	public PiGallery() {
		for (int i = 0; i < 4; ++i) {
			final ITarget target = PiShoot.newTarget(String.valueOf(i), targetPins.get(i));
			final ISensor sensor = PiShoot.newSensor(String.valueOf(i), sensorPins.get(i), target);

			targets.add(target);
		}

		final IStatusIndicator status = PiShoot.newIndicator("status", Iterables.getOnlyElement(statusPins));
		try {
			status.alert().get();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}


	public IGame getGame() {
		return new KnockAllDown();
	}


	public IGallery setGame(final IGame game) {
		return null;
	}

	/*
	 * temp configuration
	 */
	private static final List<Pin> targetPins = Lists.newArrayList(RaspiPin.GPIO_12, RaspiPin.GPIO_14, RaspiPin.GPIO_00, RaspiPin.GPIO_02);
	private static final List<Pin> sensorPins = Lists.newArrayList(RaspiPin.GPIO_01, RaspiPin.GPIO_04, RaspiPin.GPIO_05, RaspiPin.GPIO_06);
	private static final List<Pin> statusPins = Lists.newArrayList(RaspiPin.GPIO_10);
}
