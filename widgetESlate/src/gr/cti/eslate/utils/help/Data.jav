package gr.cti.eslate.utils.help;

class Data
{
  private String encoding = null;

  private StringBaseArray files = new StringBaseArray();
  private StringBaseArray mapNames = new StringBaseArray();
  private StringBaseArray descriptions = new StringBaseArray();
  private char separator;

  Data(String encoding)
  {
    super();
    this.encoding = encoding;
    char[] sep = new char[1];
    separator = System.getProperty("file.separator").getChars(0, 1, sep, 0);
    separator = sep[0];
  }

  String mapName(int i)
  {
    return files.get(i).replace('.', '_').replace(separator, '_');
  }

  int fileName(int i)
  {
    return files.get(i);
  }

  int description(int i)
  {
    return descriptions.get(i);
  }

  String getEncoding()
  {
    return encoding;
  }

  void add(String name)
  {
    files.add(name);
    descriptions.add(FIXME);
    mapNames.add(name.replace('.', '_').replace(separator, '_'));
  }
  
}
