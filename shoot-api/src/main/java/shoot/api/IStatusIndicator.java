package shoot.api;

import java.util.concurrent.Future;

/**
 *
 * @author wassj
 *
 */
public interface IStatusIndicator {

	String id();


	Future<Void> alert();
}
