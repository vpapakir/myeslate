package gr.cti.eslate.utils;

import java.io.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.*;
import javax.swing.*;

/**
 * Helper class that implements a structure mainly useful for storage.
 * Values of primitive types or objects are associated with keys. Its normal
 * use is to put all the properties of a component in the structure and save
 * it. After restoring the structure, two things are guaranteed:
 * <OL>
 * <LI>
 * That a saved condition is always restorable, and no Exceptions are thrown.
 * The user, using the <code>DataVersion</code> property can manipulate
 * multiple versions of saved data (condition) within its component, and
 * therefore make it safe for future versions as well as compatible to old
 * versions.
 * </LI>
 * <LI>
 * That the objects put in the structure are restored in the same way as they
 * were put, to guarantee that possible relations between objects are retained.
 * </LI>
 * </OL>
 * From version 1.5.65 and on (Class version 2.0.0) the structure checks
 * whether an object is Serializable or Externalizable before accepting it.
 * Furthermore, the structure prefers the Externalization methods to the
 * Serialization methods. This is to save from crashing the loading of a
 * class that has an unstable (concerning the serialVersionUID) predecessor.
 *
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 */
@SuppressWarnings(value={"unchecked"})
public class ESlateFieldMap2 implements Serializable, StorageStructure
{

  /**
   * The version of the <code>ESlateFieldMap</code> class. This property is
   * immutable and cannot be changed by the user.
   */
  private static final int CLASS_VERSION = 1;
  /**
   * The version of the data saved in this <code>ESlateFieldMap2</code> class.
   */
  private int dataVersion;
  /**
   * Cache argument, to save from creating when asking for a 0-argument
   * constructor.
   */
  private static final Class[] args = new Class[0];
  /**
   * Cached qualified externalizable classes. Determining whether a class
   * is a qualified externalizable class is expensive, and can slow down the
   * storage/retrieval of large microworlds. (In one example, there were
   * ~90000 invocations to isQualifiedExternalizable() for a total of only
   * 64 distinct classes!)
   */
  private static HashMap qualifiedExternalizableClasses = new HashMap();

  public static final long serialVersionUID = 12L;

  /**
   * The hash table data.
   */
  private transient Entry table[];

  /**
   * The total number of mappings in the hash table.
   */
  private transient int count;

  /**
   * The table is rehashed when its size exceeds this threshold.  (The
   * value of this field is (int)(capacity * loadFactor).)
   *
   * @serial
   */
  private int threshold;

  /**
   * The load factor for the hashtable.
   *
   * @serial
   */
  private float loadFactor;

  /**
   * The number of times this HashMap has been structurally modified
   * Structural modifications are those that change the number of mappings in
   * the HashMap or otherwise modify its internal structure (e.g.,
   * rehash).  This field is used to make iterators on Collection-views of
   * the HashMap fail-fast.  (See ConcurrentModificationException).
   */
  private transient int modCount = 0;
  private transient Set keySet = null;
  private static EmptyHashIterator emptyHashIterator = new EmptyHashIterator();

  public static int intCount, doubleCount, stringCount, floatCount, booleanCount, byteCount, charCount, longCount;
  public static int intArrayCount, doubleArrayCount, stringArrayCount, floatArrayCount;
  public static int booleanArrayCount, byteArrayCount, charArrayCount, longArrayCount, objectArrayCount;
  public static int fmCount, objCount, pre3VersionFmCount;


  public ESlateFieldMap2(int dataVersion, int initialCapacity, float loadFactor)
  {
    this.dataVersion = dataVersion;

    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Initial Capacity: " +
                                         initialCapacity);
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
      throw new IllegalArgumentException("Illegal Load factor: " +
                                         loadFactor);
    if (initialCapacity == 0)
      initialCapacity = 1;
    this.loadFactor = loadFactor;
    table = new Entry[initialCapacity];
    threshold = (int)(initialCapacity * loadFactor);
  }

  public ESlateFieldMap2()
  {
    this(0, 11, 0.75f);
  }

  /**
   * Creates an ESlateFieldMap with the specified <code>dataVersion</code>
   * and default <code>size</code> and <code>growth</code>.
   */
  public ESlateFieldMap2(int dataVersion)
  {
    this(dataVersion, 11, 0.75f);
  }

  /**
   * Creates an ESlateFieldMap with the specified <code>dataVersion</code>
   * and initial <code>size</code> and default <code>growth</code>.
   */
  public ESlateFieldMap2(int dataVersion, int initialCapacity)
  {
    this(dataVersion, initialCapacity, 0.75f);
  }

