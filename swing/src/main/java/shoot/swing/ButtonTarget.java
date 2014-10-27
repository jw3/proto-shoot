package shoot.swing;

import java.util.Collection;
import java.util.UUID;

import javax.swing.JButton;

import cmd4j.ICommand;

import com.google.common.collect.Lists;

import shoot.api.ITarget;

/**
 *
 * @author wassj
 *
 */
public class ButtonTarget
	extends JButton
	implements ITarget {

	private final Collection<ICommand> onHit = Lists.newArrayList();
	private final String id;


	public ButtonTarget() {
		id = UUID.randomUUID().toString().substring(0, 5);
	}


	public String id() {
		return id;
	}


	public boolean isActive() {
		return isEnabled();
	}


	public ITarget activate() {
		setEnabled(true);
		return this;
	}


	public ITarget deactivate() {
		setEnabled(false);
		return this;
	}


	public ITarget onHit(final ICommand command) {
		onHit.add(command);
		return this;
	}
}
