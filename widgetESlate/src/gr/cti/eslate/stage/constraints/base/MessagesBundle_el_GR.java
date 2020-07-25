//Title:        Stage
//Version:      2Doc1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  base constraints' localization service

package gr.cti.eslate.stage.constraints.base;

import java.util.ListResourceBundle;

public class MessagesBundle_el_GR extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"Unknown","<Αγνωστος περιορισμός>"},
     {"cantEnforce","Δεν γίνεται να ενεργοποιηθεί ο περιορισμός"},
     {"notAllowed","Δεν επιτρέπεται λόγω του περιορισμού "}, //have a space at the end
     //
     {"UnknownPointPoint","<Αγνωστος περιορισμός τύπου σημείο-σημείο>"},
     {"need2points","Ενας περιορισμός τύπου σημείο-σημείο χρειάζεται δύο σημεία ελέγχου"},
     //
     {"UnknownMasterSlave","<Αγνωστος περιορισμός τύπου αφέντης-σκλάβος>"},
     {"need2members","Ενας περιορισμός τύπου αφέντης-σκλάβος χρειάζεται δύο μέλη"}
        };

}