/*  public StorageStructure clone(StorageStructure ss) {
      Set keys = ss.keySet();
      ESlateFieldMap2 fm = new ESlateFieldMap2(ss.getDataVersionID(), keys.size());
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
          Object value
          fm.put((String) iter.next(), ss.);
      }
  }
*/

    public Object clone() {
        try {
            ESlateFieldMap2 t = (ESlateFieldMap2)super.clone();
            t.table = new Entry[table.length];
            for (int i = table.length ; i-- > 0 ; ) {
                t.table[i] = (table[i] != null)
                    ? (Entry)table[i].clone() : null;
            }
            t.keySet = null;
//          t.entrySet = null;
//            t.values = null;
            t.modCount = 0;
            t.count = count;
            t.dataVersion = dataVersion;
            return t;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

  /**
   * The <code>ClassVersion</code> is the version of the
   * <code>ESlateFieldMap</code> class. This is mostly used internally and
   * is immutable. This is <strong>NOT</strong> the same as the
   * <code>DataVersion</code>.
   * @see #getDataVersion()
   */
  public int getClassVersion()
  {
    return CLASS_VERSION;
  }

  /**
   * The <code>DataVersion</code> is user-defined. It is highly recommended
   * that the user properly sets this property correctly, to achieve correct
   * versioning of the saved data. One should change the data version when
   * there is a big change in the saved data.
   * The <code>DataVersion</code> will help the user know whether a saved
   * <code>ESlateFieldMap</code> is the new or an older version of his saved
   * data.
   * This method returns the String representation of <code>DataVersion</code>.
   * <code>ESlateFieldMap</code> uses Strings for versioning.
   * <p>
   * The property is immutable.
   */
  public String getDataVersion()
  {
    return String.valueOf(dataVersion);
  }

  /**
   * The <code>DataVersion</code> is user-defined. It is highly recommended
   * that the user properly sets this property correctly, to achieve correct
   * versioning of the saved data. One should change the data version when
   * there is a big change in the saved data.
   * The <code>DataVersion</code> will help the user know whether a saved
   * <code>ESlateFieldMap</code> is the new or an older version of his saved
   * data.<br>
   * <code>ESlateFieldMap</code> uses Strings for versioning. This method returns
   * the int representation of <code>DataVersion</code>, if this can be produced.
   * Otherwise it returns -1.
   * <p>
   * The property is immutable.
   */
  public int getDataVersionID()
  {
    return dataVersion;
  }

  /**
   * Puts a boolean to the structure and associates it with the
   * <code>key</code>.
   */
  public void put(String key, boolean value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new BooleanEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts a boolean to the structure and associates it with the
   * <code>key</code>.
   */
  public void put(String key, byte value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new ByteEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts a short to the structure and associates it with the <code>key</code>.
   */
  public void put(String key, short value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new ShortEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts an integer to the structure and associates it with the
   * <code>key</code>.
   */
  public void put(String key, int value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new IntEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts an integer to the structure and associates it with the
   * <code>key</code>.
   */
  public void put(String key, char value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new CharEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts a long to the structure and associates it with the <code>key</code>.
   */
  public void put(String key, long value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new LongEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts a float to the structure and associates it with the <code>key</code>.
   */
  public void put(String key, float value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new FloatEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts a double to the structure and associates it with the <code>key</code>.
   */
  public void put(String key, double value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
        }
      }
    } else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry e = new DoubleEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Puts an object to the structure and associates it with the
   * <code>key</code>. This method should not be used to store primitives.
   * <code>ESlateFieldMap</code> provides mechanisms that speed-up the
   * storage and retrieval of primitives. To make use of these mechanisms
   * the <code>put()</code> methods which accept primitive data types and
   * default values should be used.
   */
  public void put(String key, Object value)
  {
    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
          //                    Object old = e.value;
          //                    e.value = value;
          //                    return old;
        }
      }
    }
    else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
          //                    Object old = e.value;
          //                    e.value = value;
          //                    return old;
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }

    if (value == null)
      return ;
    checkStorable(value);

    // Creates the new entry.
    Entry e = new ObjectEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Special version of put(ket, object) which ommits to check if the object
   * is storable. It is used only by readObject().
   */
  void p(String key, Object value)
  {

    // Makes sure the key is not already in the HashMap.
    Entry tab[] = table;
    int hash = 0;
    int index = 0;

    if (key != null) {
      hash = key.hashCode();
      index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          removeKey(key);
          //                    Object old = e.value;
          //                    e.value = value;
          //                    return old;
        }
      }
    }
    else {
      for (Entry e = tab[0] ; e != null ; e = e.next) {
        if (e.key == null) {
          removeKey(key);
          //                    Object old = e.value;
          //                    e.value = value;
          //                    return old;
        }
      }
    }

    modCount++;
    if (count >= threshold) {
      // Rehash the table if the threshold is exceeded
      rehash();

      tab = table;
      index = (hash & 0x7FFFFFFF) % tab.length;
    }


    // Creates the new entry.
    Entry e = new ObjectEntry(hash, key, value, tab[index]);
    tab[index] = e;
    count++;
  }

  /**
   * Gets an object associated with the given <code>key</code>. If the key
   * corresponds to a value that is a primitive data type, the method will
   * return the respective class object.
   * This method should be avoided when retrieving primitives, cause it creates
   * the corresponding object for the primitive, which is both a waste of
   * time and memory. Use one of the other getters for primitives.
   * <p>
   * <em>e.g.,</em> <code>Boolean</code> <em>for</em> <code>boolean</code>.
   */
  public Object get(String key)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          //                    return e.value;
        }
    }
    else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null)
          entry = e;
      //                    return e.value;
    }

    Class entryClass = null;
    if (entry != null) {
      entryClass = entry.getClass();
      if (ObjectEntry.class.isAssignableFrom(entryClass))
        return ((ObjectEntry) entry).value;
      else if (IntEntry.class.isAssignableFrom(entryClass))
        return new Integer(((IntEntry) entry).value);
      else if (DoubleEntry.class.isAssignableFrom(entryClass))
        return new Double(((DoubleEntry) entry).value);
      else if (FloatEntry.class.isAssignableFrom(entryClass))
        return new Float(((FloatEntry) entry).value);
      else if (CharEntry.class.isAssignableFrom(entryClass))
        return new Character(((CharEntry) entry).value);
      else if (BooleanEntry.class.isAssignableFrom(entryClass))
        return new Boolean(((BooleanEntry) entry).value);
      else if (LongEntry.class.isAssignableFrom(entryClass))
        return new Long(((LongEntry) entry).value);
      else if (ShortEntry.class.isAssignableFrom(entryClass))
        return new Short(((ShortEntry) entry).value);
      else if (ByteEntry.class.isAssignableFrom(entryClass))
        return new Byte(((ByteEntry) entry).value);
    }
    return null;
  }

  /**
   * Gets a boolean associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a boolean, the method
   * returns the <code>defValue</code> instead.
   */
  public boolean get(String key, boolean defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && BooleanEntry.class.isAssignableFrom(entry.getClass()))
      return ((BooleanEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a byte associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a byte, the method
   * returns the <code>defValue</code> instead.
   */
  public byte get(String key, byte defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ByteEntry.class.isAssignableFrom(entry.getClass()))
      return ((ByteEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a short associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a short, the method
   * returns the <code>defValue</code> instead.
   */
  public short get(String key, short defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ShortEntry.class.isAssignableFrom(entry.getClass()))
      return ((ShortEntry) entry).value;
    return defValue;
  }

  /**
   * Gets an integer associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not an integer, the method
   * returns the <code>defValue</code> instead.
   */
  public int get(String key, int defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && IntEntry.class.isAssignableFrom(entry.getClass()))
      return ((IntEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a long associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a long, the method
   * returns the <code>defValue</code> instead.
   */
  public long get(String key, long defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && LongEntry.class.isAssignableFrom(entry.getClass()))
      return ((LongEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a float associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a float, the method
   * returns the <code>defValue</code> instead.
   */
  public float get(String key, float defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && FloatEntry.class.isAssignableFrom(entry.getClass()))
      return ((FloatEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a double associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a double, the method
   * returns the <code>defValue</code> instead.
   */
  public double get(String key, double defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && DoubleEntry.class.isAssignableFrom(entry.getClass()))
      return ((DoubleEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a String associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a String, the method
   * returns the <code>defValue</code> instead.
   */
  public String get(String key, String defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (String) ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets an Icon associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not an Icon, the method
   * returns the <code>defValue</code> instead.
   */
  public Icon get(String key, Icon defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Icon) ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets an Image associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not an Image, the method
   * returns the <code>defValue</code> instead.
   */
  public Image get(String key, Image defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Image) ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a Color associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a Color, the method
   * returns the <code>defValue</code> instead.
   */
  public Color get(String key, Color defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Color) ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a Dimension associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not a Dimension, the method
   * returns the <code>defValue</code> instead.
   */
  public Dimension get(String key, Dimension defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Dimension) ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets an Object associated with the given <code>key</code>. If the key
   * doesn't exist, or if its associated value is not an Object, the method
   * returns the <code>defValue</code> instead.
   */
  public Object get(String key, Object defValue)
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return ((ObjectEntry) entry).value;
    return defValue;
  }

  /**
   * Gets a boolean associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public boolean getBoolean(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && BooleanEntry.class.isAssignableFrom(entry.getClass()))
      return ((BooleanEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a byte associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public byte getByte(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ByteEntry.class.isAssignableFrom(entry.getClass()))
      return ((ByteEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a short associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public short getShort(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ShortEntry.class.isAssignableFrom(entry.getClass()))
      return ((ShortEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets an integer associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public int getInteger(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && IntEntry.class.isAssignableFrom(entry.getClass()))
      return ((IntEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a long associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public long getLong(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && LongEntry.class.isAssignableFrom(entry.getClass()))
      return ((LongEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a float associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public float getFloat(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && FloatEntry.class.isAssignableFrom(entry.getClass()))
      return ((FloatEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a double associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public double getDouble(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && DoubleEntry.class.isAssignableFrom(entry.getClass()))
      return ((DoubleEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a String associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public String getString(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (String) ((ObjectEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets an Icon associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public Icon getIcon(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Icon) ((ObjectEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets an Image associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public Image getImage(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Image) ((ObjectEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a Color associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public Color getColor(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Color) ((ObjectEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * Gets a Dimension associated with the given <code>key</code>. If the key
   * doesn't exist a <code>KeyDoesntExistException</code> is thrown.
   */
  public Dimension getDimension(String key) throws KeyDoesntExistException
  {
    Entry tab[] = table;
    Entry entry = null;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if ((e.hash == hash) && key.equals(e.key)) {
          entry = e;
          break;
        }
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null) {
          entry = e;
          break;
        }
    }

    if (entry != null && ObjectEntry.class.isAssignableFrom(entry.getClass()))
      return (Dimension) ((ObjectEntry) entry).value;
    throw new KeyDoesntExistException("Key " + key + " doesn't exist.");
  }

  /**
   * @return  <em>True</em>, if the structure contains the given
   * <code>key</code>.
   */
  public boolean containsKey(String key)
  {
    Entry tab[] = table;
    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;
      for (Entry e = tab[index]; e != null; e = e.next)
        if (e.hash == hash && key.equals(e.key))
          return true;
    } else {
      for (Entry e = tab[0]; e != null; e = e.next)
        if (e.key == null)
          return true;
    }

    return false;
  }

  /**
   * Removes a key and its associated value from the structure.
   */
  public void removeKey(String key)
  {
    Entry tab[] = table;

    if (key != null) {
      int hash = key.hashCode();
      int index = (hash & 0x7FFFFFFF) % tab.length;

      for (Entry e = tab[index], prev = null; e != null;
           prev = e, e = e.next) {
        if ((e.hash == hash) && key.equals(e.key)) {
          modCount++;
          if (prev != null)
            prev.next = e.next;
          else
            tab[index] = e.next;

          count--;
          //                    Object oldValue = e.value;
          //                    e.value = null;
          //                    return oldValue;
          return ;
        }
      }
    } else {
      for (Entry e = tab[0], prev = null; e != null;
           prev = e, e = e.next) {
        if (e.key == null) {
          modCount++;
          if (prev != null)
            prev.next = e.next;
          else
            tab[0] = e.next;

          count--;
          //                    Object oldValue = e.value;
          //                    e.value = null;
          //                    return oldValue;
          return ;
        }
      }
    }

  }

  /**
   * Removes all mappings from this map.
   */
  public void clear()
  {
    Entry tab[] = table;
    modCount++;
    for (int index = tab.length; --index >= 0; )
      tab[index] = null;
    count = 0;
  }

  private static void checkStorable(Object o)
  {
    if (!isQualifiedExternalizable(o.getClass()) &&
        !java.io.Serializable.class.isAssignableFrom(o.getClass())) {
      throw new RuntimeException(
        "Class " + o.getClass().getName() +
        " is neither Serializable, nor Externalizable."
      );
    }
  }


  /**
   * Checks if a class qualifies for Externalization or Serialization.
   */
  private static boolean isQualifiedExternalizable(Class cl)
  {
    boolean result;
    // First check if we already have information about this class in the
    // cache.
    Boolean b = (Boolean)(qualifiedExternalizableClasses.get(cl));
    if (b != null) {
      result = b.booleanValue();
    } else {
      // If not, perform the expensive check...
      try {
        result = java.io.Externalizable.class.isAssignableFrom(cl) &&
                 java.lang.reflect.Modifier.isPublic(cl.getModifiers()) &&
                 cl.getConstructor(args) != null;
      } catch (Throwable t) {
        result = false;
      }
      // ... and add the result to the cache.
      qualifiedExternalizableClasses.put(
        cl, (result ? Boolean.TRUE : Boolean.FALSE)
      );
    }
    return result;
  }

  /**
   * Serialization input.
   */
  private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
  {
    fmCount++;
    /*int fmClassVersion = */in.readInt();
    dataVersion = in.readInt();

    // Read in the length, threshold, and loadfactor
    loadFactor = in.readFloat();
    threshold = in.readInt();

    // Read the original length of the array and number of elements
    int origlength = in.readInt();
    int elements = in.readInt();

    // Compute new size with a bit of room 5% to grow but
    // No larger than the original size.  Make the length
    // odd if it's large enough, this helps distribute the entries.
    // Guard against the length ending up zero, that's not valid.
    int length = (int)(elements * loadFactor) + (elements / 20) + 3;
    if (length > elements && (length & 1) == 0)
      length--;
    if (origlength > 0 && length > origlength)
      length = origlength;

    table = new Entry[length];
    count = 0;

    // Read the number of elements and then all the key/value objects
    int et = 0; // entryType
    String key = null;
    try {
    for (; elements > 0; elements--) {
      key = (String) in.readObject();
      et = in.readInt();
      if (et == 1) {
        put(key, in.readBoolean());
      } else if (et == 2) {
        p(key, in.readObject());
      } else if (et == 3) {
        put(key, in.readDouble());
      } else if (et == 4) {
        put(key, in.readInt());
      } else if (et == 5) {
        put(key, in.readFloat());
      } else if (et == 6) {
        put(key, in.readShort());
      } else if (et == 7) {
        put(key, in.readByte());
      } else if (et == 8) {
        put(key, in.readChar());
      } else if (et == 9) {
        put(key, in.readLong());
      }
    }
    } catch (Throwable th) {
      RuntimeException re = new RuntimeException(
        "key = " + key +"  type = " + et,
        th
      );
      throw re;
    }
  }

  /**
   * Serialization output.
   */
  private void writeObject(ObjectOutputStream out) throws IOException
  {
    out.writeInt(CLASS_VERSION);
    out.writeInt(dataVersion);

    // Write out the threshold, loadfactor, and any hidden stuff
    out.writeFloat(loadFactor);
    out.writeInt(threshold);

    // Write out number of buckets
    out.writeInt(table.length);

    // Write out size (number of Mappings)
    out.writeInt(count);

    // Write out keys and values (alternating)
    for (int index = table.length - 1; index >= 0; index--) {
      Entry entry = table[index];

      while (entry != null) {
        out.writeObject(entry.key);
        if (BooleanEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(1);
          out.writeBoolean(((BooleanEntry) entry).value);
        } else if (ObjectEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(2);
          out.writeObject(((ObjectEntry) entry).value);
        } else if (DoubleEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(3);
          out.writeDouble(((DoubleEntry) entry).value);
        } else if (IntEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(4);
          out.writeInt(((IntEntry) entry).value);
        } else if (FloatEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(5);
          out.writeFloat(((FloatEntry) entry).value);
        } else if (ShortEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(6);
          out.writeShort(((ShortEntry) entry).value);
        } else if (ByteEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(7);
          out.writeByte(((ByteEntry) entry).value);
        } else if (CharEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(8);
          out.writeChar(((CharEntry) entry).value);
        } else if (LongEntry.class.isAssignableFrom(entry.getClass())) {
          out.writeInt(9);
          out.writeLong(((LongEntry) entry).value);
        }
        entry = entry.next;
      }
    }
  }


  /**
   * Returns an enumeration of the keys in this field map.
   * @return    The requested enumeration.
   */
  public Enumeration keys()
  {
    Enumeration enumer = new Enumeration() {
                         Entry entry = null;
                         int index = table.length;

                         public boolean hasMoreElements() {
                           Entry e = entry;
                           int i = index;
                           Entry t[] = table;
                           /* Use locals for faster loop iteration */
                           while (e == null && i > 0) {
                             e = t[--i];
                           }
                           entry = e;
                           index = i;
                           return e != null;
                         }
                         public Object nextElement() {
                           Entry et = entry;
                           int i = index;
                           Entry t[] = table;
                           /* Use locals for faster loop iteration */
                           while (et == null && i > 0) {
                             et = t[--i];
                           }
                           entry = et;
                           index = i;
                           if (et != null) {
                             Entry e = entry;
                             entry = e.next;
                             return e.key;
                           }
                           throw new NoSuchElementException("ESLateFieldMap Enumerator");
                         }
                       };
    return enumer;
  }

  /**
   * Returns a Set view of the keys contained in this field map. The Set is
   * backed by the field map, so changes to the field map are reflected in
   * the Set, and vice-versa. The Set supports element removal (which
   * removes the corresponding entry from the field map), but not element
   * addition.
   */
  public Set keySet()
  {
    if (keySet == null) {
      keySet = new AbstractSet() {
                 @Override
                 public Iterator iterator() {
                   return getHashIterator();
                 }
                 @Override
                 public int size() {
                   return count;
                 }
                 @Override
                 public boolean contains(Object o) {
                   return ESlateFieldMap2.this.containsKey((String)o);
                 }
                 @Override
                 public boolean remove(Object o) {
                   int oldSize = count;
                   ESlateFieldMap2.this.removeKey((String)o);
                   return count != oldSize;
                 }
                 @Override
                 public void clear() {
                   ESlateFieldMap2.this.clear();
                 }
               };
    }
    return keySet;
  }

  private Iterator getHashIterator()
  {
    if (count == 0) {
      return emptyHashIterator;
    } else {
      return new HashIterator();
    }
  }

  /**
   * Rehashes the contents of this map into a new <tt>HashMap</tt> instance
   * with a larger capacity. This method is called automatically when the
   * number of keys in this map exceeds its capacity and load factor.
   */
  private void rehash()
  {
    int oldCapacity = table.length;
    Entry oldMap[] = table;

    int newCapacity = oldCapacity * 2 + 1;
    Entry newMap[] = new Entry[newCapacity];

    modCount++;
    threshold = (int)(newCapacity * loadFactor);
    table = newMap;

    for (int i = oldCapacity ; i-- > 0 ;) {
      for (Entry old = oldMap[i] ; old != null ; ) {
        Entry e = old;
        old = old.next;

        int index = (e.hash & 0x7FFFFFFF) % newCapacity;
        e.next = newMap[index];
        newMap[index] = e;
      }
    }
  }

  private class HashIterator implements Iterator
  {
    Entry[] table = ESlateFieldMap2.this.table;
    int index = table.length;
    Entry entry = null;
    Entry lastReturned = null;

    /**
     * The modCount value that the iterator believes that the backing
     * List should have.  If this expectation is violated, the iterator
     * has detected concurrent modification.
     */
    private int expectedModCount = modCount;

    HashIterator()
    {}

    public boolean hasNext()
    {
      Entry e = entry;
      int i = index;
      Entry t[] = table;
      /* Use locals for faster loop iteration */
      while (e == null && i > 0)
        e = t[--i];
      entry = e;
      index = i;
      return e != null;
    }

    public Object next()
    {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      Entry et = entry;
      int i = index;
      Entry t[] = table;

      /* Use locals for faster loop iteration */
      while (et == null && i > 0)
        et = t[--i];

      entry = et;
      index = i;
      if (et != null) {
        Entry e = lastReturned = entry;
        entry = e.next;
        return e.key;
      }
      throw new NoSuchElementException();
    }

    public void remove()
    {
      if (lastReturned == null)
        throw new IllegalStateException();
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      Entry[] tab = ESlateFieldMap2.this.table;
      int index = (lastReturned.hash & 0x7FFFFFFF) % tab.length;

      for (Entry e = tab[index], prev = null; e != null;
           prev = e, e = e.next) {
        if (e == lastReturned) {
          modCount++;
          expectedModCount++;
          if (prev == null)
            tab[index] = e.next;
          else
            prev.next = e.next;
          count--;
          lastReturned = null;
          return ;
        }
      }
      throw new ConcurrentModificationException();
    }
  }

  private static class EmptyHashIterator implements Iterator
  {

    EmptyHashIterator()
    {}

    public boolean hasNext()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }

    public void remove()
    {
      throw new IllegalStateException();
    }

  }


  abstract class Entry
  {
    int hash;
    Object key;
    Entry next;

    abstract protected Object clone();
  }

  class IntEntry extends Entry
  {
    int value;

    public IntEntry(int hash, Object key, int value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new IntEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class BooleanEntry extends Entry
  {
    boolean value;

    public BooleanEntry(int hash, Object key, boolean value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new BooleanEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class ByteEntry extends Entry
  {
    byte value;

    public ByteEntry(int hash, Object key, byte value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new ByteEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class ShortEntry extends Entry
  {
    short value;

    public ShortEntry(int hash, Object key, short value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new ShortEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class DoubleEntry extends Entry
  {
    double value;

    public DoubleEntry(int hash, Object key, double value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new DoubleEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class LongEntry extends Entry
  {
    long value;

    public LongEntry(int hash, Object key, long value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new LongEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class FloatEntry extends Entry
  {
    float value;

    public FloatEntry(int hash, Object key, float value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
    protected Object clone() {
        return new FloatEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class CharEntry extends Entry
  {
    char value;

    public CharEntry(int hash, Object key, char value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new CharEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

  class ObjectEntry extends Entry
  {
    Object value;

    public ObjectEntry(int hash, Object key, Object value, Entry next)
    {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }

    protected Object clone() {
        return new ObjectEntry(hash, key, value, (next==null ? null : (Entry)next.clone()));
    }
  }

}
