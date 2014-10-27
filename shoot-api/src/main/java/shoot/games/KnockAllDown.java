package shoot.games;

import cmd4j.Commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

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
public class KnockAllDown
	implements IEndableGame {

	private ImmutableList<ITarget> targets;


	public IGame initialize(final ImmutableList<ITarget> targets) {
		this.targets = targets;
		Commands.uncheckedInvoke(Commands.forEach(targets, Targets.activate()));
		return this;
	}


	public boolean over() {
		return Iterables.all(targets, Targets.deactive);
	}


	public IGame hit(final HitInfo hit) {
		hit.sensor().target().deactivate();
		return this;
	}


	public IGame shutdown() {
		return this;
	}
}
