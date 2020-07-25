package gr.cti.structfile;

import java.io.*;
import java.util.*;

/**
 * Random access file wrapper for use with the structured file class. The
 * class adds an LRU cache to the random access file to make access faster.
 * Only those methods of RandomAccessFile that are actually
 * used by class StructFile are actually implemented.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.1, 6-Jul-2006
 */
public class RandomAccessFileLRUCache implements StructRandomAccessFile
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
   * One-byte buffer for the read() and write() methods.
   */
  private byte[] buf = new byte[1];

  /**
   * The size of an integer.
   */
  private final static int INTSIZE = 4;

  /**
   * The size of a long integer.
   */
  private final static int LONGSIZE = 8;

  /**
   * The cache.
   */
  private byte[] cache;

  /**
   * The cache index.
   */
  private HashMap<IntWrapper, BlockDescriptor> index;

  /**
   * Cache block size.
   */
  private final static int BLOCK_SIZE = 512;
  /**
   * The size of the cache in blocks. 32 appears to be optimal.
   */
  private final static int CACHE_SIZE = 32;

  /**
   * The descriptor of the current block in the cache.
   */
  private BlockDescriptor currentBlock;

  /**
   * An integer wrapper used to avoid creating new instances all the time.
   */
  private IntWrapper tmpInt;

  /**
   * The last block for which a seek was performed on the disk file.
   * Used to eliminate unnecessary seeks.
   */
  private int lastSeek = -1;

  /**
   * The list of free blocks in the cache.
   */
  private int[] freeBlocks;

  /**
   * The number of free blocks in the cache.
   */
  private int nFree;

  /**
   * The structure maintained so that the least recently used block can be
   * easily accessed.
   */
  private LRUStruct lruStruct;

  /**
   * The maximum position to which the file pointer has been set.
   */
  private long maxPos = -1L;

  /**
   * Creates a random access file stream to read from, and optionally to write
   * to, a file with the specified name. The mode argument must either be
   * equal to "r" or "rw", indicating either to open the file for input or for
   * both input and output.
   * @param     name    The system-dependent filename.
   * @param     mode    The access mode.
   */
  public RandomAccessFileLRUCache(String name, String mode)
    throws FileNotFoundException, IOException
  {
    raf = new RandomAccessFile(name, mode);
    updatePtr(0L);
    cache = new byte[BLOCK_SIZE * CACHE_SIZE];
    freeBlocks = new int[CACHE_SIZE];
    for (int i=0; i<CACHE_SIZE; i++) {
      freeBlocks[i] = CACHE_SIZE-i-1;
    }
    nFree = CACHE_SIZE;
    index = new HashMap<IntWrapper, BlockDescriptor>(CACHE_SIZE);
    tmpInt = new IntWrapper(0);
    lruStruct = new LRUStruct(CACHE_SIZE);
  }

  /**
   * Reads a signed 32-bit integer from this file.
   * @return    The integer that was read.
   */
  public int readInt() throws IOException, EOFException
  {
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
    int b1 = cache[n];
    if (b1 < 0) b1 += 256;
    int b2 = cache[n+1];
    if (b2 < 0) b2 += 256;
    int b3 = cache[n+2];
    if (b3 < 0) b3 += 256;
    int b4 = cache[n+3];
    if (b4 < 0) b4 += 256;
    int x = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;

    updatePtr(ptr + INTSIZE);
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
        return RandomAccessFileLRUCache.this.read();
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
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
    // We are assuming that we never read past the end of a block.
    int l = b.length;
    System.arraycopy(cache, n, b, off, len);
    updatePtr(ptr + l);
    return l;
  }

  /**
   * Reads a byte from this file.
   * @return    The read byte, or -1 if the end of the stream is reached.
   */
  public int read() throws IOException
  {
    int i = read(buf, 0, 1);
    if (i > 0) {
      return buf[0];
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
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
    byte b1 = (byte)((x >>> 24) & 0xFF);
    byte b2 = (byte)((x >>> 16) & 0xFF);
    byte b3 = (byte)((x >>> 8) & 0xFF);
    byte b4 = (byte)(x & 0xFF);
    cache[n] = b1;
    cache[n+1] = b2;
    cache[n+2] = b3;
    cache[n+3] = b4;
    updatePtr(ptr + INTSIZE);
    currentBlock.dirty = true;
  }

  /**
   * Writes a <code>long</code> to the file as eight bytes, high byte first.
   * @param     x       The <code>long</code> to write.
   */
  public void writeLong(long x) throws IOException
  {
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
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
    updatePtr(ptr + LONGSIZE);
    currentBlock.dirty = true;
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
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
    int l = b.length;
    System.arraycopy(b, 0, cache, n, l);
    updatePtr(ptr + l);
    currentBlock.dirty = true;
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array.
   * @param     b       The data.
   * @param     off     The start offset in the data.
   * @param     len     The number of bytes to write.
   */
  public void write(byte b[], int off, int len) throws IOException
  {
    seek(ptr);
    int n = (int)(currentBlock.offset + (ptr % BLOCK_SIZE));
    System.arraycopy(b, off, cache, n, len);
    updatePtr(ptr + len);
    currentBlock.dirty = true;
  }

  /**
   * Writes a byte.
   * @param     b       The byte to write.
   */
  public void write(int b) throws IOException
  {
    buf[0] = (byte)(b & 0xFF);
    write(buf, 0, 1);
  }

  /**
   * Close the file.
   */
  public void close() throws IOException
  {
    flushCache();
    raf.close();
    updatePtr(0L);
  }

  /**
   * Flush cache.
   */
  public void flushCache()
  {
    while (nFree != CACHE_SIZE) {
      freeCacheBlock();
    }
  }

  /**
   * Length of the file.
   * @return    The length of the file.
   */
  public long length() throws IOException
  {
    long l = Math.max(raf.length(), maxPos);
    return l;
  }

  /**
   * Sets the file-pointer offset, measured from the beginning of this file,
   * at which the next read or write occurs.
   * @param     pos     The new file-pointer offset.
   */
  public void seek(long pos) throws IOException
  {
    /*
    if (pos != ptr) {
      raf.seek(pos);
      updatePtr(pos);
    }
    */
    int block = (int)(pos / BLOCK_SIZE);
    try {
      cache(block);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    currentBlock = getBlockDescriptor(block);
    updatePtr(pos);
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
   * Updates the file pointer.
   * @param     pos     The new value of the file pointer.
   */
  private void updatePtr(long pos)
  {
    ptr = pos;
    if (maxPos < pos) {
      maxPos = pos;
    }
  }

//  /**
//   * Checks whether a given block is in the cache.
//   * @param     block   The block number.
//   * @return    True if yes, false if no.
//   */
//  private boolean inCache(int block)
//  {
//    tmpInt.n = block;
//    return index.containsKey(tmpInt);
//  }

  /**
   * Returns the block descriptor for a block in the cache.
   * @param     block   The block number.
   * @return The block descriptor for the given block.
   */
  private BlockDescriptor getBlockDescriptor(int block)
  {
    tmpInt.n = block;
    return index.get(tmpInt);
  }

  /**
   * Ensure that a block is in the cache.
   * @param     block   The block number.
   * @exception IOException     Thrown if accessing the file fails.
   */
  private void cache(int block) throws IOException
  {
    BlockDescriptor bd = getBlockDescriptor(block);
    if (bd == null) {
      int n = allocateCacheBlock(block);
      if (block != (lastSeek + 1)) {
        raf.seek(BLOCK_SIZE * block);
      }
      raf.read(cache, n, BLOCK_SIZE);
      lastSeek = block;
    }else{
      lruStruct.update(bd);
    }
  }

  /**
   * Allocates a block in the cache for a given file block.
   * @param     block   The number of the file block for which to allocate a
   *                    block in the cache.
   * @return    The offset of the allocated block in the cache.
   */
  private int allocateCacheBlock(int block)
  {
    if (nFree <= 0) {
      freeCacheBlock();
    }
    int cacheBlock = freeBlocks[--nFree];
    int offset = cacheBlock * BLOCK_SIZE;
    BlockDescriptor bd = new BlockDescriptor(block, offset);
    lruStruct.add(bd);
    IntWrapper i = new IntWrapper(block);
    index.put(i, bd);
    return offset;
  }

  /**
   * Deallocates a cache block, flushing its contents to disk if it has been
   * modified.
   */
  private void freeCacheBlock()
  {
    BlockDescriptor block = lruStruct.getLRU();
    lruStruct.remove(block);
    tmpInt.n = block.block;
    index.remove(tmpInt);
    freeBlocks[nFree++] = block.offset / BLOCK_SIZE;
    if (block.dirty) {
      try {
        raf.seek(BLOCK_SIZE * block.block);
        raf.write(cache, block.offset, BLOCK_SIZE);
        block.dirty = false;
        lastSeek = block.block;
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  /**
   * This class implements a file block descriptor for use with the cache.
   */
  private class BlockDescriptor
  {
    /**
     * The number of the block.
     */
    int block;

    /**
     * The offset of the block in the cache.
     */
    int offset;

    /**
     * Indicates whether the block is dirty, i.e., its contents are different
     * from those on disk.
     */
    boolean dirty;

    /**
     * The id of the block in the LRUStruct structure.
     */
    int id;
  
    /**
     * Construct a block descriptor.
     * @param   block   The number of the block.
     * @param   offset  The offset of the block in the cache.
     */
    BlockDescriptor(int block, int offset)
    {
      this.block = block;
      this.offset = offset;
      dirty = false;
    }

    /**
     * Returns a string representation of this object.
     * @return  A string representation of this object.
     */
    public String toString()
    {
      return
        "[BlockDescriptor: " + Integer.toString(block) + " / " +
        Integer.toString(offset) + ", " + dirty + "]";
    }
  }

  /**
   * This class implements a wrapper for ints with access to the wrapped
   * number.
   */
  private class IntWrapper
  {
    /**
     * The wrapped number.
     */
    private int n;

    /**
     * Construct a new wrapper.
     * @param   x       The nymber to wrap.
     */
    IntWrapper(int x)
    {
      n = x;
    }

    /**
     * Checks whether an object is equal to this object.
     * @param   obj     The object to check.
     */
    public boolean equals(Object obj)
    {
      if (obj == this) {
        return true;
      }
      if (obj instanceof IntWrapper) {
        return (n == ((IntWrapper)obj).n);
      }else{
        return false;
      }
    }

    /**
     * Returns a string representation of this object.
     * @return  A string representation of this object.
     */
    public String toString()
    {
      return Integer.toString(n);
    }

    /**
     * Returns a hash code value for the object.
     * @return  A hash code value for the object.
     */
    public int hashCode()
    {
      return n;
    }
  }

  /**
   * A structure maintained so that the least recently used block can be
   * easily accessed.
   */
  private class LRUStruct
  {
    /**
     * Previous element indices.
     */
    private int prev[];
    /**
     * Next element indices.
     */
    private int next[];
    /**
     * Block descriptors corresponding to each element.
     */
    private BlockDescriptor[] bd;
    /**
     * The first item in the structure that is in use.
     */
    private int first;
    /**
     * The last item in the structure that is in use.
     */
    private int last;
    /**
     * The first free item in the structure.
     */
    private int firstFree;

    /**
     * Prints the numbers of the blocks in the structure, first to last.
     */
    void print()
    {
      System.out.println("Forward:");
      for (int n=0,i=first; i>=0; n++,i=next[i]) {
        if (n >= bd.length) {
          System.out.print("\n*** Incorrect data structure");
          break;
        }
        if (bd[i] != null) {
          System.out.print(bd[i].block+"  ");
        }else{
          System.out.print("*NULL*  ");
        }
      }
      System.out.println();
    }

    /**
     * Prints the numbers of the blocks in the structure, last to first.
     */
    void printReverse()
    {
      System.out.println("Reverse:");
      for (int n=0,i=last; i>=0; n++,i=prev[i]) {
        if (n >= bd.length) {
          System.out.print("\n*** Incorrect data structure");
          break;
        }
        if (bd[i] != null) {
          System.out.print(bd[i].block+"  ");
        }else{
          System.out.print("*NULL*  ");
        }
      }
      System.out.println();
    }

    /**
     * Construct an LRUstruct.
     * @param   n       The number of items in the structure.
     */
    LRUStruct(int n)
    {
      prev = new int[n];
      next = new int[n];
      bd = new BlockDescriptor[n];
      for (int i=0; i<n; i++) {
        prev[i] = i - 1;
        next[i] = i + 1;
      }
      prev[0] = -1;
      next[n-1] = -1;
      first = -1;
      last = -1;
      firstFree = 0;
    }

    /**
     * Remove a block descriptor from the structure.
     * @param   bdesc   The block descriptor to remove.
     */
    void remove(BlockDescriptor bdesc)
    {
      int id = bdesc.id;
      int prv = prev[id];
      int nxt = next[id];
      if (prv >= 0) {
        next[prv] = nxt;
      }else{
        first = nxt;
      }
      if (nxt >= 0) {
        prev[nxt] = prv;
      }else{
        last = prv;
      }
      next[id] = firstFree;
      firstFree = id;
      prev[id] = -1;
      bd[id] = null;
    }

    /**
     * Add a block descriptor at the end of the structure.
     * @param   bdesc   The block descriptor to add.
     */
    void add(BlockDescriptor bdesc)
    {
      int id = firstFree;
      firstFree = next[firstFree];
      if (last >= 0) {
        next[last] = id;
      }
      next[id] = -1;
      prev[id] = last;
      last = id;
      if (first < 0) {
        first = id;
      }
      bdesc.id = id;
      bd[id] = bdesc;
    }

    /**
     * Update the usage of a block descriptor by placinf it last in the
     * structure.
     * @param   bdesc   The block descriptor to update.
     */
    void update(BlockDescriptor bdesc)
    {
      remove(bdesc);
      add(bdesc);
    }

    /**
     * Returns the descriptor of the least recently used block.
     * @return  The descriptor of the least recently used block.
     */
    BlockDescriptor getLRU()
    {
      return bd[first];
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
