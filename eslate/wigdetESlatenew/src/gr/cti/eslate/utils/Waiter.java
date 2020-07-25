// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Waiter.java

package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class Waiter extends JApplet
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    public Waiter()
    {
        ResourceBundle infoBundle = ResourceBundle.getBundle("gr.cti.eslate.utils.WaiterBundle", Locale.getDefault());
        String lb1Txt = infoBundle.getString("lb1");
        String lb2Txt = infoBundle.getString("lb2");
        /*
        if(infoBundle.getClass().getName().equals("gr.cti.eslate.utils.WaiterBundle_el_GR"))
            font = new Font("Helvetica", 1, 12);
        else
            font = new Font("TimesRoman", 1, 14);
        */
        //fm = getToolkit().getFontMetrics(font);
        JLabel lb1 = new JLabel(lb1Txt);
        Font font = lb1.getFont();

        Graphics2D g2 = (Graphics2D)getGraphics();
        FontRenderContext frc = g2.getFontRenderContext();
        JPanel a = new JPanel();
        a.add(Box.createGlue());
        a.add(new JLabel(loadImageIcon("shell.gif", "")));
        a.add(Box.createGlue());
        JPanel b = new JPanel(true);
        b.setLayout(new BoxLayout(b, 0));
        //lb1.setFont(font);
        int w1 = font.getStringBounds(lb1.getText(), frc).getBounds().width;
        Dimension d1 = new Dimension(w1, 17);
        b.add(lb1);
        b.setMaximumSize(d1);
        b.setMinimumSize(d1);
        b.setPreferredSize(d1);
        JPanel c = new JPanel(true);
        c.setLayout(new BoxLayout(c, 0));
        JLabel lb2 = new JLabel(lb2Txt);
        lb2.setFont(font);
        int w2 = font.getStringBounds(lb2.getText(), frc).getBounds().width;
        d1 = new Dimension(w2, 17);
        c.add(lb2);
        c.setMaximumSize(d1);
        c.setMinimumSize(d1);
        c.setPreferredSize(d1);
        JPanel d = new JPanel();
        d.setLayout(new BoxLayout(d, 1));
        d.add(a);
        d.add(b);
        d.add(c);
        d.add(Box.createGlue());
        d1 = new Dimension(d1.width + 10, 120);
        d.setMaximumSize(d1);
        d.setMinimumSize(d1);
        d.setPreferredSize(d1);
        getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
        getContentPane().add(Box.createGlue());
        getContentPane().add(d);
        getContentPane().add(Box.createGlue());
        d.setBorder(new EtchedBorder(0));
        g2.dispose();
    }

    public ImageIcon loadImageIcon(String filename, String description)
    {
        try
        {
            URL u = getClass().getResource(filename);
            if(u != null)
            {
                ImageIcon imageicon = new ImageIcon(u, description);
                ImageIcon imageicon3 = imageicon;
                return imageicon3;
            } else
            {
                ImageIcon imageicon1 = null;
                ImageIcon imageicon4 = imageicon1;
                return imageicon4;
            }
        }
        catch(Exception e)
        {
            ImageIcon imageicon2 = null;
            return imageicon2;
        }
    }

    Font font;
    //FontMetrics fm;
    static String loadingMsg = "E-Slate components'";

}
