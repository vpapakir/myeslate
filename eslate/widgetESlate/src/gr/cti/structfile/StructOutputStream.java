package gr.cti.structfile;

import java.io.*;

import gr.cti.structfile.version2.*;

/**
 * This class implements an output stream associated with an entry
 * in a structured file. This is a wrapper for the various StructOutputStream
 * implementations. Unlike the streams returned by these implementations,
 * this stream is buffered, with a sufficiently large buffer.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 6-Jul-2006
 * @see         gr.cti.structfile.version2.StructOutputStream2
 */
public class StructOutputStream extends OutputStream
{
  /**
   * The encapsulated output stream.
   */
  OutputStream os;

  /**
   * Create the output stream.
   * @param     f       The structured file on whose entry the stream
   *                    will be opened.
   * @param     e       The entry associated with the output stream.
   */
  public StructOutputStream(StructFile f, Entry e) throws IOException
  {
    super();
    AbstractStructFile sf = f.getStructFile();
    if (sf instanceof StructFile2) {
      os = new StructOutputStream2((StructFile2)sf, (Entry2)e);
      // Make the stream buffered, for speed; a 16K buffer was found to be
      // optimal for StructInputStream; we use the same size for
      // StructOutputStream as well.
      os = new BufferedOutputStream(os, 16*1024);
    }else{
      os = null;
    }
  }

  /**
   * Close the output stream.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    os.close();
    os = null;
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
    os.write(b, off, len);
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
    os.write(b);
  }

  /**
   * Write a byte to the output stream.
   * @param     b       The byte to write.
   * @exception IOException     Thrown if the writing fails.
   */
  public void write(int b) throws IOException
  {
    os.write(b);
  }
}
