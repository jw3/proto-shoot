package shoot.games;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cmd4j.Chains;
import cmd4j.Commands;
import cmd4j.ICommand;
import cmd4j.ICommand.IStateCommand.IStateCommand1;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.Targets;
import shoot.api.events.HitInfo;

/**
 * Targets are only active for a second
 * @author wassj
 *
 */
public class ShootQuick
	implements IEndableGame {

	private final Map<ITarget, Boolean> hits = Maps.newHashMap();
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private ImmutableList<ITarget> targets;


	public IGame initialize(final ImmutableList<ITarget> targets) {
		this.targets = targets;
		Commands.uncheckedInvoke(Commands.forEach(targets, Targets.activate()));

		int idx = 0;
		for (final ITarget target : targets) {
			hits.put(target, Boolean.FALSE);
			Commands.submit(Chains.create(new Timer(target, 3000 + ++idx * 700)), executor);
		}
		return this;
	}


	public boolean over() {
		return Iterables.all(targets, Targets.deactive);
	}


	public IGame hit(final HitInfo hit) {
		hits.put(hit.sensor().target(), Boolean.TRUE);
		hit.sensor().target().deactivate();
		return this;
	}


	public IGame shutdown() {
		executor.shutdownNow();
		return this;
	}


	private class Timer
		implements IStateCommand1 {

		private final ITarget target;
		private final long delay;


		public Timer(final ITarget target, final long delay) {
			this.target = target;
			this.delay = delay;
		}


		public ICommand invoke() {
			if (!hits.get(target) && delay > 100) {
				return Chains.builder()//
					.add(Commands.waitFor(delay))
					.add(target.isActive() ? Targets.deactivate() : Targets.activate())
					.input(target)
					.add(new Timer(target, (long)(delay * .75)))
					.build();
			}
			target.deactivate();
			return null;
		}
	}
}
