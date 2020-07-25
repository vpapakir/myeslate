package gr.cti.structfile.version2;

import java.io.*;
import java.util.*;

import gr.cti.structfile.*;
import gr.cti.typeArray.*;

/**
 * This class describes a directory entry in a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructFile
 */
public class Entry2 extends Entry
{
  /**
   * Localized resources.
   */
  ResourceBundle resources;
  /**
   * Child entries.
   */
  private Entry2BaseArray children;
  /**
   * Starts of allocated blocks.
   */
  private IntBaseArray blockStarts;
  /**
   * Ends of allocated blocks.
   */
  private IntBaseArray blockEnds;
  /**
   * Current index into <code>blockStarts</code>/<code>blockEnds</code>
   * arrays. Used when accessing the entry's data.
   */
  private int allocIndex = 0;
  /**
   * Indicates that the position has been set to the end of the last allocated
   * block.
   */
  private boolean needMoreBlocks = false;

  /**
   * Create a directory entry corresponding to a file whose blocks are known to
   * be contiguous, or newly created entries, with only one allocated block.
   * @param     name            The name of the entry.
   *                            It must not be null or empty.
   * @param     location        The location (block number) of the entry in
   *                            the structured file. This must be a
   *                            non-negative number.
   * @param     size            The size of the entry in bytes. This must be a
   *                            non-negative number.
   * @param     parent          The entry associated with the parent directory
   *                            of this entry.
   * @exception IllegalArgumentException        Thrown if name is is null or
   *                            empty, if location or size is negative.
   */
  Entry2(String name, int location, long size, Entry2 parent)
  {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException(resources.getString("badName"));
    }
    if (location < 0) {
      throw new IllegalArgumentException(resources.getString("badLocation"));
    }
    if (size < 0L) {
      throw new IllegalArgumentException(resources.getString("badType"));
    }
    type = FILE;
    init(parent);
    this.name = name;
    this.size = size;
    this.location = location;
    blockStarts.add(location);
    if (size > 0) {
      int nBlocks;
      nBlocks = (int)(size / StructFile2.BLOCKSIZE);
      if ((size % StructFile2.BLOCKSIZE) != 0) {
        nBlocks++;
      }
      int end = location + nBlocks - 1;
      blockEnds.add(end);
    }else{
      blockEnds.add(location);
    }
  }

  /**
   * Create a directory entry corresponding to a directory.
   * @param     name            The name of the entry.
   *                            It must not be null or empty.
   * @param     parent          The entry associated with the parent directory
   *                            of this entry.
   */
  Entry2(String name, Entry2 parent)
  {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException(resources.getString("badName"));
    }
    type = DIRECTORY;
    this.name = name;
    size = 0L;
    init(parent);
  }

  /**
   * Create a directory entry with an empty name. This is used to construct
   * the entry of the root directory, bypassing the check for a non-empty
   * name.
   * @param     name            The name of the entry.
   *                            It must not be null or empty.
   * @param     parent          The entry associated with the parent directory
   *                            of this entry.
   */
  Entry2(Entry2 parent)
  {
    type = DIRECTORY;
    name = "";
    size = 0L;
    init(parent);
  }

  /**
   * Create an uninitialized directory entry corresponding to a file. Use when
   * restoring entries from a file.
   */
  private Entry2()
  {
    type = FILE;
    init(null);
  }

  /**
   * Initialize the entry.
   * @param     parent  The parent of this entry.
   */
  private void init(Entry2 parent)
  {
    if (type == DIRECTORY) {
      children = new Entry2BaseArray();
      blockStarts = null;
      blockEnds = null;
      location = NO_LOCATION;
    }else{
      children = null;
      blockStarts = new IntBaseArray();
      blockEnds = new IntBaseArray();
    }
    if (parent != null) {
      synchronized(parent) {
        parent.insert(this, parent.getChildCount());
      }
    }
  }

  /**
   * Adds an entry to this entry.
   * @param     child   The entry to add.
   * @param     index   The position at which to add the entry.
   * @exception IllegalStateException   Thrown if this entry corresponds to
   *                    a file.
   */
  public void insert(Entry2 child, int index)
    throws IllegalStateException
  {
    if (type != DIRECTORY) {
      throw new IllegalStateException(
        resources.getString("notDir1") + name + resources.getString("notDir2")
      );
    }
    Entry2 oldParent = (Entry2)(child.parent);
    if (oldParent != null) {
      synchronized(oldParent) {
        oldParent.remove(child);
      }
    }
    synchronized(this) {
      children.add(index, child);
      child.parent = this;
    }
  }

  /**
   * Removes the entry at the specified index from this entry's children.
   * @param     index   The index of the entry to remove.
   * @exception IndexOutOfBoundsException       Thrown if <code>index</code>
   *                    is out of bounds.
   */
  public void remove(int index)
  {
    synchronized(this) {
      Entry2 e = children.get(index);
      children.remove(index);
      e.parent = null;
    }
  }

  /**
   * Removes an entry from this entry's children.
   * @param     ent     The entry to remove.
   */
  public void remove(Entry2 ent)
  {
    synchronized(this) {
      int index = children.indexOf(ent);
      if (index >= 0) {
        children.remove(index);
        ent.parent = null;
      }
    }
  }

  /**
   * Resets the user object of the receiver to <code>object</code>.
   * This method does not do anything.
   * @param     object  Ignored.
   */
  public void setUserObject(Object object)
  {
  }

  /**
   * Remove this entry from its parent.
   */
  public void removeFromParent()
  {
    if (parent != null) {
      ((Entry2)parent).remove(this);
    }
  }

  /**
   * Set the parent of this entry to a given entry.
   * @param     newParent       The new parent of the entry.
   */
  public void setParent(Entry2 newParent)
  {
    newParent.insert(this, newParent.getChildCount());
  }

  /**
   * Returns the child entry at a given index.
   * @param     childIndex      The index of the child entry.
   * @return    The child entry at </code>childIndex</code>.
   * @exception IndexOutOfBoundsException       Thrown if
   *                            <code>childIndex</code> is out of bounds.
   */
  public Entry2 getChildAt(int childIndex)
  {
    synchronized(this) {
      return children.get(childIndex);
    }
  }

  /**
   * Returns the number of child entries.
   * @return    The number of child entries.
   */
  public int getChildCount()
  {
    if (children != null) {
      synchronized(this) {
        return children.size();
      }
    }else{
      return 0;
    }
  }

  /**
   * Returns the index of an entry in this entry's children.
   * @param     ent     The entry.
   * @return    The index of <code>entry</code> in this entry's children.
   *            If this entry does not contain <code>entry</code>, -1 is
   *            returned.
   */
  public int getIndex(Entry2 ent)
  {
    synchronized(this) {
      return children.indexOf(ent);
    }
  }

  /**
   * Returns true if this entry allows children.
   * @return    True if the entry's type is <code>DIRECTORY</code>, false
   *            otherwise.
   */
  public boolean getAllowsChildren()
  {
    return (type == DIRECTORY);
  }

  /**
   * Returns true if this entry is a leaf.
   */
  public boolean isLeaf()
  {
    synchronized(this) {
      return (children == null) || (children.size() == 0);
    }
  }

  /**
   * Returns the children of this entry as an Enumeration.
   * @return    The children of this entry as an enumeration.
   */
  public Enumeration children()
  {
    Entry2BaseArray a;
    synchronized(this) {
      a = (Entry2BaseArray)children.clone();
    }
    return new Entry2Enumeration(a);
  }

  /**
   * Save the entry.
   * @param     out     The stream to which to save the entry. After saving
   *                    the entry, the stream is <em>not</em> closed.
   * @exception IOException     Thrown if saving the entry fails.
   */
  void saveEntry(DataOutputStream out) throws IOException
  {
    out.writeUTF(name);
    out.writeInt(type);
    if (type == DIRECTORY) {
      int n = children.size();
      out.writeInt(n);
      for (int i=0; i<n; i++) {
        Entry2 e = children.get(i);
        e.saveEntry(out);
      }
    }else{
      out.writeLong(size);
      int n = blockStarts.size();
      out.writeInt(n);
      for (int i=0; i<n; i++) {
        out.writeInt(blockStarts.get(i));
        out.writeInt(blockEnds.get(i));
      }
    }
  }

  /**
   * Read an entry from an input stream.
   * @param     in      The stream from which the entry will be read.
   *                    After reading the entry, the stream is <em>not</em>
   *                    closed.
   * @return    The entry that was read.
   * @exception IOException     Thrown if reading the entry fails.
   */
  static Entry2 readEntry(DataInputStream in) throws IOException
  {
    String entryName = in.readUTF();
    int entryType = in.readInt();
    Entry2 e;
    if (entryType == DIRECTORY) {
      if (entryName.equals("")) {
        e = new Entry2(null);
      }else{
        e = new Entry2(entryName, null);
      }
      int n = in.readInt();
      for (int i=0; i<n; i++) {
        Entry2 e2 = readEntry(in);
        e.insert(e2, i);
      }
    }else{
      e = new Entry2();
      e.size = in.readLong();
      int n = in.readInt();
      for (int i=0; i<n; i++) {
        e.blockStarts.add(in.readInt());
        e.blockEnds.add(in.readInt());
      }
      e.location = e.blockStarts.get(0);
    }
    e.name = entryName;
    return e;
  }

  /**
   * Adds an allocated block to the entry.
   * @param     The number of the block to add.
   */
  void addBlock(int block)
  {
    int last = blockEnds.size() - 1;
    int start = blockStarts.get(last);
    int end = blockEnds.get(last);
    if (block == (end + 1)) {
      blockEnds.set(last, block);
      if (needMoreBlocks) {
        blockPos = (block - start) * StructFile2.BLOCKSIZE;
      }
    }else{
      blockStarts.add(block);
      blockEnds.add(block);
      if (needMoreBlocks) {
        allocIndex = last + 1;
        blockPos = 0;
      }
    }
    if (needMoreBlocks) {
      needMoreBlocks = false;
      // When more blocks have been requested, the copy of the current file
      // pointer points incorrectly at the start of the first block following
      // the last allocated block. Now that we have a valid block, update the
      // pointer to point to the block's beginning.
      ptr = block * StructFile2.BLOCKSIZE;
    }
  }

  /**
   * Returns the child entry that has a specified name.
   * @param     name    The name of the entry.
   * @return    The requested entry. If no such entry exists, null is
   *            returned.
   */
  Entry2 findEntry(String name)
  {
    synchronized(this) {
      int n = children.size();
      for (int i=0; i<n; i++) {
        Entry2 e = children.get(i);
        if (e.name.equals(name)) {
          return e;
        }
      }
      return null;
    }
  }

  /**
   * Recursively deletes an entry from this entry.
   * @param     child   The entry to delete.
   * @param     sf      The structured file from which this entry will be
   *                    deleted.
   */
  void deleteEntry(Entry2 child, StructFile2 sf)
  {
    synchronized(this) {
      if (child.type == DIRECTORY) {
        // Recursively delete the child entry's children.
        Entry2BaseArray childEntries = child.children;
        int n = childEntries.size();
        for (int i=n-1; i>=0; i--) {
          Entry2 e = childEntries.get(i);
          child.deleteEntry(e, sf);
        }
      }else{
        // Free the blocks that had been allocated to the child entry.
        child.deallocateBlocks(sf);
      }
      // Remove the child entry from this entry.
      remove(child);
    }
  }

  /**
   * Deallocates the blocks allocated to this entry.
   * @param     sf      The structured file to which the entry belongs.
   */
  void deallocateBlocks(StructFile2 sf)
  {
    IntBaseArray starts = blockStarts;
    IntBaseArray ends = blockEnds;
    int n = starts.size();
    for (int i=0; i<n; i++) {
      int s = starts.get(i);
      int e = ends.get(i);
      for (int j=s; j<=e; j++) {
        sf.freeBlock(j);
      }
    }
    blockStarts.clear();
    blockEnds.clear();
    size = 0;
    location = NO_LOCATION;
    allocIndex = 0;
  }

  /**
   * Returns the children of this entry as a vector.
   * @return    A vector containing the entry's children.
   */
  @SuppressWarnings(value={"unchecked"})
  Vector childrenAsVector()
  {
    int n;
    if (type == DIRECTORY) {
      n = children.size();
    }else{
      n = 0;
    }
    Vector v = new Vector(n);
    for (int i=0; i<n; i++) {
      v.addElement(children.get(i));
    }
    return v;
  }

  /**
   * Returns the children of this entry as an array list.
   * @return    An array list containing the entry's children.
   */
  @SuppressWarnings(value={"unchecked"})
  ArrayList childrenAsArrayList()
  {
    Entry2BaseArray ch;
    synchronized(this) {
      ch = (Entry2BaseArray)children.clone();
    }
    int n;
    if (type == DIRECTORY) {
      n = ch.size();
    }else{
      n = 0;
    }
    ArrayList a = new ArrayList(n);
    for (int i=0; i<n; i++) {
      a.add(ch.get(i));
    }
    return a;
  }

  /**
   * Returns the children of this entry as an array.
   * @return    An array containing the entry's children.
   */
  Entry2[] childrenAsArray()
  {
    synchronized(this) {
      return children.toArray();
    }
  }

  /**
   * Update the current position within the entry's data.
   * @param     p       The new location.
   */
  void setCurrentPos(int p)
  {
    if (p > 0) {
      int advance = p - position;
      position = p;
      int start = blockStarts.get(allocIndex);
      int end = blockEnds.get(allocIndex);
      int nBlocks = end - start + 1;
      int available = (nBlocks * StructFile2.BLOCKSIZE) - blockPos;
      if (advance < available) {
        blockPos += advance;
        ptr = start * StructFile2.BLOCKSIZE + blockPos;
      }else{
        int lastIndex = blockStarts.size() - 1;
        do {
          advance -= available;
          if (allocIndex < lastIndex) {
            allocIndex++;
            start = blockStarts.get(allocIndex);
            end = blockEnds.get(allocIndex);
            nBlocks = end - start + 1;
            available = nBlocks * StructFile2.BLOCKSIZE;
            ptr = start * StructFile2.BLOCKSIZE + advance;
          }else{
            end = blockEnds.get(lastIndex);
            // This value of ptr is correct, but it points to an area of the
            // structured file that has not been allocated to this entry.
            // A correct value will be assigned to ptr when addblock() is
            // invoked.
            ptr = (end + 1) * StructFile2.BLOCKSIZE;
            needMoreBlocks = true;
            advance = 0;
            available = 0;
          }
        } while (advance > 0);
        blockPos = advance;
      }
    }else{
      int start = blockStarts.get(0);
      ptr = start * StructFile2.BLOCKSIZE;
      position = 0;
      blockPos = 0;
      allocIndex = 0;
      needMoreBlocks = false;
    }
  }

  /**
   * Returns the current position within the entry's data.
   * @return The current position within the entry's data.
   */
  int getCurrentPos()
  {
    return position;
  }

  /**
   * Returns the number of available contiguous bytes from the current
   * position.
   * @return    The number of available contiguous bytes from the current
   *            position.
   */
  int numberOfContiguousBytes()
  {
    if (needMoreBlocks) {
      return 0;
    }else{
      int nBlocks = blockEnds.get(allocIndex) - blockStarts.get(allocIndex) + 1;
      int available = (nBlocks * StructFile2.BLOCKSIZE) - blockPos;
      return available;
    }
  }

  /**
   * An implementation of <code>Enumeration</code> for the
   * <code>children()</code> method.
   */
  private class Entry2Enumeration implements Enumeration
  {
    /**
     * The entry's children.
     */
    private Entry2[] e;
    /**
     * The number of this entry's children.
     */
    private int n;
    /**
     * The index of the next child to return.
     */
    private int current = 0;

    /**
     * Construct the enumeration.
     * @param   a       The entry's children.
     */
    private Entry2Enumeration(Entry2BaseArray a)
    {
      if (a != null) {
        e = a.toArray();
        n = e.length;
      }else{
        e = null;
        n = 0;
      }
    }

    /**
     * Tests if this enumeration contains more elements.
     * @return  <code>true</code> if and only if this enumeration object
     *          contains at least one more element to provide;
     *          <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
      return (current < n);
    }

    /**
     * Returns the next element of this enumeration if this enumeration object
     * has at least one more element to provide.
     * @return  The next element of this enumeration.
     * @exception       NoSuchElementException  Thrown if no more
     *                                          elements exist.
     */
    public Object nextElement() throws NoSuchElementException
    {
      if ((e != null) && (current < n)) {
        return e[current++];
      }else{
        throw new NoSuchElementException();
      }
    }
  }
}
