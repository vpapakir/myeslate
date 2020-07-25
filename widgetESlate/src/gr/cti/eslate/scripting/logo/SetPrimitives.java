package gr.cti.eslate.scripting.logo;

import java.awt.*;
import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.set.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the set
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.set.Set
 */
public class SetPrimitives extends PrimitiveGroup
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = null;

  /**
   * Required for scripting.
   */
  MyMachine myMachine;

  /**
   * Exception thrown in <code>invokeAndWait</code>.
   */
  private Exception pendingException = null;

  /**
   * Register primitives.
   */
  protected void setup(Machine machine, Console console)
    throws SetupException
  {
    try {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.scripting.logo.SetResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("SELECTSUBSET", "pSELECTSUBSET", 1);
    myRegisterPrimitive("CLEARSELECTEDSUBSET", "pCLEARSELECTEDSUBSET", 0);
    myRegisterPrimitive("QUERYINSET", "pQUERYINSET", 0);
    myRegisterPrimitive("DELETEELLIPSE", "pDELETEELLIPSE", 1);
    myRegisterPrimitive("SETTABLEINSET", "pSETTABLEINSET", 1);
    myRegisterPrimitive("TABLEINSET", "pTABLEINSET", 0);
    myRegisterPrimitive("TABLESINSET", "pTABLESINSET", 0);
    myRegisterPrimitive("SETPROJECTIONFIELD", "pSETPROJECTIONFIELD", 1);
    myRegisterPrimitive("PROJECTIONFIELD", "pPROJECTIONFIELD", 0);
    myRegisterPrimitive("PROJECTIONFIELDS", "pPROJECTIONFIELDS", 0);
    myRegisterPrimitive("PROJECTINGFIELDS", "pPROJECTINGFIELDS", 0);
    myRegisterPrimitive("SETCALCULATIONTYPE", "pSETCALCULATIONTYPE", 1);
    myRegisterPrimitive("CALCULATIONTYPE", "pCALCULATIONTYPE", 0);
    myRegisterPrimitive("CALCULATIONTYPES", "pCALCULATIONTYPES", 0);
    myRegisterPrimitive("SETCALCULATIONFIELD", "pSETCALCULATIONFIELD", 1);
    myRegisterPrimitive("CALCULATIONFIELD", "pCALCULATIONFIELD", 0);
    myRegisterPrimitive("CALCULATIONFIELDS", "pCALCULATIONFIELDS", 0);
    myRegisterPrimitive("PROJECTFIELD", "pPROJECTFIELD", 1);
    myRegisterPrimitive("CALCULATEINSET", "pCALCULATEINSET", 1);
    myRegisterPrimitive("CALCULATINGINSET", "pCALCULATINGINSET", 0);
    myRegisterPrimitive("NEWELLIPSE", "pNEWELLIPSE", 0);
    myRegisterPrimitive("ACTIVATEELLIPSE", "pACTIVATEELLIPSE", 1);
    myRegisterPrimitive("DEACTIVATEELLIPSE", "pDEACTIVATEELLIPSE", 0);

    myMachine = (MyMachine)machine;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Selects a subset.
   */
  public final LogoObject pSELECTSUBSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    if (!(obj[0] instanceof LogoList)) {
      throw new LanguageException(resources.getString("badSelection"));
    }
    LogoList list = ((LogoList)obj[0]);
    int length = list.length();
    if ((length < 2) || (length > 3)) {
      throw new LanguageException(resources.getString("badSelection"));
    }
    if (length == 2) {
      int x, y;
      try {
        x = list.pickInPlace(0).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badX"));
      }
      try {
        y = list.pickInPlace(1).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badY"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        selectSubset(set, x, y);
      }
    }else{
      boolean a, b, c;
      try {
        a = list.pickInPlace(0).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      try {
        b = list.pickInPlace(1).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      try {
        c = list.pickInPlace(2).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        selectSubset(set, a, b, c);
      }
    }
    return LogoVoid.obj;
  }

  private void selectSubset(AsSet set, int x, int y) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.selectSubset(x, y);
    }else{
      final AsSet s = set;
      final int xx = x;
      final int yy = y;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.selectSubset(xx, yy);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  private void selectSubset(AsSet set, boolean a, boolean b, boolean c)
    throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.selectSubset(a, b, c);
    }else{
      final AsSet s = set;
      final boolean aa = a;
      final boolean bb = b;
      final boolean cc = c;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.selectSubset(aa, bb, cc);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  /**
   * Clears the selected subset.
   */
  public final LogoObject pCLEARSELECTEDSUBSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      AsSet set = (AsSet)v.elementAt(i);
      clearSelectedSubset(set);
    }
    return LogoVoid.obj;
  }

  private void clearSelectedSubset(AsSet set) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.clearSelectedSubset();
    }else{
      final AsSet s = set;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.clearSelectedSubset();
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  /**
   * Returns the description of the selected subset.
   */
  public final LogoObject pQUERYINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s = set.getSelText();
    return new LogoWord(s);
  }

  /**
   * Deletes one or more ellipses.
   */
  public final LogoObject pDELETEELLIPSE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    if (!(obj[0] instanceof LogoList)) {
      throw new LanguageException(resources.getString("badSelection"));
    }
    LogoList list = ((LogoList)obj[0]);
    int length = list.length();
    if ((length < 2) || (length > 3)) {
      throw new LanguageException(resources.getString("badSelection"));
    }
    if (length == 2) {
      int x, y;
      try {
        x = list.pickInPlace(0).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badX"));
      }
      try {
        y = list.pickInPlace(1).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badY"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        deleteEllipse(set, x, y);
      }
    }else{
      boolean a, b, c;
      try {
        a = list.pickInPlace(0).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      try {
        b = list.pickInPlace(1).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      try {
        c = list.pickInPlace(2).toBoolean();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badBoolean"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        deleteEllipse(set, a, b, c);
      }
    }
    return LogoVoid.obj;
  }

  private void deleteEllipse(AsSet set, int x, int y) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.deleteEllipse(x, y);
    }else{
      final AsSet s = set;
      final int xx = x;
      final int yy = y;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.deleteEllipse(xx, yy);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  private void deleteEllipse(AsSet set, boolean a, boolean b, boolean c)
    throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.deleteEllipse(a, b, c);
    }else{
      final AsSet s = set;
      final boolean aa = a;
      final boolean bb = b;
      final boolean cc = c;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.deleteEllipse(aa, bb, cc);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Select the field projected onto the component.
   */
  public final LogoObject pSETPROJECTIONFIELD(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      try {
        AsSet set = (AsSet)v.elementAt(i);
        setProjectionField(set, s);
      } catch (SetException e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  private void setProjectionField(AsSet set, String st)
    throws LanguageException, SetException
  {
    if (EventQueue.isDispatchThread()) {
      set.setProjectionField(st);
    }else{
      final AsSet s = set;
      final String ss = st;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            pendingException = null;
            try {
              s.setProjectionField(ss);
            } catch (SetException e) {
              pendingException = e;
            }
          }
        });
        if (pendingException == null) {
          throw new LanguageException(pendingException);
        }
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Return the name of the field projected onto the component.
   */
  public final LogoObject pPROJECTIONFIELD(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s = set.getProjectionField();
    if (s == null) {
      throw new LanguageException(resources.getString("noProjField"));
    }
    return new LogoWord(s);
  }

  /**
   * Returns the names of the tables that can be displayed.
   */
  public final LogoObject pPROJECTIONFIELDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s[] = set.getProjectionFields();
    LogoObject[] lo = new LogoObject[s.length];
    for (int i=0; i<s.length; i++) {
      lo[i] = new LogoWord(s[i]);
    }
    return new LogoList(lo);
  }

  /**
   * Select the calculation operation to perform.
   */
  public final LogoObject pSETCALCULATIONTYPE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      try {
        AsSet set = (AsSet)v.elementAt(i);
        setCalcOp(set, s);
      } catch (SetException e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  private void setCalcOp(AsSet set, String st)
    throws LanguageException, SetException
  {
    if (EventQueue.isDispatchThread()) {
      set.setCalcOp(st);
    }else{
      final AsSet s = set;
      final String ss = st;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            pendingException = null;
            try {
              s.setCalcOp(ss);
            } catch (SetException e) {
              pendingException = e;
            }
          }
        });
        if (pendingException == null) {
          throw new LanguageException(pendingException);
        }
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Return the name of the calculation operation that is being performed.
   */
  public final LogoObject pCALCULATIONTYPE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s = set.getCalcOp();
    if (s == null) {
      throw new LanguageException(resources.getString("noCalcOp"));
    }
    return new LogoWord(s);
  }

  /**
   * Return the names of the calculation operations that can be performed.
   */
  public final LogoObject pCALCULATIONTYPES(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s[] = set.getCalcOps();
    LogoObject[] lo = new LogoObject[s.length];
    for (int i=0; i<s.length; i++) {
      lo[i] = new LogoWord(s[i]);
    }
    return new LogoList(lo);
  }

  /**
   * Select the field on which calculations will be performed.
   */
  public final LogoObject pSETCALCULATIONFIELD(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      try {
        AsSet set = (AsSet)v.elementAt(i);
        setCalcKey(set, s);
      } catch (SetException e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  private void setCalcKey(AsSet set, String st)
    throws LanguageException, SetException
  {
    if (EventQueue.isDispatchThread()) {
      set.setCalcKey(st);
    }else{
      final AsSet s = set;
      final String ss = st;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            pendingException = null;
            try {
              s.setCalcKey(ss);
            } catch (SetException e) {
              pendingException = e;
            }
          }
        });
        if (pendingException == null) {
          throw new LanguageException(pendingException);
        }
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Return the name of the field on which calculations are being performed.
   */
  public final LogoObject pCALCULATIONFIELD(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s = set.getCalcKey();
    if (s == null) {
      throw new LanguageException(resources.getString("noCalcField"));
    }
    return new LogoWord(s);
  }

  /**
   * Return the names of the fields on which calculations can be performed.
   */
  public final LogoObject pCALCULATIONFIELDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s[] = set.getCalcKeys();
    LogoObject[] lo = new LogoObject[s.length];
    for (int i=0; i<s.length; i++) {
      lo[i] = new LogoWord(s[i]);
    }
    return new LogoList(lo);
  }

  /**
   * Select the table to display.
   */
  public final LogoObject pSETTABLEINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      try {
        AsSet set = (AsSet)v.elementAt(i);
        setSelectedTable(set, s);
      } catch (SetException e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  private void setSelectedTable(AsSet set, String st)
    throws LanguageException, SetException
  {
    if (EventQueue.isDispatchThread()) {
      set.setSelectedTable(st);
    }else{
      final AsSet s = set;
      final String ss = st;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            pendingException = null;
            try {
              s.setSelectedTable(ss);
            } catch (SetException e) {
              pendingException = e;
            }
          }
        });
        if (pendingException == null) {
          throw new LanguageException(pendingException);
        }
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Returns the name of the displayed table.
   */
  public final LogoObject pTABLEINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s = set.getSelectedTable();
    if (s == null) {
      throw new LanguageException(resources.getString("noTable"));
    }
    return new LogoWord(s);
  }

  /**
   * Returns the names of the tables that can be displayed.
   */
  public final LogoObject pTABLESINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    String s[] = set.getTables();
    LogoObject[] lo = new LogoObject[s.length];
    for (int i=0; i<s.length; i++) {
      lo[i] = new LogoWord(s[i]);
    }
    return new LogoList(lo);
  }

  /**
   * Specify whether the selected projection field should be displayed.
   */
  public final LogoObject pPROJECTFIELD(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    boolean b = obj[0].toBoolean();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      AsSet set = (AsSet)v.elementAt(i);
      setProject(set, b);
    }
    return LogoVoid.obj;
  }

  private void setProject(AsSet set, boolean b) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.setProject(b);
    }else{
      final AsSet s = set;
      final boolean bb = b;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.setProject(bb);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Returns whether the selected projection field is being displayed.
   */
  public final LogoObject pPROJECTINGFIELDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    boolean b = set.isProjecting();
    return new LogoWord(b);
  }

  /**
   * Specify whether individual elements or calculated data should be
   * displayed in the set panels.
   */
  public final LogoObject pCALCULATEINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    boolean b = obj[0].toBoolean();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      AsSet set = (AsSet)v.elementAt(i);
      setCalculate(set, b);
    }
    return LogoVoid.obj;
  }

  private void setCalculate(AsSet set, boolean b) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.setCalculate(b);
    }else{
      final AsSet s = set;
      final boolean bb = b;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.setCalculate(bb);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Returns whether calculations are displayed.
   */
  public final LogoObject pCALCULATINGINSET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSet set = (AsSet)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsSet.class
    );
    boolean b = set.isCalculating();
    return new LogoWord(b);
  }

  /**
   * Creates and activates a new ellipse.
   */
  public final LogoObject pNEWELLIPSE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      AsSet set = (AsSet)v.elementAt(i);
      newEllipse(set);
    }
    return LogoVoid.obj;
  }

  private void newEllipse(AsSet set) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.newEllipse();
    }else{
      final AsSet s = set;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.newEllipse();
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  /**
   * Activates an ellipse.
   */
  public final LogoObject pACTIVATEELLIPSE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    if ((obj[0] instanceof LogoList)) {
      LogoList list = ((LogoList)obj[0]);
      int length = list.length();
      if ((length != 2)) {
        throw new LanguageException(resources.getString("badActivation"));
      }
      int x, y;
      try {
        x = list.pickInPlace(0).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badX"));
      }
      try {
        y = list.pickInPlace(1).toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badY"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        activateEllipse(set, x, y);
      }
    }else{
      int n;
      try {
        n = obj[0].toInteger();
      } catch (LanguageException le) {
        throw new LanguageException(resources.getString("badEllipse"));
      }
      if ((n < 0) || (n > 2)) {
        throw new LanguageException(resources.getString("badEllipse"));
      }
      Vector<?> v =
        myMachine.componentPrimitives.getComponentsToTell(
          gr.cti.eslate.scripting.AsSet.class);
      for (int i=0; i<v.size(); i++) {
        AsSet set = (AsSet)v.elementAt(i);
        activateEllipse(set, n);
      }
    }
    return LogoVoid.obj;
  }

  private void activateEllipse(AsSet set, int x, int y) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.activateEllipse(x, y);
    }else{
      final AsSet s = set;
      final int xx = x;
      final int yy = y;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.activateEllipse(xx, yy);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }

  private void activateEllipse(AsSet set, int n) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.activateEllipse(n);
    }else{
      final AsSet s = set;
      final int nn = n;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.activateEllipse(nn);
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Deactivates the active ellipse.
   */
  public final LogoObject pDEACTIVATEELLIPSE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSet.class);
    for (int i=0; i<v.size(); i++) {
      AsSet set = (AsSet)v.elementAt(i);
      deactivateEllipse(set);
    }
    return LogoVoid.obj;
  }

  private void deactivateEllipse(AsSet set) throws LanguageException
  {
    if (EventQueue.isDispatchThread()) {
      set.deactivateEllipse();
    }else{
      final AsSet s = set;
      try {
        EventQueue.invokeAndWait(new Runnable()
        {
          public void run()
          {
            s.deactivateEllipse();
          }
        });
      } catch (Exception e) {
        throw new LanguageException(e);
      }
    }
  }


  /**
   * Register a LOGO primitive using both a default english name and
   * a localized name.
   * @param     pName   The name of the primitive.
   * @param     method  The name of the method implementing the primitive.
   * @param     nArgs   The number of arguments of the method implementing the
   *                    primitive.
   */
  private void myRegisterPrimitive(String pName, String method, int nArgs)
    throws SetupException
  {
    // Register localized primitive name.
    registerPrimitive(resources.getString(pName), method, nArgs);
    // Register default english primitive name.
    if (!ESlateMicroworld.getCurrentLocale().getLanguage().equals("en")) {
      registerPrimitive(pName, method, nArgs);
    }
  }
}
