package gr.cti.eslate.base;

/**
 * An <code>ActivationHandleGroup</code> that remembers previously activated
 * components, so that when removing the active component, the previously
 * active componnet can be activated.
 *
 * @version     2.0.21, 28-Sep-2007
 * @author      Kriton Kyrimis
 */
public class HistoryEnabledActivationHandleGroup extends ActivationHandleGroup
{
  /**
   * The previously activated handles.
   */
  private ESlateHandle[] history;
  /**
   * The maximum size of the history buffer.
   */
  private int maxSize;
  /**
   * The current size of the history buffer.
   */
  private int size;
  /**
   * The index of the last element in the history buffer.
   */
  private int last;

  /**
   * Construct an HistoryEnabledActivationHandleGroup instance.
   * @param     handle  The handle to use as the starting point for adding
   *                    E-Slate handles to the collection. The handles that
   *                    will be added to the collection will be either direct
   *                    children or arbitrary descendants of
   *                    <code>handle</code>, depending on the value of
   *                    <code>policy</code>. Both the E-Slate handle hierarchy
   *                    and the hosted component hierarchy are considered when
   *                    building the collection. Subsequent modifications in
   *                    either hierarchy will make corresponding changes to
   *                    the collection, as well.
   * @param     classes An array of classes, specifying that only E-Slate
   *                    handles corresponding to objects of one of these
   *                    classes should be added to the list. If
   *                    <code>classes</code> is <code>null</code>, no
   *                    exclusion of handles based on the component class will
   *                    be made.
   * @param     exclude An array of classes, specifying that E-Slate handles
   *                    corresponding to objects of one of these classes
   *                    should <em>not</em> be added to the list. This
   *                    parameter can be <code>null</code>.
   * @param     policy  Specifies which handles qualify for addition into the
   *                    collection. This can be one of:
   *                    <UL>
   *                    <LI>
   *                    <code>DIRECT_CHILDREN</code>: only direct children of
   *                    <code>handle</code> and handles hosted by <code>handle
   *                    will be considered for addition to the collection.
   *                    </LI>
   *                    <LI>
   *                    <code>ALL_CHILDREN</code>: all descendants of
   *                    <code>handle</code> will be considered for addition to
   *                    the collection.
   *                    </LI>
   *                    </UL>
   * @param     size    The number of previous activation instances to
   *                    remember.
   * @exception IllegalArgumentException        Thrown if <code>policy</code>
   *                    is not one of <code>DIRECT_CHILDREN</code> or
   *                    <code>ALL_CHILDREN</code>.
   */
  @SuppressWarnings("unchecked")
  public HistoryEnabledActivationHandleGroup(
    ESlateHandle handle, Class[] classes, Class[] exclude, int policy, int size)
    throws IllegalArgumentException
  {
    super(handle, classes, exclude, policy);
    initBuffer(size);
  }

  /**
   * Construct an HistoryEnabled ActivationHandleGroup instance.
   * @param     handle  The handle to use as the starting point for adding
   *                    E-Slate handles to the collection. The handles that
   *                    will be added to the collection will be either direct
   *                    children or arbitrary descendants of
   *                    <code>handle</code>, depending on the value of
   *                    <code>policy</code>. Both the E-Slate handle hierarchy
   *                    and the hosted component hierarchy are considered when
   *                    building the collection. Subsequent modifications in
   *                    either hierarchy will make corresponding changes to
   *                    the collection, as well.
   * @param     classes An array of classes, specifying that only E-Slate
   *                    handles corresponding to objects of one of these
   *                    classes should be added to the list. If
   *                    <code>classes</code> is <code>null</code>, no
   *                    exclusion of handles based on the component class will
   *                    be made.
   * @param     policy  Specifies which handles qualify for addition into the
   *                    collection. This can be one of:
   *                    <UL>
   *                    <LI>
   *                    <code>DIRECT_CHILDREN</code>: only direct children of
   *                    <code>handle</code> and handles hosted by <code>handle
   *                    will be considered for addition to the collection.
   *                    </LI>
   *                    <LI>
   *                    <code>ALL_CHILDREN</code>: all descendants of
   *                    <code>handle</code> will be considered for addition to
   *                    the collection.
   *                    </LI>
   *                    </UL>
   * @param     size    The number of previous activation instances to
   *                    remember.
   * @exception IllegalArgumentException        Thrown if <code>policy</code>
   *                    is not one of <code>DIRECT_CHILDREN</code> or
   *                    <code>ALL_CHILDREN</code>.
   */
  @SuppressWarnings("unchecked")
  public HistoryEnabledActivationHandleGroup(
    ESlateHandle handle, Class[] classes, int policy, int size)
    throws IllegalArgumentException
  {
    this(handle, classes, null, policy, size);
  }

  /**
   * Specifies the active handle within the group.
   * @param     h       The new active handle. If <code>h</code> does not
   *                    belong to this group, nothing happens.
   */
  public void setActiveHandle(ESlateHandle h)
  {
    super.setActiveHandle(h);
    if ((size <= 0) || !history[last].equals(h)) {
      appendToBuffer(h);
    }
  }

  /**
   * Remove an E-Slate handle from the collection.
   * @param     h       The E-Slate handle to remove.
   */
  protected void removeHandle(ESlateHandle h)
  {
    super.removeHandle(h);
    removeFromBuffer(h);
  }

  /**
   * Activates one of the handles in the group. The handle which will be
   * activated is the previously active handle, obtained from a circular
   * buffer.
   */
  protected void activateSomeHandle()
  {
    ESlateHandle h = lastInBuffer();
    if (h != null) {
      setActiveHandle(h);
    }else{
      super.activateSomeHandle();
    }
  }

  /**
   * Initialize the circular buffer.
   * @param     size    The size of the buffer.
   */
  private void initBuffer(int size)
  {
    history = new ESlateHandle[size];
    maxSize = size;
    last = -1;
    size = 0;
  }

  /**
   * Add an E-Slate handle at the end of the buffer.
   * @param     h       The handle to add.
   */
  private void appendToBuffer(ESlateHandle h)
  {
    last++;
    if (last == maxSize) {
      last = 0;
    }
    history[last] = h;
    if (size < maxSize) {
      size++;
    }
  }

  /**
   * Remove an E-Slate handle from the buffer.
   * @param     h       The handle to remove.
   */
  private void removeFromBuffer(ESlateHandle h)
  {
    if ((h != null) && (size > 0)) {
      for (int i=0; i<size; i++) {
        if (h.equals(history[indexOf(i)])) {
          for (int j=i+1; j<size; j++) {
            history[indexOf(j-1)] = history[indexOf(j)];
          }
          history[indexOf(size-1)] = null;
          if (size > 1) {
            last = indexOf(size-2);
          }else{
            last = -1;
          }
          size--;
          break;
        }
      }
    }
  }

  /**
   * Returns the last E-Slate handle in the buffer.
   * @return    The last E-Slate handle in the buffer. If the buffer is empty,
   *            <code>null</code> is returned.
   */
  private ESlateHandle lastInBuffer()
  {
    if (size > 0) {
      return history[last];
    }else{
      return null;
    }
  }

  /**
   * Returns the index in the underlying array corresponding to the
   * <code>n</code>-th element in the buffer.
   * @param     n       The index in the circular buffer.
   */
  private int indexOf(int n)
  {
    int first = last + 1 - size ;
    if (first < 0) {
      first += maxSize;
    }
    int ind = first + n;
    if (ind >= maxSize) {
      ind -= maxSize;
    }
    return ind;
  }

}
