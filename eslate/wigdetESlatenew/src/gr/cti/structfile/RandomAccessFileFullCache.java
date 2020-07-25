package gr.cti.structfile;

import java.io.*;

/**
 * Random access file wrapper for use with the structured file class. The
 * class caches the entire contents of the random access file in memory
 * to make access faster.
 * Only those methods of RandomAccessFile that are actually
 * used by class StructFile are actually implemented.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class RandomAccessFileFullCache implements StructRandomAccessFile
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
   * The cached contents of the file.
   */
  private byte[] cache = new byte[0];

  /**
   * The size of the cached copy of the file.
   */
  private int size = 0;

  /**
   * The maximum size of the cache.
   */
  private int maxSize = 0;;

  /**
   * Indicates whether the cached contents of the file are different from
   * those on disk.
   */
  boolean dirty = false;

  /**
   * Creates a random access file stream to read from, and optionally to write
   * to, a file with the specified name. The mode argument must either be
   * equal to "r" or "rw", indicating either to open the file for input or for
   * both input and output.
   * @param     name    The system-dependent filename.
   * @param     mode    The access mode.
   */
  public RandomAccessFileFullCache(String name, String mode)
    throws FileNotFoundException, IOException
  {
    raf = new RandomAccessFile(name, mode);
    int n = (int)(raf.length());
    ensureCapacity(n);
    raf.read(cache, 0, n);
    ptr = 0L;
  }

  /**
   * Reads a signed 32-bit integer from this file.
   * @return    The integer that was read.
   */
  public int readInt() throws IOException, EOFException
  {
    int n = (int)ptr;
    int b1 = cache[n];
    if (b1 < 0) b1 += 256;
    int b2 = cache[n+1];
    if (b2 < 0) b2 += 256;
    int b3 = cache[n+2];
    if (b3 < 0) b3 += 256;
    int b4 = cache[n+3];
    if (b4 < 0) b4 += 256;
    int x = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;

    ptr += INTSIZE;
    return x;
  }

  /**
   * Reads a signed 64-bit integer from this file.
   * @return    The integer that was read.
   */
  public long readLong() throws IOException, EOFException
  {
    long i1 = readInt();
    long i2 = readInt();
    long x = (i1 << 32) + i2;
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
        return RandomAccessFileFullCache.this.read();
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
    int n = (int)ptr;
    // We are assuming that we never read past the end of a block.
    int l = b.length;
    System.arraycopy(cache, n, b, off, len);
    ptr += l;
    return l;
  }

  /**
   * Reads a byte from this file.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException
  {
    if (ptr < cache.length) {
      int n = (int)ptr++;
      return cache[n];
    }else{
      return -1;
    }
  }

  /**
   * Writes an <code>int</code> to the file as four bytes, high byte first.
   * @param     x       The <code>int</code> to write.
   */
  public void writeInt(int x) throws IOException
  {
    int n = (int)ptr;
    ensureCapacity(n + INTSIZE);
    byte b1 = (byte)((x >>> 24) & 0xFF);
    byte b2 = (byte)((x >>> 16) & 0xFF);
    byte b3 = (byte)((x >>> 8) & 0xFF);
    byte b4 = (byte)(x & 0xFF);
    cache[n] = b1;
    cache[n+1] = b2;
    cache[n+2] = b3;
    cache[n+3] = b4;
    ptr += INTSIZE;
    dirty = true;
  }

  /**
   * Writes a <code>long</code> to the file as eight bytes, high byte first.
   * @param     x       The <code>long</code> to write.
   */
  public void writeLong(long x) throws IOException
  {
    int n = (int)ptr;
    ensureCapacity(n + LONGSIZE);
    byte b1 = (byte)((x >>> 56) & 0xFF);
    byte b2 = (byte)((x >>> 48) & 0xFF);
    byte b3 = (byte)((x >>> 32) & 0xFF);
    byte b4 = (byte)((x >>> 24) & 0xFF);
    byte b5 = (byte)((x >>> 24) & 0xFF);
    byte b6 = (byte)((x >>> 16) & 0xFF);
    byte b7 = (byte)((x >>> 8) & 0xFF);
    byte b8 = (byte)(x & 0xFF);
    cache[n] = b1;
    cache[n+1] = b2;
    cache[n+2] = b3;
    cache[n+3] = b4;
    cache[n+4] = b5;
    cache[n+5] = b6;
    cache[n+6] = b7;
    cache[n+7] = b8;
    ptr += LONGSIZE;
    dirty = true;
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
   * Writes <code>b.length</code> bytes from the specified byte array
   * to this file.
   * @param     b       The data.
   */
  public void write(byte b[]) throws IOException
  {
    int n = (int)ptr;
    int l = b.length;
    ensureCapacity(n + l);
    System.arraycopy(b, 0, cache, n, l);
    ptr += l;
    dirty = true;
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array.
   * @param     b       The data.
   * @param     off     The start offset in the data.
   * @param     len     The number of bytes to write.
   */
  public void write(byte b[], int off, int len) throws IOException
  {
    int n = (int)ptr;
    ensureCapacity(n + len);
    System.arraycopy(b, off, cache, n, len);
    ptr += len;
    dirty = true;
  }

  /**
   * Writes a byte.
   * @param     b       The byte to write.
   */
  public void write(int b) throws IOException
  {
    int n = (int)ptr++;
    ensureCapacity(n + 1);
    cache[n] = (byte)(b & 0xFF);
  }

  /**
   * Close the file.
   */
  public void close() throws IOException
  {
    flushCache();
    raf.close();
    ptr = 0L;
  }

  /**
   * Flush cache.
   */
  public void flushCache()
  {
    if (dirty) {
      try {
        raf.seek(0L);
        raf.write(cache, 0, size);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  /**
   * Length of the file.
   * @return    The length of the file.
   */
  public long length() throws IOException
  {
    return (long)size;
  }

  /**
   * Sets the file-pointer offset, measured from the beginning of this file,
   * at which the next read or write occurs.
   * @param     pos     The new file-pointer offset.
   */
  public void seek(long pos) throws IOException
  {
    ptr = pos;
  }

  /**
   * Returns the current offset in this file.
   * @return    The offset from the beginning of the file, in bytes, at which
   *            the next read or write occurs.
   */
  public long getFilePointer() throws IOException
  {
    return ptr;
  }

  /**
   * Ensures that the array in which the contents of the file are cahed has
   * at least the specified capacity.
   * @param     reqSize The requested size.
   */
  private void ensureCapacity(int reqSize)
  {
    if (reqSize > maxSize) {
      maxSize = Math.max(reqSize, maxSize + 100000);
      byte[] newCache = new byte[maxSize];
      System.arraycopy(cache, 0, newCache, 0, size);
      cache = newCache;
    }
    if (reqSize > size) {
      size = reqSize;
    }
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
