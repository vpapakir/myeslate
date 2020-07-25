package gr.cti.structfile;

import java.io.*;

/**
 * This class implements an input stream that starts at a specified position
 * in a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class RAFInputStream extends InputStream
{
  /**
   * The random access file on which the stream is opened.
   */
  private StructRandomAccessFile raf;

  /**
   * Create a RAFInputStream.
   * @param     raf     The random access file on which the stream will be
   *                    opened.
   * @param     start   The position where the stream will start.
   * @exception IOEXception     Thrown if the creation fails.
   */
  public RAFInputStream(StructRandomAccessFile raf, long start)
    throws IOException
  {
    this.raf = raf;
    raf.seek(start);
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
    return raf.read(b, off, len);
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
    return raf.read(b);
  }

  /**
   * Reads a byte from the input stream.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException
  {
    return raf.read();
  }
}
