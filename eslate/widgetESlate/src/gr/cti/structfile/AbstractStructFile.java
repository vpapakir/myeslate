package gr.cti.structfile;

import java.io.*;
import java.util.*;

/**
 * This class describes the functionality that structured file implementations
 * must implement.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * 
 */
public abstract class AbstractStructFile implements Access
{
  /**
   * Indicates that the structured file already exists.
   */
  public final static int OLD = 0;
  /**
   * Indicates that this is a new file, and that an existing file with the
   * same name should be deleted.
   */
  public final static int NEW = 1;

  /**
   * Size of an integer in bytes.
   */
  protected final static int INTSIZE = 4;
  /**
   * Number of bits per byte.
   */
  protected final static int BITSPERBYTE = 8;
  /**
   * Block number of the comment block.
   */
  protected final static int COMMENTBLOCK = 0;

  /**
   * Indicates that a path is relative to the current directory.
   */
  public final static boolean RELATIVE_PATH = true;
  /**
   * Indicates that a path is relative to the root directory.
   */
  public final static boolean ABSOLUTE_PATH = false;

  /**
   * No cache is associated with the file.
   */
  public final static int NO_CACHE = 0;

  /**
   * An LRU cache is associated with the file. Faster than no cache.
   */
  public final static int LRU_CACHE = 1;

  /**
   * The entire file is cached in memory. Faster than using an LRU cache,
   * but may require a prohibitively large amount of memory if the file is
   * large.
   */
  public final static int FULL_CACHE = 2;

  /**
   * The cache type used.
   */
  protected static int cacheType = LRU_CACHE;

