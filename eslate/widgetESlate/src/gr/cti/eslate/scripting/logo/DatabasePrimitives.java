package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.database.DBTable;
import gr.cti.eslate.database.Database;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.BooleanTableField;
import gr.cti.eslate.database.engine.CurrencyTableField;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.DateTableField;
import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.FloatTableField;
import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.IntegerTableField;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidLogicalExpressionException;
import gr.cti.eslate.database.engine.InvalidRecordIndexException;
import gr.cti.eslate.database.engine.LogicalExpression;
import gr.cti.eslate.database.engine.NoFieldsInTableException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.ObjectComparator;
import gr.cti.eslate.database.engine.RecordEntryStructure;
import gr.cti.eslate.database.engine.StringTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.TableNotExpandableException;
import gr.cti.eslate.database.engine.TimeTableField;
import gr.cti.eslate.database.engine.URLTableField;
import gr.cti.eslate.database.engine.UnableToAddNewRecordException;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.LogoWord;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;


public class DatabasePrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("DB.SETCELL", "pSETCELL", 3);
        registerPrimitive("DB.CELL", "pCELL", 2);
        registerPrimitive("DB.FIELD", "pFIELD", 1);
        registerPrimitive("DB.RECORD", "pRECORD", 1);
        registerPrimitive("DB.RECORDCOUNT", "pRECORDCOUNT", 0);
        registerPrimitive("DB.SETACTIVERECORD", "pSETACTIVERECORD", 1);
        registerPrimitive("DB.ACTIVERECORD", "pACTIVERECORD", 0);
        registerPrimitive("DB.CURTIME", "pTIME", 0);

        registerPrimitive("DB.SELECTRECORDS", "pSELECTRECORDS", 1);
        registerPrimitive("DB.ADDITIONALLYSELECTRECORD", "pADDITIONALLYSELECTRECORD", 1);
        registerPrimitive("DB.SELECTEDRECORDS", "pSELECTEDRECORDS", 0);
        registerPrimitive("DB.SELECTEDRECORDCOUNT", "pSELECTEDRECORDCOUNT", 0);
        registerPrimitive("DB.SELECT", "pSELECT", 1);
        registerPrimitive("DB.CLEARSELECTION", "pCLEARSELECTION", 0);
        registerPrimitive("DB.INVERTSELECTION", "pINVERTSELECTION", 0);
        registerPrimitive("DB.SELECTALL", "pSELECTALL", 0);
        registerPrimitive("DB.SFIELD", "pSFIELD", 1);

        registerPrimitive("DB.FIELDNAMES", "pFIELDNAMES", 0);

        registerPrimitive("DB.DMIN", "pDMIN", 1);
        registerPrimitive("DB.DMAX", "pDMAX", 1);
        registerPrimitive("DB.DSMALL", "pDSMALL", 2);
        registerPrimitive("DB.DBIG", "pDBIG", 2);
        registerPrimitive("DB.DRANK", "pDRANK", 2);
        registerPrimitive("DB.DSUM", "pDSUM", 1);
        registerPrimitive("DB.DPRODUCT", "pDPRODUCT", 1);
        registerPrimitive("DB.DAVERAGE", "pDAVERAGE", 1);
        registerPrimitive("DB.DGEOMEAN", "pDGEOMEAN", 1);
        registerPrimitive("DB.DVARIANCE", "pDVARIANCE", 1);
        registerPrimitive("DB.DSTDEV", "pDSTDEV", 1);
        registerPrimitive("DB.DCOUNT", "pDCOUNT", 1);
        registerPrimitive("DB.ADDRECORD", "pADDRECORD", 0);

