package gr.cti.eslate.base;

import java.io.*;
import java.text.*;
import java.util.*;

import gr.cti.eslate.utils.*;
import gr.cti.typeArray.*;

/**
 * This class helps manage currency data, providing conversion and display
 * facilities. The methods of this class will only function properly under
 * Java 1.4 or later.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class CurrencyManager implements Externalizable
{
  /**
   * Euro.
   */
  public final static int EUR = 0;
  /**
   * German Mark.
   */
  public final static int DEM = 1;
  /**
   * Belgian Frank.
   */
  public final static int BEF = 2;
  /**
   * Italian Lira.
   */
  public final static int ITL = 3;
  /**
   * Dutch Guilder.
   */
  public final static int NLG = 4;
  /**
   * Finnish Markka.
   */
  public final static int FIM = 5;
  /**
   * French Franc.
   */
  public final static int FRF = 6;
  /**
   * Austrian Schilling.
   */
  public final static int ATS = 7;
  /**
   * Spanish Peseta.
   */
  public final static int ESP = 8;
  /**
   * Irish Punt.
   */
  public final static int IEP = 9;
  /**
   * Portuguese Escudo.
   */
  public final static int PTE = 10;
  /**
   * Greek Drachma.
   */
  public final static int GRD = 11;
  /**
   * Danish Krone.
   */
  public final static int DKK = 12;
  /**
   * Swedish Krona.
   */
  public final static int SEK = 13;
  /**
   * US Dollar.
   */
  public final static int USD = 14;
  /**
   * Australian Dollar.
   */
  public final static int AUD = 15;
  /**
   * Great Britain Pound.
   */
  public final static int GBP = 16;
  /**
   * Swiss Franc.
   */
  public final static int CHF = 17;
  /**
   * Japanese Yen.
   */
  public final static int JPY = 18;
  /**
   * Canadian Dollar.
   */
  public final static int CAD = 19;

  /**
   * The <code>CurrencyManager</code> instance used by the platform.
   */
  static CurrencyManager currencyManager = null;
  /**
   * The list of known currencies.
   */
  private IntBaseArray currencies = new IntBaseArray();
  /**
   * The list of known currency symbols.
   */
  private StringBaseArray symbols = new StringBaseArray();
  /**
   * The list of known currency exchange rates.
   */
  private DblBaseArray rates = new DblBaseArray();
  /**
   * The list of known currency fraction digits.
   */
  private IntBaseArray decimals = new IntBaseArray();
  /**
   * Used to format currency values.
   */
  private DecimalFormat df;
  /**
   * Listeners for exchange rate changed events.
   */
  private ArrayList<ExchangeRateListener> exchangeRateListeners =
    new ArrayList<ExchangeRateListener>();
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.base.CurrencyResource", Locale.getDefault()
  );

  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Save format version.
   */
  private final static int saveVersion = 1;

  // StorageStructure keys.
  private final static String CURRENCIES = "0";
  private final static String SYMBOLS = "1";
  private final static String RATES = "2";
  private final static String DECIMALS = "3";

  /**
   * Create a new <code>CurrencyManager</code> instance;
   * invoke when you need more than the one <code>CurrencyManager</code>
   * instance returned by <code>getCurrencyManager()</code>.
   */
  public CurrencyManager()
  {
    if (ESlate.runningOnJava14()) {
      String[] codes = new String[] {
        "EUR", "DEM", "BEF", "ITL", "NLG", "FIM", "FRF", "ATS", "ESP", "IEP",
        "PTE", "GRD", "DKK", "SEK", "USD", "AUD", "GBP", "CHF", "JPY", "CAD"
      };
      double[] rate = new double[]
      {
        1.0,      // EUR
        1.95583,  // DEM
        40.3399,  // BEF
        1936.27,  // ITL
        2.20371,  // NLG
        5.94573,  // FIM
        6.55957,  // FRF
        13.7603,  // ATS
        166.386,  // ESP
        0.787564, // IEP
        200.482,  // PTE
        340.750,  // GRD

        7.4265,   // DKK
        9.1458,   // SEK
        1.0805,   // USD
        1.7837,   // AUD
        0.69,     // GBP
        1.4948,   // CHF
        129.18,   // JPY
        1.5813    // CAD
      };
      int n = codes.length;
      for (int i=0; i<n; i++) {
        Currency c = Currency.getInstance(codes[i]);
        symbols.add(c.getSymbol());
        rates.add(rate[i]);
        decimals.add(c.getDefaultFractionDigits());
      }
      df = new DecimalFormat();
      df.setMinimumIntegerDigits(1);
    }
  }

  /**
   * Returns the <code>CurrencyManager</code> instance provided by the
   * platform. The platform does not actually use this instance, but
   * applications can use it so that they can all use the same currency
   * manager.
   * @return    The <code>CurrencyManager</code> instance provided by the
   *            platform.
   */
  public static CurrencyManager getCurrencyManager()
  {
    if (currencyManager == null) {
      currencyManager = new CurrencyManager();
    }
    return currencyManager;
  }

  /**
   * Adds a new currency to the list of known currencies.
   * @param     code            The
   * <A HREF="http://www.bsi-global.com/iso4217currency">ISO 4217</a></code>
   *                            code of the currency.
   * @param     exchangeRate    The number by which a value expressed in this
   *                            currency must be divided to convert it into
   *                            euros.
   * @return    A number identifying the new currency, or -1 if the provided
   *            <code>code</code> could not be recognized.
   */
  public int addCurrency(String code, double exchangeRate)
    throws IllegalArgumentException
  {
    if (!ESlate.runningOnJava14()) {
      return -1;
    }
    Currency c;
    try {
      c = Currency.getInstance(code);
    } catch (IllegalArgumentException e) {
      return -1;
    }
    int n = symbols.size();
    symbols.add(c.getSymbol());
    rates.add(exchangeRate);
    decimals.add(c.getDefaultFractionDigits());
    return n;
  }

  /**
   * Adds a new currency to the list of known currencies.
   * @param     symbol          The symbol of the currency.
   * @param     maxFractDigits  The maximum number of decimal digits with
   *                            which values expressed in this currency should
   *                            be printed.
   * @param     exchangeRate    The number by which a value expressed in this
   *                            currency must be divided to convert it into
   *                            euros.
   * @return    A number identifying the new currency.
   */

  public int addCurrency(String symbol, int maxFractDigits, double exchangeRate)
  {
    if (!ESlate.runningOnJava14()) {
      return -1;
    }
    int n = symbols.size();
    symbols.add(symbol);
    rates.add(exchangeRate);
    decimals.add(maxFractDigits);
    return n;
  }

  /**
   * Returns the symbol of a given currency.
   * @param     currency        The currency.
   * @return    The symbol of the given currency.
   */
  public String getSymbol(int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return "";
    }
    return symbols.get(currency);
  }

  /**
   * Changes the exchange rate of a given currency.
   * @param     currency        The currency.
   * @param     rate            The number by which a value expressed in this
   *                            currency must be divided to convert it into
   *                            euros.
   * @exception IllegalArgumentException        Thrown if
   * <code>currency</code> is <code>EURO</code>.
   */
  public void setExchangeRate(int currency, double rate)
  {
    if (!ESlate.runningOnJava14()) {
      return;
    }
    if (currency != EUR) {
      double oldRate = rates.get(currency);
      rates.set(currency, rate);
      exchangeRateChanged(currency, oldRate, rate);
    }else{
      throw new IllegalArgumentException(resources.getString("notEuro"));
    }
  }

  /**
   * Returns the exchange rate of a given currency.
   * @param     currency        The currency.
   * @return    The number by which a value expressed in this currency must
   *            be divided to convert it into euros.
   */
  public double getExchangeRate(int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return 1.0;
    }
    return rates.get(currency);
  }

  /**
   * Adds a listener for exchange rate changed events.
   * @param     listener        The listener to add.
   */
  public void addExchangeRateListener(ExchangeRateListener listener)
  {
    if (!ESlate.runningOnJava14()) {
      return;
    }
    synchronized (exchangeRateListeners) {
      if (!exchangeRateListeners.contains(listener)) {
        exchangeRateListeners.add(listener);
      }
    }
  }

  /**
   * Removes a listener for exchange rate changed events.
   * @param     listener        The listener to remove.
   */
  public void removeExchangeRateListener(ExchangeRateListener listener)
  {
    if (!ESlate.runningOnJava14()) {
      return;
    }
    synchronized (exchangeRateListeners) {
      int i = exchangeRateListeners.indexOf(listener);
      if (i >= 0) {
        exchangeRateListeners.remove(listener);
      }
    }
  }

  /**
   * Notifies registered listeners that the exchange rate for a particular
   * currency has changed.
   * @param     currency        The currency.
   * @param     oldRate         The old exchange rate.
   * @param     newRate         The new exchange rate.
   */
  @SuppressWarnings(value={"unchecked"})
  private void exchangeRateChanged(int currency, double oldRate, double newRate)
  {
    if (exchangeRateListeners.size() > 0) {
      ArrayList<ExchangeRateListener> listeners;
      synchronized (exchangeRateListeners) {
        listeners = (ArrayList<ExchangeRateListener>)exchangeRateListeners.clone();
      }
      ExchangeRateEvent e =
        new ExchangeRateEvent(this, currency, oldRate, newRate);
      int nListeners = listeners.size();
      for (int i=0; i<nListeners; i++) {
        ExchangeRateListener l = (ExchangeRateListener)listeners.get(i);
        l.exchangeRateChanged(e);
      }
    }
  }

  /**
   * Converts a value from one currency to another.
   * @param     value           The value to convert.
   * @param     currency1       The currency in which the value is expressed.
   * @param     currency2       The currency to which the value will be
   *                            converted.
   * @return    The converted value.
   */
  public double convert(double value, int currency1, int currency2)
  {
    if (!ESlate.runningOnJava14()) {
      return value;
    }
    if (currency1 != EUR) {
      value /= rates.get(currency1);
    }
    if (currency2 != EUR) {
      value *= rates.get(currency2);
    }
    return value;
  }

  /**
   * Converts a value to Euros. This is slightly faster than invoking
   * <code>convert(value, currency, CurrencyManager.EUR)</code>.
   * @param     value           The value to convert.
   * @param     currency        The currency in which the value is expressed.
   */
  public double convertToEuro(double value, int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return value;
    }
    if (currency != EUR) {
      value /= rates.get(currency);
    }
    return value;
  }

  /**
   * Converts a value from Euros to another currency. This is slightly faster
   * than invoking
   * <code>convert(value, CurrencyManager.EUR, currency)</code>.
   * @param     value           The value to convert.
   * @param     currency        The currency to which the value will be
   *                            converted.
   */
  public double convertFromEuro(double value, int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return value;
    }
    if (currency != EUR) {
      value *= rates.get(currency);
    }
    return value;
  }

  /**
   * Converts a value from one currency to another and returns an appropriate
   * string for printing.
   * @param     value           The value to convert.
   * @param     currency1       The currency in which the value is expressed.
   * @param     currency2       The currency to which the value will be
   *                            converted.
   * @return    A string displaying the converted value prefixed by the
   *            appropriate symbol.
   */
  public String format(double value, int currency1, int currency2)
  {
    if (!ESlate.runningOnJava14()) {
      return "";
    }
    if (currency1 != EUR) {
      value /= rates.get(currency1);
    }
    if (currency2 != EUR) {
      value *= rates.get(currency2);
    }
    df.setMaximumFractionDigits(decimals.get(currency2));
    StringBuffer s = new StringBuffer();
    s.append(symbols.get(currency2));
    s.append(' ');
    s.append(df.format(value));
    return s.toString();
  }

  /**
   * Converts a value from Euros to another currency and returns an appropriate
   * string for printing.
   * @param     value           The value to convert.
   * @param     currency        The currency to which the value will be
   *                            converted.
   * @return    A string displaying the converted value prefixed by the
   *            appropriate symbol.
   */
  public String formatFromEuro(double value, int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return "";
    }
    if (currency != EUR) {
      value *= rates.get(currency);
    }
    df.setMaximumFractionDigits(decimals.get(currency));
    StringBuffer s = new StringBuffer();
    s.append(symbols.get(currency));
    s.append(' ');
    s.append(df.format(value));
    return s.toString();
  }

  /**
   * Converts a value to Euros from another currency and returns an appropriate
   * string for printing.
   * @param     value           The value to convert.
   * @param     currency        The currency in which the value is expressed.
   * @return    A string displaying the converted value prefixed by the
   *            Euro symbol.
   */
  public String formatToEuro(double value, int currency)
  {
    if (!ESlate.runningOnJava14()) {
      return "";
    }
    if (currency != EUR) {
      value /= rates.get(currency);
    }
    df.setMaximumFractionDigits(decimals.get(EUR));
    StringBuffer s = new StringBuffer();
    s.append(symbols.get(EUR));
    s.append(' ');
    s.append(df.format(value));
    return s.toString();
  }

  /**
   * Save the currency manager's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    oo.writeObject(getState());
  }

  /**
   * Returns the state of the currency manager.
   * @return    The state of the currency manager.
   */
  StorageStructure getState()
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 4);
    if (ESlate.runningOnJava14()) {
      map.put(CURRENCIES, currencies);
      map.put(SYMBOLS, symbols);
      map.put(RATES, rates);
      map.put(DECIMALS, decimals);
    }
    return map;
  }

  /**
   * Load the currency manager's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)oi.readObject();
    setState(map);
  }

  /**
   * Sets the state of the currency manager.
   * @param     map     The state of the currencyManager.
   */
  void setState(StorageStructure map)
  {
    if (!ESlate.runningOnJava14()) {
      return;
    }
    DblBaseArray oldRates = rates;

    currencies = (IntBaseArray)map.get(CURRENCIES);
    symbols = (StringBaseArray)map.get(SYMBOLS);
    rates = (DblBaseArray)map.get(RATES);
    decimals = (IntBaseArray)map.get(DECIMALS);

    // Notify listeners for any exchange rate changes.
    int oldSize = oldRates.size();
    int newSize = rates.size();
    int n = Math.max(oldSize, newSize);
    for (int i=0; i<n; i++) {
      double oldRate;
      if (i < oldSize) {
        oldRate = oldRates.get(i);
      }else{
        oldRate = 0.0;
      }
      double newRate;
      if (i < newSize) {
        newRate = rates.get(i);
      }else{
        newRate = 0.0;
      }
      if (oldRate != newRate) {
        exchangeRateChanged(i, oldRate, newRate);
      }
    }
  }
}
