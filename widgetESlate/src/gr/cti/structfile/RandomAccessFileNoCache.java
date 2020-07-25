package gr.cti.structfile;

import java.io.*;

/**
 * Random access file wrapper for use with the structured file class. The
 * class optimizes seeks by keeping track of the file pointer and skipping
 * unnecessary seeks. Only those methods of RandomAccessFile that are actually
 * used by class StructFile are actually implemented.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class RandomAccessFileNoCache implements StructRandomAccessFile
{
  /**
   * The random access file that this class handles.
   */
  private RandomAccessFile raf;

  /**
   * The cached file pointer.
   */
  private long ptr;

  /**
   * The size of an integer.
   */
  private final static int INTSIZE = 4;

  /**
   * The size of a long integer.
   */
  private final static int LONGSIZE = 8;

  /**
   * Creates a random access file stream to read from, and optionally to write
   * to, a file with the specified name. The mode argument must either be
   * equal to "r" or "rw", indicating either to open the file for input or for
   * both input and output.
   * @param     name    The system-dependent filename.
   * @param     mode    The access mode.
   */
  public RandomAccessFileNoCache(String name, String mode)
    throws FileNotFoundException, IOException
  {
    raf = new RandomAccessFile(name, mode);
    ptr = 0L;
  }

  /**
   * Wrapper for RandomAccessFile.readInt().
   */
  public int readInt() throws IOException, EOFException
  {
    int x = raf.readInt();
    ptr += INTSIZE;
    return x;
  }

  /**
   * Wrapper for RandomAccessFile.readLong().
   */
  public long readLong() throws IOException, EOFException
  {
    long x = raf.readLong();
    ptr += LONGSIZE;
    return x;
  }

  /**
   * Reads in a string from this file. The string has been encoded using a
   * modified UTF-8 format.
   * @return    A Unicode string.
   */
  public String readUTF() throws IOException
  {
    InputStream is = new InputStream() {
      public int read() throws IOException
      {
        return RandomAccessFileNoCache.this.read();
      }
    };
    DataInputStream dis = new DataInputStream(is);
    String str = dis.readUTF();
    dis.close();
    return str;
  }

  /**
   * Reads <code>b.length</code> bytes of data from this file into an array of
   * bytes.
   * @param     b       The buffer into which the data are read.
   * @return    <code>b.length</code>
   */
  public int read(byte b[]) throws IOException
  {
    return read(b, 0, b.length);
  }

  /**
   * Reads up to <code>len</code> bytes of data from this file into an array
   * of bytes.
   * @param     b       The buffer into which the data are read.
   * @param     off     The start offset of the data.
   * @param     len     The maximum number of bytes read.
   * @return    The total number of bytes read into the buffer, or -1 if there
   *            are no more data because the end of the file has been reached.
   */
  public int read(byte b[], int off, int len) throws IOException
  {
    int n = raf.read(b, off, len);
    if (n > 0) {
      ptr += n;
    }
    return n;
  }

  /**
   * Wrapper for RandomAccessFile.read().
   */
  public int read() throws IOException
  {
    int x = raf.read();
    if (x >= 0) {
      ptr++;
    }
    return x;
  }

  /**
   * Wrapper for RandomAccessFile.writeInt(int).
   */
  public void writeInt(int x) throws IOException
  {
    raf.writeInt(x);
    ptr += INTSIZE;
  }

  /**
   * Wrapper for RandomAccessFile.writeLong(long).
   */
  public void writeLong(long x) throws IOException
  {
    raf.writeLong(x);
    ptr += LONGSIZE;
  }

  /**
   * Writes a string to the file using UTF-8 encoding in a machine-independent
   * manner.
   * @param     str     A string to be written.
   */
  public void writeUTF(String str) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bos);
    dos.writeUTF(str);
    byte[] b = bos.toByteArray();
    dos.close();
    write(b);
  }

  /**
   * Wrapper for RandomAccessFile.write(byte[]).
   */
  public void write(byte b[]) throws IOException
  {
    write(b, 0, b.length);
  }

  /**
   * Wrapper for RandomAccessFile.write(byte[], int, int).
   */
  public void write(byte b[], int off, int len) throws IOException
  {
    raf.write(b, off, len);
    ptr += len;
  }

  /**
   * Wrapper for RandomAccessFile.write().
   */
  public void write(int b) throws IOException
  {
    raf.write(b);
    ptr++;
  }

  /**
   * Wrapper for RandomAccessFile.close().
   */
  public void close() throws IOException
  {
    raf.close();
    ptr = 0L;
  }

  /**
   * Flush cache. This method currently does nothing.
   */
  public void flushCache()
  {
  }

  /**
   * Wrapper for RandomAccessFile.length().
   */
  public long length() throws IOException
  {
    return raf.length();
  }

  /**
   * Wrapper for RandomAccessFile.seek(long).
   */
  public void seek(long pos) throws IOException
  {
    if (pos != ptr) {
      raf.seek(pos);
      ptr = pos;
    }
  }

  /**
   * Wrapper for RandomAccessFile.getFilePointer().
   */
  public long getFilePointer() throws IOException
  {
    return ptr;
  }

  /**
   * Sets the length of this file.
   * @param length      The desired length of the file.
   * @exception IOException     Thrown if an I/O error occurs.
   */
  public void setLength (long length) throws IOException
  {
    raf.setLength(length);
  }

}
