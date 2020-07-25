package gr.cti.structfile;

import java.io.*;
import java.util.*;

import gr.cti.structfile.version2.*;

/**
 * This class is an interface to the various structured file implementations.
 * <code>NEW</code> files are created using the default implementation
 * while for <code>OLD</code> files, successive attempts to open them are
 * made, using all supported implementations.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 6-Jul-2006
 * 
 */
public class StructFile extends AbstractStructFile
{
  /**
   * The structured file encapsulated by this class.
   */
  AbstractStructFile sf;
  /**
   * Version of encapsulated structured file.
   */
  int version;

  /**
   * Creates a structured file.
   * @param     file    The name of the disk file on which the structure will
   *                    be imposed.
   * @param     mode    If OLD, then an existing file is opened. If NEW, a new
   *                    file is created, overwriting any file with the same
   *                    name.
   * @exception IOException     Thrown if opening the file failed.
   * @exception IllegalArgumentException        Thrown if mode is neither OLD
   *                                            nor NEW.
   * @exception UnsupportedVersionException     Thrown if trying toopen a file
   *                     whose version is not supported.
   */
  public StructFile(String file, int mode)
    throws IOException, IllegalArgumentException, UnsupportedVersionException
  {
    switch (mode) {
      case NEW:
        // Use the latest implementation.
        sf = new StructFile2(file, NEW);
        version = 2;
        break;
      case OLD:
        // Try all implementations.
        try {
          sf = new StructFile2(file, OLD);
          version = 2;
        } catch (UnsupportedVersionException uve) {
          // uve.getMessage() contains the string
          // "VERSION=n", so use that to identify which version to use,
          // rather than trying all versions to see which one manages to
          // open the file.
          //
          // (Currently there is nothing to do with the version string.)
          //
          // If uve.getMessage() does not contain such a string, then we are
          // trying to open a plain file.
          version = -1;
          throw uve;
        }
        break;
    }
  }

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
  public void open(String file, int mode)
    throws IOException, IllegalArgumentException
  {
    // If the structured file being opened is a different version from the one
    // that was last opened, then the encapsulated structured file cannot be
    // reused.
    switch (version) {
      case 2:
        switch (mode) {
          case NEW:
              // Open using the latest version.
              sf.open(file, NEW);
              version = 2;
            break;
          case OLD:
            try {
              // Try reusing old structured file.
              sf.open(file, OLD);
              version = 2;
            }catch (UnsupportedVersionException uve) {
              version = -1;
              throw new IOException(uve.getMessage());
            }
            break;
        }
        break;
      // Add cases for newer structured file implementation versions here.
    }
  }

  /**
   * Closes the structured file. After invoking this method, the StructFile
   * object cannot be used unless the <code>open</code> method is invoked, to
   * associate the object with a new file.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    sf.close();
  }

  /**
   * Returns the file on which the structured file has been opened.
   * @return    The file on which the structured file has been opened. If the
   *            structured file has been closed, this method returns
   *            <code>null</code>.
   */
  public File getFile()
  {
    return sf.getFile();
  }

  /**
   * Flush the cache of the underlying file. If the underlying file does not
   * have an associated cache, this method does nothing.
   */
  public void flushCache()
  {
    sf.flushCache();
  }

  /**
   * Checks whether the structured file is open.
   * @return    True if the structured file is open, false if it is closed.
   */
  public boolean isOpen()
  {
    return sf.isOpen();
  }

