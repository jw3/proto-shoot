package shoot.api;

/**
 * models a target that is available in the gallery
 * @author wassj
 *
 */
public interface ITarget {

	String id();


	boolean isActive();


	ITarget activate();


	ITarget deactivate();
}
