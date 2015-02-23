package shoot.pi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shoot.api.IGame;

/**
 *
 * @author wassj
 *
 */
public class Driver {
	private static final Logger logger = LoggerFactory.getLogger(Driver.class);


	public static void main(final String[] args)
		throws Exception {

		final PiGallery gallery = new PiGallery();
		if (args.length == 1) {
			final IGame game = (IGame)Class.forName(args[0]).newInstance();

			logger.info("starting game[" + args[0] + "]");
			gallery.setGame(game);
		}
	}
}