/*        registerPrimitive("SUMLIST", "pSUMLIST", 1);
        registerPrimitive("PRODUCTLIST", "pPRODUCTLIST", 1);
        registerPrimitive("AVERAGE", "pAVERAGE", 1);
        registerPrimitive("GEOMEAN", "pGEOMEAN", 1);
        registerPrimitive("VARIANCE", "pVARIANCE", 1);
        registerPrimitive("STDEV", "pSTDEV", 1);
        registerPrimitive("MINLIST", "pMINLIST", 1);
        registerPrimitive("MAXLIST", "pMAXLIST", 1);
        registerPrimitive("MEDIAN", "pMEDIAN", 1);
*/
        
        //17Mar2004
        registerPrimitive("DB.ACTIVETABLENAME", "pACTIVETABLENAME", 0);
        registerPrimitive("DB.ADDEMPTYRECORD","pADDEMPTYRECORD",0);
        registerPrimitive("DB.ADDFIELD", "pADDFIELD", 2);
        registerPrimitive("DB.ADDCALCULATEDFIELD", "pADDCALCULATEDFIELD", 2);
        registerPrimitive("DB.REMOVEFIELD", "pREMOVEFIELD", 1);
        registerPrimitive("DB.REMOVERECORD", "pREMOVERECORD", 1);
        //18Mar2004
        registerPrimitive("DB.SETTABLENAME", "pSETTABLENAME", 1);
        registerPrimitive("DB.SETFIELDEDITABLE", "pSETFIELDEDITABLE", 2);

        myMachine=(MyMachine)machine;

        if (console != null)
            console.putSetupMessage("Loaded Database primitives");
    }


    public final LogoObject pTIME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        return new LogoWord(System.currentTimeMillis());
    }

    public final LogoObject pSETCELL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 4);
        testMinParams(aLogoObject, 3);

        String tableName = "", value, fieldName;
        int row;

        if (aLogoObject.length == 4) {
            tableName = aLogoObject[0].toString();
            row = aLogoObject[1].toInteger();
            fieldName = aLogoObject[2].toString();
            value = aLogoObject[3].toString();
        }else{
            row = aLogoObject[0].toInteger();
            fieldName = aLogoObject[1].toString();
            value = aLogoObject[2].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");

            Table ctable;
            if (aLogoObject.length == 4)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            if (row <= 0 || row > ctable.getRecordCount())
                throw new LanguageException("Invalid record number: " + row);

            int fieldIndex;
            try{
                fieldIndex = ctable.getFieldIndex(fieldName);
            }catch (InvalidFieldNameException e) {
                throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + tableName + "\"");
            }

            try{
                ctable.setCell(fieldIndex, dbTable.recordForRow(row-1), value);
            }catch (InvalidCellAddressException exc) {
                throw new LanguageException(exc.message);
            }catch (NullTableKeyException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidDataFormatException exc) {
                throw new LanguageException(exc.message);
            }catch (DuplicateKeyException exc) {
                throw new LanguageException(exc.message);
            }catch (AttributeLockedException exc) {
                throw new LanguageException(exc.getMessage());
            }
        }

        return LogoVoid.obj;
    }

    public final LogoObject pCELL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 2);

        String tableName = "", fieldName;
        int row;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            row = aLogoObject[1].toInteger();
            fieldName = aLogoObject[2].toString();
        }else{
            row = aLogoObject[0].toInteger();
            fieldName = aLogoObject[1].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName);
        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 3)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            if (row <= 0 || row > ctable.getRecordCount())
                throw new LanguageException("Invalid record number: " + row);

            int fieldIndex;
            try{
                fieldIndex = ctable.getFieldIndex(fieldName);
            }catch (InvalidFieldNameException e) {
                throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + tableName + "\"");
            }

            try{
                Object value = ctable.getCell(fieldIndex, dbTable.recordForRow(row-1));
                if (value == null) return new LogoWord("");
                if (value.getClass().equals(Boolean.class))
                    return new LogoWord(((Boolean) value).booleanValue());
                else if (value.getClass().equals(Integer.class))
                    return new LogoWord(((Integer) value).intValue());
                else if (value.getClass().equals(Double.class))
                    return new LogoWord(((Double) value).doubleValue());
                else
                    return new LogoWord(value.toString());
            }catch (InvalidCellAddressException exc) {
                throw new LanguageException(exc.message);
            }
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }

    public final LogoObject pFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                AbstractTableField fld = ctable.getTableField(fieldName);
                ArrayList fieldContents = fld.getDataArrayList();
