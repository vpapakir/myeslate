//8Nov1999//

package gr.cti.eslate.scripting;

public interface AsLogoNamesMemory
{
 public String[] getLogoNames(); //get all the named values' names

 public String getLogoValue(String name);             //get a value as a String, given its name
 public void setLogoValue(String name, String value); //set a "named value" given its name and its value (as its String representation)
}
