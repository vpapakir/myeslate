package gr.cti.eslate.scripting.logo;

import java.util.*;
import java.awt.event.ActionEvent;
import virtuoso.logo.*;
//import gr.cti.eslate.logo.*;
import gr.cti.eslate.database.engine.*;
//import gr.cti.eslate.database.Database;
//import gr.cti.eslate.database.DBTable;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;
import gr.cti.typeArray.ObjectBaseArray;
import com.objectspace.jgl.Array;
import com.objectspace.jgl.LessNumber;
import com.objectspace.jgl.LessString;
import com.objectspace.jgl.GreaterString;
import com.objectspace.jgl.GreaterNumber;
import com.objectspace.jgl.BinaryPredicate;
import com.objectspace.jgl.Sorting;


public class TablePrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("TABLE.SETCELL", "pSETCELL", 3);
        registerPrimitive("TABLE.CELL", "pCELL", 2);
        registerPrimitive("TABLE.FIELD", "pFIELD", 1);
        registerPrimitive("TABLE.RECORD", "pRECORD", 1);
        registerPrimitive("TABLE.RECORDCOUNT", "pRECORDCOUNT", 0);
        registerPrimitive("TABLE.ROWCOUNT", "pRECORDCOUNT", 0);
        registerPrimitive("TABLE.SETACTIVERECORD", "pSETACTIVERECORD", 1);
        registerPrimitive("TABLE.ACTIVERECORD", "pACTIVERECORD", 0);

        registerPrimitive("TABLE.SELECTRECORDS", "pSELECTRECORDS", 1);
        registerPrimitive("TABLE.ADDITIONALLYSELECTRECORD", "pADDITIONALLYSELECTRECORD", 1);
        registerPrimitive("TABLE.SELECTEDRECORDS", "pSELECTEDRECORDS", 0);
        registerPrimitive("TABLE.SELECTEDRECORDCOUNT", "pSELECTEDRECORDCOUNT", 0);
        registerPrimitive("TABLE.SELECT", "pSELECT", 1);
        registerPrimitive("TABLE.CLEARSELECTION", "pCLEARSELECTION", 0);
        registerPrimitive("TABLE.INVERTSELECTION", "pINVERTSELECTION", 0);
        registerPrimitive("TABLE.SELECTALL", "pSELECTALL", 0);
        registerPrimitive("TABLE.SFIELD", "pSFIELD", 1);

        registerPrimitive("TABLE.FIELDNAMES", "pFIELDNAMES", 0);

        registerPrimitive("TABLE.MIN", "pMIN", 1);
        registerPrimitive("TABLE.MAX", "pMAX", 1);
        registerPrimitive("TABLE.SMALL", "pSMALL", 2);
        registerPrimitive("TABLE.BIG", "pBIG", 2);
        registerPrimitive("TABLE.RANK", "pRANK", 2);
        registerPrimitive("TABLE.SUM", "pSUM", 1);
        registerPrimitive("TABLE.PRODUCT", "pPRODUCT", 1);
        registerPrimitive("TABLE.AVERAGE", "pAVERAGE", 1);
        registerPrimitive("TABLE.GEOMEAN", "pGEOMEAN", 1);
        registerPrimitive("TABLE.VARIANCE", "pVARIANCE", 1);
        registerPrimitive("TABLE.STDEV", "pSTDEV", 1);
        registerPrimitive("TABLE.COUNT", "pCOUNT", 1);
        registerPrimitive("TABLE.ADDRECORD", "pADDRECORD", 0);
        registerPrimitive("TABLE.ADDROW", "pADDRECORD", 0);

        registerPrimitive("TABLE.ADDFIELD", "pADDFIELD", 1);
        registerPrimitive("TABLE.ADDCOLUMN", "pADDFIELD", 1);
        registerPrimitive("TABLE.REMOVEFIELD", "pREMOVEFIELD", 1);
        registerPrimitive("TABLE.RENAMEFIELD", "pRENAMEFIELD", 2);
        registerPrimitive("TABLE.FIELDCOUNT", "pFIELDCOUNT", 0);
        registerPrimitive("TABLE.COLUMNCOUNT", "pFIELDCOUNT", 0);
        registerPrimitive("TABLE.REMOVERECORD", "pREMOVERECORD", 1);
        registerPrimitive("TABLE.RENAME", "pRENAME", 1);

        myMachine=(MyMachine)machine;

        if (console != null)
            console.putSetupMessage("Loaded Table primitives");
    }


    public final LogoObject pTIME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        return new LogoWord(System.currentTimeMillis());
    }

    public final LogoObject pSETCELL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 3);

        String value, fieldName = null;
        int row, fieldIndex = -1;

        row = aLogoObject[0].toInteger();
        try{
            fieldName = aLogoObject[1].toString();
        }catch (Exception exc) {
            fieldIndex = aLogoObject[1].toInteger();
        }
        value = aLogoObject[2].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);

            if (row <= 0 || row > table.getRecordCount())
                throw new LanguageException("Invalid record number: " + row);

            if (fieldIndex == -1) {
                try{
                    fieldIndex = table.getFieldIndex(fieldName);
                }catch (InvalidFieldNameException e) {
                    throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + table.getTitle() + "\"");
                }
            }

            try{
                table.setCell(fieldIndex, table.recordForRow(row-1), value);
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
        testNumParams(aLogoObject, 2);

        String fieldName;
        int row;

        row = aLogoObject[0].toInteger();
        fieldName = aLogoObject[1].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        if (row <= 0 || row > table.getRecordCount())
            throw new LanguageException("Invalid record number: " + row);

        int fieldIndex;
        try{
            fieldIndex = table.getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
            throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + table.getTitle() + "\"");
        }

        try{
            Object value = table.getCell(fieldIndex, table.recordForRow(row-1));
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
    }

    public final LogoObject pFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String fieldName;
        fieldName = aLogoObject[0].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            AbstractTableField fld = table.getTableField(fieldName);
            ArrayList fieldContents = fld.getDataArrayList();

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
            throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + table.getTitle() + "\"");
        }
    }


    public final LogoObject pRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int row;
        row = aLogoObject[0].toInteger();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            ArrayList recordContents = table.getRow(table.recordForRow(row-1));
            LogoWord[] results = new LogoWord[recordContents.size()];
