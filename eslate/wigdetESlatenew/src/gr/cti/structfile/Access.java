package gr.cti.structfile;

import java.io.*;
import java.util.*;

/**
 * This interface specifies the functions used to access subfiles and
 * subdirectories in a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public interface Access
{
  /**
   * Sets the current directory to a given subdirectory of the current
   * directory.
   * @param     name    The name of the new current directory.
   * @exception IOException     Thrown if changing to the new directory fails.
   * @exception IllegalArgumentException        Thrown if the new directory
   *                                            name is empty or null.
   */
  public void changeDir(String name)
    throws IOException, IllegalArgumentException;

  /**
   * Sets the current directory by specifying a sequence of directories,
   * either
   * relative to the current directory or relative to the root directory.
   * @param     path    A vector containing the names of directories to which
   *                    to which to switch successively.
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
    throws IOException, IllegalArgumentException;

  /**
   * Sets the current directory to the root directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToRootDir() throws IOException;

  /**
   * Sets the current directory to the parent of the current directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToParentDir() throws IOException;

  /**
   * Opens a sub-file for input in the current directory.
   * @param     name    The name of the file.
   * @return    An input stream associated with the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public InputStream openInputFile(String name) throws IOException;

  /**
   * Opens a sub-file for output in the current directory.
   * @param     name    The name of the file.
   * @return    An output stream associated with the file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public OutputStream openOutputFile(String name) throws IOException;

  /**
   * Deletes recursively an entry from the current directory.
   * @param     name    The name of the entry to delete.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @exception IllegalArgumentException        Thrown if the entry name is
   *                                            null or empty.
   */
  public void deleteFile(String name)
    throws IOException, IllegalArgumentException;

  /**
   * Renames an entry in the current directory.
   * @param     oldName The old name of the entry.
   * @param     newName The new name of the entry.
   * @exception IOException     Thrown if the there is no entry with the
   *                            given old name or if there is already an entry
   *                            with the given name in the current directory.
   * @exception IllegalArgumentException        Thrown if the new name is
   *                                            empty or null.
   */
  public void renameFile(String oldName, String newName)
    throws IOException, IllegalArgumentException;

  /**
   * Creates a new directory in the current directory.  If the current
   * directory contains a subfile or directory with the same name, the
   * latter is recursively deleted.
   * @param     name    The name of the directory.
   * @exception IOException     Thrown if the directory cannot be created.
   */
  public void createDirectory(String name) throws IOException;

  /**
   * Check whether there is an entry with a given name in the current
   * directory.
   * @param     name    The name to check.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   * @return    True if yes, false if no.
   */
  public boolean fileExists(String name) throws IOException;

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
    throws IOException, IllegalArgumentException;

  /**
   * Return a list of the names of the entries in the current directory.
   * @exception IOException     Thrown if accessing the structured file failes
   *                            for some reason.
   * @return    An array containing the requested names.
   */
  public String[] listFiles() throws IOException;

  /**
   * Flush the cache associated with the structured file, if there is one.
   */
  public void flushCache();
}
