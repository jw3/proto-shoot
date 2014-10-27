package shoot.games;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cmd4j.Chains;
import cmd4j.Chains.IChainBuilder;
import cmd4j.Commands;
import cmd4j.ICommand;
import cmd4j.ICommand.IStateCommand.IStateCommand1;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.Targets;
import shoot.api.events.HitInfo;

/**
 * Targets activate after a delay after being hit.  Goal is to have them all knocked down at once.
 * @author wassj
 *
 */
public class KeepThemDown
	implements IEndableGame {

	private ExecutorService executor;
	private ImmutableList<ITarget> targets;


	public IGame initialize(final ImmutableList<ITarget> targets) {
		this.executor = Executors.newCachedThreadPool();
		this.targets = targets;
		Commands.uncheckedInvoke(Commands.forEach(targets, Targets.activate()));
		return this;
	}


	public boolean over() {
		return Iterables.all(targets, Targets.deactive);
	}


	public IGame hit(final HitInfo hit) {
		hit.sensor().target().deactivate();
		final IChainBuilder builder = Chains.builder() //
			.add(Commands.invokeIf(new Timer(hit.sensor().target()), isNotOver()))
			.input(this);

		Commands.submit(builder.build(), executor);
		return this;
	}


	public IGame shutdown() {
		executor.shutdownNow();
		return this;
	}


	private Predicate<IEndableGame> isNotOver() {
		return new Predicate<IEndableGame>() {
			public boolean apply(IEndableGame input) {
				return !input.over();
			}
		};
	}


	private class Timer
		implements IStateCommand1 {

		private final ITarget target;


		public Timer(final ITarget target) {
			this.target = target;
		}


		public ICommand invoke() {
			if (!over()) {
				return Chains.builder() //
					.add(Commands.waitFor(3000))
					.add(Commands.invokeIf(Targets.activate(target), isNotOver()))
					.input(KeepThemDown.this)
					.build();
			}
			return null;
		}
	}
}
