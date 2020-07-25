package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the animation component
 * primitive group.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 24-Jun-2002
 * @see		gr.cti.eslate.scripting.logo.AnimationPrimitives
 */
public class AnimationPrimitivesResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"PLAYFROMFRAME", "PLAYFROMFRAME"},
    {"PLAYFROMFRAMETOFRAME", "PLAYFROMFRAMETOFRAME"},
    {"PLAYFROMLABEL", "PLAYFROMLABEL"},
    {"PLAYFROMLABELTOLABEL", "PLAYFROMLABELTOLABEL"},
    {"GOTOFRAME", "GOTOFRAME"},
  };
}