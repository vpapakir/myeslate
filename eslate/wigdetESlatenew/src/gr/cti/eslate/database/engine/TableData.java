package gr.cti.eslate.database.engine;

import gr.cti.typeArray.*;
import java.util.ArrayList;

/**
 * <p>Title: TableData</p>
 * <p>Description: Stores the data of a Table.</p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TableData extends ObjectBaseArray {
	Table table = null;
	byte[][] nullArray = new byte[0][0];

    public TableData(Table table) {
		this.table = table;
    }

/*	void addField(TableField fld) {
		Class fldType = fld.getFieldType();
		int recCount = table.recordCount+1;
		if (Double.class.isAssignableFrom(fldType)) {
			DblBaseArray arr = new DblBaseArray(recCount);
			for (int i=0; i<recordCount+1; i++)
			add(arr);
			for (int i=0; i<recCount; i++) {
//				arr.add(null);
			}
		}else if (Boolean.class.isAssignableFrom(fldType)) {
			add(new BoolBaseArray(recCount));
		}else{
			add(new ArrayList(recCount));
		}
	}
*/
}