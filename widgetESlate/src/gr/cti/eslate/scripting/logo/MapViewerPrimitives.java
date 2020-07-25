package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.mapViewer.MapViewer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.typeArray.IntBaseArray;

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

public class MapViewerPrimitives extends PrimitiveGroup {
    private static ResourceBundle bundle,bundleMessages;

    public MapViewerPrimitives() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.mapViewer.BundlePrimitives",Locale.getDefault());
    }

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("MB.SETLAYERVISIBLE", "pSETLAYERVISIBLE", 2);
        registerPrimitive("MB.ISLAYERVISIBLE", "pISLAYERVISIBLE", 1);
        registerPrimitive("MB.SETLAYERORDER", "pSETLAYERORDER", 2);
        registerPrimitive("MB.PROMOTELAYER", "pPROMOTELAYER", 1);
        registerPrimitive("MB.DEMOTELAYER", "pDEMOTELAYER", 1);
        registerPrimitive("MB.LAYERORDER", "pLAYERORDER", 1);
        registerPrimitive("MB.AGENTPIXELX", "pAGENTPIXELX", 1);
        registerPrimitive("MB.AGENTPIXELY", "pAGENTPIXELY", 1);

        myMachine=(MyMachine) machine;
        myConsole=console;
        if (console!=null)
            console.putSetupMessage(bundle.getString("setup"));
    }

    public final LogoObject pSETLAYERVISIBLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,2);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN))
                        layers[j].setVisible(aLogoObject[1].toBoolean());
            } catch(Exception e) {
                throw new LanguageException(bundle.getString("layervisibilityunchanged"));
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pISLAYERVISIBLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN))
                        return new LogoWord(layers[j].isVisible());
            } catch(Exception e) {
                throw new LanguageException(e.getMessage());
            }
        }
        throw new LanguageException(bundle.getString("layerdoesnotexist"));
    }

    public final LogoObject pSETLAYERORDER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,2);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                int pos=0;
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN)) {
                        pos=j;
                        break;
                    }
                int newpos=aLogoObject[1].toInteger();
                if (newpos>layers.length || newpos<1)
                    throw new LanguageException(bundle.getString("layerincorrectorder"));
                //Revert the order. The internal order is reverse-bottommost is 0.
                newpos=layers.length-newpos;
                ((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().reorderLayers(reorder(pos,newpos,layers.length));
            } catch(Exception e) {
                throw new LanguageException(bundle.getString("layerincorrectorder"));
            }
        }
        return LogoVoid.obj;
    }

    private int[] reorder(int pos,int newpos,int size) {
        //Construct the new order
        int[] neworder=new int[size];
        IntBaseArray a=new IntBaseArray();
        for (int j=0;j<size;j++)
            a.add(j);
            a.remove(pos);
            a.add(newpos,pos);
        for (int j=0;j<size;j++)
            neworder[j]=a.get(j);
        return neworder;
    }

    public final LogoObject pPROMOTELAYER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                int pos=0;
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN)) {
                        pos=j;
                        break;
                    }
                //Can't promote any more
                if (pos==layers.length-1)
                    return LogoVoid.obj;
                ((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().swapLayers(pos,pos+1);
            } catch(Exception e) {
                throw new LanguageException(bundle.getString("layerincorrectorder"));
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pDEMOTELAYER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                int pos=0;
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN)) {
                        pos=j;
                        break;
                    }
                //Can't demote any more
                if (pos==0)
                    return LogoVoid.obj;
                ((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().swapLayers(pos,pos-1);
            } catch(Exception e) {
                throw new LanguageException(bundle.getString("layerincorrectorder"));
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pLAYERORDER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                ILayerView[] layers=((MapViewer) v.elementAt(i)).getMap().getActiveRegionView().getLayerViews();
                String givenLN=aLogoObject[0].toString();
                for (int j=0;j<layers.length;j++)
                    if (layers[j].getName().equalsIgnoreCase(givenLN))
                        return new LogoWord(layers.length-j);
            } catch(Exception e) {
                throw new LanguageException(bundle.getString("layerdoesnotexist"));
            }
        }
        throw new LanguageException(bundle.getString("layerdoesnotexist"));
    }

    public final LogoObject pAGENTPIXELX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                gr.cti.eslate.protocol.IAgent ag=(gr.cti.eslate.protocol.IAgent) ((MapViewer) v.elementAt(i)).getAgent(aLogoObject[0].toString());
                double[] src=new double[]{ag.getLongitude(),ag.getLatitude()};
                double[] dst=new double[2];
                ((MapViewer) v.elementAt(i)).getPositionTransform().transform(src,0,dst,0,1);
                return new LogoWord((int) dst[0]);
            } catch(Exception e) {
                e.printStackTrace();
                throw new LanguageException(bundle.getString("agentdoesnotexist"));
            }
        }
        throw new LanguageException(bundle.getString("agentdoesnotexist"));
    }

    public final LogoObject pAGENTPIXELY(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.mapViewer.MapViewer.class);
        for (int i=0;i<v.size();i++) {
            try {
                gr.cti.eslate.protocol.IAgent ag=(gr.cti.eslate.protocol.IAgent) ((MapViewer) v.elementAt(i)).getAgent(aLogoObject[0].toString());
                double[] src=new double[]{ag.getLongitude(),ag.getLatitude()};
                double[] dst=new double[2];
                ((MapViewer) v.elementAt(i)).getPositionTransform().transform(src,0,dst,0,1);
                return new LogoWord((int) dst[1]);
            } catch(Exception e) {
                throw new LanguageException(e.getMessage());
            }
        }
        throw new LanguageException(bundle.getString("agentdoesnotexist"));
    }

    private MyMachine myMachine;
    private Console myConsole;
}
