package gr.cti.structfile;

import java.util.*;

/**
 * This class describes a directory entry in a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructFile
 */
public abstract class Entry
{
  /**
   * Indicates that the entry describes a subfile in the structured file.
   */
  final public static int FILE = 0;
  /**
   * Indicates that the entry describes a directory in the structured file.
   */
  final public static int DIRECTORY = 1;

  /**
   * Value of "location" for entries that do not refer to objects on disk.
   */
  final public static int NO_LOCATION = -1;

  /**
   * The name of the entry.
   */
  public String name;
  /**
   * The size of the entry in bytes.
   */
  public long size;
  /**
   * The type of the entry (FILE or DIRECTORY).
   */
  public int type;
  /**
   * The location of the entry in the structured file. This is a block number.
   * The first block in the structured file is block 0.
   */
  public int location;
  /**
   * The entry associated with the parent directory of the entry. This is
   * <code>null</code> for the root directory.
   */
  public transient Entry parent;
  /**
   * Current position in the current block when accessing the entry's data.
   */
  public transient int blockPos;
  /**
   * Current position in the entry when accessing data in the file.
   */
  public transient int position;
  /**
   * A copy of the current file pointer. Used for switching back and forth
   * between entries when accessing multiple entries simultaneously.
   */
  public transient long ptr = 0L;

  /**
   * Localized resources.
   */
  protected static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.structfile.EntryResources", Locale.getDefault()
  );

  /**
   * Compares the entry with another. Two entries are considered equal if
   * they have the same type and location.
   * @param     obj     The entry to which to compare this entry.
   * @return    True if the entries are equal, false otherwise.
   */
  public boolean equals(Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Entry)) {
      return false;
    }
    Entry e = (Entry)obj;
    if (sameEntry(e)) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Checks whether an entry is the same as this entry.
   * @param     e       The entry to check.
   */
  private boolean sameEntry(Entry e)
  {
    // Entries must be of the same type.
    if (type != e.type) {
      return false;
    }
    if (location != NO_LOCATION) {
      // If entries point to data on disk, they must point to the same
      // location.
      return (location == e.location);
    }else{
      // Otherwise, they must refer to the same path from the root.
      if (!name.equals(e.name)) {
        // Check that the name of each path component matches.
        return false;
      }else{
        // If entry names match, check the names of their parent.
        Entry eParent = e.parent;
        if ((parent == null || eParent == null)) {
          return parent == eParent;
        }else{
          return parent.sameEntry(eParent);
        }
      }
    }
  }

  /**
   * Returns a hash code for this entry.
   * @return    A hash code for this entry.
   */
  public int hashCode()
  {
    int result = 17;
    result = 37 * result + type;
    result = 37 * result + location;
    return result;
  }

  /**
   * Returns the parent entry of this entry.
   * @return    The parent entry of this entry. If this entry corresponds to
   *            the root directory, then <code>null</code> is returned.
   */
  public Entry getParent()
  {
    return parent;
  }

  /**
   * Returns a printable representation of the entry.
   * @return    The requested representation.
   */
  public String toString()
  {
    String t;
    String n = name;
    if (n.equals("")) {
      n = resources.getString("root");
    }
    if (type == FILE) {
      t = resources.getString("file");
    }else{
      t = resources.getString("directory");
    }
    return
      "[" + resources.getString("entry") + " " + n +
      ", " + resources.getString("size") + " = " + size +
      ", " + resources.getString("type") + " = " + t +
      ", " + resources.getString("location") + " = " + location +
      ", " + resources.getString("parent") + " = " +
      ((parent == null) ?
        "null" :
        (parent.name.equals("") ?
          resources.getString("root") :
          parent.name
        )
      ) + "]";
  }

  /**
   * Returns the name of the entry.
   * @return    The requested name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the size of the entry in bytes.
   * @return    The requested size.
   */
  public long getSize()
  {
    return size;
  }

  /**
   * Returns the type of the entry (FILE or DIRECTORY).
   * @return    The requested type.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Returns the location (block number) of the entry.
   * @return    The requested location. If the entry does not refer to an
   *            object on disk, <code>NO_LOCATION</code> is returned.
   */
  public int getLocation()
  {
    return location;
  }
}
