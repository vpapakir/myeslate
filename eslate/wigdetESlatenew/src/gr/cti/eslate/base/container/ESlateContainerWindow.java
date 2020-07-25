package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JWindow;


public class ESlateContainerWindow extends JWindow{
  ESlateContainer container;
  MyPanel windowPanel = null;

  public ESlateContainerWindow() {
    super();
    windowPanel = new MyPanel();
    setContentPane(windowPanel);
    container = new ESlateContainer(); //new JButton("sdfasfdsaf"); //
    container.setContainerTitleEnabled(false);
    windowPanel.add(container);
  }

  public void setContainerBounds(Rectangle bounds) {
      container.setLocation(bounds.getLocation());
      container.setSize(bounds.getSize());
  }

  public Rectangle getContainerBounds() {
      return container.getBounds();
  }

  public void setContainerSize(Dimension d) {
      container.setSize(d);
  }

  public Dimension getContainerSize() {
      return container.getSize();
  }

  public MyPanel getWindowPamel() {
      return windowPanel;
  }

  public ESlateContainer getESlateContainer() {
      return container;
  }

  public static void main(String[] args)
  {
    //Arguments passed:
    //  window_x
    //  window_y
    //  window_width
    //  window_height
    //  container_x
    //  container_y
    //  container_width
    //  container_height
    //  background color bgc
    //  background image im
    //  background image display mode imdispmode

//    System.out.println();

    int window_x=-1, window_y=-1, window_width=-1, window_height=-1;
    int container_x=-1, container_y=-1, container_width=-1, container_height=-1;
    Color bgc = null;
    String im=null, imdispmode = null;

    StringBuffer curArg;
    for (int i=0; i<args.length; i++)
    {
      curArg = new StringBuffer( args[i] );
      StringBuffer curArgName, curArgValue;
      try
      {
        curArgName = new StringBuffer( curArg.substring(0, args[i].indexOf(":")) );
        curArgValue = new StringBuffer( curArg.substring(args[i].indexOf(":")+1) );

        if (curArgName.toString().equals("window_x"))
        {
          if (window_x==-1)
            window_x = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("window_x already defined");
        }
        else if (curArgName.toString().equals("window_y"))
        {
          if (window_y==-1)
            window_y = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("window_y already defined");
        }
        else if (curArgName.toString().equals("window_width"))
        {
          if (window_width==-1)
            window_width = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("window_width already defined");
        }
        else if (curArgName.toString().equals("window_height"))
        {
          if (window_height==-1)
            window_height = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("window_height already defined");
        }
        else if (curArgName.toString().equals("container_x"))
        {
          if (container_x==-1)
            container_x = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("container_x already defined");
        }
        else if (curArgName.toString().equals("container_y"))
        {
          if (container_y==-1)
            container_y = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("container_y already defined");
        }
        else if (curArgName.toString().equals("container_width"))
        {
          if (container_width==-1)
            container_width = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("container_width already defined");
        }
        else if (curArgName.toString().equals("container_height"))
        {
          if (container_height==-1)
            container_height = Integer.valueOf(curArgValue.toString()).intValue();
          else
            System.out.println("container_height already defined");
        }
        else if (curArgName.toString().equals("bgc"))
        {
          if (bgc==null)
          {
            int r=0, g=0, b=0;
            String rgb = curArgValue.toString();
            try
            {
              if (rgb.indexOf(" ")>0)
              {
                r = Integer.valueOf( rgb.substring(0, rgb.indexOf(" ")) ).intValue();
                rgb = rgb.substring( rgb.indexOf(" ")+1 );
              }
              else if (rgb.length()>0)
              {
                r = Integer.valueOf( rgb ).intValue();
                rgb = "";
              }
              if (rgb.indexOf(" ")>0)
              {
                g = Integer.valueOf( rgb.substring(0, rgb.indexOf(" ")) ).intValue();
                rgb = rgb.substring( rgb.indexOf(" ")+1 );
              }
              else if (rgb.length()>0)
              {
                g = Integer.valueOf( rgb ).intValue();
                rgb = "";
              }
              if (rgb.length()>0)
              {
                b = Integer.valueOf( rgb ).intValue();
              }
            }
            catch (NumberFormatException nfe)
            {
              System.out.println("Integer values in range 0-255 expected corresponting to RGB values" + nfe.toString());
            }
            if (r>255 || r<0)
            {
              r=0;
              System.out.println("Integer value corresponting to RGB red should be >=0 and <= 255. Set 0");
            }
            if (g>255 || g<0)
            {
              g=255;
              System.out.println("Integer value corresponting to RGB green should be >=0 and <= 255. Set 0");
            }
            if (b>255 || b<0)
            {
              b=255;
              System.out.println("Integer value corresponting to RGB blue should be >=0 and <= 255. Set 0");
            }
            bgc = new Color(r, g, b);
          }
          else
            System.out.println("background color already defined");
        }
        else if (curArgName.toString().equals("im"))
        {
          if (im==null)
            im = curArgValue.toString();
          else
            System.out.println("background image already defined");
        }
        else if (curArgName.toString().equals("imdispmode"))
        {
          if (imdispmode==null)
            imdispmode = curArgValue.toString();
          else
            System.out.println("background image display mode already defined");
        }
        else
          System.out.println("Unrecognised argument = " + args[i]);
      }
      catch (NumberFormatException nfe)
      {
        System.out.println("EXCEPTION: Integer values expected for window and conatiner coordinates. " + nfe.toString());
      }
      catch (StringIndexOutOfBoundsException sioobe)
      {
        System.out.println("EXCEPTION: all arguments passed must have this format: <arg_name>:<arg_value>.  Argument that caused exception = " + args[i]);
      }
      catch (Exception e)
      {
        System.out.println(e.toString());
      }
    }

    //Check if any neccessary value yet unset, and set defaults.
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();

    if (window_x==-1)
    {
      if (window_width==-1)
        window_x=0;
      else
        window_x = Math.abs( (scrSize.width-window_width)/2 );
      System.out.println("*** window_x not given. Using default (" + window_x + ")");
    }
    if (window_y==-1)
    {
      if (window_height==-1)
        window_y=0;
      else
        window_y = Math.abs( (scrSize.height-window_height)/2 );
      System.out.println("*** window_y not given. Using default (" + window_y + ")");
    }
    if (window_width==-1)
    {
      window_width= Math.abs(scrSize.width-2*window_x);
      System.out.println("*** window_width not given. Using default (" + window_width + ")");
    }
    if (window_height==-1)
    {
      window_height= Math.abs(scrSize.height-2*window_y);
      System.out.println("*** window_height not given. Using default (" + window_height + ")");
    }

    if (container_x==-1)
    {
      if (container_width==-1)
        container_x=0;//window_x;
      else
        container_x = Math.abs( (window_width-container_width)/2 );
      System.out.println("*** container_x not given. Using default (" + container_x + ")");
    }
    if (container_y==-1)
    {
      if (container_height==-1)
        container_y=0;//window_y;
      else
        container_y = Math.abs( (window_height-container_height)/2 );
      System.out.println("*** container_y not given. Using default (" + container_y + ")");
    }
    if (container_width==-1)
    {
      container_width= Math.abs( window_width-2*container_x );
      System.out.println("*** container_width not given. Using default (" + container_width + ")");
    }
    if (container_height==-1)
    {
      container_height= Math.abs( window_height-2*container_y );
      System.out.println("*** container_height not given. Using default (" + container_height + ")");
    }
    if (bgc==null)
    {
      bgc = new Color(0, 0, 0);
      System.out.println("*** background color not given. Using default (" + bgc + ")");
    }
    if (imdispmode==null && im!=null)
    {
      imdispmode = "stretch";
      System.out.println("*** background image display mode not given. Using default (" + imdispmode + ")");
    }

    //Default values set where neccessary. Do some work now...

    ESlateContainerWindow myWin = new ESlateContainerWindow();
System.out.println("Settings bounds to: " + window_x + ", " + window_y + ", " + window_width + ", " + window_height);
    myWin.setBounds(window_x, window_y, window_width, window_height);

    if (im != null)
      myWin.windowPanel.setIcon(im);
    myWin.windowPanel.setBackground(bgc);

    if (imdispmode != null)
      myWin.windowPanel.setDisplayMode(imdispmode);
    myWin.setContainerBounds(new Rectangle(container_x, container_y, container_width, container_height));

/*    JLabel myLabel = new JLabel("cu cu");
    myWin.getContentPane().add(myLabel);
    myLabel.setVerticalAlignment(JLabel.CENTER);
    myLabel.setHorizontalAlignment(JLabel.CENTER);
    myLabel.setForeground(Color.blue);
    myLabel.setBounds(container_x, container_y, container_width, container_height);
*/
/*    ESlateContainer container = new ESlateContainer();
    myWin.getContentPane().add(container);
    container.setBounds(container_x, container_y, container_width, container_height);
*/

    myWin.setVisible(true);
  }
}


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

