package gr.cti.eslate.base;

/**
 * This interface encapsulates the methods required for updating a progress
 * bar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ProgressBarInterface
{
  /**
   * Sets the value of the progress bar.
   * @param     value   The value to set.
   */
  public void setProgress(int value);

  /**
   * Gets the value of the progress bar.
   * @return    The requested value.
   */
  public int getProgress();

  /**
   * Gets the minimum value of the progress bar.
   * @return    The requested value.
   */
  public int getMinimum();

  /**
   * Gets the maximum value of the progress bar.
   * @return    The requested value.
   */
  public int getMaximum();

  /**
   * Sets the message displayed under the progress bar.
   * @param     message The message to display.
   */
  public void setMessage(String message);
}
