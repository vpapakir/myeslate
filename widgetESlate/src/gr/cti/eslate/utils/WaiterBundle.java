// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WaiterBundle.java

package gr.cti.eslate.utils;

import java.util.ListResourceBundle;

public class WaiterBundle extends ListResourceBundle
{

    public WaiterBundle()
    {
    }

    public Object[][] getContents()
    {
        return contents;
    }

    static final Object contents[][] = {
        {
            "lb1", "E-Slate has been succesfully installed"
        }, {
            "lb2", "You can now continue browsing the E-Slate site"
        }
    };

}