  /**
   * Localized resources.
   */
  protected static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.structfile.StructFileResources", Locale.getDefault()
    );

  /**
   * Opens a structured file. This method allows the StructFile object to be
   * reused, instead of having to create a new one.
   * @param     file    The name of the disk file on which the structure will
   *                    be imposed.
   * @param     mode    If OLD, then an existing file is opened. If NEW, a new
   *                    file is created, overwriting any file with the same
   *                    name.
   * @exception IOException     Thrown if opening the file failed.
   * @exception IllegalArgumentException        Thrown if mode is neither OLD
   *                                            nor NEW.
   */
  public abstract void open(String file, int mode)
    throws IOException, IllegalArgumentException;

  /**
   * Closes the structured file. After invoking this method, the StructFile
   * object cannot be used unless the <code>open</code> method is invoked, to
   * associate the object with a new file.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public abstract void close() throws IOException;

  /**
   * Returns the file on which the structured file has been opened.
   * @return    The file on which the structured file has been opened. If the
   *            structured file has been closed, this method returns
   *            <code>null</code>.
   */
  public abstract File getFile();

  /**
   * Flush the cache of the underlying file. If the underlying file does not
   * have an associated cache, this method does nothing.
   */
  public abstract void flushCache();

  /**
   * Checks whether the structured file is open.
   * @return    True if the structured file is open, false if it is closed.
   */
  public abstract boolean isOpen();

  /**
   * Opens a sub-file for input in the current directory.
   * @param     name    The name of the file.
   * @return    An input stream associated with the sub-file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public abstract InputStream openInputFile(String name) throws IOException;

  /**
   * Opens a sub-file for input in the current directory.
   * @param     path    A vector containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @return    An input stream associated with the sub-file.
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public InputStream openInputFile(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return newOpenInputFile(path, relative);
  }

  /**
   * Opens a sub-file for input in the current directory.
   * @param     path    A list containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @return    An input stream associated with the sub-file.
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  @SuppressWarnings("rawtypes")
public abstract InputStream newOpenInputFile(
    AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Opens a sub-file for output in the current directory.
   * @param     name    The name of the file.
   * @return    An output stream associated with the sub-file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public abstract OutputStream openOutputFile(String name) throws IOException;

  /**
   * Opens a sub-file for output in the current directory.
   * @param     path    A vector containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @return    An output stream associated with the sub-file.
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public OutputStream openOutputFile(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return newOpenOutputFile(path, relative);
  }

  /**
   * Opens a sub-file for output in the current directory.
   * @param     path    A list containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @return    An output stream associated with the sub-file.
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  @SuppressWarnings("rawtypes")
public abstract OutputStream newOpenOutputFile(
    AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Returns the directory entry contained in the current directory that has a
   * specified name.
   * @param     name    The name of the entry.
   * @return    The requested directory entry.
   * @exception IOException     Thrown if the requested entry cannot be
   *                            found.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                            name is null or empty.
   */
  public abstract Entry findEntry(String name)
    throws IOException, IllegalArgumentException;

  /**
   * Returns a directory entry by specifying a sequence of entry names, either
   * relative to the current directory or relative to the root directory.
   * This method doesn not change the current directory.
   * @param     path    A vector containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public Entry findEntry(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return newFindEntry(path, relative);
  }

  /**
   * Returns a directory entry by specifying a sequence of entry names, either
   * relative to the current directory or relative to the root directory.
   * This method doesn not change the current directory.
   * @param     path    A list containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if switching to any of the
   *                    directories in the path fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  @SuppressWarnings("rawtypes")
public abstract Entry newFindEntry(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Creates a new subfile in the current directory.  If the current directory
   * contains a subfile or directory with the same name, the latter is
   * recursively deleted.
   * @param     name    The name of the subfile.
   * @return    The entry associated with the created subfile.
   * @exception IOException     Thrown if the subfile cannot be created.
   */
  public abstract Entry createFile(String name) throws IOException;

  /**
   * Creates a new directory in the current directory.  If the current
   * directory contains a subfile or directory with the same name, the
   * latter is recursively deleted.
   * @param     name    The name of the directory.
   * @return    The entry associated with the created directory.
   * @exception IOException     Thrown if the directory cannot be created.
   */
  public abstract Entry createDir(String name) throws IOException;

  /**
   * Creates a new directory in the current directory.  If the current
   * directory contains a subfile or directory with the same name, the
   * latter is recursively deleted. This is a synonym for
   * <code>createDir()</code>.
   * @param     name    The name of the directory.
   * @exception IOException     Thrown if the directory cannot be created.
   */
  public void createDirectory(String name) throws IOException
  {
    createDir(name);
  }

  /**
   * Renames an entry in the current directory.
   * @param     oldName The old name of the entry.
   * @param     newName The new name of the entry.
   * @exception IOException     Thrown if the there is no entry with the
   *                            given old name or if there is already an entry
   *                            with the given name in the current directory.
   * @exception IllegalArgumentException        Thrown if the new name is
   *                            empty or null.
   */
  public abstract void renameEntry(String oldName, String newName)
    throws IOException, IllegalArgumentException;

  /**
   * A synonym for renameEntry.
   * @param     oldName The old name of the entry.
   * @param     newName The new name of the entry.
   * @exception IOException     Thrown if the there is no entry with the
   *                            given old name or if there is already an entry
   *                            with the given name in the current directory.
   * @exception IllegalArgumentException        Thrown if the new name is
   *                            empty or null.
   */
  public void renameFile(String oldName, String newName)
    throws IOException, IllegalArgumentException
  {
    renameEntry(oldName, newName);
  }

  /**
   * Deletes recursively an entry from the current directory.
   * @param     name    The name of the entry to be deleted.
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the entry name is
   *                                            null or empty.
   */
  public abstract void deleteEntry(String name)
    throws IOException, IllegalArgumentException;

  /**
   * Deletes recursively an entry from its parent directory.
   * @param     path    A vector containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public void deleteEntry(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    newDeleteEntry(path, relative);
  }

  /**
   * Deletes recursively an entry from its parent directory.
   * @param     path    A list containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public abstract void newDeleteEntry(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Deletes recursively an entry.
   * @param     e       The entry to delete.
   * @exception IOException     Thrown if the entry cannot be deleted.
   */
  public abstract void deleteEntry(Entry e) throws IOException;

  /**
   * Deletes recursively an entry from the current directory. This is a
   * synonym for <code>deleteEntry(String)</code>.
   * @param     name    The name of the entry to be deleted.
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the entry name is
   *                                            null or empty.
   */
  public void deleteFile(String name) 
    throws IOException, IllegalArgumentException
  {
    deleteEntry(name);
  }

  /**
   * Deletes recursively an entry from its parent directory. This is a
   * synonym for <code>deleteEntry(Vector, boolean)</code>.
   * @param     path    A vector containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public void deleteFile(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    deleteEntry(path, relative);
  }

  /**
   * Deletes recursively an entry from its parent directory. This is a
   * synonym for <code>deleteEntry(Vector, boolean)</code>.
   * @param     path    A list containing the names of entry names to
   *                    search.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public void newDeleteFile(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    newDeleteEntry(path, relative);
  }

  /**
   * Sets the current directory to a given subdirectory of the current
   * directory.
   * @param     name    The name of the new current directory.
   * @exception IOException     Thrown if changing to the new directory fails.
   * @exception IllegalArgumentException        Thrown if the new directory
   *                                            name is empty or null.
   */
  public abstract void changeDir(String name)
    throws IOException, IllegalArgumentException;

  /**
   * Sets the current directory by specifying a sequence of directories, either
   * relative to the current directory or relative to the root directory.
   * @param     path    A vector containing the names of directories to which
   *                    to switch successively.
   * @param     relative        Specifies whether the path of directories is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if switching to any of the specified
   *                    directories in the path fails. If this happens, the
   *                    current directory remains unchanged.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public void changeDir(Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    newChangeDir(path, relative);
  }

  /**
   * Sets the current directory by specifying a sequence of directories, either
   * relative to the current directory or relative to the root directory.
   * @param     path    A list containing the names of directories to which
   *                    to switch successively.
   * @param     relative        Specifies whether the path of directories is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if switching to any of the specified
   *                    directories in the path fails. If this happens, the
   *                    current directory remains unchanged.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public abstract void newChangeDir(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Sets the current directory to that associated with a specified directory
   * entry.
   * @param     newEntry        The entry associated with the new directory.
   * @exception IOException     Thrown if changing to the new directory fails.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                            is null or empty, or if it points to a subfile
   *                            rather than a directory.
   */
  public abstract void changeDir(Entry newEntry)
    throws IOException, IllegalArgumentException;

  /**
   * Sets the current directory to the parent of the current directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public abstract void changeToParentDir() throws IOException;

  /**
   * Sets the current directory to the root directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public abstract void changeToRootDir() throws IOException;

  /**
   * Returns the directory entry associated with the current directory.
   * @return    The requested entry.
   */
  public abstract Entry getCurrentDirEntry();

  /**
   * Returns the directory entry associated with the root directory.
   * @return    The requested entry.
   */
  public abstract Entry getRootEntry();

  /**
   * Returns a list of the entries in the current directory.
   * @return    A vector containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public abstract Vector list();

  /**
   * Returns a list of the entries in the current directory.
   * @return    A list containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public abstract AbstractList newList();

  /**
   * Returns a list of the entries in a specified directory.
   * @param     entry   The entry correspronding to the directory to list.
   * @return    A list containing the entries in the specified directory.
   *            The order of the entries is unspecified.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                    is null, or if it points to a subfile rather than a
   *                    directory.
   */
  public abstract AbstractList list(Entry entry);

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified string are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public abstract void setComment(String comment) throws IOException;

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified byte array are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public abstract void setComment(byte[] comment) throws IOException;

  /**
   * Returns the contents of the comment block as a byte array.
   * @return    The contents of the comment block.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public abstract byte[] getCommentBytes() throws IOException;

  /**
   * Returns the contents of the comment block as a string. The string is
   * truncated at the first null character.
   * @return    The requested comment.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public abstract String getCommentString() throws IOException;

  /**
   * Check whether there is an entry with a given name in the current
   * directory.
   * @param     name    The name to check.
   * @exception IOException     Not actually thrown.
   * @return    True if yes, false if no.
   */
  public boolean fileExists(String name) throws IOException
  {
    try {
      findEntry(name);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Check whether there is an entry with a given name.
   * @param     path    A list containing the names of entry names to
   *                    from root or currentdirectory (depending on
   *                    <code>relative</code>) to the entry whose existence is
   *                    being checked.
   * @param     relative        Specifies whether the path of entry names is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @return    True if yes, false if no.
   */
  public boolean fileExists(AbstractList path, boolean relative)
  {
    try {
      newFindEntry(path, relative);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Check whether an entry in the current directory with a given name
   * is a plain file.
   * @param     name    The name to check.
   * @return    True if the name corresponds to a plain file, false if the
   *            name corresponds to a directory.
   * @exception IOException     Thrown if the requested entry cannot be
   *                            found.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                            name is null or empty.
   */
  public boolean isPlainFile(String name)
    throws IOException, IllegalArgumentException
  {
    Entry e = findEntry(name);
    if (e.type == Entry.DIRECTORY) {
      return false;
    }else{
      return true;
    }
  }

  /**
   * Return a list of the names of the entries in the current directory.
   * @exception IOException     Not actually thrown.
   * @return    An array containing the requested names.
   */
  public String[] listFiles() throws IOException
  {
    AbstractList dir = newList();
    int nEntries = dir.size();
    String[] names = new String[nEntries];
    for (int i=0; i<nEntries; i++) {
      names[i] = ((Entry)(dir.get(i))).name;
    }
    return names;
  }

  /**
   * Returns the ratio of space in the structured file that is actually used
   * and the total space that the file takes on disk.
   * @return    A number between 0.0 (the file is completely empty) and 1.0
   *            (the file contains no unused space).
   */
  public abstract double usedRatio();

  /**
   * Returns the ratio of space in the structured file that is unused
   * and the total space that the file takes on disk. This method is
   * equivalent to 1.0 - usedRatio();
   * @return    A number between 0.0 (the file is contains no unused space)
   *            and 1.0 (the file is completely empty.).
   */
  public double freeRatio()
  {
    return 1.0 - usedRatio();
  }

  /**
   * Returns the size of the structured file.
   * @return    The size of the structured file in bytes.
   * @exception IOException     Thrown if something goes wrong.
   */
  public abstract long length() throws IOException;

  /**
   * Copies an external file to a subfile of the structured file.
   * This method does not modify the current subdirectory of the structured
   * file.
   * @param     file    The file to copy. If the file is a directory, its
   *                    contents are recursively copied to the subfile.
   * @param     path    The path of the subdirectory of the structured file
   *                    where the file with copied.
   *                    This is a vector containing the names of the
   *                    directories in the path.
   * @param     relative        Specifies whether the path of directories is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the copy operation fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public void copyFile(File file, Vector path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    newCopyFile(file, path, relative);
  }

  /**
   * Copies an external file to a subfile of the structured file.
   * This method does not modify the current subdirectory of the structured
   * file.
   * @param     file    The file to copy. If the file is a directory, its
   *                    contents are recursively copied to the subfile.
   * @param     path    The path of the subdirectory of the structured file
   *                    where the file with copied.
   *                    This is a list containing the names of the
   *                    directories in the path.
   * @param     relative        Specifies whether the path of directories is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @exception IOException     Thrown if the copy operation fails.
   * @exception IllegalArgumentException        Thrown if the specified path
   *                    is null, if any of the path components are not
   *                    strings, or if any of the path components are null or
   *                    empty.
   */
  public abstract void newCopyFile(
    File file, AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException;

  /**
   * Copies an external file to a subfile in the current subdirectory
   * of the structured file. This method does not modify the current
   * subdirectory of the structured file.
   * @param     file    The file to copy. If the file is a directory, its
   *                    contents are recursively copied to the subfile.
   * @exception IOException     Thrown if the copy operation fails.
   */
  public void copyFile(File file) throws IOException
  {
    newCopyFile(file, new ArrayList(), RELATIVE_PATH);
  }

  /**
   * Copies a subfile or subdirectory to a subdirectory of the structured file.
   * This method does not modify the current subdirectory of the structured
   * file.
   * @param     sourcePath      The path of the subfile or subdirectory of the
   *                    structured file that will be copied.
   *                    This is a vector containing the names of the
   *                    directories in the path, ending with the name of the
   *                    subfile or subdirectory that will be copied.
   * @param     relativeSource  Specifies whether <code>sourcePath</code> is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @param     destinationPath The path of the subdirectory of the
   *                    structured file where the file with copied.
   *                    This is a vector containing the names of the
   *                    directories in the paths, ending with the name of the
   *                    destination subdirectory.
   * @param     relativeDestination     Specifies whether
   *                    <code>destinationPath</code> is relative to the
   *                    current directory (true or RELATIVE_PATH) or to the
   *                    root directory (false or ABSOLUTE_PATH).
   * @exception IOException     Thrown if the copy operation fails.
   * @exception IllegalArgumentException        Thrown if any of the
   *                    specified paths is null, if any of the path
   *                    components are not strings, or if any of the path
   *                    components are null or empty.
   */
  public void copySubFile(Vector sourcePath, boolean relativeSource,
                          Vector destinationPath, boolean relativeDestination)
    throws IOException, IllegalArgumentException
  {
    newCopySubFile(
      sourcePath, relativeSource,
      this, destinationPath, relativeDestination
    );
  }

  /**
   * Copies a subfile or subdirectory of the structured file to a subdirectory
   * of another structured file. The destination structured file can be the
   * same as the current structured file.
   * This method does not modify the current subdirectory of either structured
   * file.
   * @param     sourcePath      The path of the subfile or subdirectory of the
   *                    structured file that will be copied.
   *                    This is a list containing the names of the
   *                    directories in the path, ending with the name of the
   *                    subfile or subdirectory that will be copied.
   * @param     relativeSource  Specifies whether <code>sourcePath</code> is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @param     destination     The structured file to which subfiles will be
   *                    copied.
   * @param     destinationPath The path of the subdirectory of the
   *                    structured file where the file with copied.
   *                    This is a list containing the names of the
   *                    directories in the paths, ending with the name of the
   *                    destination subdirectory.
   * @param     relativeDestination     Specifies whether
   *                    <code>destinationPath</code> is relative to the
   *                    current directory (true or RELATIVE_PATH) or to the
   *                    root directory (false or ABSOLUTE_PATH).
   * @exception IOException     Thrown if the copy operation fails.
   * @exception IllegalArgumentException        Thrown if any of the
   *                    specified paths is null, if any of the path
   *                    components are not strings, if any of the path
   *                    components are null or empty, or if the destination
   *                    structured file is null.
   */
  public abstract void newCopySubFile(
                          AbstractList sourcePath, boolean relativeSource,
                          AbstractStructFile destination,
                          AbstractList destinationPath,
                          boolean relativeDestination)
    throws IOException, IllegalArgumentException;

  /**
   * Copies a subfile or subdirectory of the structured file to a subdirectory
   * of another structured file. The destination structured file can be the
   * same as the current structured file.
   * This method does not modify the current subdirectory of either structured
   * file.
   * @param     sourcePath      The path of the subfile or subdirectory of the
   *                    structured file that will be copied.
   *                    This is a vector containing the names of the
   *                    directories in the path, ending with the name of the
   *                    subfile or subdirectory that will be copied.
   * @param     relativeSource  Specifies whether <code>sourcePath</code> is
   *                    relative to the current directory (true or
   *                    RELATIVE_PATH) or to the root directory (false or
   *                    ABSOLUTE_PATH).
   * @param     destination     The structured file to which subfiles will be
   *                    copied.
   * @param     destinationPath The path of the subdirectory of the
   *                    structured file where the file with copied.
   *                    This is a vector containing the names of the
   *                    directories in the paths, ending with the name of the
   *                    destination subdirectory.
   * @param     relativeDestination     Specifies whether
   *                    <code>destinationPath</code> is relative to the
   *                    current directory (true or RELATIVE_PATH) or to the
   *                    root directory (false or ABSOLUTE_PATH).
   * @exception IOException     Thrown if the copy operation fails.
   * @exception IllegalArgumentException        Thrown if any of the
   *                    specified paths is null, if any of the path
   *                    components are not strings, if any of the path
   *                    components are null or empty, or if the destination
   *                    structured file is null.
   */
  public void copySubFile(Vector sourcePath, boolean relativeSource,
                StructFile destination, Vector destinationPath,
                boolean relativeDestination)
    throws IOException, IllegalArgumentException
  {
    newCopySubFile(
      sourcePath, relativeSource,
      destination.getStructFile(), destinationPath, relativeDestination
    );
  }

  /**
   * Converts a list containing a path to a string, where the path
   * components are separated by the current system's file separator.
   */
  protected static String listToPath(AbstractList list)
  {
    int n = list.size();
    String sep = System.getProperty("file.separator");
    StringBuffer sb = new StringBuffer();
    for (int i=0; i<n; i++) {
      if (i > 0) {
        sb.append(sep);
      }
      sb.append((String)(list.get(i)));
    }
    return sb.toString();
  }

  /**
   * Opens a StructRandomAccessFile.
   * @param     file    The name of the file to open.
   * @param     mode    The access mode. One of "r" or "rw".
   * @return    The StructRandomAccessFile nthat was opened.
   * @exception FileNotFoundException   Thrown if the file exists but is a
   *                    directory rather than a regular file, or cannot be
   *                    opened or created for any other reason.
   * @exception IOException     Thrown if something went wrong while
   *                    initilaizing the StructRandomAccessFile.
   */
  protected StructRandomAccessFile
    openRandomAccessFile(String file, String mode)
    throws FileNotFoundException, IOException
  {
    StructRandomAccessFile result = null;
    switch (getCacheType()) {
      case NO_CACHE:
        result = new RandomAccessFileNoCache(file, mode);
        break;
      case LRU_CACHE:
        result = new RandomAccessFileLRUCache(file, mode);
        break;
      case FULL_CACHE:
        result = new RandomAccessFileFullCache(file, mode);
        break;
    }
    return result;
  }

  /**
   * Sets the type of cache associated with structured files.
   * @param     type    The type of cache. One of NO_CACHE, LRU_CACHE,
   *                    or FULL_CACHE. Anything else is equivalent to
   *                    NO_CACHE.
   */
  public static void setCacheType(int type)
  {
    switch (type) {
      case NO_CACHE:
      case LRU_CACHE:
      case FULL_CACHE:
        cacheType = type;
        break;
      default:
        cacheType = NO_CACHE;
        break;
    }
  }

  /**
   * Returns the type of cache associated with stuctured files.
   * @return    One of NO_CACHE, LRU_CACHE, or FULL_CACHE.
   */
  public static int getCacheType()
  {
    return cacheType;
  }

  /**
   * Returns the version of the storage format used by the structured file.
   * @return    The version of the storage format used by the structured file.
   */
  public abstract int getFormatVersion();
}
