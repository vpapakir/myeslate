//Title:        Stage
//Version:      18May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  base constraints' localization service

package gr.cti.eslate.stage.constraints;

import java.util.ListResourceBundle;

public class MessagesBundle_el_GR extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"PointsArithmeticMedianPoint","σημείο ως αριθμητικό μέσο σημείων"}, //19Apr2000: fixed-bug: had wrong key string
     {"PointsGeometricMedianPoint","σημείο ως γεωμετρικό μέσο σημείων"}, //19Apr2000
     {"PointOntoPoint","σημείο πάνω σε σημείο"},
     {"PointIntoShape","σημείο μέσα σε σχήμα"},
     {"FixedPointOffsetFromPoint","σταθερή απομάκρυνση σημείου από σημείο"},
     {"FixedPointDistanceFromPoint","σταθερή απόσταση σημείου από σημείο"},
     {"MinPointDistanceFromPoint","ελάχιστη απόσταση σημείου από σημείο"},
     {"MaxPointDistanceFromPoint","μέγιστη απόσταση σημείου από σημείο"}
        };

}

