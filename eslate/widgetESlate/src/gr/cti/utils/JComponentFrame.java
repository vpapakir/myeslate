//23Jul1999: derived from JAppletFrame
//27Aug1999: catching exception thrown when one component can't start, so that others can start OK

package gr.cti.utils;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.Event;
import java.awt.Dimension;
import java.awt.AWTEvent;

// JComponent to Application Frame window
public class JComponentFrame extends JFrame
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
/////////////

    public static JComponent initJComponent(String className,
                                    Rectangle bounds)
    {
       JComponent a;

       try
       {
          // create an instance of your applet class
          a = (JComponent) Class.forName(className).newInstance();
       }
       catch (ClassNotFoundException e) { System.err.println("Couldn't find class "+className); return null; } //15-4-1999
       catch (InstantiationException e) { System.err.println("Couldn't instantiate class "+className); return null; } //15-4-1999
       catch (IllegalAccessException e) { System.err.println("Not enough rights to access class "+className); return null; } //15-4-1999

       // initialize the applet
       //a.init();
       //a.start(); //26Apr1999 - don't start the applet till you add it to a container window

       return a;
    }

//////////////////////////////////

    public static void startJComponent(String className,
                                   String title,
                                   String args[])
    {
       // local variables
       JComponent a;
       Dimension appletSize;

       try
       {
          // create an instance of your applet class
          a = (JComponent) Class.forName(className).newInstance();
       }
       catch (ClassNotFoundException e) { System.err.println("Couldn't find class "+className); return; } //15-4-1999
       catch (InstantiationException e) { System.err.println("Couldn't instantiate class "+className); return; } //15-4-1999
       catch (IllegalAccessException e) { System.err.println("Not enough rights to access class "+className); return; } //15-4-1999

       // initialize the applet
       //a.init();

       // create new application frame window
       JComponentFrame f = new JComponentFrame(title);

       // add applet to frame window
       f.getContentPane().add("Center", a);

       // resize frame window to fit applet
       // assumes that the applet sets its own size
       // otherwise, you should set a specific size here.
       appletSize =  a.getPreferredSize();
       f.pack();
       f.setSize(appletSize);

       // start the applet //26Apr1999 - this should be consistent with JComponentViewer's behaviour, start the applet after adding it to a parent which resolves to a peer...
       //a.start();

       // show the window
       f.setVisible(true);

    }  // end startJComponent()

    public static void startJComponents(String[] classNames,String titles[]){ //27-3-1999
     for(int i=0;i<classNames.length;i++)
      try{
       startJComponent(classNames[i],titles[i],null);
      }catch(Exception e){System.err.println("Couldn't start "+classNames[i]+"\n"+e);} //27Aug1999
    }

///////////////////

    public static void startJComponentInFrame(JFrame f,String className, Rectangle bounds){
     JComponent a=initJComponent(className,bounds);
     f.getContentPane().add(a);
     a.setBounds(bounds);
     //a.start(); //start after adding to the frame
    }

    public static void startJComponentsInOneFrame(String frameTitle, Dimension frameSize, String[] classNames, Rectangle[] bounds){ //26-4-1999
     JFrame f=new JComponentFrame(frameTitle);
     f.setLayout(null);
     for(int i=0;i<classNames.length;i++)
      try{
       startJComponentInFrame(f,classNames[i], bounds[i]);
      }catch(Exception e){System.err.println("Couldn't start "+classNames[i]+"\n"+e);} //27Aug1999
     f.pack();
     f.setSize(frameSize);
     f.setVisible(true);
    }

    // constructor needed to pass window title to class JFrame
    public JComponentFrame(String name)
    {
       // call java.awt.Frame(String) constructor
       super(name);
    }

    // needed to allow window close
    public void processEvent(AWTEvent e)
    {
       // Window Destroy event
       if (e.getID() == Event.WINDOW_DESTROY)
       {
          // exit the program
          System.exit(0);
       }    
   }  // end handleEvent()

}   // end class JComponentFrame