//                System.out.println("Here1");
//1            Array columnOrder = table.getTableView().getColumnOrder();
            for (int i=0; i<recordContents.size(); i++) {
//1                int k = ((Integer) columnOrder.at(i)).intValue();
//                    System.out.println("i: " + i + ", k: " + k + ", value: " + recordContents.at(k));
//1                Object value = recordContents.get(k);
                Object value = recordContents.get(i);
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
    }


    public final LogoObject pRECORDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return new LogoWord(table.getRecordCount());
    }


    public final LogoObject pACTIVERECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return new LogoWord(table.rowForRecord(table.getActiveRecord())+1);
    }


    public final LogoObject pSETACTIVERECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int row;
        row = aLogoObject[0].toInteger();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);

//        System.out.println("table: " + tableName + ", row: " + row + ", fieldName: " + fieldName + ", value: " + value);

        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            if (row <= 0 || row > table.getRecordCount())
                throw new LanguageException("Invalid record number: " + row);

            table.setActiveRecord(table.recordForRow(row-1));
        }

        return LogoVoid.obj;
    }


    public final LogoObject pSELECTRECORDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int[] row;
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

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            for (int k=0; k<row.length; k++)
                row[k] = table.recordForRow(row[k]-1);

            table.setSelectedSubset(row);
        }
        return LogoVoid.obj;
    }


    public final LogoObject pADDITIONALLYSELECTRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int row;
        row = aLogoObject[0].toInteger();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            int rec = table.recordForRow(row-1);

            table.addToSelectedSubset(rec);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSELECTEDRECORDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        IntBaseArray selectedSubset = table.getSelectedSubset();

        int[] selectedRows = new int[selectedSubset.size()];
        for (int i=0; i<selectedSubset.size(); i++)
            selectedRows[i] = selectedSubset.get(i); //((Integer) selectedSubset.at(i)).intValue();

        for (int i=0; i<selectedRows.length; i++)
            selectedRows[i] = table.rowForRecord(selectedRows[i]);

        LogoWord[] results = new LogoWord[selectedRows.length];
        for (int i=0; i<selectedRows.length; i++)
            results[i] = new LogoWord(selectedRows[i]+1);

        return new LogoList(results);
    }


    public final LogoObject pSELECTEDRECORDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return new LogoWord(table.getSelectedSubset().size());
    }


    public final LogoObject pSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String query = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        Table firstTable = null;
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            if (i==0)
                firstTable = table;

            try{
                LogicalExpression le = new LogicalExpression(table, query, LogicalExpression.NEW_SELECTION, true);
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

            for (int i=0; i<selectedRows.length; i++)
                selectedRows[i] = firstTable.rowForRecord(selectedRows[i]);

            LogoWord[] results = new LogoWord[selectedRows.length];
            for (int i=0; i<selectedRows.length; i++)
                results[i] = new LogoWord(selectedRows[i]+1);

            return new LogoList(results);
        }else
            return LogoVoid.obj;
    }


    public final LogoObject pFIELDNAMES(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        StringBaseArray fieldNames = table.getFieldNames();
        LogoWord[] results = new LogoWord[fieldNames.size()];
        String name;
        StringBuffer buff;
        int index, counter;
        for (int i=0; i<fieldNames.size(); i++) {
            name = fieldNames.get(i);
//1            name = (String) fieldNames.get(((Integer) table.getTableView().getColumnOrder().at(i)).intValue());
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
    }


    public final LogoObject pCLEARSELECTION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);

        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            table.resetSelectedSubset();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pSELECTALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            table.selectAll();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pINVERTSELECTION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            table.invertSelection();
        }
        return LogoVoid.obj;
    }


    public final LogoObject pSFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String fieldName;
        fieldName = aLogoObject[0].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            ArrayList fieldContents = table.getTableField(fieldName).getDataArrayList();
            Array sFieldContents = new Array();
            IntBaseArray selectedSet = table.getSelectedSubset();
            for (int k=0; k<selectedSet.size(); k++) {
                sFieldContents.add(fieldContents.get(selectedSet.get(k))); //((Integer) selectedSet.at(k)).intValue()));
            }

            AbstractTableField fld = table.getTableField(fieldName);

            LogoWord[] results = new LogoWord[sFieldContents.size()];
            if (fld.getDataType().equals(Double.class)) {
                for (int i=0; i<sFieldContents.size(); i++)
                    results[i] = (sFieldContents.at(i) == null)? new LogoWord(""):new LogoWord(((Double) sFieldContents.at(i)).doubleValue());
            }else if (fld.getDataType().equals(Integer.class)) {
                for (int i=0; i<sFieldContents.size(); i++)
                    results[i] = (sFieldContents.at(i) == null)? new LogoWord(""):new LogoWord(((Integer) sFieldContents.at(i)).intValue());
            }else if (fld.getDataType().equals(Boolean.class)) {
                for (int i=0; i<sFieldContents.size(); i++)
                    results[i] = (sFieldContents.at(i) == null)? new LogoWord(""):new LogoWord(((Boolean) sFieldContents.at(i)).booleanValue());
            }else{
                for (int i=0; i<sFieldContents.size(); i++)
                    results[i] = (sFieldContents.at(i) == null)? new LogoWord(""):new LogoWord(sFieldContents.at(i).toString());
            }

            return new LogoList(results);

        }catch (InvalidFieldNameException exc) {
            throw new LanguageException("No field \"" + fieldName + "\" contained in table \"" + table.getTitle() + "\"");
        }
    }


    public final LogoObject pMIN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else
            fieldName = aLogoObject[0].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return findMinMax(table, fieldName, criteria, true);
    }


    public final LogoObject pMAX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;
        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else
            fieldName = aLogoObject[0].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return findMinMax(table, fieldName, criteria, false);
    }


    public final LogoObject pSMALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 2);

        String fieldName = "", criteria = null;
        int index = 1;
        if (aLogoObject.length == 3) {
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
            criteria = aLogoObject[2].toString();
        }else if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
            if (valuesToWorkOn.size() == 0)
                throw new LanguageException("No values to compare");

            if (index < 1 || index > valuesToWorkOn.size())
                throw new LanguageException("Invalid order: " + index);

            AbstractTableField fld = table.getTableField(fieldName);
            ObjectComparator comparator = getComparator(fld, true);
            fld.sort(valuesToWorkOn, comparator);

