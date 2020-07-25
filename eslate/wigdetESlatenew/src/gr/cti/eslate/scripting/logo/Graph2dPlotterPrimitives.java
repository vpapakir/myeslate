// 4Jun1999: Created by George Birbilis

package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.AsGraph2dPlotter; //

/**
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Kriton Kyrimis"
 * @version     2.0.1, 23-Jan-2008
 */
public class Graph2dPlotterPrimitives extends PrimitiveGroup //
{

    MyMachine myMachine; //uses new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("Graph.NEWGRAPH", "pNEWGRAPH", 1);
        registerPrimitive("Graph.NEWPOINT", "pNEWPOINT", 2);
        registerPrimitive("Graph.CLEARALL", "pCLEARALL", 0);

        myMachine=(MyMachine)machine; //uses new-scripting-mechanism

        if(console != null)
            console.putSetupMessage("Loaded Avakeeo's 2D Graph Plotter primitives");
    }

//Graph.NEWGRAPH//

    public final LogoObject pNEWGRAPH(InterpEnviron interpenviron, LogoObject alogoobject[]) //
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       String name=alogoobject[0].toString();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.scripting.AsGraph2dPlotter.class);
       for(int i=0;i<v.size();i++) ((AsGraph2dPlotter)v.elementAt(i)).newGraph(name);
       return LogoVoid.obj;
    }

//Graph.NEWPOINT//

    public final LogoObject pNEWPOINT(InterpEnviron interpenviron, LogoObject alogoobject[]) //
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       double x=alogoobject[0].toNumber();
       double y=alogoobject[1].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.scripting.AsGraph2dPlotter.class);
       for(int i=0;i<v.size();i++) ((AsGraph2dPlotter)v.elementAt(i)).newPoint(x,y);
       return LogoVoid.obj;
    }

//Graph.CLEARALL//

    public final LogoObject pCLEARALL(InterpEnviron interpenviron, LogoObject alogoobject[]) //
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.scripting.AsGraph2dPlotter.class);
       for(int i=0;i<v.size();i++) ((AsGraph2dPlotter)v.elementAt(i)).clearAll();
       return LogoVoid.obj;
    }

}
