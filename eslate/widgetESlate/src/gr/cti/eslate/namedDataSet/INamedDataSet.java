// @created by Drossos Nicolas 1/2K
// Interface for Dynamic Named Structure
// Updated 30/10/2000
package gr.cti.eslate.namedDataSet;

//import com.sun.java.util.collections.*;
import java.util.*;
import gr.cti.eslate.namedDataSet.event.*;

public interface INamedDataSet extends Collection{

    final byte NUMERIC_TYPE = 1;
    final byte STRING_TYPE = 2;
    final byte ICON_TYPE = 3;
    final byte NO_TYPE = 4;

    public void setName(String name);
    public String getName();
    public Object get(int index);
    public byte getTypeForClass(Class typeClass);
    public void setDataType(Class type);
    public Class getDataType();

    public void addDataSetChangedListener(DataSetChangedListener dscl);
    public void removeDataSetChangedListener(DataSetChangedListener dscl);

}