package gr.cti.structfile;

import java.io.*;

import gr.cti.structfile.version2.*;

/**
 * This class implements an input stream associated with an entry
 * in a structured file. This is a wrapper for the various StructInputStream
 * implementations. Unlike the streams returned by these implementations,
 * this stream is buffered, with a sufficiently large buffer.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 6-Jul-2006
 * @see         gr.cti.structfile.version2.StructInputStream2
 */
public class StructInputStream extends InputStream
{
  /**
   * The encapsulated input stream.
   */
  private InputStream is;

  /**
   * Create the input stream.
   * @param     f       The structured file on whose entry the stream
   *                    will be opened.
   * @param     e       The entry to associate with the input stream.
   */
  public StructInputStream(StructFile f, Entry e) throws IOException
  {
    super();
    AbstractStructFile sf = f.getStructFile();
    if (sf instanceof StructFile2) {
      is = new StructInputStream2((StructFile2)sf, (Entry2)e);
      // Make the stream buffered, for speed; a 16K buffer seems to be
      // optimal.
      is = new BufferedInputStream(is, 16*1024);
    }else{
      is = null;
    }
  }

  /**
   * Close the input stream.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    is.close();
    is = null;
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
    return is.read(b, off, len);
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
    return is.read(b);
  }

  /**
   * Read a byte from the input stream.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException
  {
    return is.read();
  }

}
