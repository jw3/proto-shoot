package shoot.games;

import java.util.Map;

import cmd4j.Commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.Targets;
import shoot.api.events.HitInfo;

/**
 * Requires each target to be hit twice to knock it down
 * @author wassj
 *
 */
public class HitTwice
	implements IEndableGame {

	private final Map<ITarget, Integer> hits = Maps.newHashMap();
	private ImmutableList<ITarget> targets;


	public IGame initialize(final ImmutableList<ITarget> targets) {
		Preconditions.checkArgument(!targets.isEmpty());
		this.targets = targets;
		Commands.uncheckedInvoke(Commands.forEach(targets, Targets.activate()));
		for (final ITarget target : targets) {
			hits.put(target, 0);
		}
		return this;
	}


	public boolean over() {
		return Iterables.all(targets, Targets.deactive);
	}


	public IGame hit(final HitInfo hit) {
		final int count = hits.get(hit.sensor().target()) + 1;
		hits.put(hit.sensor().target(), count);
		if (count > 1) {
			hit.sensor().target().deactivate();
		}
		return this;
	}


	public IGame shutdown() {
		return this;
	}
}
