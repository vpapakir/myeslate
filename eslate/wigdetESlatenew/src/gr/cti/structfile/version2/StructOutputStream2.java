package gr.cti.structfile.version2;

import java.io.*;

/**
 * This class implements an output stream associated with an entry
 * in a structured file. This stream is unbuffered.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 * @see         gr.cti.structfile.version2.StructFile2
 */
public class StructOutputStream2 extends OutputStream
{
  /**
   * The structured file on which the entry is stored.
   */
  private StructFile2 file;
  /**
   * The entry associated with the output stream.
   */
  Entry2 entry;
  /**
   * A one-byte buffer used by the <code>write()</code> method.
   */
  byte[] buf = new byte[1];

  /**
   * Create the output stream.
   * @param     f       The structured file on whose entry the stream
   *                    will be opened.
   * @param     e       The entry associated with the output stream.
   */
  public StructOutputStream2(StructFile2 f, Entry2 e) throws IOException
  {
    super();
    if (e.size != 0) {
      e.deallocateBlocks(f);
    }
    file = f;
    entry = e;
    file.openOut(entry);
  }

  /**
   * Close the output stream.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    super.close();
    file.closeOut(entry);
    file = null;
    entry = null;
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off<code> to the output stream.
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
  public void write(byte[] b, int off, int len)
    throws IOException, NullPointerException, IndexOutOfBoundsException
  {
    file.write(entry, b, off, len);
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off<code> to the output stream.
   * @param     b       The data.
   * @exception IOException     Thrown if the writing fails.
   * @exception NullPointerException    Thrown if <code>b</code> is
   *                                    <code>null</code>.
   */
  public void write(byte[] b)
    throws IOException, NullPointerException
  {
    file.write(entry, b, 0, b.length);
  }

  /**
   * Write a byte to the output stream.
   * @param     b       The byte to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void write(int b) throws IOException
  {
    buf[0] = (byte)b;
    file.write(entry, buf, 0, 1);
  }
}
