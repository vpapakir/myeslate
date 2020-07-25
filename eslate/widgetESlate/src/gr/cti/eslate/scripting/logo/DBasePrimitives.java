package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.InsufficientPrivilegesException;
import gr.cti.eslate.database.engine.InvalidTitleException;
import gr.cti.eslate.database.engine.Table;
import virtuoso.logo.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;


public class DBasePrimitives extends PrimitiveGroup {
    MyMachine myMachine;
    ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.scripting.logo.DBasePrimitivesBundle", Locale.getDefault());

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("DBASE.SETACTIVETABLE", "pSETACTIVETABLE", 1);
        registerPrimitive("DBASE.ACTIVETABLE", "pACTIVETABLE", 0);
        registerPrimitive("DBASE.TABLEINDEX", "pTABLEINDEX", 1);
        registerPrimitive("DBASE.TABLECOUNT", "pTABLECOUNT", 0);
        registerPrimitive("DBASE.NEWTABLE", "pNEWTABLE", 0);
        registerPrimitive("DBASE.REMOVETABLE", "pREMOVETABLE", 1);
        registerPrimitive("DBASE.TABLENAMES", "pTABLENAMES", 0);

        myMachine=(MyMachine)machine;
        if (console != null)
            console.putSetupMessage("Loaded DBase primitives");
    }


    public final LogoObject pSETACTIVETABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String tableName = null;
        int tableIndex = -1;
        try{
            tableName = aLogoObject[0].toString();
        }catch (Exception exc) {
            tableIndex = aLogoObject[0].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.DBase.class);
        for (int i=0; i<v.size(); i++) {
            DBase dbase = (DBase) v.elementAt(i);
            try{
                if (tableIndex == -1)
                    tableIndex = dbase.indexOf(tableName);
                dbase.activateTable(tableIndex, true);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pACTIVETABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        DBase dbase = (DBase) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.DBase.class);
        if (dbase == null)
            throw new LanguageException("There is no DBase to TELL this to");

        try{
            int activeTableIndex = dbase.getActiveTableIndex();
            if (activeTableIndex != -1)
                return new LogoWord(dbase.getActiveTable().getTitle());
            return LogoVoid.obj;
        }catch (Exception exc) {
            throw new LanguageException(exc.getMessage());
        }
    }

    public final LogoObject pTABLEINDEX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String title = aLogoObject[0].toString();

        DBase dbase = (DBase) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.DBase.class);
        if (dbase == null)
            throw new LanguageException("There is no DBase to TELL this to");

        return new LogoWord(dbase.indexOf(title));
    }

    public final LogoObject pTABLECOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

System.out.println("In pTableCOUNT()");
        DBase dbase = (DBase) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.DBase.class);
System.out.println("dbase: " + dbase);
        if (dbase == null)
            throw new LanguageException("There is no DBase to TELL this to");

        return new LogoWord(dbase.getTableCount());
    }

    public final LogoObject pNEWTABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMinParams(aLogoObject, 0);
        testMaxParams(aLogoObject, 1);

        String tableName = null;
        if (aLogoObject.length == 1)
            tableName = aLogoObject[0].toString();
        if (tableName == null)
            tableName = bundle.getString("New Table");

        DBase dbase = (DBase) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.DBase.class);
        if (dbase == null)
            throw new LanguageException("There is no DBase to TELL this to");

        try{
            Table newTable = new Table();
            dbase.addTable(newTable, tableName, true);
            return new LogoWord(tableName);
        }catch (InvalidTitleException exc) {
            throw new LanguageException(exc.getMessage());
        }catch (InsufficientPrivilegesException exc) {
            throw new LanguageException(exc.getMessage());
        }
    }

    public final LogoObject pREMOVETABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String tableName = null;
        int tableIndex = -1;
        try{
            tableName = aLogoObject[0].toString();
        }catch (Exception exc) {
            tableIndex = aLogoObject[0].toInteger();
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.database.engine.DBase.class);
        for (int i=0; i<v.size(); i++) {
            DBase dbase = (DBase) v.elementAt(i);
            try{
                if (tableIndex == -1)
                    tableIndex = dbase.indexOf(tableName);
                Table table = dbase.getTableAt(tableIndex);
                dbase.removeTable(table, false);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pTABLENAMES(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        DBase dbase = (DBase) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.database.engine.DBase.class);
        if (dbase == null)
            throw new LanguageException("There is no DBase to TELL this to");

        ArrayList tableNames = dbase.getTableTitles();
        LogoWord[] results = new LogoWord[tableNames.size()];
        String name;
        StringBuffer buff;
        int index;
        for (int i=0; i<tableNames.size(); i++) {
            name = (String) tableNames.get(i);
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
}