//            ArrayListSorting.sort(valuesToWorkOn, bp);
            return new LogoWord(valuesToWorkOn.toString());

        }catch (InvalidFieldNameException exc) {
            throw new LanguageException(exc.message);
        }
    }


    public final LogoObject pBIG(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 2);

        String fieldName, criteria = null;
        int index;

        if (aLogoObject.length == 3) {
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
            criteria = aLogoObject[2].toString();
        }else{
            fieldName = aLogoObject[0].toString();
            index = aLogoObject[1].toInteger();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
            if (valuesToWorkOn.size() == 0)
                throw new LanguageException("No values to compare");

            if (index < 1 || index > valuesToWorkOn.size())
                throw new LanguageException("Invalid order: " + index);

            AbstractTableField fld = table.getTableField(fieldName);
            ObjectComparator comparator = getComparator(fld, false);
            fld.sort(valuesToWorkOn, comparator);

//            ArrayListSorting.sort(valuesToWorkOn, comparator);
            return new LogoWord(valuesToWorkOn.get(index-1).toString());

        }catch (InvalidFieldNameException exc) {
            throw new LanguageException(exc.message);
        }
    }


    public final LogoObject pRANK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 2);

        String fieldName, criteria = null;
        int rowIndex;

        if (aLogoObject.length == 3) {
            rowIndex = aLogoObject[0].toInteger();
            fieldName = aLogoObject[1].toString();
            criteria = aLogoObject[2].toString();
        }else{
            rowIndex = aLogoObject[0].toInteger();
            fieldName = aLogoObject[1].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        if (rowIndex < 1 || rowIndex > table.getRecordCount())
            throw new LanguageException("Invalid row number: " + rowIndex);

        try{
            Object value = table.getCell(fieldName, table.recordForRow(rowIndex-1));
            if (value == null)
                throw new LanguageException("No value for record " + rowIndex + " in field " + fieldName);

            ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);

            if (valuesToWorkOn.size() == 0)
                throw new LanguageException("No values to rank record " + rowIndex + " against");

            AbstractTableField fld = table.getTableField(fieldName);
            ObjectComparator comparator = getComparator(fld, true);
            ObjectBaseArray uniqueValues = getUniqueValues(valuesToWorkOn, comparator);

            return new LogoWord(uniqueValues.indexOf(value)+1);
        }catch (InvalidFieldNameException exc) {
            throw new LanguageException(exc.message);
        }catch (InvalidCellAddressException exc) {
            throw new LanguageException(exc.message);
        }
    }


    public final LogoObject pSUM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else
            fieldName = aLogoObject[0].toString();

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
        try{
            double sum = 0;
            for (int i=0; i<valuesToWorkOn.size(); i++)
                sum = sum + ((Number) valuesToWorkOn.get(i)).doubleValue();

            return new LogoWord(sum);
        }catch (Exception exc) {
            throw new LanguageException("Number expected");
        }
    }


    public final LogoObject pPRODUCT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
        try{
            double product = 1;
            for (int i=0; i<valuesToWorkOn.size(); i++)
                product = product * ((Number) valuesToWorkOn.get(i)).doubleValue();

            return new LogoWord(product);
        }catch (Exception exc) {
            throw new LanguageException("Number expected");
        }
    }


    public final LogoObject pAVERAGE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
        try{
            double sum = 0;
            for (int i=0; i<valuesToWorkOn.size(); i++)
                sum = sum + ((Number) valuesToWorkOn.get(i)).doubleValue();

            return new LogoWord(sum / valuesToWorkOn.size());
        }catch (Exception exc) {
            throw new LanguageException("Number expected");
        }
    }


    public final LogoObject pGEOMEAN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
        try{
            double product = 1;
            for (int i=0; i<valuesToWorkOn.size(); i++)
                product = product * ((Number) valuesToWorkOn.get(i)).doubleValue();

            return new LogoWord(Math.pow(product, (1d/valuesToWorkOn.size())));

        }catch (Exception exc) {
            throw new LanguageException("Number expected");
        }
    }


    public final LogoObject pVARIANCE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
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
    }


    public final LogoObject pSTDEV(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        String fieldName, criteria = null;

        if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            criteria = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        ObjectBaseArray valuesToWorkOn = getValues(criteria, table, fieldName);
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
    }


    public final LogoObject pCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String criteria = aLogoObject[0].toString();
        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        try{
            LogicalExpression le = new LogicalExpression(table, criteria, LogicalExpression.NEW_SELECTION, false);
            return new LogoWord(le.getQueryResults().size());
        }catch (InvalidLogicalExpressionException exc) {
            throw new LanguageException(exc.message);
        }
    }

    public final LogoObject pADDRECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        ArrayList data = null;
        if (aLogoObject.length == 1) {
            if (aLogoObject[0] instanceof LogoList) {
                data = new ArrayList();
                LogoList ll = (LogoList) aLogoObject[0];
                for (int i=1; i<=ll.length(); i++)
                    data.add( ((LogoWord) ll.pick(i)).toString());
            }
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            try{
                int fieldCount = table.getFieldCount();
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
                table.addRecord(data, false);
            }catch (DuplicateKeyException exc) {
                throw new LanguageException(exc.message);
            }catch (NullTableKeyException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidDataFormatException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (NoFieldsInTableException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (TableNotExpandableException exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pADDFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 6);
        testMinParams(aLogoObject, 1);

        String fieldName, dataType = AbstractTableField.getInternalDataTypeName(String.class);
        boolean editable = true, isKey = false, removable = true, hidden = false;

        if (aLogoObject.length == 6) {
            fieldName = aLogoObject[0].toString();
            dataType = aLogoObject[1].toString();
            editable = aLogoObject[2].toBoolean();
            isKey = aLogoObject[3].toBoolean();
            removable = aLogoObject[4].toBoolean();
            hidden = aLogoObject[5].toBoolean();
        }else if (aLogoObject.length == 5) {
            fieldName = aLogoObject[0].toString();
            dataType = aLogoObject[1].toString();
            editable = aLogoObject[2].toBoolean();
            isKey = aLogoObject[3].toBoolean();
            removable = aLogoObject[4].toBoolean();
        }else if (aLogoObject.length == 4) {
            fieldName = aLogoObject[0].toString();
            dataType = aLogoObject[1].toString();
            editable = aLogoObject[2].toBoolean();
            isKey = aLogoObject[3].toBoolean();
        }else if (aLogoObject.length == 3) {
            fieldName = aLogoObject[0].toString();
            dataType = aLogoObject[1].toString();
            editable = aLogoObject[2].toBoolean();
        }else if (aLogoObject.length == 2) {
            fieldName = aLogoObject[0].toString();
            dataType = aLogoObject[1].toString();
        }else{
            fieldName = aLogoObject[0].toString();
        }

		Class fieldType = AbstractTableField.getInternalDataType(dataType);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            try{
                table.addField(fieldName, fieldType, editable, removable, hidden);
            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidKeyFieldException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidFieldTypeException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (AttributeLockedException exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pREMOVEFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String fieldName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            try{
                table.removeField(fieldName, false);
            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }catch (TableNotExpandableException exc) {
                throw new LanguageException(exc.message);
            }catch (CalculatedFieldExistsException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (FieldNotRemovableException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (AttributeLockedException exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pRENAMEFIELD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);
        String fieldName = aLogoObject[0].toString();
        String newFieldName = aLogoObject[1].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            try{
                table.renameField(fieldName, newFieldName);
            }catch (InvalidFieldNameException exc) {
                throw new LanguageException(exc.message);
            }catch (FieldNameInUseException exc) {
                throw new LanguageException(exc.message);
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pFIELDCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Table table = (Table) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.Table.class);
        if (table == null)
            throw new LanguageException("There is no Table to TELL this to");

        return new LogoWord(table.getFieldCount());
    }

    public final LogoObject pREMOVERECORD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int rowIndex = aLogoObject[0].toInteger();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            int recIndex = table.recordForRow(rowIndex-1);
            try{
                table.removeRecord(recIndex, rowIndex-1, false);
            }catch (TableNotExpandableException exc) {
                throw new LanguageException(exc.message);
            }catch (InvalidRecordIndexException exc) {
                throw new LanguageException(exc.message);
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pRENAME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String newName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.Table.class);
        for (int i=0; i<v.size(); i++) {
            Table table = (Table) v.elementAt(i);
            try{
                table.setTitle(newName);
            }catch (InvalidTitleException exc) {
                throw new LanguageException(exc.getMessage());
            }catch (java.beans.PropertyVetoException exc) {
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
/*            if (className.equals("Integer")) {
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
*/
        }else{
            comparator = fld.getComparatorFor(">");
            if (className.equals("Boolean"))
                comparator = StringTableField.getComparator(">");
/*            if (className.equals("Integer")) {
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
*/
        }
        return comparator;
    }


    private LogoObject findMinMax(Table ctable, String fieldName, String criteria, boolean min)
    throws LanguageException {
            try{
                AbstractTableField fld = ctable.getTableField(fieldName);
                ObjectBaseArray valuesToWorkOn = getValues(criteria, ctable, fieldName);

                if (valuesToWorkOn.size() == 0)
                    throw new LanguageException("No values to compare");

                ObjectComparator bp = getComparator(fld, min);
                if (bp == null)
                    return LogoVoid.obj;

                Object result = valuesToWorkOn.get(0);
                for (int i=1; i<valuesToWorkOn.size(); i++) {
                    if (bp.execute(valuesToWorkOn.get(i), result))
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

}