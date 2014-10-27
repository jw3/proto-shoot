package shoot.api;

import cmd4j.ICommand;

/**
 *
 * @author wassj
 *
 */
public interface ISensor {

	String id();


	/**
	 * add a {@link ICommand} to be executed when sensor is triggered
	 * @param command
	 * @return
	 */
	ISensor addListener(ICommand command);
}
