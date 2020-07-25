package gr.cti.structfile.version2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Vector;

import gr.cti.typeArray.BytBaseArray;

import gr.cti.structfile.AbstractStructFile;
import gr.cti.structfile.Entry;
import gr.cti.structfile.RAFInputStream;
import gr.cti.structfile.RAFOutputStream;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructRandomAccessFile;
import gr.cti.structfile.UnsupportedVersionException;

/**
 * This class implements a structured file containing subfiles and
 * directories containing other subfiles or directories. Subfiles and
 * directories are created with a name as a key, and support is provided for
 * traversing the directory structure of the file and for building input and
 * output streams to access the contents of the subfiles. The file is divided
 * into 512-byte blocks, and has the following structure:
 * <UL>
 * <LI>
 * Block #0 contains comments. Applications can store magic numbers or other
 * identification strings in it.
 * </LI>
 * <LI>
 * Block #1 is the header block. It contains a version string
 * (<code>VERSION=2</code>) in UTF format, then a four-byte integer containing
 * the block number with the position of the directory of the structured file.
 * <LI>
 * After the header block follow the contents of the file, and then the
 * directory, which is a dump of the directory tree structure.
 * </LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.StructInputStream
 * @see         gr.cti.structfile.StructOutputStream
 * 
 */
public class StructFile2 extends AbstractStructFile
{
  /**
   * Number of bytes per block.
   */
  public final static int BLOCKSIZE = 512;
  /**
   * Block number of the header block.
   */
  private final static int HEADERBLOCK = 1;

  /**
   * String identifying the structured file format version used in files
   * created by this class.
   */
  private final static String VERSION_STRING = "VERSION=2";

  /**
   * The file on which the structure is imposed.
   */
  private StructRandomAccessFile raf = null;
  /**
   * The block allocation bitmap.
   */
  private BytBaseArray bitmap;
  /**
   * The entry associated with the current directory.
   */
  private Entry2 currentDirEntry;
  /**
   * The entry associated with the root directory.
   */
  private Entry2 rootEntry;
  /**
   * Indicates that the structured file has been modified since opening.
   */
  private boolean modified;

  /**
   * The file on which the structured file has been opened.
   */
  private File myFile = null;

