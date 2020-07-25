package gr.cti.structfile;

import java.io.*;

/**
 * Interface that random file implementations for the structured file
 * management package must implement.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public interface StructRandomAccessFile
{
  /**
   * Reads a signed 32-bit integer from this file.
   * @return    The integer that was read.
   */
  public int readInt() throws IOException, EOFException;

  /**
   * Reads a signed 64-bit integer from this file.
   * @return    The integer that was read.
   */
  public long readLong() throws IOException, EOFException;

  /**
   * Reads in a string from this file. The string has been encoded using a
   * modified UTF-8 format.
   * @return    A Unicode string.
   */
  public String readUTF() throws IOException;

  /**
   * Reads <code>b.length</code> bytes of data from this file into an array of
   * bytes.
   * @return    <code>b.length</code>
   */
  public int read(byte b[]) throws IOException;

  /**
   * Reads up to <code>len</code> bytes of data from this file into an array
   * of bytes.
   * @param     b       The buffer into which the data are read.
   * @param     off     The start offset of the data.
   * @param     len     The maximum number of bytes read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   */
  public int read(byte b[], int off, int len) throws IOException;

  /**
   * Reads a byte from this file.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException;

  /**
   * Writes an <code>int</code> to the file as four bytes, high byte first.
   * @param     x       The <code>int</code> to write.
   */
  public void writeInt(int x) throws IOException;

  /**
   * Writes a <code>long</code> to the file as eight bytes, high byte first.
   * @param     x       The <code>long</code> to write.
   */
  public void writeLong(long x) throws IOException;

  /**
   * Writes a string to the file using UTF-8 encoding in a machine-independent
   * manner.
   * @param     str     A string to be written.
   */
  public void writeUTF(String str) throws IOException;

  /**
   * Writes <code>b.length</code> bytes from the specified byte array
   * to this file.
   * @param     b       The data.
   */
  public void write(byte b[]) throws IOException;

  /**
   * Writes <code>len</code> bytes from the specified byte array.
   * @param     b       The data.
   * @param     off     The start offset in the data.
   * @param     len     The number of bytes to write.
   */
  public void write(byte b[], int off, int len) throws IOException;

  /**
   * Writes a byte.
   * @param     b       The byte to write.
   */
  public void write(int b) throws IOException;

  /**
   * Close the file.
   */
  public void close() throws IOException;

  /**
   * Flush cache, if there is one.
   */
  public void flushCache();

  /**
   * Length of the file.
   * @return    The length of the file.
   */
  public long length() throws IOException;

  /**
   * Sets the file-pointer offset, measured from the beginning of this file,
   * at which the next read or write occurs.
   * @param     pos     The new file-pointer offset.
   */
  public void seek(long pos) throws IOException;

  /**
   * Returns the current offset in this file.
   * @return    The offset from the beginning of the file, in bytes, at which
   *            the next read or write occurs.
   */
  public long getFilePointer() throws IOException;

}