//                ArrayList fieldContents = ctable.getField(fieldName);
//                TableField fld = ctable.getTableField(fieldName);

                LogoWord[] results = new LogoWord[fieldContents.size()];
                if (fld.getDataType().equals(Double.class)) {
                    for (int i=0; i<fieldContents.size(); i++)
                        results[i] = (fieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Double) fieldContents.get(i)).doubleValue());
                }else if (fld.getDataType().equals(Integer.class)) {
                    for (int i=0; i<fieldContents.size(); i++)
                        results[i] = (fieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Integer) fieldContents.get(i)).intValue());
                }else if (fld.getDataType().equals(Boolean.class)) {
                    for (int i=0; i<fieldContents.size(); i++)
                        results[i] = (fieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Boolean) fieldContents.get(i)).booleanValue());
                }else{
                    for (int i=0; i<fieldContents.size(); i++)
                        results[i] = (fieldContents.get(i) == null)? new LogoWord(""):new LogoWord(fieldContents.get(i).toString());
                }

                return new LogoList(results);

            }catch (InvalidFieldNameException exc) {
                throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + tableName + "\"");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "";
        int row;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            row = aLogoObject[1].toInteger();
        }else{
            row = aLogoObject[0].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                ArrayList recordContents = ctable.getRecord(dbTable.recordForRow(row-1));
                LogoWord[] results = new LogoWord[recordContents.size()];
//                System.out.println("Here1");
//1                IntBaseArray columnOrder = dbTable.getColumnOrder();
                for (int i=0; i<recordContents.size(); i++) {
//1                    int k = columnOrder.get(i);
                    int k = dbTable.getJTable().convertColumnIndexToView(i);
//                    System.out.println("i: " + i + ", k: " + k + ", value: " + recordContents.at(k));
                    Object value = recordContents.get(k);
                    if (value == null) {
                        results[i] = new LogoWord("");
                        continue;
                    }
                    if (value.getClass().equals(Boolean.class))
                        results[i] = new LogoWord(((Boolean) value).booleanValue());
                    else if (value.getClass().equals(Integer.class))
                        results[i] = new LogoWord(((Integer) value).intValue());
                    else if (value.getClass().equals(Double.class))
                        results[i] = new LogoWord(((Double) value).doubleValue());
                    else
                        results[i] = new LogoWord(value.toString());
                }

                return new LogoList(results);
            }catch (InvalidRecordIndexException exc) {
                throw new LanguageException("Invalid record index: " + row);
            }
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pRECORDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";

        if (aLogoObject.length == 1) {
            tableName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);
        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");


            return new LogoWord(ctable.getRecordCount());
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pACTIVERECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";

        if (aLogoObject.length == 1) {
            tableName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            return new LogoWord(ctable.rowForRecord(ctable.getActiveRecord())+1);

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pSETACTIVERECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "";
        int row;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            row = aLogoObject[1].toInteger();
        }else{
            row = aLogoObject[0].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            if (row <= 0 || row > ctable.getRecordCount())
                throw new LanguageException("Invalid record number: " + row);

            ctable.setActiveRecord(dbTable.recordForRow(row-1));
        }

        return LogoVoid.obj;
    }


    public final LogoObject pSELECTRECORDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "";
        int[] row;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();

            if (aLogoObject[1] instanceof LogoList) {
                LogoList ll = (LogoList) aLogoObject[1];
                row = new int[ll.length()];
                for (int i=1; i<=ll.length(); i++)
                    row[i-1] = ((LogoWord) ll.pick(i)).toInteger();
            }else{
                row = new int[1];
                row[0] = aLogoObject[1].toInteger();
            }
        }else{
            if (aLogoObject[0] instanceof LogoList) {
                LogoList ll = (LogoList) aLogoObject[0];
                row = new int[ll.length()];
                for (int i=1; i<=ll.length(); i++) {
                    row[i-1] = ((LogoWord) ll.pick(i)).toInteger();
                }
            }else{
                row = new int[1];
                row[0] = aLogoObject[0].toInteger();
            }
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            for (int k=0; k<row.length; k++)
                row[k] = dbTable.recordForRow(row[k]-1);

            ctable.setSelectedSubset(row);
        }
        return LogoVoid.obj;
    }


    public final LogoObject pADDITIONALLYSELECTRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "";
        int row;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            row = aLogoObject[1].toInteger();
        }else{
            row = aLogoObject[0].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            int rec = dbTable.recordForRow(row-1);

            ctable.addToSelectedSubset(rec);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSELECTEDRECORDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";

        if (aLogoObject.length == 1) {
            tableName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            IntBaseArray selectedSubset = ctable.getSelectedSubset();

            int[] selectedRows = new int[selectedSubset.size()];
            for (int i=0; i<selectedSubset.size(); i++)
                selectedRows[i] = selectedSubset.get(i); //((Integer) selectedSubset.at(i)).intValue();

            selectedRows = dbTable.rowsForRecords(selectedRows);

            LogoWord[] results = new LogoWord[selectedRows.length];
            for (int i=0; i<selectedRows.length; i++)
                results[i] = new LogoWord(selectedRows[i]+1);

            return new LogoList(results);
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pSELECTEDRECORDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";

        if (aLogoObject.length == 1) {
            tableName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");


            return new LogoWord(ctable.getSelectedSubset().size());
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "";
        String query;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            query = aLogoObject[1].toString();
        }else{
            query = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);
        Table firstTable = null;
        DBTable firstDBTable = null;

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            if (i==0) {
                firstTable = ctable;
                firstDBTable = dbTable;
            }

            try{
                LogicalExpression le = new LogicalExpression(ctable, query, LogicalExpression.NEW_SELECTION, true);
            }catch (InvalidLogicalExpressionException exc) {
                exc.printStackTrace();
                throw new LanguageException(exc.message);
            }
        }

        if (firstTable != null) {
            IntBaseArray selectedSubset = firstTable.getSelectedSubset();

            int[] selectedRows = new int[selectedSubset.size()];
            for (int i=0; i<selectedSubset.size(); i++)
                selectedRows[i] = selectedSubset.get(i); //((Integer) selectedSubset.at(i)).intValue();

            selectedRows = firstDBTable.rowsForRecords(selectedRows);

            LogoWord[] results = new LogoWord[selectedRows.length];
            for (int i=0; i<selectedRows.length; i++)
                results[i] = new LogoWord(selectedRows[i]+1);

            return new LogoList(results);
        }else
            return LogoVoid.obj;
    }


    public final LogoObject pFIELDNAMES(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";

        if (aLogoObject.length == 1)
            tableName = aLogoObject[0].toString();


        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            StringBaseArray fieldNames = ctable.getFieldNames();
            LogoWord[] results = new LogoWord[fieldNames.size()];
            String name;
            StringBuffer buff;
            int index, counter;
            for (int i=0; i<fieldNames.size(); i++) {
//1                name = (String) fieldNames.get(dbTable.getColumnOrder().get(i));
                name = (String) fieldNames.get(dbTable.getJTable().convertColumnIndexToView(i));
                counter = 0;
                /* Checking for blanks. Blank chars are prefixed with the '\' char.
                 */
                if ((index = name.indexOf(' ')) != -1) {
                    buff = new StringBuffer(name);
                    for (int k=index; k<buff.length(); k++) {
                        if (buff.charAt(k) == ' ') {
                            buff.insert(k, '\\');
                            k++;
                        }
                    }
                    name = buff.toString();
                }
                results[i] = new LogoWord(name);
            }

            return new LogoList(results);
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pCLEARSELECTION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";
        int[] row;

        if (aLogoObject.length == 1)
            tableName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ctable.resetSelectedSubset();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pSELECTALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";
        int[] row;

        if (aLogoObject.length == 1)
            tableName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ctable.selectAll();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pINVERTSELECTION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        String tableName = "";
        int[] row;

        if (aLogoObject.length == 1)
            tableName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ctable.invertSelection();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pSFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                AbstractTableField fld = ctable.getTableField(fieldName);
                ArrayList fieldContents = fld.getDataArrayList();
//                ArrayList fieldContents = ctable.getField(fieldName);
                ArrayList sFieldContents = new ArrayList();
                IntBaseArray selectedSet = ctable.getSelectedSubset();
                for (int k=0; k<selectedSet.size(); k++) {
                    sFieldContents.add(fieldContents.get(selectedSet.get(k))); //((Integer) selectedSet.at(k)).intValue()));
                }


                LogoWord[] results = new LogoWord[sFieldContents.size()];
                if (fld.getDataType().equals(Double.class)) {
                    for (int i=0; i<sFieldContents.size(); i++)
                        results[i] = (sFieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Double) sFieldContents.get(i)).doubleValue());
                }else if (fld.getDataType().equals(Integer.class)) {
                    for (int i=0; i<sFieldContents.size(); i++)
                        results[i] = (sFieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Integer) sFieldContents.get(i)).intValue());
                }else if (fld.getDataType().equals(Boolean.class)) {
                    for (int i=0; i<sFieldContents.size(); i++)
                        results[i] = (sFieldContents.get(i) == null)? new LogoWord(""):new LogoWord(((Boolean) sFieldContents.get(i)).booleanValue());
                }else{
                    for (int i=0; i<sFieldContents.size(); i++)
                        results[i] = (sFieldContents.get(i) == null)? new LogoWord(""):new LogoWord(sFieldContents.get(i).toString());
                }

                return new LogoList(results);

            }catch (InvalidFieldNameException exc) {
                throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + tableName + "\"");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDMIN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            return findMinMax(ctable, fieldName, criteria, true);

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDMAX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            return findMinMax(ctable, fieldName, criteria, false);

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDSMALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 4);
        testMinParams(aLogoObject, 2);

        String tableName = "", fieldName, criteria = null;
        int index;

        if (aLogoObject.length == 4) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            index = aLogoObject[2].toInteger();
            criteria = aLogoObject[3].toString();
        }else if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            index = aLogoObject[2].toInteger();
        }else{
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 3)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);
                if (valuesToWorkOn.size() == 0)
                    throw new LanguageException("No values to compare");

                if (index < 1 || index > valuesToWorkOn.size())
                    throw new LanguageException("Invalid order: " + index);

                AbstractTableField fld = ctable.getTableField(fieldName);
                ObjectComparator comparator = getComparator(fld, true);
                AbstractTableField.sort(valuesToWorkOn, comparator);