  /**
   * The shutdown hook.
   */
  private Thread shutdownHook = null;
  /**
   * Indicates that the shutdown hook is executing.
   */
  private boolean inShutdownHook = false;

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
   * @exception UnsupportedVersionException     Thrown if the file being
   *                    opened with <code>mode=NEW</code> is not a version 2
   *                    structured file.
   */
  public StructFile2(String file, int mode)
    throws IOException, IllegalArgumentException, UnsupportedVersionException
  {
    open(file, mode);
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
   * @exception UnsupportedVersionException     Thrown if the file being
   *                    opened with <code>mode=NEW</code> is not a version 2
   *                    structured file.
   */
  public void open(String file, int mode)
    throws IOException, IllegalArgumentException, UnsupportedVersionException
  {
    if (raf != null) {
      throw new IOException(resources.getString("alreadyOpen"));
    }

    File f = new File(file);

    if (mode == NEW && f.exists()) {
      if (!f.canWrite()) {
        throw new IOException(
          resources.getString("writeProtected1") +
          file +
          resources.getString("writeProtected2")
        );
      }
      boolean deleted = f.delete();
      if (!deleted) {
        throw new IOException(
          resources.getString("couldNotDelete") + " " + file
        );
      }
    }
    if (mode == OLD && !f.exists()) {
      throw new IOException(
        resources.getString("cannotOpen1") + file +
        resources.getString("cannotOpen2")
      );
    }
    if (mode != OLD && mode != NEW) {
      throw new IllegalArgumentException(resources.getString("badMode"));
    }
    int ct = cacheType;
    try {
      if (ct == LRU_CACHE) {
        // LRU_CACHE won't work with version 2 files; besides, it doesn't make
        // much sense in this case.
        cacheType = NO_CACHE;
      }
      raf = openRandomAccessFile(file, "rw");
    } catch (IOException e) {
      // If opening an existing file, try opening it as read-only, if opening
      // it for read-write fails.
      if (mode == OLD) {
        raf = openRandomAccessFile(file, "r");
      }else{
        throw e;
      }
    } finally {
      cacheType = ct;
    }
    if (mode == NEW) {
      // Write empty comment and header blocks block
      writeEmptyBlock();
      writeEmptyBlock();
      modified = true;
    }else{
      modified = false;
    }

    // Load bitmap and directory stucture.
    if (mode == NEW) {
      bitmap = new BytBaseArray();
      bitmap.add((byte)0);
      reserveBlock(COMMENTBLOCK);
      reserveBlock(HEADERBLOCK);
      rootEntry = new Entry2((Entry2)null);
    }else{
      readInfo();
    }

    currentDirEntry = rootEntry;
    myFile = f;

    // Version 2 format is designed for speed, not robustness. The directory
    // is maintained in memory, and it is only written to disk when the file
    // is closed. If the JVM shuts down before the file is closed (e.g., if
    // the user hits ^C), then the directory will not be written to disk. In
    // such a case, if the directory has been modified, the structured file
    // will be completely unrecoverable. To avoid this,we register a shutdown
    // hook which will ensure that the file is closed when the JVM shuts down.
    shutdownHook = new Thread(){
      public void run()
      {
        if (raf != null) {
          try {
            inShutdownHook = true;
            close();
          } catch (IOException ioe) {
            // Can't do much if closing fails. Display the stack trace, so
            // that, at least, we are aware of the problem.
            ioe.printStackTrace();
          }
        }
      }
    };
    Runtime.getRuntime().addShutdownHook(shutdownHook);
  }

  /**
   * Reads the allocation bitmap and directory structure of the structured
   * file.
   * @exception IOException     Throws if the reading fails.
   * @exception UnsupportedVersionException     Thrown if the file being read
   *                            is not a version 2 structured file.
   */
  private void readInfo()
    throws IOException, UnsupportedVersionException
  {
    goToBlock(HEADERBLOCK);
    String version;
    try {
      version = raf.readUTF();
    } catch (Throwable th) {
      version = "";
    }
    if (!version.equals(VERSION_STRING)) {
      throw new UnsupportedVersionException(version);
    }
    int infoBlock = raf.readInt();
    long start = (long)infoBlock * (long)BLOCKSIZE;
    RAFInputStream ris = new RAFInputStream(raf, start);
    BufferedInputStream bis = new BufferedInputStream(ris);
    DataInputStream dis = new DataInputStream(bis);
    int n = dis.readInt();
    byte[] b = new byte[n];
    dis.readFully(b, 0, n);
    bitmap = new BytBaseArray(n);
    for (int i=0; i<n; i++) {
      bitmap.add(b[i]);
    }
    b = null;
    rootEntry = Entry2.readEntry(dis);
    dis.close();
  }

  /**
   * Writes the allocation bitmap and directory structure of the structured
   * file.
   * @exception IOException     Throws if the writing fails.
   */
  private void writeInfo() throws IOException
  {
    int infoBlock = getLastBlock() + 1;
    long start = (long)infoBlock * (long)BLOCKSIZE;
    RAFOutputStream ros = new RAFOutputStream(raf, start);
    BufferedOutputStream bos = new BufferedOutputStream(ros);
    DataOutputStream dos = new DataOutputStream(bos);
    int n = bitmap.size();
    dos.writeInt(n);
    dos.write(bitmap.toArray(), 0, n);
    rootEntry.saveEntry(dos);
    dos.close();
    goToBlock(HEADERBLOCK);
    raf.writeUTF(VERSION_STRING);
    raf.writeInt(infoBlock);
  }

  /**
   * Returns the last used block in the structured file.
   * @return    The last used block in the structured file.
   */
  private int getLastBlock()
  {
    int n = bitmap.size();
    for (int i=n-1; i>=0; i--) {
      if (bitmap.get(i) != (byte)0) {
        for (int j=BITSPERBYTE-1; j>=0; j--) {
          if (getBit(i*BITSPERBYTE+j) != 0) {
            return i*BITSPERBYTE+j;
          }
        }
      }
    }
    return n * BITSPERBYTE;
  }

  /**
   * Closes the structured file. After invoking this method, the StructFile
   * object cannot be used unless the <code>open</code> method is invoked, to
   * associate the object with a new file.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    flushCache();
    if (!inShutdownHook) {
      Runtime.getRuntime().removeShutdownHook(shutdownHook);
    }
    shutdownHook = null;
    currentDirEntry = null;
    bitmap = null;
    raf.close();
    raf = null;
    myFile = null;
  }

  /**
   * Returns the file on which the structured file has been opened.
   * @return    The file on which the structured file has been opened. If the
   *            structured file has been closed, this method returns
   *            <code>null</code>.
   */
  public File getFile()
  {
    return myFile;
  }

  /**
   * Flush the cache of the underlying file. This method will also write to
   * disk the directory of the structured file, if the structured file has
   * been modified
   */
  public void flushCache()
  {
    raf.flushCache();
    if (modified) {
      try {
        writeInfo();
        modified = false;
      } catch (Exception e) {
        // Writing might fail if the file is read-only. Should not happen
        // using version 2 of the structured file format.
      }
    }
  }

  /**
   * Checks whether the structured file is open.
   * @return    True if the structured file is open, false if it is closed.
   */
  public boolean isOpen()
  {
    return (raf != null);
  }

  /**
   * Positions the file at the beginning of a given block.
   * @param     block   The number of the block to which the file must be
   *                    positioned.
   */
  private void goToBlock(int block)
  {
    if (raf == null) {
      return;
    }
    try {
      raf.seek(block * BLOCKSIZE);
    } catch (IOException ex) {
    }
  }

  /**
   * Returns the value of a bit in the block allocation bitmap.
   * @param     n       The number of the bit.
   * @return    The value of the bit (0 or 1).
   */
  private int getBit(int n)
  {
    int ind = n / BITSPERBYTE;
    int shft = n % BITSPERBYTE;
    return (int)((bitmap.get(ind) >> shft) & (byte)1);
  }

  /**
   * Sets a bit in the block allocation bitmap.
   * @param     n       The number of the bit to set.
   */
  private void setBit(int n)
  {
    int ind = n / BITSPERBYTE;
    int shft = n % BITSPERBYTE;
    byte b = bitmap.get(ind);
    b |= ((byte)1 << shft);
    bitmap.set(ind, b);
  }

  /**
   * Clears a bit in the block allocation bitmap.
   * @param     n       The number of the bit to clear.
   */
  private void clearBit(int n)
  {
    int ind = n / BITSPERBYTE;
    int shft = n % BITSPERBYTE;
    byte b = bitmap.get(ind);
    b &= ~((byte)1 << shft);
    bitmap.set(ind, b);
  }

  /**
   * Returns the number of the next available free block. If the block
   * allocation bitmap is full, the bitmap is extended by one block.
   * @return    The requested number.
   * @exception IOException     Thrown if extending the block allocation
   *                            bitmap fails.
   */
  private int getFreeBlock() throws IOException
  {
    int bitmapSize = bitmap.size();
    for (int i=0; i<bitmapSize; i++) {
      if (bitmap.get(i) != (byte)0xFF) {
        for (int j=0; j<BITSPERBYTE; j++) {
          if (getBit(i*BITSPERBYTE+j) == 0) {
            return i*BITSPERBYTE+j;
          }
        }
      }
    }
    // If getting a free block failed (bitmap is full), extend the bitmap,
    // giving us BITSPERBYTE more free blocks.
    int oldBitmapSize = bitmap.size();
    bitmap.add((byte)0);

    // Return the first free block in the bitmap extension.
    return oldBitmapSize * BITSPERBYTE;
  }

  /**
   * Reserves a block in the block allocation bitmap.
   * @param     n       The number of the block to reserve.
   */
  private void reserveBlock(int n)
  {
    setBit(n);
  }

  /**
   * Frees a block from the block allocation bitmap.
   * @param     n       The number of the block to free.
   */
  void freeBlock(int n)
  {
    synchronized(this) {
      clearBit(n);
    }
  }

  /**
   * Prepares an entry for input.
   * @param     e       The entry to be prepared for input
   * @exception IOException     Thrown if the entry cannot be prepared for
   *                            input.
   * @exception IllegalArgumentException        Thrown if the entry is null.
   */
  void openIn(Entry e)
    throws IOException, IllegalArgumentException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (e == null) {
      throw new IllegalArgumentException(resources.getString("nullEntry"));
    }
    if (e.type == Entry.DIRECTORY) {
      throw new IOException(resources.getString("noOpenDir"));
    }
    synchronized(this) {
      ((Entry2)e).setCurrentPos(0);
    }
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
    Entry e = findEntry(name);
    if (e.type == Entry.DIRECTORY) {
      throw new IOException(resources.getString("noOpenDir"));
    }
    return new StructInputStream2(this, (Entry2)e);
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
    Entry e = newFindEntry(path, relative);
    if (e.type == Entry.DIRECTORY) {
      throw new IOException(resources.getString("noOpenDir"));
    }
    return new StructInputStream2(this, (Entry2)e);
  }

