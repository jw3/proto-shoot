package shoot.swing;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;

import shoot.api.IGallery;
import shoot.api.IGame;
import shoot.api.IGame.IEndableGame;
import shoot.api.ITarget;
import shoot.api.events.HitInfo;

/**
 *
 * @author wassj
 *
 */
public class GalleryPanel
	extends JPanel
	implements IGallery {

	private final ImmutableList<ITarget> targets;
	private IGame game;


	public <T extends JComponent & ITarget> GalleryPanel(final Collection<T> targets) {
		this.targets = ImmutableList.<ITarget> copyOf(targets);
		for (final T target : targets) {
			this.add(target);
		}
	}


	public IGame getGame() {
		return game;
	}


	public GalleryPanel setGame(@Nullable final IGame game) {
		this.game = game;
		if (null != this.game) {
			game.initialize(targets);
		}
		return this;
	}


	public List<ITarget> getTargets() {
		return targets;
	}


	@Subscribe
	public void hit(final HitInfo hit) {
		game.hit(hit);

		if (game instanceof IEndableGame && ((IEndableGame)game).over()) {
			System.out.println("done!");
		}
	}
}
