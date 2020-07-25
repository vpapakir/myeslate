package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the animation component
 * primitive group.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 24-Jun-2002
 * @see		gr.cti.eslate.scripting.logo.AnimationPrimitives
 */
public class AnimationPrimitivesResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"PLAYFROMFRAME", "����������FRAME"},
    {"PLAYFROMFRAMETOFRAME", "����������FRAME���FRAME"},
    {"PLAYFROMLABEL", "���������������"},
    {"PLAYFROMLABELTOLABEL", "�����������������������"},
    {"GOTOFRAME", "����������FRAME"},
  };
}
