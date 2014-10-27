package shoot.api;

import com.google.common.collect.ImmutableList;

import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public interface IGame {
	IGame initialize(ImmutableList<ITarget> targets);


	// startup

	IGame shutdown();


	IGame hit(HitInfo hit);


	public interface IEndableGame
		extends IGame {

		boolean over();
	}
}