  /**
   * Opens a sub-file for input in the current directory.
   * @param     name    The name of the file.
   * @return    An input stream associated with the sub-file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public InputStream openInputFile(String name) throws IOException
  {
    return sf.openInputFile(name);
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
  public InputStream newOpenInputFile(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return sf.newOpenInputFile(path, relative);
  }

  /**
   * Opens a sub-file for output in the current directory.
   * @param     name    The name of the file.
   * @return    An output stream associated with the sub-file.
   * @exception IOException     Thrown when accessing the structured file
   *                            fails for some reason.
   */
  public OutputStream openOutputFile(String name) throws IOException
  {
    return sf.openOutputFile(name);
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
  public OutputStream newOpenOutputFile(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return sf.newOpenOutputFile(path, relative);
  }

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
  public Entry findEntry(String name)
    throws IOException, IllegalArgumentException
  {
    return sf.findEntry(name);
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
  public Entry newFindEntry(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    return sf.newFindEntry(path, relative);
  }

  /**
   * Creates a new subfile in the current directory.  If the current directory
   * contains a subfile or directory with the same name, the latter is
   * recursively deleted.
   * @param     name    The name of the subfile.
   * @return    The entry associated with the created subfile.
   * @exception IOException     Thrown if the subfile cannot be created.
   */
  public Entry createFile(String name) throws IOException
  {
    return sf.createFile(name);
  }

  /**
   * Creates a new directory in the current directory.  If the current
   * directory contains a subfile or directory with the same name, the
   * latter is recursively deleted.
   * @param     name    The name of the directory.
   * @return    The entry associated with the created directory.
   * @exception IOException     Thrown if the directory cannot be created.
   */
  public Entry createDir(String name) throws IOException
  {
    return sf.createDir(name);
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
  public void renameEntry(String oldName, String newName)
    throws IOException, IllegalArgumentException
  {
    sf.renameEntry(oldName, newName);
  }

  /**
   * Deletes recursively an entry from the current directory.
   * @param     name    The name of the entry to be deleted.
   * @exception IOException     Thrown if the entry cannot be deleted.
   * @exception IllegalArgumentException        Thrown if the entry name is
   *                                            null or empty.
   */
  public void deleteEntry(String name)
    throws IOException, IllegalArgumentException
  {
    sf.deleteEntry(name);
  }

  /**
   * Deletes recursively an entry from the current directory.
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
  public void newDeleteEntry(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    sf.newDeleteEntry(path, relative);
  }

  /**
   * Deletes recursively an entry.
   * @param     e       The entry to delete.
   * @exception IOException     Thrown if the entry cannot be deleted.
   */
  public void deleteEntry(Entry e) throws IOException
  {
    sf.deleteEntry(e);
  }

  /**
   * Sets the current directory to a given subdirectory of the current
   * directory.
   * @param     name    The name of the new current directory.
   * @exception IOException     Thrown if changing to the new directory fails.
   * @exception IllegalArgumentException        Thrown if the new directory
   *                                            name is empty or null.
   */
  public void changeDir(String name)
    throws IOException, IllegalArgumentException
  {
    sf.changeDir(name);
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
  public void newChangeDir(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    sf.newChangeDir(path, relative);
  }

  /**
   * Sets the current directory to that associated with a specified directory
   * entry.
   * @param     newEntry        The entry associated with the new directory.
   * @exception IOException     Thrown if changing to the new directory fails.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                            is null or empty, or if it points to a subfile
   *                            rather than a directory.
   */
  public void changeDir(Entry newEntry)
    throws IOException, IllegalArgumentException
  {
    sf.changeDir(newEntry);
  }

  /**
   * Sets the current directory to the parent of the current directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToParentDir() throws IOException
  {
    sf.changeToParentDir();
  }

  /**
   * Sets the current directory to the root directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToRootDir() throws IOException
  {
    sf.changeToRootDir();
  }

  /**
   * Returns the directory entry associated with the current directory.
   * @return    The requested entry.
   */
  public Entry getCurrentDirEntry()
  {
    return sf.getCurrentDirEntry();
  }

  /**
   * Returns the directory entry associated with the root directory.
   * @return    The requested entry.
   */
  public Entry getRootEntry()
  {
    return sf.getRootEntry();
  }

  /**
   * Returns a list of the entries in the current directory.
   * @return    A vector containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public Vector list()
  {
    return sf.list();
  }

  /**
   * Returns a list of the entries in the current directory.
   * @return    A list containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public AbstractList newList()
  {
    return sf.newList();
  }

  /**
   * Returns a list of the entries in a specified directory.
   * @param     entry   The entry correspronding to the directory to list.
   * @return    A list containing the entries in the specified directory.
   *            The order of the entries is unspecified.
   * @exception IllegalArgumentException        Thrown if the specified entry
   *                    is null, or if it points to a subfile rather than a
   *                    directory.
   */
  public AbstractList list(Entry entry)
  {
    return sf.list(entry);
  }

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified string are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void setComment(String comment) throws IOException
  {
    sf.setComment(comment);
  }

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified byte array are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void setComment(byte[] comment) throws IOException
  {
    sf.setComment(comment);
  }

  /**
   * Returns the contents of the comment block as a byte array.
   * @return    The contents of the comment block.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public byte[] getCommentBytes() throws IOException
  {
    return sf.getCommentBytes();
  }

  /**
   * Returns the contents of the comment block as a string. The string is
   * truncated at the first null character.
   * @return    The requested comment.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public String getCommentString() throws IOException
  {
    return sf.getCommentString();
  }

  /**
   * Returns the ratio of space in the structured file that is actually used
   * and the total space that the file takes on disk.
   * @return    A number between 0.0 (the file is completely empty) and 1.0
   *            (the file contains no unused space).
   */
  public double usedRatio()
  {
    return sf.usedRatio();
  }

  /**
   * Returns the size of the structured file.
   * @return    The size of the structured file in bytes.
   * @exception IOException     Thrown if something goes wrong.
   */
  public long length() throws IOException
  {
    return sf.length();
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
  public void newCopyFile(File file, AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    sf.newCopyFile(file, path, relative);
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
  public void newCopySubFile(AbstractList sourcePath, boolean relativeSource,
                          AbstractStructFile destination,
                          AbstractList destinationPath,
                          boolean relativeDestination)
    throws IOException, IllegalArgumentException
  {
    AbstractStructFile dest;
    // Resolve actual destination structured file.
    if (destination instanceof StructFile) {
      dest = ((StructFile)destination).getStructFile();
    }else{
      dest = destination;
    }
    sf.newCopySubFile(
      sourcePath, relativeSource, dest, destinationPath, relativeDestination
    );
  }

  /**
   * Returns the encapsulated structured file.
   * @return    The encapsulated structured file.
   */
  public AbstractStructFile getStructFile()
  {
    return sf;
  }

  /**
   * Returns the version of the storage format used by the structured file.
   * @return    The version of the storage format used by the structured file.
   */
  public int getFormatVersion()
  {
    return sf.getFormatVersion();
  }
}
