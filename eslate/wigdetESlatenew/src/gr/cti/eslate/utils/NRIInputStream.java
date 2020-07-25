package gr.cti.eslate.utils;

import java.io.*;

/**
 * An input stream that encapsulates an object input stream, peeking at the
 * first few bytes so that it can identify whether the encapsulated stream
 * contains a new style <code>NewRestorableImageIcon</code> or not.
 * This is by no means a generic method; it assumes that the peeked data will
 * be read by the <code>read()</code> method, and not by one of the
 * <code>readXXX()</code> methods.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class NRIInputStream
  extends InputStream implements ObjectInput, DataInput
{
  /**
   * The encapsulated input stream.
   */
  private ObjectInputStream ois;
  /**
   * The length of the header that identifies the stream as a new format
   * <code>NewRestorableImageIcon</code>.
   */
  final static int HEADER_SIZE = 3;
  /**
   * The data that have been read to identify the 
   <code>NewRestorableImageIcon</code> format.
   */
  private int[] data = new int[HEADER_SIZE];
  /**
   * The index of the next byte in the <code>data</code> array that will be
   * returned by <code>read()</code>.
   */
  int currentByte = 0;
  /**
   * The number of bytes that have been read to identify the 
   * <code>NewRestorableImageIcon</code> format.
   */
  int totalBytes = 0;

  /**
   * Construct a <code>NRIInputStream</code> instance.
   * @param     s       The encapsulated object input stream.
   */
  public NRIInputStream(ObjectInputStream s)
  {
    try {
      ois = s;
      for (int i=0; i<HEADER_SIZE; i++) {
        int n = ois.read();
        if (n >= 0) {
          data[totalBytes++] = n;
        }else{
          break;
        }
      }
    }catch (IOException ioe) {
      totalBytes = 0;
    }
  }

  /**
   * Checks whether the encapsulated object input stream contains a
   * <code>NewRestorableImageIcon</code> in the new format.
   * @return    True if yes, false if no.
   */
  public boolean isNewNRIFormat()
  {
    byte[] bytes = new byte[totalBytes];
    for (int i=0; i<totalBytes; i++) {
      bytes[i] = (byte)(data[i]);
    }
    String s = new String(bytes);
    if (s.equals("NRI")) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Reads a byte.
   * @return    The byte read or -1 if the end of file has been reached.
   * @exception IOException     Thrown if an error occurs.
   */
  public int read() throws IOException
  {
    if (currentByte < totalBytes) {
      return data[currentByte++];
    }else{
      return ois.read();
    }
  }

  /**
   * Reads an array of bytes.
   * @param     b       The array.
   * @return    The number of bytes read, or -1 if the end of file has been
   *            reached.
   * @exception IOException     Thrown if an error occurs.
   */
  public int read(byte[] b) throws IOException
  {
    return read(b, 0, b.length);
  }

  /**
   * Reads an array of bytes.
   * @param     b       The array.
   * @param     off     The index of the first byte to be read.
   * @param     len     The maximum number of bytes to be read.
   * @return    The number of bytes read, or -1 if the end of file has been
   *            reached.
   * @exception IOException     Thrown if an error occurs.
   */
  public int read(byte[] b, int off, int len) throws IOException
  {
    int total = 0;
    for (int i=0; i<len; i++) {
      int n = read();
      if (n >= 0) {
        b[off+i] = (byte)n;
        total++;
      }else{
        if (total > 0) {
          return total;
        }else{
          return -1;
        }
      }
    }
    return total;
  }

  /**
   * Skip a number of bytes in the stream.
   * @param     n       The number of bytes to be skipped.
   * @return    The number of bytes skipped.
   * @exception IOException     Thrown if an error occurs.
   */
  public long skip(long n) throws IOException {
    if (currentByte + n <= totalBytes) {
      currentByte += n;
      return n;
    }else{
      int n0 = totalBytes - currentByte;
      n -= n0;
      currentByte = totalBytes;
      return n0 + ois.skip(n);
    }
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public Object readObject() throws ClassNotFoundException, IOException
  {
    return ois.readObject();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public void readFully(byte b[]) throws IOException
  {
    read(b);
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public void readFully(byte b[], int off, int len) throws IOException
  {
    read(b, off, len);
  }

  /**
   * Skip a number of bytes in the stream.
   * @param     n       The number of bytes to be skipped.
   * @return    The number of bytes skipped.
   * @exception IOException     Thrown if an error occurs.
   */
  public int skipBytes(int n) throws IOException
  {
    if (currentByte + n <= totalBytes) {
      currentByte += n;
      return n;
    }else{
      if (currentByte < totalBytes) {
        int n0 = totalBytes - currentByte;
        currentByte = totalBytes;
        return n0 + ois.skipBytes(n - n0);
      }else{
        return ois.skipBytes(n);
      }
    }
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public boolean readBoolean() throws IOException
  {
    return ois.readBoolean();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public byte readByte() throws IOException
  {
    return ois.readByte();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public int readUnsignedByte() throws IOException
  {
    return ois.readUnsignedByte();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public short readShort() throws IOException
  {
    return ois.readShort();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public int readUnsignedShort() throws IOException
  {
    return ois.readUnsignedShort();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public char readChar() throws IOException
  {
    return ois.readChar();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public int readInt() throws IOException
  {
    return ois.readInt();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public long readLong() throws IOException
  {
    return ois.readLong();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public float readFloat() throws IOException
  {
    return ois.readFloat();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public double readDouble() throws IOException
  {
    return ois.readDouble();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  @SuppressWarnings(value={"deprecation"})
  public String readLine() throws IOException
  {
    return ois.readLine();
  }

  /**
   * A wrapper for the corresponding method of the encapsulated object input
   * stream.
   */
  public String readUTF() throws IOException
  {
    return ois.readUTF();
  }

}
