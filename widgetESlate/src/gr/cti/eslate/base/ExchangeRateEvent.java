package gr.cti.eslate.base;

/**
 * Event sent when an exchange rate for a particular currency changes.
 * The event's getSource() method will return the associated
 * <code>CurrencyManager</code> instance.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ExchangeRateEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The currency whose exchange rate has changed.
   */
  private int currency;
  /**
   * The old exchange rate.
   */
  private double oldRate;
  /**
   * The new exchange rate.
   */
  private double newRate;

  /**
   * Constructs an <code>ExchangeRateEvent</code> instance.
   * @param     cm              The associate <code>CurrencyManager</code>
   *                            instance. This will be the source of the event.
   * @param     currency        The currency whose exchange rate has changed.
   * @param     oldRate         The old exchange rate.
   * @param     newRate         The new exchange rate.
   */
  public ExchangeRateEvent(
    CurrencyManager cm, int currency, double oldRate, double newRate)
  {
    super(cm);
    this.currency = currency;
    this.oldRate = oldRate;
    this.newRate = newRate;
  }

  /**
   * Returns the currency whose exchange rate has changed.
   * @return    The currency whose exchange rate has changed.
   */
  public int getCurrency()
  {
    return currency;
  }

  /**
   * Returns the old exchange rate.
   * @return    The old exchange rate.
   */
  public double getOldRate()
  {
    return oldRate;
  }

  /**
   * Returns the new exchange rate.
   * @return    The new exchange rate.
   */
  public double getNewRate()
  {
    return newRate;
  }

}
