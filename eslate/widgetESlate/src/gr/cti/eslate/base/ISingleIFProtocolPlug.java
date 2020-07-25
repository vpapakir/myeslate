package gr.cti.eslate.base;

/**
 * Methods implemented by plugs associated with protocols. This interface
 * unifies protocol plugs and mixed mode plugs. This cannot be done by making
 * mixed mode plugs a subclass of protocol plugs, as ther are already a
 * subclass of shared object plugs.

 * Methods implemented by plugs that accept a single connection without an
 * associated shared object where, in the associated protocol, the right side
 * requires that  the "left" side implements a number of interfaces, while the
 * "left" side does not demand anything from the "right" side. This interface
 * is currently empty, serving as an identifying tag for the plug classes that
 * implement it.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ISingleIFProtocolPlug
{
}
