package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the vector component primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.scripting.logo.VectorComponentPrimitives
 */
public class VectorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SETVECTOR", "SETVECTOR"},
    {"VECTOR", "VECTOR"},
    {"SETVECTORNORTH", "SETVECTORNORTH"},
    {"VECTORNORTH", "VECTORNORTH"},
    {"SETVECTOREAST", "SETVECTOREAST"},
    {"VECTOREAST", "VECTOREAST"},
    {"SETVECTORPOLAR", "SETVECTORPOLAR"},
    {"VECTORPOLAR", "VECTORPOLAR"},
    {"SETVECTORLENGTH", "SETVECTORLENGTH"},
    {"VECTORLENGTH", "VECTORLENGTH"},
    {"SETVECTORANGLE", "SETVECTORANGLE"},
    {"VECTORANGLE", "VECTORANGLE"},
    {"SETVECTORSCALE", "SETVECTORSCALE"},
    {"VECTORSCALE", "VECTORSCALE"},
    {"SETVECTORPRECISION", "SETVECTORPRECISION"},
    {"VECTORPRECISION", "VECTORPRECISION"},
    {"badCoords", "Please specify the horizontal and vertical coordinates of the vector"},
    {"badX", "Illegal horizontal coordinate"},
    {"badY", "Illegal vertical coordinate"},
    {"whichVector", "Please specify a vector"},
    {"badL", "Illegal length"},
    {"badA", "Illegal angle"}
  };
}
