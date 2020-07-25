package gr.cti.structfile;

import java.io.*;

/**
 * This class implements an output stream that starts at a specified position
 * in a structured file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class RAFOutputStream extends OutputStream
{
  /**
   * The random access file on which the stream is opened.
   */
  private StructRandomAccessFile raf;

  /**
   * Create a RAFOutputStream.
   * @param     raf     The random access file on which the stream will be
   *                    opened.
   * @param     start   The position where the stream will start.
   * @exception IOException     Thrown if the creation fails.
   */
  public RAFOutputStream(StructRandomAccessFile raf, long start)
    throws IOException
  {
    this.raf = raf;
    raf.seek(start);
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off<code> to the output stream.
   * @param     e       The entry to which the data will be written.
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
  public void write(Entry e, byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    raf.write(b, off, len);
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off<code> to the output stream.
   * @param     e       The entry to which the data will be written.
   * @param     b       The data.
   * @exception IOException     Thrown if the writing fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   */
  public void write(Entry e, byte[] b)
    throws IOException, NullPointerException
  {
    raf.write(b, 0, b.length);
  }

  /**
   * Writes a byte to the output stream.
   * @param     b       The byte to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void write(int b) throws IOException
  {
    raf.write(b);
  }

}
