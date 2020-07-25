package gr.cti.eslate.scripting.logo;


import gr.cti.eslate.spinButton.SpinButton;

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


public class SpinButtonPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("SBUTTON.INCREASE", "pINCREASE", 0);
        registerPrimitive("SBUTTON.DECREASE", "pDECREASE", 0);
        registerPrimitive("SBUTTON.SETSTEP", "pSETSTEP", 1);
        registerPrimitive("SBUTTON.STEP", "pSTEP", 0);
        registerPrimitive("SBUTTON.SETMODELTYPE", "pSETMODELTYPE", 1);
        registerPrimitive("SBUTTON.MODELTYPE", "pMODELTYPE", 0);
        registerPrimitive("SBUTTON.SETVALUE", "pSETVALUE", 1);
        registerPrimitive("SBUTTON.VALUE", "pVALUE", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded SpinButton primitives");

    }

    public final LogoObject pINCREASE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.spinButton.SpinButton.class);

        for (int i = 0; i < v.size(); i++) {
            SpinButton button = (SpinButton) v.elementAt(i);

            button.increase();
        }

        //      System.out.println("model value is : "+ button.model.getStringValue()+"step is : "+button.getStep());

        return LogoVoid.obj;
    }

    public final LogoObject pDECREASE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.spinButton.SpinButton.class);

        for (int i = 0; i < v.size(); i++) {
            SpinButton button = (SpinButton) v.elementAt(i);

            button.decrease();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETSTEP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        double step = aLogoObject[0].toNumber();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.spinButton.SpinButton.class);

        for (int i = 0; i < v.size(); i++) {
            SpinButton button = (SpinButton) v.elementAt(i);

            button.setStep(step);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSTEP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        SpinButton button = (SpinButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.spinButton.SpinButton.class);

        double step = button.getStep();

        return new LogoWord(step);
    }

    public final LogoObject pSETMODELTYPE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int modelType = aLogoObject[0].toInteger();

        if (modelType < 0 || modelType > 3)
            throw new LanguageException("Invalid model type.Please select an integer between 0 and 3.");

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.spinButton.SpinButton.class);

        for (int i = 0; i < v.size(); i++) {
            SpinButton button = (SpinButton) v.elementAt(i);

            button.setModelType(modelType);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pMODELTYPE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        SpinButton button = (SpinButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.spinButton.SpinButton.class);

        return new LogoWord(button.getModelType());
    }

    public final LogoObject pSETVALUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String value = aLogoObject[0].toString();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.spinButton.SpinButton.class);

        for (int i = 0; i < v.size(); i++) {
            SpinButton button = (SpinButton) v.elementAt(i);

            button.setValue(value);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pVALUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        SpinButton button = (SpinButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.spinButton.SpinButton.class);

        return new LogoWord(button.getModel().getStringValue());
    }

}
