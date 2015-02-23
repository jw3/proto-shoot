package shoot.games;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public class NashornGame
	implements IEndableGame {

	private final Invocable invocable;
	private ImmutableList<ITarget> targets;


	public NashornGame(final String game)
		throws Exception {

		final ScriptEngineManager factory = new ScriptEngineManager();
		final ScriptEngine engine = factory.getEngineByName("nashorn");

		engine.eval(new FileReader(Resources.getResource(game + "/game.js").getFile()));
		this.invocable = (Invocable)engine;

	}


	public IGame initialize(final ImmutableList<ITarget> targets) {
		this.targets = targets;

		try {
			invocable.invokeFunction("setup", targets);
			return this;
		}
		catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}


	public IGame shutdown() {
		return this;
	}


	public IGame hit(final HitInfo hit) {
		try {
			invocable.invokeFunction("onHit", hit.sensor().target());
			return this;
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}


	public boolean over() {
		try {
			return (boolean)invocable.invokeFunction("isOver", targets);
		}
		catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
