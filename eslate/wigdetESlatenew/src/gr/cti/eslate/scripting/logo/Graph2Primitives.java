package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.scripting.AsGraph2;
import gr.cti.eslate.scripting.logo.convertions.LogoAWT;
import gr.cti.typeArray.DblBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.awt.Color;
import java.text.*;
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

/**
 * This class describes the Logo primitives implemented by the component.
 * 
 * @version     1.0.6, 17-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class Graph2Primitives extends PrimitiveGroup {
	/** Required for scripting. */
	private MyMachine myMachine;

	protected void setup(Machine machine, Console console)
			throws SetupException {
		myRegisterPrimitive("GRAPH.ADDFUNCTION", "pGRAPHADDFUNCTION", 1);
		myRegisterPrimitive("GRAPH.REMOVEFUNCTION", "pGRAPHREMOVEFUNCTION", 1);
		myRegisterPrimitive("GRAPH.FUNCTIONDOMAIN", "pGRAPHFUNCTIONDOMAIN", 3);
		myRegisterPrimitive("GRAPH.FUNCTIONCOLOR", "pGRAPHFUNCTIONCOLOR", 2);
		myRegisterPrimitive("GRAPH.ZOOM", "pGRAPHZOOM", 1);
		myRegisterPrimitive("GRAPH.VIEW", "pGRAPHVIEW", 2);
		myRegisterPrimitive("GRAPH.PLOT", "pGRAPHPLOT", 2);
		myRegisterPrimitive("GRAPH.PLOTCOLOR", "pGRAPHPLOTCOLOR", 1);
		myRegisterPrimitive("GRAPH.PLOTCLEAR", "pGRAPHPLOTCLEAR", 0);

		myRegisterPrimitive("GRAPH.SETXAXISFIELD", "pGRAPHSETXAXISFIELD", 1);
		myRegisterPrimitive("GRAPH.SETYAXISFIELD", "pGRAPHSETYAXISFIELD", 1);
		myRegisterPrimitive("GRAPH.XAXISFIELD", "pGRAPHXAXISFIELD", 0);
		myRegisterPrimitive("GRAPH.YAXISFIELD", "pGRAPHYAXISFIELD", 0);
		myRegisterPrimitive("GRAPH.XAXISMIN", "pGRAPHXAXISMIN", 0);
		myRegisterPrimitive("GRAPH.XAXISMAX", "pGRAPHXAXISMAX", 0);
		myRegisterPrimitive("GRAPH.SETXAXISMIN", "pGRAPHSETXAXISMIN", 1);
		myRegisterPrimitive("GRAPH.SETXAXISMAX", "pGRAPHSETXAXISMAX", 1);
		myRegisterPrimitive("GRAPH.YAXISMIN", "pGRAPHYAXISMIN", 0);
		myRegisterPrimitive("GRAPH.YAXISMAX", "pGRAPHYAXISMAX", 0);
		myRegisterPrimitive("GRAPH.XAXISORIGIN", "pGRAPHXAXISORIGIN", 0);
		myRegisterPrimitive("GRAPH.YAXISORIGIN", "pGRAPHYAXISORIGIN", 0);
		myRegisterPrimitive("GRAPH.SETXAXISORIGIN", "pGRAPHSETXAXISORIGIN", 1);

		myRegisterPrimitive("GRAPH.ADDDATASET", "pGRAPHADDDATASET", 2);
		myRegisterPrimitive("GRAPH.SETDATASET", "pGRAPHSETDATASET", 2);
		myRegisterPrimitive("GRAPH.REMOVEDATASET", "pGRAPHREMOVEDATASET", 1);
		myRegisterPrimitive("GRAPH.SETELEMENT", "pGRAPHSETELEMENT", 3);
		myRegisterPrimitive("GRAPH.ADDELEMENT", "pGRAPHADDELEMENT", 2);
		myRegisterPrimitive("GRAPH.REMOVEELEMENT", "pGRAPHREMOVEELEMENT", 2);
		myRegisterPrimitive("GRAPH.ELEMENT", "pGRAPHELEMENT", 2);
		myRegisterPrimitive("GRAPH.DATASET", "pGRAPHDATASET", 1);
		myRegisterPrimitive("GRAPH.TITLES", "pGRAPHTITLES", 0);
		myRegisterPrimitive("GRAPH.SETTITLE", "pGRAPHSETTITLE", 2);

		myMachine = (MyMachine) machine;

		if(console != null)
			console.putSetupMessage("Loaded ESlate's Graph2 component primitives");
	}

	private Vector<?> getComponents()
    {
		return myMachine.componentPrimitives.getComponentsToTell(
          AsGraph2.class
        );
	}

    private AsGraph2 getFirstComponent() throws LanguageException
    {
      return (AsGraph2)myMachine.componentPrimitives.getFirstComponentToTell(
        AsGraph2.class
      );
    }
	 
	//	if no instance is found, the "getFirstComponentsToTell" method will throw a LanguageException to the Logo console saying "No component to TELL this to!"
