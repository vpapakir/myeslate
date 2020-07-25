package gr.cti.eslate.base;

/**
 * This interface identifies a component as E-Slate aware. By implementing
 * the interface's getESlateHandle() method, components can provide a
 * reference to their E-Slate handle to the E-Slate container, enabling the
 * latter to treat them as E-Slate components instead of generic components.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ESlatePart
{
  /**
   * Return a reference to the component's E-Slate handle.
   */
  public ESlateHandle getESlateHandle();
}
