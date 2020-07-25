package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateSlider.*;
import java.awt.*;


public class SliderPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {

        registerPrimitive("SLIDER.ENABLE", "pENABLE", 0);
        registerPrimitive("SLIDER.DISABLE", "pDISABLE", 0);
        registerPrimitive("SLIDER.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("SLIDER.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("SLIDER.SETMINORTICKSPACE", "pSETMINORTICKSPACE", 1);
        registerPrimitive("SLIDER.MINORTICKSPACE", "pMINORTICKSPACE", 0);
        registerPrimitive("SLIDER.SETMAJORTICKSPACE", "pSETMAJORTICKSPACE", 1);
        registerPrimitive("SLIDER.MAJORTICKSPACE", "pMAJORTICKSPACE", 0);
        registerPrimitive("SLIDER.SNAPTOTICKS", "pSNAPTOTICKS", 0);
        registerPrimitive("SLIDER.NOTSNAPTOTICKS", "pNOTSNAPTOTICKS", 0);
        registerPrimitive("SLIDER.PAINTLABELS", "pPAINTLABELS", 0);
        registerPrimitive("SLIDER.NOTPAINTLABELS", "pNOTPAINTLABELS", 0);
        registerPrimitive("SLIDER.VALUE", "pVALUE", 0);
        registerPrimitive("SLIDER.SETVALUE", "pSETVALUE", 1);
        //registerPrimitive("SLIDER.EXTENT",                        "pEXTENT", 0);
        //registerPrimitive("SLIDER.SETEXTENT",                     "pSETEXTENT", 1);
        registerPrimitive("SLIDER.ORIENTATION", "pORIENTATION", 0);
        registerPrimitive("SLIDER.SETORIENTATION", "pSETORIENTATION", 1);
        registerPrimitive("SLIDER.SETMAX", "pSETMAX", 1);
        registerPrimitive("SLIDER.SETMIN", "pSETMIN", 1);
        registerPrimitive("SLIDER.MAX", "pMAX", 0);
        registerPrimitive("SLIDER.MIN", "pMIN", 0);
        registerPrimitive("SLIDER.PAINTTRACK", "pPAINTTRACK", 0);
        registerPrimitive("SLIDER.NOTPAINTTRACK", "pNOTPAINTTRACK", 0);
        registerPrimitive("SLIDER.PAINTTICKS", "pPAINTTICKS", 0);
        registerPrimitive("SLIDER.NOTPAINTTICKS", "pNOTPAINTTICKS", 0);

        registerPrimitive("SLIDER.ENABLED", "pENABLED", 0);
        registerPrimitive("SLIDER.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("SLIDER.GETSNAPTOTICKS", "pGETSNAPTOTICKS", 0);
        registerPrimitive("SLIDER.LABELSPAINTED", "pLABELSPAINTED", 0);
        registerPrimitive("SLIDER.TRACKPAINTED", "pTRACKPAINTED", 0);
        registerPrimitive("SLIDER.TICKSPAINTED", "pTICKSPAINTED", 0);

        registerPrimitive("SLIDER.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("SLIDER.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("SLIDER.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("SLIDER.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded Slider primitives");

    }

    public final LogoObject pMINORTICKSPACE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int space;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        space = slider.getMinorTickSpacing();

        return new LogoWord(space);
    }

    public final LogoObject pMAJORTICKSPACE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int space;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        space = slider.getMajorTickSpacing();

        return new LogoWord(space);
    }

    public final LogoObject pSNAPTOTICKS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setSnapToTicks(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTSNAPTOTICKS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setSnapToTicks(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pVALUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int value;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        value = slider.getValue();

        return new LogoWord(value);
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setOpaque(true);
            slider.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setOpaque(false);
            slider.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETVALUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int value;

        value = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setValue(value);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETMINORTICKSPACE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int space;

        space = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setMinorTickSpacing(space);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETMAJORTICKSPACE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int space;

        space = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setMajorTickSpacing(space);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pORIENTATION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int orientation;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        orientation = slider.getOrientation();

        return new LogoWord(orientation);
    }

    public final LogoObject pSETORIENTATION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int orientation;

        orientation = aLogoObject[0].toInteger();
        if (orientation < 0 || orientation > 1)
            throw new LanguageException("Invalid orientation " + orientation + ". Please select between 0 (horizontal) and 1 (vertical).");

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setOrientation(orientation);
            slider.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pEXTENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int extent;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        extent = slider.getExtent();

        return new LogoWord(extent);
    }

    public final LogoObject pSETEXTENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int extent;

        extent = aLogoObject[0].toInteger();
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            if (extent < 0 || (slider.getMaximum() - slider.getMinimum() + 1) < extent)
                throw new LanguageException("Invalid extent value " + extent + ". Please select an integer between 1 and " + (slider.getMaximum() - slider.getMinimum()) + ".");

            slider.setExtent(extent);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pPAINTLABELS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintLabels(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTPAINTLABELS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintLabels(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pPAINTTRACK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintTrack(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTPAINTTRACK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintTrack(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pPAINTTICKS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintTicks(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTPAINTTICKS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setPaintTicks(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pMAX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int max;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        max = slider.getMaximum();

        return new LogoWord(max);
    }

    public final LogoObject pMIN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int min;

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        min = slider.getMinimum();

        return new LogoWord(min);
    }

    public final LogoObject pSETMAX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int max = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setMaximum(max);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETMIN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int min = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setMinimum(min);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pTICKSPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.getPaintTicks());
    }

    public final LogoObject pTRACKPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.getPaintTrack());
    }

    public final LogoObject pLABELSPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.getPaintLabels());
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.isOpaque());
    }

    public final LogoObject pGETSNAPTOTICKS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        return new LogoWord(slider.getSnapToTicks());
    }

    public final LogoObject pSETBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int red;
        int green;
        int blue;

        red = aLogoObject[0].pick(1).toInteger();
        green = aLogoObject[0].pick(2).toInteger();
        blue = aLogoObject[0].pick(3).toInteger();
        Color bgColor = new Color(red, green, blue);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setBackground(bgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int red;
        int green;
        int blue;

        red = aLogoObject[0].pick(1).toInteger();
        green = aLogoObject[0].pick(2).toInteger();
        blue = aLogoObject[0].pick(3).toInteger();
        Color fgColor = new Color(red, green, blue);
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateSlider slider = (ESlateSlider) v.elementAt(i);

            slider.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(slider.getBackground().getRed());
        LogoWord green = new LogoWord(slider.getBackground().getGreen());
        LogoWord blue = new LogoWord(slider.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateSlider slider = (ESlateSlider) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateSlider.ESlateSlider.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(slider.getForeground().getRed());
            LogoWord green = new LogoWord(slider.getForeground().getGreen());
            LogoWord blue = new LogoWord(slider.getForeground().getBlue());

            vColor.addElement(red);
            vColor.addElement(green);
            vColor.addElement(blue);
            return new LogoList(vColor);
        } catch (Exception exc) {
            exc.printStackTrace();
            return LogoVoid.obj;
        }
    }

}