//	private AsGraph2 getFirstComponent() throws LanguageException 	{
//		return	(AsGraph2)myMachine.componentPrimitives.getFirstComponentToTell(AsGraph2.class);
//	}
	
	public final LogoObject pGRAPHADDFUNCTION(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String expression = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).addFunction(expression);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHREMOVEFUNCTION(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String expression = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).removeFunction(expression);
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pGRAPHFUNCTIONDOMAIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 3);
		String expression = logoObject[0].toString();
		double domainFrom = logoObject[1].toNumber();
		double domainTo = logoObject[2].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i)))
					.setFunctionDomain(expression, domainFrom, domainTo);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHFUNCTIONCOLOR(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String expression = logoObject[0].toString();
		Color color = LogoAWT.toColor(logoObject[1]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).setFunctionColor(expression, color);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHZOOM(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		boolean zoomIn = logoObject[0].toBoolean();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).zoom(zoomIn);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHVIEW(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		double from = logoObject[0].toNumber();
		double to = logoObject[1].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).setView(from, to);
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pGRAPHPLOT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		double x = logoObject[0].toNumber();
		double y = logoObject[1].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).plot(x, y);
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pGRAPHPLOTCOLOR(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		Color color = LogoAWT.toColor(logoObject[0]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).plotColor(color);
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pGRAPHPLOTCLEAR(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((AsGraph2) (vector.get(i))).plotClear();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHSETXAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
                String field = logoObject[0].toString();
		Vector<?> vector = getComponents();
                int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  boolean ok = ((AsGraph2) (vector.get(i))).setXAxisField(field);
          if (!ok) {
            String msg = MessageFormat.format(
              Graph2PrimitivesBundle.getMessage("CantUseX"), field
            );
            throw new LanguageException(msg);
          }
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pGRAPHSETYAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
        String field = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  boolean ok = ((AsGraph2) (vector.get(i))).setYAxisField(field);
          if (!ok) {
            String msg = MessageFormat.format(
            Graph2PrimitivesBundle.getMessage("CantUseY"), field
          );
          throw new LanguageException(msg);
          }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHXAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getXAxisField());
	}

	public final LogoObject pGRAPHYAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getYAxisField());
	}

	public final LogoObject pGRAPHXAXISMIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getXAxisMin());
	}

	public final LogoObject pGRAPHXAXISMAX(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getXAxisMax());
	}
	
	public final LogoObject pGRAPHYAXISMIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getYAxisMin());
	}

	public final LogoObject pGRAPHYAXISMAX(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        return new LogoWord(comp.getYAxisMax());
	}

	public final LogoObject pGRAPHSETXAXISMIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
        double min = logoObject[0].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  ((AsGraph2) (vector.get(i))).setXAxisMin(min);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHSETXAXISMAX(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
        double max = logoObject[0].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  ((AsGraph2) (vector.get(i))).setXAxisMax(max);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHXAXISORIGIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
        // Origin is always at the bottom left of the graph.
        return pGRAPHXAXISMIN(interpEnviron, logoObject);
	}

	public final LogoObject pGRAPHYAXISORIGIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
        // Origin is always at the bottom left of the graph.
        return pGRAPHYAXISMIN(interpEnviron, logoObject);
	}

	public final LogoObject pGRAPHSETXAXISORIGIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		return pGRAPHSETXAXISMIN(interpEnviron, logoObject);
	}


    /**
     * Converts a logo object, that is a list of numbers, to an array
     * of doubles.
     * @param   obj The logo object.
     * @return  An array of doubles.
     */
    private static double[] logoListToDoubleArray(LogoObject obj)
      throws LanguageException
    {
      LogoList list = (LogoList)obj;
      int nData = list.length();
      double[] data = new double[nData];
      for (int i=0; i<nData; i++) {
        data[i] = list.pickInPlace(i).toNumber();
      }
      return data;
    }

	public final LogoObject pGRAPHADDDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
        double[] data = logoListToDoubleArray(logoObject[1]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).addDataSet(title, data);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHSETDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
        double[] data = logoListToDoubleArray(logoObject[1]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).setDataSet(title, data);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHSETELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 3);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
		double value = logoObject[2].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).setElement(title, pos, value);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHADDELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		double value = logoObject[1].toNumber();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).addElement(title, value);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHREMOVEELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
          AsGraph2 g = (AsGraph2)vector.get(i);
          try {
            g.removeElement(title, pos);
          } catch (Exception e) {
            throw new LanguageException(e.getMessage(), e);
          }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHREMOVEDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String title = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).removeDataSet(title);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pGRAPHELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
        AsGraph2 comp = getFirstComponent();
        Double element;
        try {
          element = (Double)comp.getElement(title, pos);
        } catch (Exception e) {
          throw new LanguageException(e.getMessage(), e);
        }
        return new LogoWord(element);
	}

	public final LogoObject pGRAPHDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String title = logoObject[0].toString();
        AsGraph2 comp = getFirstComponent();
        DblBaseArray values;
        try {
          values = (DblBaseArray)comp.getDataSet(title);
        } catch (Exception e) {
          throw new LanguageException(e.getMessage(), e);
        }
        int nValues = values.size();
        LogoObject[] v = new LogoObject[nValues];
        for (int i=0; i<nValues; i++) {
          v[i] = new LogoWord(values.get(i));
        }
        return new LogoList(v);
	}

	public final LogoObject pGRAPHTITLES(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsGraph2 comp = getFirstComponent();
        StringBaseArray titles;
        titles = (StringBaseArray)comp.getDataSetTitles();
        int nTitles = titles.size();
        LogoObject[] t = new LogoObject[nTitles];
        for (int i=0; i<nTitles; i++) {
          t[i] = new LogoWord(titles.get(i));
        }
        return new LogoList(t);
	}

	public final LogoObject pGRAPHSETTITLE(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String oldTitle = logoObject[0].toString();
		String newTitle = logoObject[1].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsGraph2) (vector.get(i))).setDataSetTitle(oldTitle, newTitle);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}
	
	/**
	 * Register a LOGO primitive using both a default English name and a
	 * localized name.
	 * 
	 * @param pName
	 *            The name of the primitive.
	 * @param method
	 *            The name of the method implementing the primitive.
	 * @param nArgs
	 *            The number of arguments of the method implementing the
	 *            primitive.
	 * @throws SetupException
	 */
	private void myRegisterPrimitive(String pName, String method, int nArgs)
			throws SetupException {
		// Register localized primitive name.
		registerPrimitive(Graph2PrimitivesBundle.getMessage(pName), method, nArgs);
		// Register default English primitive name.
		if (!ESlateMicroworld.getCurrentLocale().getLanguage().equals("en"))
			registerPrimitive(pName, method, nArgs);
	}
}