//                Sorting.sort(valuesToWorkOn, bp);
                return new LogoWord(valuesToWorkOn.get(index-1).toString());

            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDBIG(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 4);
        testMinParams(aLogoObject, 2);

        String tableName = "", fieldName, criteria = null;
        int index;

        if (aLogoObject.length == 4) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            index = aLogoObject[2].toInteger();
            criteria = aLogoObject[3].toString();
        }else if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            index = aLogoObject[2].toInteger();
        }else{
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 3)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);
                if (valuesToWorkOn.size() == 0)
                    throw new LanguageException("No values to compare");

                if (index < 1 || index > valuesToWorkOn.size())
                    throw new LanguageException("Invalid order: " + index);

                AbstractTableField fld = ctable.getTableField(fieldName);
                ObjectComparator comparator = getComparator(fld, false);
//                BinaryPredicate bp = getComparator(fld, false);

                AbstractTableField.sort(valuesToWorkOn, comparator);
//                Sorting.sort(valuesToWorkOn, bp);
                return new LogoWord(valuesToWorkOn.get(index-1).toString());

            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDRANK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 4);
        testMinParams(aLogoObject, 2);

        String tableName = "", fieldName, criteria = null;
        int rowIndex;

        if (aLogoObject.length == 4) {
            tableName = aLogoObject[0].toString();
            rowIndex = aLogoObject[1].toInteger();
            fieldName = aLogoObject[2].toString();
            criteria = aLogoObject[3].toString();
        }else if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            rowIndex = aLogoObject[1].toInteger();
            fieldName = aLogoObject[2].toString();
        }else{
            rowIndex = aLogoObject[0].toInteger();
            fieldName = aLogoObject[1].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 3)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            if (rowIndex < 1 || rowIndex > ctable.getRecordCount())
                throw new LanguageException("Invalid row number: " + rowIndex);

            try{
                Object value = ctable.getCell(fieldName, dbTable.recordForRow(rowIndex-1));
                if (value == null)
                    throw new LanguageException("No value for record " + rowIndex + " in field " + fieldName);

                ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

                if (valuesToWorkOn.size() == 0)
                    throw new LanguageException("No values to rank record " + rowIndex + " against");

                AbstractTableField fld = ctable.getTableField(fieldName);
                ObjectComparator comparator = getComparator(fld, true);
                ObjectBaseArray uniqueValues = getUniqueValues(valuesToWorkOn, comparator);

                return new LogoWord(uniqueValues.indexOf(value)+1);
            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidCellAddressException exc) {
                throw new LanguageException(exc.message);
            }
        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pSUMLIST(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("SUMLIST accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double sum = 0;
        for (int i=1; i<=ll.length(); i++)
            sum = sum + ll.pick(i).toNumber();

        return new LogoWord(sum);
    }


    public final LogoObject pPRODUCTLIST(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("PRODUCTLIST accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double product = 1;
        for (int i=1; i<=ll.length(); i++)
            product = product * ll.pick(i).toNumber();

        return new LogoWord(product);
    }


    public final LogoObject pAVERAGE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("AVERAGE accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        double sum = 0;
        for (int i=1; i<=ll.length(); i++)
            sum = sum + ll.pick(i).toNumber();

        return new LogoWord(sum/ll.length());
    }


    public final LogoObject pGEOMEAN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("GEOMEAN accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double product = 1;
        for (int i=1; i<=ll.length(); i++)
            product = product * ll.pick(i).toNumber();

        return new LogoWord(Math.pow(product, (1d/ll.length())));
    }


    public final LogoObject pVARIANCE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("VARIANCE accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        double sum = 0;
        double squareSum = 0;
        double tmp;
        for (int i=1; i<=elementCount; i++) {
            tmp = ll.pick(i).toNumber();
            sum = sum + tmp;
            squareSum = squareSum + (tmp * tmp);
        }

        double elementCountd = new Integer(elementCount).doubleValue();
        return new LogoWord( ( ( elementCountd*squareSum ) - ( sum*sum ) ) / ( ( elementCountd * ( elementCountd - 1d ) ) ) );
    }


    public final LogoObject pMINLIST(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("MINLIST accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        try{
            double min = ll.pick(1).toNumber();
            for (int i=2; i<=elementCount; i++) {
                if (min > ll.pick(i).toNumber())
                    min = ll.pick(i).toNumber();
            }
            return new LogoWord(min);
        }catch (LanguageException exc) {
            String min = ll.pick(1).toString();
            for (int i=2; i<=elementCount; i++) {
                if (min.compareTo(ll.pick(i).toString()) > 0)
                    min = ll.pick(i).toString();
            }
            return new LogoWord(min);
        }
    }


    public final LogoObject pMAXLIST(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("MAXLIST accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        try{
            double max = ll.pick(1).toNumber();
            for (int i=2; i<=elementCount; i++) {
                if (max < ll.pick(i).toNumber())
                    max = ll.pick(i).toNumber();
            }
            return new LogoWord(max);
        }catch (LanguageException exc) {
            String max = ll.pick(1).toString();
            for (int i=2; i<=elementCount; i++) {
                if (max.compareTo(ll.pick(i).toString()) < 0)
                    max = ll.pick(i).toString();
            }
            return new LogoWord(max);
        }
    }


    public final LogoObject pMEDIAN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("MEDIAN accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        if (elementCount % 2 == 1)
            return ll.pick(elementCount/2 + 1);
        else
            return new LogoWord(( ll.pick(elementCount/2).toNumber() + ll.pick(elementCount/2 + 1).toNumber() ) / 2);
    }


    public final LogoObject pSTDEV(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("STDEV accepts only lists as argument");

        LogoWord lw = (LogoWord) pVARIANCE(interpenviron, aLogoObject);
        return new LogoWord(Math.sqrt(lw.toNumber()));
    }


    public final LogoObject pDSUM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                double sum = 0;
                for (int i=0; i<valuesToWorkOn.size(); i++)
                    sum = sum + ((Number) valuesToWorkOn.get(i)).doubleValue();

                return new LogoWord(sum);
            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDPRODUCT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                double product = 1;
                for (int i=0; i<valuesToWorkOn.size(); i++)
                    product = product * ((Number) valuesToWorkOn.get(i)).doubleValue();

                return new LogoWord(product);
            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDAVERAGE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                double sum = 0;
                for (int i=0; i<valuesToWorkOn.size(); i++)
                    sum = sum + ((Number) valuesToWorkOn.get(i)).doubleValue();

                return new LogoWord(sum / valuesToWorkOn.size());
            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDGEOMEAN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                double product = 1;
                for (int i=0; i<valuesToWorkOn.size(); i++)
                    product = product * ((Number) valuesToWorkOn.get(i)).doubleValue();

                return new LogoWord(Math.pow(product, (1d/valuesToWorkOn.size())));

            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDVARIANCE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                int elementCount = valuesToWorkOn.size();
                double sum = 0;
                double squareSum = 0;
                double tmp;
                for (int i=0; i<elementCount; i++) {
                    tmp = ((Number) valuesToWorkOn.get(i)).doubleValue();
                    sum = sum + tmp;
                    squareSum = squareSum + (tmp * tmp);
                }

                double elementCountd = new Integer(elementCount).doubleValue();
                return new LogoWord( ( ( elementCountd*squareSum ) - ( sum*sum ) ) / ( ( elementCountd * ( elementCountd - 1d ) ) ) );
            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDSTDEV(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        String tableName = "", fieldName, criteria = null;

        if (aLogoObject.length == 3) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            fieldName = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length >= 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

            try{
                int elementCount = valuesToWorkOn.size();
                double sum = 0;
                double squareSum = 0;
                double tmp;
                for (int i=0; i<elementCount; i++) {
                    tmp = ((Number) valuesToWorkOn.get(i)).doubleValue();
                    sum = sum + tmp;
                    squareSum = squareSum + (tmp * tmp);
                }

                double elementCountd = new Integer(elementCount).doubleValue();
                return new LogoWord( Math.sqrt( ( ( elementCountd*squareSum ) - ( sum*sum ) ) / ( ( elementCountd * ( elementCountd - 1d ) ) ) ) );
            }catch (Exception exc) {
                throw new LanguageException("Number expected");
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }


    public final LogoObject pDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String tableName = "", criteria = null;

        if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            criteria = aLogoObject[0].toString();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

        try{
            Database dbComponent = (Database) v.firstElement();
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            Table ctable;
            if (aLogoObject.length == 2)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                LogicalExpression le = new LogicalExpression(ctable, criteria, LogicalExpression.NEW_SELECTION, false);
                return new LogoWord(le.getQueryResults().size());
            }catch (InvalidLogicalExpressionException exc) {
                throw new LanguageException(exc.message);
            }

        }catch (NoSuchElementException exc) {
            throw new LanguageException("There is no Database to TELL this to");
        }
    }

    public final LogoObject pADDRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 0);

        String tableName = "";;
        ArrayList data = null;
        if (aLogoObject.length == 1) {
            tableName = aLogoObject[0].toString();
        }else if (aLogoObject.length == 2) {
            tableName = aLogoObject[0].toString();
            if (aLogoObject[1] instanceof LogoList) {
                data = new ArrayList();
                LogoList ll = (LogoList) aLogoObject[1];
                for (int i=1; i<=ll.length(); i++)
                    data.add( ((LogoWord) ll.pick(i)).toString());
            }
        }
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Database dbComponent = (Database) v.elementAt(i);
            DBase db = dbComponent.getDBase();

            if (db == null)
                throw new LanguageException("No open database in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");

            Table ctable;
            if (aLogoObject.length == 1)
                ctable = db.getTable(tableName);
            else{
                if (dbComponent.getActiveDBTable() != null) {
                    ctable = dbComponent.getActiveDBTable().getTable();
                    tableName = ctable.getTitle();
                }else
                    throw new LanguageException("No table in database component \"" + dbComponent.getESlateHandle().getComponentName() + "\"");
            }

            if (ctable == null)
                throw new LanguageException("No table \"" +tableName+ "\" contained in database \"" + db.getTitle() + "\"");

            DBTable dbTable = dbComponent.getDBTableOfTable(ctable);
            if (dbTable == null)
                throw new LanguageException("Table \"" + tableName + "\" is hidden. Cannot act on this table");

            try{
                int fieldCount = ctable.getFieldCount();
                if (data == null) {
                    data = new ArrayList();
                    for (int k=0; k<fieldCount; k++)
                        data.add(null);
                }else if (data.size() < fieldCount) {
                    int moreElements = fieldCount - data.size();
                    for (int k=0; k<moreElements; k++)
                        data.add(null);
                }else if (data.size() > fieldCount) {
                    ArrayList tmp = data;
                    data = new ArrayList();
                    for (int k=0; k<fieldCount; k++)
                        data.add(tmp.get(k));
                }
//                System.out.println("ctable.addRecord() called ctable: " + ctable.getTitle() + ", data: " + data);
                RecordEntryStructure res=ctable.getRecordEntryStructure();
                res.startRecordEntry();
                for (int j=0;j<data.size();j++)
                	res.setCell(ctable.getTableField(j),data.get(j));
                res.commitNewRecord(false);
            }catch (DuplicateKeyException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidDataFormatException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (NoFieldsInTableException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (TableNotExpandableException exc) {
                throw new LanguageException(exc.getMessage());
            } catch (UnableToAddNewRecordException exc) {
            	throw new LanguageException(exc.getMessage());
			} catch (InvalidFieldIndexException exc) {
				throw new LanguageException(exc.getMessage());
			}

        }

        return LogoVoid.obj;
    }


    private ObjectComparator getComparator(AbstractTableField fld, boolean ascending) throws LanguageException {
        String fieldType = fld.getDataType().getName();
        String className = fieldType.substring(fieldType.lastIndexOf('.') + 1);
        ObjectComparator comparator = null;

        if (ascending) {
            comparator = fld.getComparatorFor("<");
            if (className.equals("Boolean"))
                comparator = StringTableField.getComparator("<");
        }else{
            comparator = fld.getComparatorFor(">");
            if (className.equals("Boolean"))
                comparator = StringTableField.getComparator(">");
        }
        return comparator;
    }

