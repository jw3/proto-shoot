package shoot.api;

import cmd4j.ICommand;
import cmd4j.ICommand.ICommand1;
import cmd4j.ICommand.ICommand2;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 *
 * @author wassj
 *
 */
public class Targets {

	public static ICommand activate() {
		return new ICommand2<ITarget>() {
			public void invoke(final ITarget target) {
				target.activate();
			}
		};
	}


	public static ICommand activate(final ITarget target) {
		return new ICommand1() {
			public void invoke() {
				target.activate();
			}
		};
	}


	public static ICommand deactivate() {
		return new ICommand2<ITarget>() {
			public void invoke(final ITarget target) {
				target.deactivate();
			}
		};
	}

	public static final Predicate<ITarget> active = new Predicate<ITarget>() {
		public boolean apply(final ITarget input) {
			return input.isActive();
		}
	};

	public static final Predicate<ITarget> deactive = Predicates.not(active);
}
