package gr.cti.eslate.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.*;
import javax.swing.*;

/**
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
public interface StorageStructure extends Cloneable
{
  public int getDataVersionID();

  public String getDataVersion();

  public void put(String key, boolean b);

  public void put(String key, byte b);

  public void put(String key, short s);

  public void put(String key, int i);

  public void put(String key, long l);

  public void put(String key, float f);

  public void put(String key, double d);

  public void put(String key, Object o);

  public Object get(String key);

  public boolean get(String key, boolean defValue);

  public byte get(String key, byte defValue);

  public short get(String key, short defValue);

  public int get(String key, int defValue);

  public long get(String key, long defValue);

  public float get(String key, float defValue);

  public double get(String key, double defValue);

  public String get(String key, String defValue);

  public Icon get(String key, Icon defValue);

  public Image get(String key, Image defValue);

  public Color get(String key, Color defValue);

  public Dimension get(String key, Dimension defValue);

  public Object get(String key, Object defValue);

  public boolean getBoolean(String key) throws KeyDoesntExistException;

  public byte getByte(String key) throws KeyDoesntExistException;

  public short getShort(String key) throws KeyDoesntExistException;

  public int getInteger(String key) throws KeyDoesntExistException;

  public long getLong(String key) throws KeyDoesntExistException;

  public float getFloat(String key) throws KeyDoesntExistException;

  public double getDouble(String key) throws KeyDoesntExistException;

  public String getString(String key) throws KeyDoesntExistException;

  public Icon getIcon(String key) throws KeyDoesntExistException;

  public Image getImage(String key) throws KeyDoesntExistException;

  public Color getColor(String key) throws KeyDoesntExistException;

  public Dimension getDimension(String key) throws KeyDoesntExistException;

  public boolean containsKey(String key);

  public void removeKey(String key);

  public Set keySet();

  public Enumeration keys();

  public Object clone();
}
