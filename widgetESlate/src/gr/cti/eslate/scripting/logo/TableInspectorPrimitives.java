package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.tableInspector.TableInspector;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.LogoWord;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;

public class TableInspectorPrimitives extends PrimitiveGroup {
    private static ResourceBundle bundle,bundleMessages;

    public TableInspectorPrimitives() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.BundlePrimitives",Locale.getDefault());
        bundleMessages=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.MessagesBundle",Locale.getDefault());
    }

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("RB.LOCK", "pLOCK", 1);
        registerPrimitive("RB.UNLOCK", "pUNLOCK", 1);
        registerPrimitive("RB.SETFIELDQUERYTOOLVISIBLE", "pSETFIELDQUERYTOOLVISIBLE", 2);
        registerPrimitive("RB.ISFIELDQUERYTOOLVISIBLE", "pISFIELDQUERYTOOLVISIBLE", 1);
        registerPrimitive("RB.SHOWTABLE", "pSHOWTABLE", 1);
        registerPrimitive("RB.TABLESHOWN", "pTABLESHOWN", 0);
        myMachine=(MyMachine) machine;
        myConsole=console;
        if (console!=null)
            console.putSetupMessage(bundle.getString("setup"));
    }

    public final LogoObject pLOCK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,3);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                if (aLogoObject.length==3) {
                    ti.lock(aLogoObject[0].toInteger(),aLogoObject[1].toString(),aLogoObject[2].toString());
                } else if (aLogoObject.length==2) {
                    try {
                        //Check if the parameters are number and String.
                        int num=aLogoObject[0].toInteger();
                        ti.lock(num,aLogoObject[1].toString());
                    } catch(LanguageException ex) {
                        //Assume the parameters are String and String
                        ti.lock(ti.getSelectedTabIndex()+1,aLogoObject[0].toString(),aLogoObject[1].toString());
                    }
                } else
                    ti.lock(ti.getSelectedTabIndex()+1,aLogoObject[0].toString());
                return new LogoWord(true);
            } catch(Exception e) {
                myConsole.putStatusMessage(bundleMessages.getString("notlocked"));
            }
        }
        return new LogoWord(false);
    }

    public final LogoObject pUNLOCK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                if (aLogoObject.length==2)
                    ti.unlock(aLogoObject[0].toInteger(),aLogoObject[1].toString());
                else
                    ti.unlock(ti.getSelectedTabIndex()+1,aLogoObject[0].toString());
                return new LogoWord(true);
            } catch(Exception e) {
                myConsole.putStatusMessage(bundleMessages.getString("notunlocked"));
            }
        }
        return new LogoWord(false);
    }

    public final LogoObject pSETFIELDQUERYTOOLVISIBLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,3);
        testMinParams(aLogoObject,2);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                if (aLogoObject.length==3)
                    ti.setFieldQueryToolVisible(aLogoObject[0].toInteger(),aLogoObject[1].toString(),aLogoObject[2].toBoolean());
                else
                    ti.setFieldQueryToolVisible(ti.getSelectedTabIndex()+1,aLogoObject[0].toString(),aLogoObject[1].toBoolean());
            } catch(Exception e) {
                e.printStackTrace();
                throw new LanguageException(e.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pISFIELDQUERYTOOLVISIBLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                if (aLogoObject.length==2)
                    return new LogoWord(ti.isFieldQueryToolVisible(aLogoObject[0].toInteger(),aLogoObject[1].toString()));
                else
                    return new LogoWord(ti.isFieldQueryToolVisible(ti.getSelectedTabIndex()+1,aLogoObject[0].toString()));
            } catch(Exception e) {
                e.printStackTrace();
                throw new LanguageException(e.getMessage());
            }
        }
        return new LogoWord(false);
    }

    public final LogoObject pSHOWTABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                ti.setSelectedTabIndex(aLogoObject[0].toInteger()-1);
            } catch(Exception e) {
                e.printStackTrace();
                throw new LanguageException(e.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pTABLESHOWN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.tableInspector.TableInspector.class);
        for (int i=0;i<v.size();i++) {
            TableInspector ti=(TableInspector) v.elementAt(i);
            try {
                return new LogoWord(ti.getSelectedTabIndex()+1);
            } catch(Exception e) {
                e.printStackTrace();
                throw new LanguageException(e.getMessage());
            }
        }
        return new LogoWord(-1);
    }

    private MyMachine myMachine;
    private Console myConsole;
}