  /**
   * Cleans up after having read data from an entry.
   * @param     e       The entry that we have just finished reading.
   * @exception IOException     Thrown if the clean-up failed.
   */
  void closeIn(Entry e) throws IOException
  {
  }

  /**
   * Reads up to <code>len</code> bytes of data from an entry into an
   * array of bytes.
   * @param     e       The entry from which the data will be read.
   * @param     b       The buffer into which the data are read.
   * @param     off     The start offset of the data.
   * @param     len     The maximum number of bytes read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   * @exception IOException     Thrown if the reading fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   * @exception IndexOutOfBoundsException       Thrown if <code>off</code>
   *                    is negative, or <code>len</code> is negative, or
   *                    <code>off+len</code> is greater than the length of
   *                    the array <code>b</code>.
   */
  int read(Entry e, byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (b == null) {
      throw new NullPointerException("nullBuffer");
    }
    if ((off < 0) || (len < 0) || ((off + len) > b.length)) {
      throw new IndexOutOfBoundsException();
    }
    if (len == 0) {
      return 0;
    }
    int n;
    synchronized(this) {
      n = doRead(e, b, off, len);
    }
    return n;
  }

  /**
   * Reads up to <code>len</code> bytes of data from an entry into an
   * array of bytes starting from a specified position in the entry.
   * @param     e       The entry from which the data will be read.
   * @param     pos     The position from which to start reading.
   * @param     b       The buffer into which the data are read.
   * @param     off     The start offset of the data.
   * @param     len     The maximum number of bytes read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   * @exception IOException     Thrown if the reading fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   * @exception IndexOutOfBoundsException       Thrown if <code>pos</code> is
   *                    negative, or <code>off</code>
   *                    is negative, or <code>len</code> is negative, or
   *                    <code>off+len</code> is greater than the length of
   *                    the array <code>b</code>.
   */
  int read(Entry e, int pos, byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (b == null) {
      throw new NullPointerException(resources.getString("nullBuffer"));
    }
    if ((off < 0) || (pos < 0) || (len < 0) || ((off + len) > b.length)) {
      throw new IndexOutOfBoundsException();
    }
    if (len == 0) {
      return 0;
    }
    int n;
    // No locking!
    doSeek(e, pos);
    n = doRead(e, b, off, len);
    return n;
  }

