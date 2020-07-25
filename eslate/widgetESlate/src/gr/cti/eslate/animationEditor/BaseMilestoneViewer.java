package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.*;
import java.awt.geom.*;
import java.awt.*;

/**
 * This class implements a view and controller for the base milestone model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 26-Jun-2002
 * @see		gr.cti.eslate.animationEditor.BaseSegmentViewer
 */
public abstract class BaseMilestoneViewer {
  BaseSegmentViewer segmentViewer;
  BaseMilestone milestone;
  Ellipse2D.Double milestoneShape;
  MilestoneListener milestoneListener;
  int zeroTime;
}
