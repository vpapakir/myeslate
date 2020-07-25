// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ESlateFileDialogMessagesBundle.java

package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

public class ESlateFileDialogMessagesBundle extends ListResourceBundle
{

    public ESlateFileDialogMessagesBundle()
    {
    }

    public Object[][] getContents()
    {
        return contents;
    }

    static final Object contents[][] = {
        {
            "notExist1", "File "
        }, {
            "notExist2", " does not exist."
        }, {
            "exist1", "File "
        }, {
            "exist2", " already exists.\nDo you want to replace it?"
        }, {
            "yes", "Yes"
        }, {
            "no", "No"
        }, {
            "ok", "OK"
        }
    };

}
