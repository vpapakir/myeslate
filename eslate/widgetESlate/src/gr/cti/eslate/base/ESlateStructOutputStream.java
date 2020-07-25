package gr.cti.eslate.base;

import java.io.*;

import gr.cti.structfile.*;

/**
 * This class extends the StructOutputStream class so that close() can be
 * called multiple times.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateStructOutputStream extends StructOutputStream
{
  /**
   * Indicates whether the stream is open.
   */
  private boolean open = true;

  /**
   * Create the input stream.
   * @param     f       The structured file on whose entry the stream
   *                    will be opened.
   * @param     e       The entry to associate with the input stream.
   */
  public ESlateStructOutputStream(StructFile f, Entry e) throws IOException
  {
    super(f, e);
  }

  /**
   * Close the input stream. This method can be invoked multiple times. The
   * first call will actually close the file, while subsequent calls will do
   * nothing.
   * @exception IOException     Thrown if the file cannot be closed.
   */
  public void close() throws IOException
  {
    if (open) {
      super.close();
      open = false;
    }
  }

}
