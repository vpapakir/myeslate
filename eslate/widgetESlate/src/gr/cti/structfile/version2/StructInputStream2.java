package gr.cti.structfile.version2;

import java.io.*;

/**
 * This class implements an input stream associated with an entry
 * in a structured file. This stream is unbuffered.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.version2.StructFile2
 */
public class StructInputStream2 extends InputStream
{
  /**
   * The structured file on which the entry is stored.
   */
  private StructFile2 file;
  /**
   * The entry associated with the input stream.
   */
  private Entry2 entry;
  /**
   * A one-byte buffer for the read() method.
   */
  private byte[] buf = new byte[1];

  /**
   * Create the input stream.
   * @param     f       The structured file on whose entry the stream
   *                    will be opened.
   * @param     e       The entry to associate with the input stream.
   */
  public StructInputStream2(StructFile2 f, Entry2 e) throws IOException
  {
    super();
    file = f;
    entry = e;
    file.openIn(entry);
  }

  /**
   * Close the input stream.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    super.close();
    file.closeIn(entry);
    file = null;
    entry = null;
  }

  /**
   * Reads up to <code>len</code> bytes of data from the input stream into an
   * array of bytes.
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
  public int read(byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    return file.read(entry, b, off, len);
  }

  /**
   * Reads up to <code>len</code> bytes of data from the input stream into an
   * array of bytes.
   * @param     b       The buffer into which the data are read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   * @exception IOException     Thrown if the reading fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   */
  public int read(byte[] b) throws IOException, NullPointerException
  {
    return file.read(entry, b, 0, b.length);
  }

  /**
   * Read a byte from the input stream.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException
  {
    int i = file.read(entry, buf, 0, 1);
    if (i > 0) {
      return (int)(buf[0]) & 0xFF;
    }else{
      return -1;
    }
  }

}
