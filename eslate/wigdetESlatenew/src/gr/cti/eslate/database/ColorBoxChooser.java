package gr.cti.eslate.database;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Vector;


public class ColorBoxChooser extends JPanel {
    private Color[] colors = new Color[110];
    private int activeColorIndex = -1;
    private int cellSize = 10;
    private int firstEmptyColorIndex = 104;
    transient private Vector activeColorListeners = new Vector();
    private int colorType = 0;


    public ColorBoxChooser() {
        super(true);
        Dimension d = new Dimension(130, 143); //107, 165);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);

        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK);

        colors[0] = Color.white;
        colors[1] = Color.red;
        colors[2] = Color.green;
        colors[3] = Color.blue;
        colors[4] = Color.yellow;
        colors[5] = Color.magenta;
        colors[6] = Color.cyan;
        colors[7] = new Color(224, 232, 224);
        colors[8] = Color.lightGray;
        colors[9] = new Color(159, 152, 159);
        colors[10] = Color.gray;
        colors[11] = Color.darkGray;
        colors[12] = Color.black;
        colors[13] = new Color(207, 0, 48);
        colors[14] = new Color(255,239, 0);
        colors[15] = new Color(0, 151, 79);
        colors[16] = new Color(0, 175, 223);
        colors[17] = new Color(32, 16, 111);
        colors[18] = new Color(207, 0, 112);
        colors[19] = new Color(239,167,128);
        colors[20] = new Color(240, 191,143);
        colors[21] = new Color(255,208,144);
        colors[22] = new Color(255, 240, 175);
        colors[23] = new Color(208, 224, 160);
        colors[24] = new Color(191, 216, 160);
        colors[25] = new Color(160, 207, 160);
        colors[26] = new Color(159, 208, 207);
        colors[27] = new Color(159, 208, 239);
        colors[28] = new Color(159, 176, 208);
        colors[29] = new Color(159, 160, 192);
        colors[30] = new Color(159, 143, 191);
        colors[31] = new Color(175, 151, 191);
        colors[32] = new Color(192, 159, 191);
        colors[33] = new Color(239, 168, 192);
        colors[34] = new Color(239, 168, 160);
        colors[35] = new Color(224, 119, 80);
        colors[36] = new Color(239, 159, 95);
        colors[37] = new Color(240, 191, 96);
        colors[38] = new Color(255, 239, 112);
        colors[39] = new Color(191, 216, 127);
        colors[40] = new Color(159, 200, 127);
        colors[41] = new Color(111, 184, 127);
        colors[42] = new Color(96,184,176);
        colors[43] = new Color(95,    191,    239);
        colors[44] = new Color(111, 144, 192);
        colors[45] = new Color(111,   120,    175);
        colors[46] = new Color(111,   95,     159);
        colors[47] = new Color(143,   103,    159);
        colors[48] = new Color(175,   111,    159);
        colors[49] = new Color(224,   120,    160);
        colors[50] = new Color(224,   120,    127);
        colors[51] = new Color(207,   0,      48);
        colors[52] = new Color(223,   112,    0);
        colors[53] = new Color(239,   167,    0);
        colors[54] = new Color(159,   200,    0);
        colors[55] = new Color(95,    176,    32);
        colors[56] = new Color(0,     151,    79);
        colors[57] = new Color(0,     159,    159);
        colors[58] = new Color(0,     175,    223);
        colors[59] = new Color(0,     119,    175);
        colors[60] = new Color(0,     79,     144);
        colors[61] = new Color(32,    16,     111);
        colors[62] = new Color(96,    0,      111);
        colors[63] = new Color(143,   0,      111);
        colors[64] = new Color(207,   0,      112);
        colors[65] = new Color(207,   0,      95);
        colors[66] = new Color(143,   0,      32);
        colors[67] = new Color(159,   79,     0);
        colors[68] = new Color(160,   119,    0);
        colors[69] = new Color(176,   168,    0);
        colors[70] = new Color(111,   143,    0);
        colors[71] = new Color(63,    127,    31);
        colors[72] = new Color(0,     104,    48);
        colors[73] = new Color(0,     111,    111);
        colors[74] = new Color(0,     120,    159);
        colors[75] = new Color(0,     80,     127);
        colors[76] = new Color(0,     55,     96);
        colors[77] = new Color(31,    8,      79);
        colors[78] = new Color(64,    0,      79);
        colors[79] = new Color(96,    0,      79);
        colors[80] = new Color(143,   0,      80);
        colors[81] = new Color(143,   0,      63);
        colors[82] = new Color(111,   0,      31);
        colors[83] = new Color(112,   63,     0);
        colors[84] = new Color(127,   88,     0);
        colors[85] = new Color(128,   128,    0);
        colors[86] = new Color(80,    104,    0);
        colors[87] = new Color(47,    95,     16);
        colors[88] = new Color(0,     79,     47);
        colors[89] = new Color(0,     80,     80);
        colors[90] = new Color(0,     88,     112);
        colors[91] = new Color(0,     63,     95);
        colors[92] = new Color(79,    0,      63);
        colors[93] = new Color(111,   0,      47);
        colors[94] = new Color(207,   191,    160);
        colors[95] = new Color(160,   151,    128);
        colors[96] = new Color(128,   112,    96);
        colors[97] = new Color(95,    80,     64);
        colors[98] = new Color(63,    55,     48);
        colors[99] = new Color(207,   168,    127);
        colors[100] = new Color(175,   143,    95);
        colors[101] = new Color(144,   112,    64);
        colors[102] = new Color(127,   88,     47);
        colors[103] = new Color(96,    71,     16);
        colors[104] = null;
        colors[105] = null;
        colors[106] = null;
        colors[107] = null;
        colors[108] = null;
        colors[109] = null;
    }

    public void paint(Graphics gr) {
        /* Draw the grid
         */
        gr.setColor(Color.lightGray);
        for (int i=1; i<11; i++)
            gr.drawLine(i*13-1, 0, i*13-1, 143);
        for (int i=1; i<12; i++)
            gr.drawLine(0, i*13-1, 130, i*13-1);

        int curX = 0;
        int curY = 0;
        int colorIndex = 0;
        for (int i=0; i<11; i++) {
            for (int k=0; k<10; k++) {
                gr.setColor(Color.gray);
                gr.drawLine(curX, curY, curX, curY+cellSize+1);
                gr.drawLine(curX, curY, curX+10+1, curY);
                gr.setColor(Color.white);
                gr.drawLine(curX, curY+cellSize+1, curX+cellSize+1, curY+cellSize+1);
                gr.drawLine(curX+cellSize+1, curY, curX+cellSize+1, curY+cellSize+1);
                if (colors[colorIndex] != null) {
//                    System.out.println("colorIndex: " + colorIndex);
                    gr.setColor(colors[colorIndex]);
                    gr.fillRect(curX+1, curY+1, cellSize, cellSize);
                }else{
                    gr.setColor(Color.lightGray);
                    gr.fillRect(curX+1, curY+1, cellSize, cellSize);
                }
                colorIndex++;
                curX = curX + cellSize + 3;
            }
            curX = 0;
            curY = curY + cellSize + 3;
        }

        if (activeColorIndex != -1) {
            int row = activeColorIndex / 10;
            int column = activeColorIndex % 10;
            if (column == 0)
                curX = 0;
            else
                curX = cellSize+2 + (column-1)*(cellSize+2+1);

            if (row == 0)
                curY = 0;
            else
                curY = cellSize+2 + (row-1)*(cellSize+2+1);

            gr.setColor(Color.red);

            if (curX == 0 && curY == 0) {
                gr.drawRect(0, 0, cellSize+2, cellSize+2);
                return;
            }

            if (curY == 0)
                gr.drawRect(curX, 0, cellSize+3, cellSize+2);
            else if (curX == 0)
                gr.drawRect(0, curY, cellSize+2, cellSize+3);
            else
                gr.drawRect(curX, curY, cellSize+3, cellSize+3);
        }

    }

    /** Returns the index of the supplied color. If the
     *  color does not exist in the colors' array, -1 is returned.
     */
    public int getColorIndex(Color color) {
//        System.out.println("getColorIndex -- color: " + color);
        if (color == null)
            return -1;

        for (int i=0; i<colors.length; i++) {
            if (colors[i] != null && colors[i].equals(color))
                return i;
        }
        return -1;
    }

    /** Sets the active color index. If the supplied index
     *  is invalid, the active color index is set to -1.
     *  Returns the new active color index.
     */
    public int setActiveColorIndex(int index) {
        Color previousColor = getActiveColor();
        if (index < 0 || index > 109) {
            activeColorIndex = -1;
            fireActiveColorChanged(new ActiveColorEvent(this, previousColor, null, colorType));
            return -1;
        }else{
            if (colors[index] == null) {
                activeColorIndex = -1;
                fireActiveColorChanged(new ActiveColorEvent(this, previousColor, null, colorType));
                return -1;
            }
            activeColorIndex = index;
            fireActiveColorChanged(new ActiveColorEvent(this, previousColor, getActiveColor(), colorType));
            return activeColorIndex;
        }
    }

    /** Sets the active color index. If the supplied index
     *  is invalid, the active color index is set to -1.
     *  Returns the new active color index.
     */
    public int initActiveColorIndex(int index) {
        if (index < 0 || index > 109) {
            activeColorIndex = -1;
            return -1;
        }else{
            if (colors[index] == null) {
                activeColorIndex = -1;
                return -1;
            }
            activeColorIndex = index;
            return activeColorIndex;
        }
    }

    /** Sets the active color. If the supplied color does
     *  not exist in the array of the valid colors, then
     *  the active color index is set to -1. Returns the
     *  index of the active color.
     */
    public int setActiveColor(Color color) {
        Color previousColor = getActiveColor();
        activeColorIndex = getColorIndex(color);
        fireActiveColorChanged(new ActiveColorEvent(this, previousColor, getActiveColor(), colorType));
        return activeColorIndex;

    }

    /** Returns the active color index
     */
    public int getActiveColorIndex() {
        return activeColorIndex;
    }

    /** Returns the active color. If the active color index
     *  is -1, <i>null</i> is returned.
     */
    public Color getActiveColor() {
        if (activeColorIndex != -1)
            return colors[activeColorIndex];
        else
            return null;
    }

    /** Adds a color to the colors' array. The index of the
     *  added color is returned.
     */
    public int addColor(Color color) {
        if (color == null)
            return -1;

        colors[firstEmptyColorIndex] = color;
//        System.out.println("Added color: " + color + " at : " + firstEmptyColorIndex);
        int tmp = firstEmptyColorIndex;
        if (getGraphics() != null)
            paint(getGraphics());
        firstEmptyColorIndex++;
        if (firstEmptyColorIndex > 109)
            firstEmptyColorIndex = 104;

        return tmp;
    }


    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            int row = e.getY()/(cellSize+3);
            int column = e.getX()/(cellSize+3);
//            System.out.println("row: " + row + ", column: " + column);
            if (row >= 0 && row <= 11 && column >= 0 && column <= 10) {
                setActiveColorIndex(row*10 + column);
                if (getGraphics() != null)
                    paint(getGraphics());
            }
        }
    }

    /** Supplementary information which may be of use in the cases
     *  where the same ColorBoxChooser is used to set colors for
     *  different kind of items. This information is loaded on the
     *  ActiveColorEvent, generated when the active color changes.
     */
    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getColorType() {
        return colorType;
    }

    protected void fireActiveColorChanged(ActiveColorEvent e) {
        Vector listeners;
        synchronized(this) {listeners = (Vector) activeColorListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((ActiveColorListener) listeners.elementAt(i)).activeColorChanged(e);
    }

    public synchronized void addActiveColorListener(ActiveColorListener acl) {
        if (activeColorListeners.indexOf(acl) == -1)
            activeColorListeners.addElement(acl);
//        System.out.println("databaseListeners: " + databaseListeners);
    }


    public synchronized void removeActiveColorListener(ActiveColorListener acl) {
        if (activeColorListeners.indexOf(acl) != -1)
            activeColorListeners.removeElement(acl);
//        System.out.println("databaseListeners: " + databaseListeners);
    }

}

