package shoot.games;

import cmd4j.Commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.Targets;
import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public class CountsUp
	implements IEndableGame {

	private ImmutableList<ITarget> targets;
	private int hits;


	public IGame initialize(final ImmutableList<ITarget> targets) {
		Preconditions.checkArgument(!targets.isEmpty());
		this.targets = targets;
		Commands.uncheckedInvoke(Commands.forEach(targets, Targets.deactivate()));
		targets.iterator().next().activate();
		hits = 0;
		return this;
	}


	public boolean over() {
		return !(hits < targets.size());
	}


	public IGame hit(final HitInfo hit) {
		hit.sensor().target().deactivate();
		if (++hits < targets.size()) {
			targets.get(hits).activate();
		}
		return this;
	}


	public IGame shutdown() {
		return this;
	}
}