class MyPanel extends JPanel
{
  private ImageIcon icon = null;
  private String displayMode = "stretch";

  public MyPanel() {
    super(true);
    setBackground( Color.black );
    setLayout(null);
  }

  public void setIcon(String iconPath)
  {
    // If an icon already exists... bye bye...
    if (icon != null)
    {
      icon.getImage().flush();
    }
    icon = new ImageIcon(iconPath);
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public void setDisplayMode(String strDisplayMode) {
    displayMode = strDisplayMode;
  }

  public String getDisplayMode() {
    if ( displayMode.equals("tile") || displayMode.equals("center") || displayMode.equals("stretch") )
      return displayMode;
    else
      return "*** UNSUPPORTED displayMode. Using Default: stretch";
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (icon != null) {
      if (displayMode.equals("center"))
      {
        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();

        Rectangle bounds = getBounds();
        int windowXStart = bounds.x;
        int windowYStart = bounds.y;
        int windowXEnd = bounds.width;
        int windowYEnd = bounds.height;

        int iconPosX = (windowXEnd - windowXStart - iconWidth)/2 ;
        int iconPosY = (windowYEnd - windowYStart - iconHeight)/2 ;

        Graphics cg;
        cg = g.create();
        cg.setClip(iconPosX, iconPosY, iconWidth, iconHeight);
        icon.paintIcon(this, cg, iconPosX, iconPosY);
        cg.dispose();
      }
      else if (displayMode.equals("tile"))
      {
        int tileW = icon.getIconWidth();
        int tileH = icon.getIconHeight();

        int iconPosX = 0;
        int iconPosY = 0;

        Rectangle bounds = getBounds();
        int windowXStart = bounds.x;
        int windowYStart = bounds.y;
        int windowXEnd = bounds.width;
        int windowYEnd = bounds.height;
        int windowWidth = windowXEnd-windowXStart;
        int windowHeight = windowYEnd-windowYStart;

        Graphics cg;
        cg = g.create();
        cg.setClip(iconPosX, iconPosY, windowWidth, windowHeight);
        for (iconPosY=0; windowHeight - iconPosY > 0; iconPosY += tileH)
        {
          for (iconPosX=0; windowWidth - iconPosX > 0; iconPosX += tileW)
          {
            icon.paintIcon(this, cg, iconPosX, iconPosY);
          }
        }
        cg.dispose();
      }
      else //if (displayMode.equals("stretch"))     **** DEFAULT ****
      {
        Rectangle bounds = getBounds();
        int windowXStart = bounds.x;
        int windowYStart = bounds.y;
        int windowXEnd = bounds.width;
        int windowYEnd = bounds.height;
        int windowWidth = windowXEnd-windowXStart;
        int windowHeight = windowYEnd-windowYStart;

        Graphics cg;
        cg = g.create();
        cg.setClip(windowXStart, windowYStart, windowWidth, windowHeight);
        cg.drawImage(icon.getImage(), windowXStart, windowYStart, windowWidth, windowHeight, null);
        cg.dispose();
      }
    }


  }

}
