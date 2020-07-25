package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.graph2.BarChart;
import gr.cti.eslate.scripting.AsBarChart;
import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.DblBaseArray;
import gr.cti.typeArray.StringBaseArray;

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
 * @version     1.0.6, 18-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BarChartPrimitives extends PrimitiveGroup {
	/** Required for scripting. */
	private MyMachine myMachine;

	protected void setup(Machine machine, Console console)
			throws SetupException {
		myRegisterPrimitive("BARCHART.ZOOM", "pBARCHARTZOOM", 1);

		myRegisterPrimitive("BARCHART.SETXAXISFIELD", "pBARCHARTSETXAXISFIELD", 1);
		myRegisterPrimitive("BARCHART.SETYAXISFIELD", "pBARCHARTSETYAXISFIELD", 1);
        myRegisterPrimitive("BARCHART.XAXISFIELD", "pBARCHARTXAXISFIELD", 0);
		myRegisterPrimitive("BARCHART.YAXISFIELD", "pBARCHARTYAXISFIELD", 0);
		myRegisterPrimitive("BARCHART.YAXISMIN", "pBARCHARTYAXISMIN", 0);
		myRegisterPrimitive("BARCHART.YAXISMAX", "pBARCHARTYAXISMAX", 0);
        myRegisterPrimitive("BARCHART.YAXISORIGIN", "pBARCHARTYAXISORIGIN", 0);

		myRegisterPrimitive("BARCHART.ADDNUMBERDATASET", "pBARCHARTADDNUMBERDATASET", 2);
		myRegisterPrimitive("BARCHART.ADDNAMEDATASET", "pBARCHARTADDNAMEDATASET", 2);
		myRegisterPrimitive("BARCHART.SETDATASET", "pBARCHARTSETDATASET", 2);
		myRegisterPrimitive("BARCHART.REMOVEDATASET", "pBARCHARTREMOVEDATASET", 1);
		myRegisterPrimitive("BARCHART.SETELEMENT", "pBARCHARTSETELEMENT", 3);
		myRegisterPrimitive("BARCHART.ADDELEMENT", "pBARCHARTADDELEMENT", 2);
		myRegisterPrimitive("BARCHART.REMOVEELEMENT", "pBARCHARTREMOVEELEMENT", 2);
		myRegisterPrimitive("BARCHART.ELEMENT", "pBARCHARTELEMENT", 2);
		myRegisterPrimitive("BARCHART.DATASET", "pBARCHARTDATASET", 1);
		myRegisterPrimitive("BARCHART.TITLES", "pBARCHARTTITLES", 0);
		myRegisterPrimitive("BARCHART.SETTITLE", "pBARCHARTSETTITLE", 2);

		myMachine = (MyMachine) machine;

		if(console != null)
			console.putSetupMessage("Loaded ESlate's BarChart component primitives");
	}

	private Vector<?> getComponents()
    {
		return myMachine.componentPrimitives.getComponentsToTell(
          AsBarChart.class
        );
	}

    private AsBarChart getFirstComponent() throws LanguageException
    {
      return (AsBarChart)myMachine.componentPrimitives.getFirstComponentToTell(
        AsBarChart.class
      );
    }

	//	if no instance is found, the "getFirstComponentsToTell" method will throw a LanguageException to the Logo console saying "No component to TELL this to!"
//	private AsBarChart getFirstComponent() throws LanguageException 	{
//		return	(AsBarChart)myMachine.componentPrimitives.getFirstComponentToTell(BarChart.class);
//	}
	
	public final LogoObject pBARCHARTZOOM(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		boolean zoomIn = logoObject[0].toBoolean();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
			((BarChart) (vector.get(i))).zoom(zoomIn);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTSETXAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
                String field = logoObject[0].toString();
		Vector<?> vector = getComponents();
                int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  boolean ok = ((BarChart) (vector.get(i))).setXAxisField(field);
          if (!ok) {
            String msg = MessageFormat.format(
              BarChartPrimitivesBundle.getMessage("CantUseX"), field
            );
            throw new LanguageException(msg);
          }
		}
		return LogoVoid.obj;
	}
	
	public final LogoObject pBARCHARTSETYAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
                String field = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
		  boolean ok = ((BarChart) (vector.get(i))).setYAxisField(field);
          if (!ok) {
            String msg = MessageFormat.format(
            BarChartPrimitivesBundle.getMessage("CantUseY"), field
          );
          throw new LanguageException(msg);
          }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTXAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsBarChart comp = getFirstComponent();
        return new LogoWord(comp.getXAxisField());
	}

	public final LogoObject pBARCHARTYAXISFIELD(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsBarChart comp = getFirstComponent();
        return new LogoWord(comp.getYAxisField());
	}

	public final LogoObject pBARCHARTYAXISMIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
          AsBarChart comp = getFirstComponent();
          return new LogoWord(comp.getYAxisMin());
	}

	public final LogoObject pBARCHARTYAXISMAX(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
          AsBarChart comp = getFirstComponent();
          return new LogoWord(comp.getYAxisMax());
    }

	public final LogoObject pBARCHARTYAXISORIGIN(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
        // Origin is always at the bottom left of the graph.
        return pBARCHARTYAXISMIN(interpEnviron, logoObject);
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

    /**
     * Converts a logo object, that is a list of strings, to an array
     * of strings.
     * @param   obj The logo object.
     * @return  An array of strings.
     */
    private static String[] logoListToStringArray(LogoObject obj)
      throws LanguageException
    {
      LogoList list = (LogoList)obj;
      int nData = list.length();
      String[] data = new String[nData];
      for (int i=0; i<nData; i++) {
        data[i] = list.pickInPlace(i).toString();
      }
      return data;
    }

	public final LogoObject pBARCHARTADDNUMBERDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
        double[] data = logoListToDoubleArray(logoObject[1]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsBarChart) (vector.get(i))).addDataSet(title, data);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTADDNAMEDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
        String[] data = logoListToStringArray(logoObject[1]);
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsBarChart) (vector.get(i))).addDataSet(title, data);
            } catch (Exception e) {
              e.printStackTrace();
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTSETDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              AsBarChart bc = (AsBarChart)vector.get(i);
              if (bc.getDataSetType(title) == BarChart.NUMBER) {
                double[] data = logoListToDoubleArray(logoObject[1]);
                bc.setDataSet(title, data);
              }else{
                String[] data = logoListToStringArray(logoObject[1]);
                bc.setDataSet(title, data);
              }
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTSETELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 3);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              AsBarChart bc = (AsBarChart)vector.get(i);
              if (bc.getDataSetType(title) == BarChart.NUMBER) {
                double value = logoObject[2].toNumber();
                bc.setElement(title, pos, value);
              }else{
                String value = logoObject[2].toString();
                bc.setElement(title, pos, value);
              }
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTADDELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              AsBarChart bc = (AsBarChart)vector.get(i);
              if (bc.getDataSetType(title) == BarChart.NUMBER) {
                double value = logoObject[1].toNumber();
                bc.addElement(title, value);
              }else{
                String value = logoObject[1].toString();
                bc.addElement(title, value);
              }
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTREMOVEELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
          AsBarChart bc = (AsBarChart)vector.get(i);
          try {
            bc.removeElement(title, pos);
          } catch (Exception e) {
            throw new LanguageException(e.getMessage(), e);
          }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTREMOVEDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String title = logoObject[0].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsBarChart) (vector.get(i))).removeDataSet(title);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	public final LogoObject pBARCHARTELEMENT(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String title = logoObject[0].toString();
		int pos = logoObject[1].toInteger();
        AsBarChart bc = getFirstComponent();
        LogoWord result;
        try {
          if (bc.getDataSetType(title) == BarChart.NUMBER) {
            Double element = (Double)bc.getElement(title, pos);
            result = new LogoWord(element);
          }else{
            String element = (String)bc.getElement(title, pos);
            result = new LogoWord(element);
          }
        } catch (Exception e) {
          throw new LanguageException(e.getMessage(), e);
        }
        return result;
	}

	public final LogoObject pBARCHARTDATASET(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 1);
		String title = logoObject[0].toString();
        AsBarChart bc = getFirstComponent();
        try {
          ArrayBase vals = bc.getDataSet(title);
          int nValues = vals.size();
          LogoObject[] v = new LogoObject[nValues];
          if (bc.getDataSetType(title) == BarChart.NUMBER) {
            DblBaseArray values = (DblBaseArray)vals;
            for (int i=0; i<nValues; i++) {
              v[i] = new LogoWord(values.get(i));
            }
          }else{
            StringBaseArray values = (StringBaseArray)vals;
            for (int i=0; i<nValues; i++) {
              v[i] = new LogoWord(values.get(i));
            }
          }
          return new LogoList(v);
        } catch (Exception e) {
          throw new LanguageException(e.getMessage(), e);
        }
	}

	public final LogoObject pBARCHARTTITLES(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 0);
        AsBarChart bc = getFirstComponent();
        StringBaseArray titles;
        titles = (StringBaseArray)bc.getDataSetTitles();
        int nTitles = titles.size();
        LogoObject[] t = new LogoObject[nTitles];
        for (int i=0; i<nTitles; i++) {
          t[i] = new LogoWord(titles.get(i));
        }
        return new LogoList(t);
	}

	public final LogoObject pBARCHARTSETTITLE(InterpEnviron interpEnviron,
			LogoObject logoObject[]) throws LanguageException {
		testNumParams(logoObject, 2);
		String oldTitle = logoObject[0].toString();
		String newTitle = logoObject[1].toString();
		Vector<?> vector = getComponents();
        int nComponents = vector.size();
		for (int i = 0; i < nComponents; i++) {
            try {
              ((AsBarChart) (vector.get(i))).setDataSetTitle(oldTitle, newTitle);
            } catch (Exception e) {
              throw new LanguageException(e.getMessage(), e);
            }
		}
		return LogoVoid.obj;
	}

	/**
	 * Register a LOGO primitive using both a default english name and a
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
		registerPrimitive(BarChartPrimitivesBundle.getMessage(pName), method, nArgs);
		// Register default English primitive name.
		if (!ESlateMicroworld.getCurrentLocale().getLanguage().equals("en"))
			registerPrimitive(pName, method, nArgs);
	}
}