/*    private BinaryPredicate getComparator(TableField fld, boolean ascending) throws LanguageException {
        String fieldType = fld.getDataType().getName();
        String className = fieldType.substring(fieldType.lastIndexOf('.') + 1);
        BinaryPredicate comparator = null;

        if (ascending) {
            if (className.equals("Integer")) {
                try{
                    comparator = new LessNumber(java.lang.Class.forName("java.lang.Integer"));
                }catch (ClassNotFoundException e) {}
            }else if (className.equals("Double")) {
                try{
                    comparator = new LessNumber(java.lang.Class.forName("java.lang.Double"));
                }catch (ClassNotFoundException e) {}
            }else if (className.equals("String"))
                comparator = new LessString();
            else if (className.equals("Boolean"))
                comparator = new LessString();
            else if (className.equals("URL"))
                comparator = new LessString();
            else if (className.equals("Date"))
                comparator = new LessDate();
            else if (className.equals("CImageIcon"))
                throw new LanguageException("Cannot compare Image values");
            else{
                System.out.println("Serious inconsistency error in DatabasePrimitives getComparator() : (1)");
                return null;
            }
        }else{
            if (className.equals("Integer")) {
                try{
                    comparator = new GreaterNumber(java.lang.Class.forName("java.lang.Integer"));
                }catch (ClassNotFoundException e) {}
            }else if (className.equals("Double")) {
                try{
                    comparator = new GreaterNumber(java.lang.Class.forName("java.lang.Double"));
                }catch (ClassNotFoundException e) {}
            }else if (className.equals("String"))
                comparator = new GreaterString();
            else if (className.equals("Boolean"))
                comparator = new GreaterString();
            else if (className.equals("URL"))
                comparator = new GreaterString();
            else if (className.equals("Date"))
                comparator = new GreaterDate();
            else if (className.equals("CImageIcon"))
                throw new LanguageException("Cannot compare Image values");
            else{
                System.out.println("Serious inconsistency error in DatabasePrimitives getComparator() : (2)");
                return null;
            }
        }
        return comparator;
    }
*/

    private LogoObject findMinMax(Table ctable, String fieldName, String criteria, boolean min)
    throws LanguageException {
            try{
                ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

                if (valuesToWorkOn.size() == 0)
                    throw new LanguageException("No values to compare");

                AbstractTableField fld = ctable.getTableField(fieldName);
                ObjectComparator comparator = getComparator(fld, min);
//                BinaryPredicate bp = getComparator(fld, min);
                if (comparator == null)
                    return LogoVoid.obj;

                Object result = valuesToWorkOn.get(0);
                for (int i=1; i<valuesToWorkOn.size(); i++) {
                    if (comparator.execute(valuesToWorkOn.get(i), result))
                        result = valuesToWorkOn.get(i);
                }

                return new LogoWord(result.toString());
            }catch (InvalidFieldNameException exc) {
                throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + ctable.getTitle() + "\"");
            }
    }


    private ObjectBaseArray getValues(String criteria, Table ctable, String fieldName)
    throws LanguageException {
        ObjectBaseArray valuesToWorkOn;
        try{
            AbstractTableField field = ctable.getTableField(fieldName);
            if (criteria == null) {
                valuesToWorkOn = field.getDataBaseArray(); //ArrayList) ctable.getColumn(fieldName).clone();
                for (int i=0; i<valuesToWorkOn.size(); i++) {
                    if (valuesToWorkOn.get(i) == null) {
                        valuesToWorkOn.remove(i);
                        i--;
                    }
                }
            }else{
                LogicalExpression le = new LogicalExpression(ctable, criteria, LogicalExpression.NEW_SELECTION, false);

                IntBaseArray results = le.getQueryResults();
//                int fieldIndex = ctable.getFieldIndex(fieldName);
                valuesToWorkOn = new ObjectBaseArray();
                Object o;
                for (int i=0; i<results.size(); i++) {
//                   o = ctable.riskyGetCell(fieldIndex, results.get(i)); //((Integer) results.at(i)).intValue());
                   o = field.getCellObject(results.get(i));
                   if (o != null)
                       valuesToWorkOn.add(o);
                }
            }
        }catch (InvalidLogicalExpressionException exc) {
            throw new LanguageException(exc.message);
        }catch (InvalidCellAddressException exc) {
            throw new LanguageException(exc.message);
        }catch (InvalidFieldNameException exc) {
            throw new LanguageException(exc.message);
        }

        return valuesToWorkOn;
    }


    private ObjectBaseArray getUniqueValues(ObjectBaseArray data, ObjectComparator comparator) {
        ObjectBaseArray nonNulldata = new ObjectBaseArray(data.size());
//        Array data = (Array) tableData.at(fieldIndex);
//        nonNulldata.ensureCapacity(data.size());

//        System.out.println("Getting rid of null...");
        for (int i=0; i<data.size(); i++) {
            if (data.get(i) != null)
                nonNulldata.add(data.get(i));
        }

//        System.out.println("Sorting....");
        AbstractTableField.sort(nonNulldata, comparator);
//        ArrayListSorting.sort(nonNulldata, bp);

        data = new ObjectBaseArray();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNulldata.size()) {
            data.add(nonNulldata.get(i));
            i++;
            k++;
            while (i<nonNulldata.size() && data.get(k).equals(nonNulldata.get(i)))
                i++;
        }

