package shoot.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmd4j.Chains;
import cmd4j.Commands;
import cmd4j.ICommand;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import shoot.api.IGame;
import shoot.api.ISensor;
import shoot.api.ITarget;
import shoot.api.ITargetSensor;
import shoot.api.events.HitInfo;
import shoot.games.CountsUp;
import shoot.games.HitTwice;
import shoot.games.KeepThemDown;
import shoot.games.KnockAllDown;
import shoot.games.ShootQuick;

/**
 *
 * @author wassj
 *
 */
public class ShootFrame
	extends JFrame {

	public ShootFrame(final EventBus eventbus) {
		setSize(1024, 768);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Collection<ButtonTarget> targets = Lists.newArrayList();
		for (int i = 0; i < 5; ++i) {
			final ButtonTarget target = new ButtonTarget();
			target.addActionListener(new Sensor(String.valueOf(i), target, eventbus));
			target.setText(String.valueOf(i));
			targets.add(target);
		}

		final GalleryPanel gallery = new GalleryPanel(targets);
		eventbus.register(gallery);

		add(gallery, BorderLayout.CENTER);

		final JList<IGame> list = new JList<>(gameList());
		list.setCellRenderer(new GameCellRenderer());
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (gallery.getGame() != null) {
						gallery.getGame().shutdown();
					}
					gallery.setGame(list.getSelectedValue());
				}
			}
		});
		add(list, BorderLayout.WEST);

		setVisible(true);
	}


	public static ListModel<IGame> gameList() {
		final DefaultListModel<IGame> model = new DefaultListModel<>();
		model.addElement(new CountsUp());
		model.addElement(new HitTwice());
		model.addElement(new KeepThemDown());
		model.addElement(new KnockAllDown());
		model.addElement(new ShootQuick());
		return model;
	}


	private static class GameCellRenderer
		extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setText(value.getClass().getSimpleName());
			return this;
		}
	}


	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(GalleryPanel.class);
		final EventBus eventbus = new EventBus(new SubscriberExceptionHandler() {
			public void handleException(final Throwable exception, final SubscriberExceptionContext context) {
				exception.printStackTrace();
			}
		});
		new ShootFrame(eventbus);
	}


	/**
	 * @author wassj
	 */
	static class Sensor
		implements ITargetSensor, ActionListener {

		private final Collection<ICommand> listeners = Lists.newArrayList();
		private final String id;
		private final ITarget target;
		private final EventBus eventbus;


		public Sensor(final String id, final ITarget target, final EventBus eventbus) {
			this.id = id;
			this.target = target;
			this.eventbus = eventbus;
		}


		public void actionPerformed(final ActionEvent e) {
			final HitInfo hit = new HitInfo(this, System.currentTimeMillis());
			Commands.uncheckedInvoke(Chains.create(listeners), hit);
			eventbus.post(hit);
		}


		public String id() {
			return id;
		}


		public ITarget target() {
			return target;
		}


		public ISensor addListener(final ICommand command) {
			listeners.add(command);
			return this;
		}
	}
}
