package gr.cti.eslate.base;

import java.util.*;

/**
 * This class encapsulates information about a connection between two
 * components.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class ConnectedPair
{
  /**
   * The E-Slate handle of the provider component.
   */
  ESlateHandle provider;

  /**
   * The E-Slate handle of the dependent component.
   */
  ESlateHandle dependent;

  /**
   * The plug through which the provider component is connected.
   */
  Plug providerPlug;

  /**
   * The plug through which the dependent component is connected.
   */
  Plug dependentPlug;

  /**
   * Create a ConnectedPair instance.
   * @param     prov    The E-Slate handle of the provider component.
   * @param     dep     The E-Slate handle of the dependent component.
   * @param     provPlug        The plug through which the provider component is
   *                    connected.
   * @param     depPlug The plug through which the dependent component is
   *                    connected.
   *
   */
  ConnectedPair(ESlateHandle prov, ESlateHandle dep,
                Plug provPlug, Plug depPlug)
  {
    super();
    provider = prov;
    dependent = dep;
    providerPlug = provPlug;
    dependentPlug = depPlug;
  }

  /**
   * Check if this object is equal to another object.
   * @param     anObject        The object with which to compare this
   *                            instance.
   * @return    True, if the <code>anObject</code> is equal to this object,
   *            false otherwise.
   */
  public boolean equals(Object anObject)
  {
    if (anObject == this) {
      return true;
    }
    if (!(anObject instanceof ConnectedPair)) {
      return false;
    }
    ConnectedPair pair = (ConnectedPair)anObject;
    boolean result1 = provider == pair.provider &&
                      dependent == pair.dependent &&
                      providerPlug == pair.providerPlug &&
                      dependentPlug == pair.dependentPlug;
    boolean result2;
    // For pure protocol plugs, the terms "provider" and "dependent"
    // actually mean "first" and "second", so do the equality check in
    // reverse, as well. If one of the two tests succeeds, the pairs are
    // equal.
    if (pair.providerPlug.isOnlyProtocolPlug() &&
        pair.dependentPlug.isOnlyProtocolPlug()) {
      result2 = provider == pair.dependent &&
                dependent == pair.provider &&
                providerPlug == pair.dependentPlug &&
                dependentPlug == pair.providerPlug;
    } else {
      result2 = false;
    }
    return result1 || result2;
  }

  /**
   * Finds the component that is the nearest common ancestor of the components
   * owning the two connected plugs.
   * @return    The E-Slate handle of the requested component. If the plugs
   *            have no common ancestor, this method returns
   *            <code>null</code>.
   */
  ESlateHandle findCommonAncestor()
  {
    if ((provider == null) || (dependent == null)) {
      return null;
    }
    ArrayList<ESlateHandle> a = new ArrayList<ESlateHandle>();
    for (ESlateHandle h=provider; h!= null; h=h.getParentHandle()) {
      a.add(h);
    }
    ArrayList<ESlateHandle> b = new ArrayList<ESlateHandle>();
    for (ESlateHandle h=dependent; h!= null; h=h.getParentHandle()) {
      b.add(h);
    }
    int n1 = a.size();
    int n2 = b.size();
    for (int i=0; i<n1; i++) {
      ESlateHandle hh1 = a.get(i);
      for (int j=0; j<n2; j++) {
        ESlateHandle hh2 = b.get(j);
        if (hh1.equals(hh2)) {
          return hh1;
        }
      }
    }
    return null;
  }

  /**
   * Returns a hash code value for the object.
   * @return    A hash code value for this object.
   */
  public int hashCode()
  {
    int result = 17;
    result = 37 * result + ((provider == null) ? 0 : provider.hashCode());
    result = 37 * result + ((dependent == null) ? 0 : dependent.hashCode());
    result = 37 * result +
      ((providerPlug == null) ? 0 : providerPlug.hashCode());
    result = 37 * result +
      ((dependentPlug == null) ? 0 : dependentPlug.hashCode());
    return result;
  }

}
