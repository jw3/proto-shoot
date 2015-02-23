package shoot.pi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import shoot.api.IGallery;
import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ISensor;
import shoot.api.IStatusIndicator;
import shoot.api.ITarget;
import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public class PiGallery
	implements IGallery {

	private static final Logger logger = LoggerFactory.getLogger(IGallery.class);
	private final List<ITarget> targets = Lists.newArrayList();
	private IGame game;


	public PiGallery() {
		final EventBus eventbus = new EventBus();
		eventbus.register(this);

		for (int i = 0; i < 4; ++i) {
			logger.info("setup target {}", i);
			final ITarget target = PiShoot.newTarget(String.valueOf(i), targetPins.get(i));
			final ISensor sensor = PiShoot.newSensor(String.valueOf(i), sensorPins.get(i), target);
			targets.add(target);

			((HitSensor)sensor).setEventbus(eventbus);
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
		return game;
	}


	public IGallery setGame(final IGame game) {
		this.game = game;
		if (null != this.game) {
			game.initialize(ImmutableList.copyOf(targets));
		}
		return this;
	}


	@Subscribe
	public void hit(final HitInfo hit) {
		game.hit(hit);

		if (game instanceof IEndableGame && ((IEndableGame)game).over()) {
			logger.info("{} done!", getClass().getSimpleName());
		}
	}

	/*
	 * temp configuration
	 */
	private static final List<Pin> targetPins = Lists.newArrayList(RaspiPin.GPIO_12, RaspiPin.GPIO_14, RaspiPin.GPIO_00, RaspiPin.GPIO_02);
	private static final List<Pin> sensorPins = Lists.newArrayList(RaspiPin.GPIO_01, RaspiPin.GPIO_04, RaspiPin.GPIO_05, RaspiPin.GPIO_06);
	private static final List<Pin> statusPins = Lists.newArrayList(RaspiPin.GPIO_10);
}