//        System.out.println("Done.");

        return data;
    }

/*    private Array getValues(String criteria, Table ctable, String fieldName)
    throws LanguageException {
        Array valuesToWorkOn;
        try{
            if (criteria == null) {
                valuesToWorkOn = (Array) ctable.getField(fieldName).clone();
                for (int i=0; i<valuesToWorkOn.size(); i++) {
                    if (valuesToWorkOn.at(i) == null) {
                        valuesToWorkOn.remove(i);
                        i--;
                    }
                }
            }else{
                LogicalExpression le = new LogicalExpression(ctable, criteria, LogicalExpression.NEW_SELECTION, false);

                IntBaseArray results = le.getQueryResults();
                int fieldIndex = ctable.getFieldIndex(fieldName);
                valuesToWorkOn = new Array();
                Object o;
                for (int i=0; i<results.size(); i++) {
                   o = ctable.riskyGetCell(fieldIndex, results.get(i)); //((Integer) results.at(i)).intValue());
                   if (o != null)
                       valuesToWorkOn.add(o);
                }
            }
        }catch (InvalidLogicalExpressionException exc) {
            throw new LanguageException(exc.message);
        }catch (InvalidFieldNameException exc) {
            throw new LanguageException(exc.message);
        }

        return valuesToWorkOn;
    }


    private Array getUniqueValues(Array data, BinaryPredicate bp) {
        Array nonNulldata = new Array();
//        Array data = (Array) tableData.at(fieldIndex);
        nonNulldata.ensureCapacity(data.size());

//        System.out.println("Getting rid of null...");
        for (int i=0; i<data.size(); i++) {
            if (data.at(i) != null)
                nonNulldata.add(data.at(i));
        }

//        System.out.println("Sorting....");
        Sorting.sort(nonNulldata, bp);

        data = new Array();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNulldata.size()) {
            data.add(nonNulldata.at(i));
            i++;
            k++;
            while (i<nonNulldata.size() && data.at(k).equals(nonNulldata.at(i)))
                i++;
        }

//        System.out.println("Done.");

        return data;
    }
*/
    //EXTRA DB PRIMITIVES
    //utility methods//

    private Vector getListeningDatabases(){
     return myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.Database.class);
    }

    private Database getFirstListeningDatabase() throws LanguageException{
     return (Database)myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.Database.class); //if no Database components are to be found in the microworld it will throw a language exception
    }

    private void addField(Database db, String tableName, String fieldName, String datatypeName, boolean editable, boolean key, boolean removable, boolean hidden)
    throws Exception
   { //datatypeName=Integer|String|Double|Image|URL|Boolean|Date|Time
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    Class type=null;
    datatypeName=datatypeName.toLowerCase();
    if (BooleanTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=BooleanTableField.DATA_TYPE;
    else if (CurrencyTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=CurrencyTableField.DATA_TYPE;
    else if (DateTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=DateTableField.DATA_TYPE;
    else if (DoubleTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=DoubleTableField.DATA_TYPE;
    else if (FloatTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=FloatTableField.DATA_TYPE;
    else if (ImageTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=ImageTableField.DATA_TYPE;
    else if (IntegerTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=IntegerTableField.DATA_TYPE;
    else if (StringTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=StringTableField.DATA_TYPE;
    else if (TimeTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=TimeTableField.DATA_TYPE;
    else if (URLTableField.DATA_TYPE.getName().toLowerCase().contains(datatypeName))
    	type=URLTableField.DATA_TYPE;
    if (type!=null) {
   		table.addField(fieldName,type,editable,removable,hidden);
   		if (key)
   			table.addToKey(fieldName);
    	table.setModified();
    }
   }

   private void addCalculatedField(Database db, String tableName, String fieldName, String textFormula, boolean key, boolean removable, boolean editingFormula)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.addCalculatedField(fieldName,textFormula,removable,editingFormula); //for new fields must have editingFormula=false
    if (key)
    	table.addToKey(fieldName);
    table.setModified();
   }

   private void removeField(Database db, String tableName, String fieldName)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.setFieldRemovable(fieldName,true); //always remove the field, even if it was non-removable
    table.removeField(fieldName);
    table.setModified();
   }

   private void removeRecord(Database db, String tableName, int recordIndex)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.removeRecord(recordIndex,recordIndex,false); //remove only one record
    table.setModified();
   }

   private void addEmptyRecord(Database db, String tableName)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.addRecord(table.createEmptyRecordEntryForm(),false);
    table.setActiveRecord(table.getRecordCount()-2); //remove the focus (set other record as active)
    table.setActiveRecord(table.getRecordCount()-1); //set last record as active again
    table.setModified();
   }

   private void setTableName(Database db, String tableName, String newTableName)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.setTitle(newTableName);
   }

   private void setFieldEditable(Database db, String tableName, String fieldName, boolean flag)
    throws Exception
   {
    Table table=(tableName==null)? db.getDBase().getActiveTable() : db.getDBase().getTable(tableName);
    table.setFieldEditable(fieldName,flag);
   }

   //DB.ACTIVETABLENAME//

   public final LogoObject pACTIVETABLENAME(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,0);
    return new LogoWord(getFirstListeningDatabase().getDBase().getActiveTable().getTitle());
   }

   //DB.ADDEMPTYRECORD//

   public final LogoObject pADDEMPTYRECORD(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,0,1);
    String tableName=(params.length==0)?null:params[0].toString();
    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      addEmptyRecord((Database)v.elementAt(i),tableName);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.ADDFIELD//

   public final LogoObject pADDFIELD(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    String tableName;
    String fieldName,datatypeName;
    boolean editable=true,key=false,removable=true,hidden=false;

    switch(params.length){
     case 2:
      tableName=null;
      fieldName=params[0].toString();
      datatypeName=params[1].toString(); //datatypeName=Integer|String|Double|Image|URL|Boolean|Date|Time
      break;
     case 3:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      datatypeName=params[2].toString(); //datatypeName=Integer|String|Double|Image|URL|Boolean|Date|Time
      break;
     case 6:
      tableName=null;
      fieldName=params[0].toString();
      datatypeName=params[1].toString(); //datatypeName=Integer|String|Double|Image|URL|Boolean|Date|Time
      editable=params[2].toBoolean();
      key=params[3].toBoolean();
      removable=params[4].toBoolean();
      hidden=params[5].toBoolean();
      break;
     case 7:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      datatypeName=params[2].toString(); //datatypeName=Integer|String|Double|Image|URL|Boolean|Date|Time
      editable=params[3].toBoolean();
      key=params[4].toBoolean();
      removable=params[5].toBoolean();
      hidden=params[6].toBoolean();
      break;
     default:
      throw new LanguageException("Wrong number of arguments");
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      addField((Database)v.elementAt(i),tableName,fieldName,datatypeName,editable,key,removable,hidden);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.ADDCALCULATEDFIELD//

   public final LogoObject pADDCALCULATEDFIELD(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    String tableName;
    String fieldName,textFormula;
    boolean key=false,removable=true,editingFormula=false; //for new fields must have editingFormula=false

    switch(params.length){
     case 2:
      tableName=null;
      fieldName=params[0].toString();
      textFormula=params[1].toString();
      break;
     case 3:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      textFormula=params[2].toString();
      break;
     case 5:
      tableName=null;
      fieldName=params[0].toString();
      textFormula=params[1].toString();
      key=params[2].toBoolean();
      removable=params[3].toBoolean();
      editingFormula=params[4].toBoolean();
      break;
     case 6:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      textFormula=params[2].toString();
      key=params[3].toBoolean();
      removable=params[4].toBoolean();
      editingFormula=params[5].toBoolean();
      break;
     default:
      throw new LanguageException("Wrong number of arguments");
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      addCalculatedField((Database)v.elementAt(i),tableName,fieldName,textFormula,key,removable,editingFormula);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.REMOVEFIELD//

   public final LogoObject pREMOVEFIELD(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,1,2);

    String tableName;
    String fieldName;
    switch(params.length){
     case 1:
      tableName=null;
      fieldName=params[0].toString();
      break;
     case 2:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      break;
     default:
      throw new LanguageException("Wrong number of arguments"); //dummy
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      removeField((Database)v.elementAt(i),tableName,fieldName);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.REMOVERECORD//

   public final LogoObject pREMOVERECORD(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,1,2);

    String tableName;
    int recordIndex;
    switch(params.length){
     case 1:
      tableName=null;
      recordIndex=params[0].toInteger();
      break;
     case 2:
      tableName=params[0].toString();
      recordIndex=params[1].toInteger();
      break;
     default:
      throw new LanguageException("Wrong number of arguments"); //dummy
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      removeRecord((Database)v.elementAt(i),tableName,recordIndex-1); //the Logo db API has indexes starting from 1, whereas the Java one has them starting from 0
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.SETTABLENAME//

   public final LogoObject pSETTABLENAME(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,1,2);

    String tableName;
    String newTableName;
    switch(params.length){
     case 1:
      tableName=null;
      newTableName=params[0].toString();
      break;
     case 2:
      tableName=params[0].toString();
      newTableName=params[1].toString();
      break;
     default:
      throw new LanguageException("Wrong number of arguments"); //dummy
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      setTableName((Database)v.elementAt(i),tableName,newTableName);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }

   //DB.SETFIELDEDITABLE//

   public final LogoObject pSETFIELDEDITABLE(InterpEnviron interp, LogoObject[] params)
    throws LanguageException
   {
    testNumParams(params,2,3);

    String tableName;
    String fieldName;
    boolean flag;
    switch(params.length){
     case 2:
      tableName=null;
      fieldName=params[0].toString();
      flag=params[1].toBoolean();
      break;
     case 3:
      tableName=params[0].toString();
      fieldName=params[1].toString();
      flag=params[2].toBoolean();
      break;
     default:
      throw new LanguageException("Wrong number of arguments"); //dummy
    }

    Vector v=getListeningDatabases();
    for(int i=0;i<v.size();i++)
     try{
      setFieldEditable((Database)v.elementAt(i),tableName,fieldName,flag);
     }catch(Exception e){
      //e.printStackTrace();
      throw new LanguageException(e.getMessage());
     }
    return LogoVoid.obj; //return nothing
   }
    
}