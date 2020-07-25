// Decompiled by Jad v1.5.7d. Copyright 2000 Pavel Kouznetsov.
// Jad home page: = http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)

package gr.cti.eslate.sharedObject;

import gr.cti.typeArray.StringBaseArray;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.database.engine.Table;
//import gr.cti.eslate.mapModel.csg3CatchImpl;
import java.util.Vector;
import java.util.ArrayList;

/**
 * Record shared object.
 *
 * @author      George Tsironis
 * @version     5.0.0, 19-May-2006
 */
public class RecordSO extends SharedObject
{

    public ArrayList getFieldTypes()
    {
        return fieldTypes;
    }

    public StringBaseArray getFieldNames()
    {
        return fieldNames;
    }

/*    public ArrayList getRecord()
    {
        return recordData;
    }
*/

    public int getRecordIndex() {
        return recordIndex;
    }

    @SuppressWarnings(value={"deprecation"})
    public void setFieldTypes(ArrayList fieldTypes, Vector vector)
    {
//        try
//        {
            this.fieldTypes = fieldTypes;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(this, vector);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }

    @SuppressWarnings(value={"deprecation"})
    public void setFieldTypes(ArrayList fieldTypes)
    {
//        try
//        {
            this.fieldTypes = fieldTypes;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(getHandle().getComponent(), this);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }

    @SuppressWarnings(value={"deprecation"})
    public void setFieldNames(StringBaseArray fieldNames, Vector vector)
    {
//        try
//        {
            this.fieldNames = fieldNames;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(this, vector);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }

    @SuppressWarnings(value={"deprecation"})
    public void setFieldNames(StringBaseArray fieldNames)
    {
//        try
//        {
            this.fieldNames = fieldNames;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(getHandle().getComponent(), this);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }

/*    public void setRecord(ArrayList recordData, Vector vector)
    {
//        try
//        {
            this.recordData = recordData;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(this, vector);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }
*/
    @SuppressWarnings(value={"deprecation"})
    public void setRecordIndex(int recIndex) {
        if (this.recordIndex == recIndex) return;
        recordIndex = recIndex;
        SharedObjectEvent sharedobjectevent = new SharedObjectEvent(getHandle().getComponent(), this);
        fireSharedObjectChanged(sharedobjectevent);
    }

/*    public void setRecord(ArrayList recordData)
    {
//        try
//        {
            this.recordData = recordData;
            SharedObjectEvent sharedobjectevent = new SharedObjectEvent(getHandle().getComponent(), this);
            fireSharedObjectChanged(sharedobjectevent);
            return;
//        }
//        catch(csg3CatchImpl _ex) { }
    }
*/
    public RecordSO(ESlateHandle eslatehandle)
    {
        super(eslatehandle);
        table = (Table) eslatehandle.getComponent();
    }

    public Table getTable() {
        return table;
    }

    private ArrayList fieldTypes;
    int recordIndex = -1;
    Table table = null;
    private StringBaseArray fieldNames;
}