  /**
   * Does the actual job of reading up to <code>len</code> bytes of data from
   * an entry into an array of bytes. No checks or synchronization are made.
   * @param     e       The entry from which the data will be read.
   * @param     b       The buffer into which the data are read.
   * @param     off     The start offset of the data.
   * @param     len     The maximum number of bytes read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   * @exception IOException     Thrown if the reading fails.
   */
  private int doRead(Entry e, byte[] b, int off, int len) throws IOException
  {
    Entry2 e2 = (Entry2)e;
    int available = (int)(e2.size - e2.position);
    if (available <= 0) {
      // No data are available.
      return -1;
    }
    if (len > available) {
      // Requested amount of data is more than the available data.
      len = available;
    }
    int origLen = len;
    int total = 0;
    // Read each contiguous block of data with one invocation to read().
    while (total < origLen) {
      raf.seek(e2.ptr);
      int avail = Math.min(len, e2.numberOfContiguousBytes());
      //raf.seek(e2.startOfCurrentContiguousArea() * BLOCKSIZE);
      raf.read(b, off, avail);
      off += avail;
      len -= avail;
      total += avail;
      e2.setCurrentPos(e2.getCurrentPos() + avail);
    }
    return total;
  }

  /**
   * Sets the current position within an entry to a specified position.
   * @param     e               The entry.
   * @param     position        The position.
   * @return    A non-negative number if the seek was successful, a negative
   *            number otherwise.
   * @exception IOException     Thrown when an I/O error occurs.
   * @exception IndexOutOfBoundsException       Thrown if
   *                            <code>position</code> is negative.
   */
  int seek(Entry e, int position)
    throws IOException, IndexOutOfBoundsException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (position < 0) {
      throw new IndexOutOfBoundsException();
    }
    int n;
    synchronized (this) {
      n = doSeek(e, position);
    }
    return n;
  }

  /**
   * Does the actual job of setting the current position within an entry
   * to a specified position. No checks or synchronization are made.
   * @param     e               The entry.
   * @param     position        The position.
   * @return    A non-negative number if the seek was successful, a negative
   *            number otherwise.
   * @exception IOException     Thrown when an I/O error occurs.
   */
  int doSeek(Entry e, int position) throws IOException
  {
    int len;
    Entry2 e2 = (Entry2)e;
    if (position > e2.position) {
      len = position - e2.position;
    }else{
      e2.setCurrentPos(0);
      len = position;
    }
    if (len == 0) {
      raf.seek(e2.ptr);
      return 0;
    }
    int available = (int)(e2.size - e2.position);
    if (available <= 0) {
      // No data are available.
      return -1;
    }
    if (len > available) {
      // Requested amount of data is more than available.
      return -1;
    }
    int total = 0;
    int origLen = len;
    // Skip each contiguous block of data with one seek operation.
    while (total < origLen) {
      int avail = Math.min(len, e2.numberOfContiguousBytes());
      //raf.seek(e2.ptr + avail);
      len -= avail;
      total += avail;
      e2.setCurrentPos(e2.getCurrentPos() + avail);
    }
    raf.seek(e2.ptr);
    return total;
  }

  /**
   * Prepares an entry for output.
   * @param     e       The entry to be prepared for output
   * @exception IOException     Thrown if the entry cannot be prepared for
   *                            output.
   * @exception IllegalArgumentException        Thrown if the entry is null.
   */
  void openOut(Entry2 e) throws IOException, IllegalArgumentException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (e == null) {
      throw new IllegalArgumentException(resources.getString("nullEntry"));
    }
    if (e.type == Entry.DIRECTORY) {
      throw new IOException(resources.getString("noOpenDir"));
    }
    synchronized(this) {
      goToBlock(e.location);
      e.size = 0;
      e.setCurrentPos(0);
    }
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
    Entry e;
    try {
      e = findEntry(name);
      if (e.type == Entry.DIRECTORY) {
        throw new IOException(resources.getString("noOpenDir"));
      }
    } catch (IOException ioe) {
    }
    e = createFile(name);
    return new StructOutputStream2(this, (Entry2)e);
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
    if (path == null) {
      throw new IllegalArgumentException("badPath");
    }
    int n = path.size();
    ArrayList<String> dirPath = new ArrayList<String>(n);
    for (int i=0; i<n; i++) {
      dirPath.add((String)path.get(i));
    }
    if (n == 0) {
      throw new IllegalArgumentException("badPath");
    }
    Object name = dirPath.get(n-1);
    if (!(name instanceof String)) {
      throw new IllegalArgumentException("badPath");
    }
    dirPath.remove(n-1);
    Entry oldDir = currentDirEntry;
    try {
      newChangeDir(dirPath, relative);
      Entry e = createFile((String)name);
      if (e.type == Entry.DIRECTORY) {
        throw new IOException(resources.getString("noOpenDir"));
      }
      return new StructOutputStream2(this, (Entry2)e);
    } finally {
      changeDir(oldDir);
    }
  }

  /**
   * Cleans up after having written data to an entry.
   * @param     e       The entry that we have just finished writing.
   * @exception IOException     Thrown if the clean-up failed.
   */
  void closeOut(Entry e) throws IOException
  {
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off<code> to an entry.
   * @param     e       The entry from which the data will be read.
   * @param     b       The data.
   * @param     off     The start offset in the data.
   * @param     len     The number of bytes to write.
   * @exception IOException     Thrown if the writing fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   * @exception IndexOutOfBoundsException       Thrown if <code>off</code>
   *                    is negative, or <code>len</code> is negative, or
   *                    <code>off+len</code> is greater than the length of
   *                    the array <code>b</code>.
   */
  void write(Entry e, byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (b == null) {
      throw new NullPointerException("nullBuffer");
    }
    if ((off < 0) || (len < 0) || ((off + len) > b.length)) {
      throw new IndexOutOfBoundsException();
    }
    synchronized(this) {
      doWrite(e, b, off, len);
    }
  }

  /**
   * Does the actual job of writing <code>len</code> bytes from the specified
   * byte array starting at offset <code>off<code> to an entry. No checks or
   * synchronization are made.
   * @param     e       The entry from which the data will be read.
   * @param     b       The data.
   * @param     off     The start offset in the data.
   * @param     len     The number of bytes to write.
   * @exception IOException     Thrown if the writing fails.
   */
  private void doWrite(Entry e, byte[] b, int off, int len) throws IOException
  {
    modified = true;
    Entry2 e2 = (Entry2)e;
    // Allocate the space on disk.
    int available = e2.numberOfContiguousBytes();
    int requiredLen = len - available;
    if (requiredLen > 0) {
      int requiredBlocks = (int)(requiredLen / BLOCKSIZE);
      if ((requiredLen % BLOCKSIZE) != 0) {
        requiredBlocks++;
      }
      for(int i=0; i<requiredBlocks; i++) {
        int block = getFreeBlock();
        reserveBlock(block);
        e2.addBlock(block);
      }
    }
    int total = 0;
    int origLen = len;
    // Write each contiguous block of data with one invocation to write().
    while (total < origLen) {
      raf.seek(e2.ptr);
      int avail = Math.min(len, e2.numberOfContiguousBytes());
      raf.write(b, off, avail);
      off += avail;
      len -= avail;
      e2.size += avail;
      total += avail;
      e2.setCurrentPos(e2.getCurrentPos() + avail);
    }
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
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException(resources.getString("badEntry"));
    }
    Entry2 e = currentDirEntry.findEntry(name);
    if (e != null) {
      return e;
    }else{
      throw new IOException(
        resources.getString("notFound1") + name +
        resources.getString("notFound2")
      );
    }
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
    int size = path.size();
    for (int i=0; i<size; i++) {
      if (path == null || !(path.get(i) instanceof String)) {
        throw new IllegalArgumentException(resources.getString("badPath"));
      }
    }
    Entry2 oldDir = currentDirEntry;
    Entry result = null;
    try {
      if (!relative) {
        currentDirEntry = rootEntry;
      }
      int size2 = path.size() - 1;
      for (int i=0; i<size2; i++) {
        changeDir((String)(path.get(i)));
      }
      result = findEntry((String)(path.get(size2)));
    } catch (IOException e) {
      currentDirEntry = oldDir;
      throw e;
    }
    currentDirEntry = oldDir;
    return result;
  }

  /**
   * Creates a new entry in the current directory. If the current directory
   * contains a subfile or directory with the same name, the latter is
   * recursively deleted.
   * @param     name    The name of the entry.
   * @param     type    The type of the entry (Entry.FILE or Entry.DIRECTORY).
   * @return    The created entry.
   * @exception IOException     Thrown if the entry cannot be created.
   * @exception IllegalArgumentException        Thrown if the entry name is
   *                            null or empty or if the entry type is neither
   *                            Entry.FILE nor Entry.DIRECTORY.
   */
  private Entry createEntry(String name, int type)
    throws IOException, IllegalArgumentException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (name == null || name.equals("")){
      throw new IllegalArgumentException(resources.getString("badEntry"));
    }
    if (type != Entry.FILE && type != Entry.DIRECTORY) {
      throw new IllegalArgumentException(resources.getString("fileOrDir"));
    }
    modified = true;
    Entry2 parent = currentDirEntry;
    try {
      deleteEntry(name);
    } catch (IOException ex) {
    }
    Entry2 newEntry;
    if (type == Entry.FILE) {
      synchronized(this) {
        int newBlock = getFreeBlock();
        reserveBlock(newBlock);
        newEntry = new Entry2(name, newBlock, 0L, parent);
        goToBlock(newBlock);
      }
    }else{
      newEntry = new Entry2(name, parent);
    }

    return newEntry;
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
    Entry newEntry = null;
    try {
      newEntry = createEntry(name, Entry.FILE);
    } catch (IllegalArgumentException ex) {
    }
    return newEntry;
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
    Entry newEntry = null;
    try {
      newEntry = createEntry(name, Entry.DIRECTORY);
    } catch (IllegalArgumentException ex) {
    }
    return newEntry;
  }

  /**
   * Writes an empty block to disk.
   * @exception IOException     Thrown if the block cannot be written.
   */
  private void writeEmptyBlock() throws IOException
  {
    byte[] emptyBlock = new byte[BLOCKSIZE];
    raf.write(emptyBlock);
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
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (newName == null || newName.equals("")) {
      throw new IllegalArgumentException(resources.getString("badEntry"));
    }
    if (newName.equals(oldName)) {
      return;
    }

    Entry2 e = currentDirEntry.findEntry(newName);
    if (e != null) {
      throw new IOException(
        resources.getString("anotherEntry1") +
        newName +
        resources.getString("anotherEntry2")
      );
    }else{
      e = currentDirEntry.findEntry(oldName);
      if (e == null) {
        throw new IOException(
          resources.getString("notFound1") +
          oldName +
          resources.getString("notFound2")
        );
      }else{
        e.name = newName;
        modified = true;
      }
    }
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
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException(resources.getString("badEntry"));
    }
    Entry2 e = currentDirEntry.findEntry(name);
    if (e != null) {
      currentDirEntry.deleteEntry(e, this);
      modified = true;
    }else{
      throw new IOException(
        resources.getString("notFound1") + name +
        resources.getString("notFound2")
      );
    }
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
  public void newDeleteEntry(AbstractList path, boolean relative)
    throws IOException, IllegalArgumentException
  {
    if (path == null) {
      throw new IllegalArgumentException("badPath");
    }
    int n = path.size();
    ArrayList<String> dirPath = new ArrayList<String>(n);
    for (int i=0; i<n; i++) {
      dirPath.add((String)path.get(i));
    }
    if (n == 0) {
      throw new IllegalArgumentException("badPath");
    }
    Object name = dirPath.get(n-1);
    if (!(name instanceof String)) {
      throw new IllegalArgumentException("badPath");
    }
    dirPath.remove(n-1);
    Entry oldDir = currentDirEntry;
    try {
      newChangeDir(dirPath, relative);
      deleteEntry((String)name);
    } finally {
      changeDir(oldDir);
    }
  }

  /**
   * Deletes recursively an entry.
   * @param     e       The entry to delete.
   * @exception IOException     Thrown if the entry cannot be deleted.
   */
  public void deleteEntry(Entry e) throws IOException
  {
    Entry2 e2 = (Entry2)e;
    ((Entry2)(e2.parent)).deleteEntry(e2, this);
    modified = true;
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
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException(resources.getString("badEntry"));
    }
    Entry2 e = currentDirEntry.findEntry(name);
    if (e != null) {
      if (e.type == Entry.DIRECTORY) {
        currentDirEntry = e;
      }else{
        throw new IOException(
          resources.getString("notDir1") +
          name +
          resources.getString("notDir2")
        );
      }
    }else{
      throw new IOException(
        resources.getString("notFound1") +
        name +
        resources.getString("notFound2")
      );
    }
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
    int size = path.size();
    for (int i=0; i<size; i++) {
      if (path == null || !(path.get(i) instanceof String)) {
        throw new IllegalArgumentException(resources.getString("badPath"));
      }
    }
    Entry2 oldDir = currentDirEntry;
    try {
      if (!relative) {
        currentDirEntry = rootEntry;
      }
      int size2 = path.size();
      for (int i=0; i<size2; i++) {
        changeDir((String)(path.get(i)));
      }
    } catch (IOException e) {
      currentDirEntry = oldDir;
      throw e;
    }
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
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    if (newEntry == null) {
      throw new IllegalArgumentException(resources.getString("nullEntry"));
    }
    if (newEntry.type != Entry.DIRECTORY) {
      throw new IllegalArgumentException(
        resources.getString("notDir1") +
        newEntry.getName() +
        resources.getString("notDir2")
      );
    }else{
      currentDirEntry = (Entry2)newEntry;
    }
  }

  /**
   * Sets the current directory to the parent of the current directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToParentDir() throws IOException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notEmpty"));
    }
    if (currentDirEntry.parent != null) {
      currentDirEntry = (Entry2)(currentDirEntry.parent);
    }else{
      throw new IOException(resources.getString("rootDir"));
    }
  }

  /**
   * Sets the current directory to the root directory.
   * @exception IOException     Thrown if changing to the parent directory
   *                            fails.
   */
  public void changeToRootDir() throws IOException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    currentDirEntry = rootEntry;
  }

  /**
   * Returns the directory entry associated with the current directory.
   * @return    The requested entry.
   */
  public Entry getCurrentDirEntry()
  {
    if (raf == null) {
      return null;
    }else{
      return currentDirEntry;
    }
  }

  /**
   * Returns the directory entry associated with the root directory.
   * @return    The requested entry.
   */
  public Entry getRootEntry()
  {
    if (raf == null) {
      return null;
    }else{
      return rootEntry;
    }
  }

  /**
   * Returns a list of the entries in the current directory.
   * @return    A vector containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public Vector list()
  {
    if (raf == null) {
      return null;
    }else{
      return currentDirEntry.childrenAsVector();
    }
  }

  /**
   * Returns a list of the entries in the current directory.
   * @return    A list containing the entries in the current directory.
   *            The order of the entries is unspecified.
   */
  public AbstractList newList()
  {
    if (raf == null) {
      return null;
    }else{
      return currentDirEntry.childrenAsArrayList();
    }
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
    if (raf == null) {
      return null;
    }
    if (entry == null) {
      throw new IllegalArgumentException(resources.getString("nullEntry"));
    }
    if (entry.type != Entry.DIRECTORY) {
      throw new IllegalArgumentException(
        resources.getString("notDir1") +
        entry.getName() +
        resources.getString("notDir2")
      );
    }
    return ((Entry2)entry).childrenAsArrayList();
  }

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified string are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void setComment(String comment) throws IOException
  {
    setComment(comment.getBytes());
  }

  /**
   * Writes a comment in the comment block. Only the first 512 bytes of the
   * specified byte array are written.
   * @param     comment The comment to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void setComment(byte[] comment) throws IOException
  {
    if (raf == null) {
      return;
    }
    int size;
    if (comment.length <= BLOCKSIZE) {
      size = comment.length;
    }else{
      size = BLOCKSIZE;
    }
    synchronized(this) {
      long pos = raf.getFilePointer();
      goToBlock(COMMENTBLOCK);
      raf.write(comment, 0, size);
      if (size < BLOCKSIZE) {
        byte[] pad = new byte[BLOCKSIZE-size];
        for (int i=0; i<pad.length; i++) {
          pad[i] = (byte)0;
        }
        raf.write(pad);
      }
      raf.seek(pos);
    }
  }

  /**
   * Returns the contents of the comment block as a byte array.
   * @return    The contents of the comment block.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public byte[] getCommentBytes() throws IOException
  {
    if (raf == null) {
      return null;
    }
    byte[] comment = new byte[BLOCKSIZE];
    synchronized(this) {
      long pos = raf.getFilePointer();
      goToBlock(COMMENTBLOCK);
      raf.read(comment);
      raf.seek(pos);
    }
    return comment;
  }

  /**
   * Returns the contents of the comment block as a string. The string is
   * truncated at the first null character.
   * @return    The requested comment.
   * @exception IOException     Thrown if reading the comments fails.
   */
  public String getCommentString() throws IOException
  {
    if (raf == null) {
      return null;
    }
    byte[] comment = getCommentBytes();
    int i;
    for (i=0; i<BLOCKSIZE; i++) {
      if (comment[i] == (byte)0) {
        break;
      }
    }
    return new String(comment, 0, i);
  }

  /**
   * Returns the ratio of space in the structured file that is actually used
   * and the total space that the file takes on disk.
   * @return    A number between 0.0 (the file is completely empty) and 1.0
   *            (the file contains no unused space).
   */
  public double usedRatio()
  {
    int bitmapSize = bitmap.size();
    int nBlocks = bitmapSize * BITSPERBYTE;
    int usedBlocks = 0;
    for (int i=0; i<nBlocks; i++) {
      usedBlocks += getBit(i);
    }
    return (double)usedBlocks / (double)nBlocks;
  }

  /**
   * Returns the size of the structured file.
   * @return    The size of the structured file in bytes.
   * @exception IOException     Thrown if something goes wrong.
   */
  public long length() throws IOException
  {
    if (raf == null) {
      throw new IOException(resources.getString("notOpen"));
    }
    return raf.length();
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
    Entry2 oldDir = currentDirEntry;
    try {
      newChangeDir(path, relative);
      if (!file.isDirectory()) {
        internalCopyFile(file);
      }else{
        File[] files = file.listFiles();
        String dirName = file.getName();
        createDir(dirName);
        ArrayList<String> newPath = new ArrayList<String>();
        newPath.add(dirName);
        int nFiles = files.length;
        for (int i=0; i<nFiles; i++) {
          newCopyFile(files[i], newPath, RELATIVE_PATH);
        }
      }
      currentDirEntry = oldDir;
    } catch (IOException ioe) {
      currentDirEntry = oldDir;
      throw ioe;
    } catch (IllegalArgumentException iae) {
      currentDirEntry = oldDir;
      throw iae;
    }
  }

  /**
   * Copies the contents of a file to the current subdirectory.
   * @exception IOException     Thrown if the copying fails.
   */
  private void internalCopyFile(File file) throws IOException
  {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      bis = new BufferedInputStream(fis);
      Entry2 e = (Entry2)(createFile(file.getName()));
      StructOutputStream2 sos = new StructOutputStream2(this, e);
      bos = new BufferedOutputStream(sos);
      byte[] b = new byte[BLOCKSIZE];
      int n;
      while ((n = bis.read(b, 0, b.length)) >= 0 ){
        bos.write(b, 0, n);
      }
      bos.close();
      bos = null;
      bis.close();
      bis = null;
    } catch (IOException ioe) {
      try {
        if (bis != null) {
          bis.close();
        }
        if (bos != null) {
          bos.close();
        }
      } catch (IOException ioe2) {
      }
      throw ioe;
    }
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
    if (destination == null) {
      throw new IllegalArgumentException("nullDestination");
    }
    if (sourcePath == null) {
      throw new IllegalArgumentException("badPath");
    }

    if (destination instanceof StructFile) {
      destination = ((StructFile)destination).getStructFile();
    }

    Entry oldSourceDir = currentDirEntry;
    Entry oldDestDir = destination.getCurrentDirEntry();
    try {
      destination.changeDir(oldDestDir);
      try {
        destination.newChangeDir(destinationPath, relativeDestination);
      } catch (IOException ioe) {
        // Throw a more informative exception.
        throw new IOException(
          resources.getString("cantCdDest1") + listToPath(destinationPath) +
          resources.getString("cantCdDest2") + destination.getFile()
        );
      }
      Entry dest = destination.getCurrentDirEntry();
      int n = sourcePath.size();
      ArrayList<String> sp = new ArrayList<String>();
      for (int i=0; i<n; i++) {
        sp.add((String)sourcePath.get(i));
      }
      int last = n - 1;
      String lastFile = null;
      if (last >= 0) {
        lastFile = sp.get(last);
        sp.remove(last);
      }
      try {
        newChangeDir(sp, relativeSource);
      } catch (IOException ioe) {
        // Throw a more informative exception.
        throw new IOException(
          resources.getString("cantCdSrc1") + listToPath(sp) +
          resources.getString("cantCdSrc2") + getFile()
        );
      }
      if (lastFile != null) {
        Entry e = findEntry(lastFile);
        if (e != null) {
          if (e.type == Entry.FILE) {
            copySubEntry(e, destination, dest);
          }else{
            changeDir(e);
            Entry src = currentDirEntry;
            copySubEntry(src, destination, dest);
          }
        }
      }
    } finally {
      changeDir(oldSourceDir);
      destination.changeDir(oldDestDir);
    }
  }

  /**
   * Copies a subfile or subdirectory of the structured file to a subdirectory
   * of another structured file. The destination structured file can be the
   * same as the current structured file.
   * @param     src     The entry pointing to the subfile or subdirectory to
   *                    copy.
   * @param     destination     The structured file to which subfiles will be
   *                    copied.
   * @param     dest    The entry pointing to the destination subdirectory.
   * @exception IOException     Thrown if the copy operation fails.
   */
  private void copySubEntry(
    Entry src, AbstractStructFile destination, Entry dest)
    throws IOException
  {
    if (src.type == Entry.FILE) {
      internalCopySubFile(src, destination, dest);
    }else{
      Entry newDest;
      // The contents of the root subdirectory will be copied to the
      // destination subdirectory; the contents of other subdirectories will
      // be copied to subdirectories of the destination subdirectory with the
      // same names as in the source file.
      if (src.equals(rootEntry)) {
        newDest = dest;
      }else{
        destination.changeDir(dest);
        newDest = destination.createDir(src.getName());
      }
      changeDir(src);
      Entry2[] e = currentDirEntry.childrenAsArray();
      int n = e.length;
      for (int i=0; i<n; i++) {
        copySubEntry(e[i], destination, newDest);
      }
    }
  }

  /**
   * Copies the contents of a subfile of the structured file to the current
   * subdirectory of another structured file. The destination structured file
   * can be the same as the current structured file.
   * @exception IOException     Thrown if the copying fails.
   */
  private void internalCopySubFile(Entry src, AbstractStructFile destination,
                                   Entry dest)
    throws IOException
  {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      StructInputStream2 sis = new StructInputStream2(this, (Entry2)src);
      bis = new BufferedInputStream(sis);
      destination.changeDir(dest);
      Entry e = destination.createFile(src.getName());
      OutputStream sos;
      if (e instanceof Entry2) {
        sos = new StructOutputStream2((StructFile2)destination, (Entry2)e);
      }else{
        sos = null;
      }
      bos = new BufferedOutputStream(sos);
      byte[] b = new byte[BLOCKSIZE];
      int n;
      while ((n = bis.read(b, 0, b.length)) >= 0 ){
        bos.write(b, 0, n);
      }
      bos.close();
      bos = null;
      bis.close();
      bis = null;
    } catch (IOException ioe) {
      try {
        if (bis != null) {
          bis.close();
        }
        if (bos != null) {
          bos.close();
        }
      } catch (IOException ioe2) {
      }
      throw ioe;
    }
  }

  /**
   * Returns the version of the storage format used by the structured file.
   * @return    The version of the storage format used by the structured file.
   */
  public int getFormatVersion()
  {
    return 2;
  }

}
