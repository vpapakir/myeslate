// ColorPalette by Drossos Nikos @ 4/2000
// A color palette that offers parameterized features like the size of the palette,
// the size of the custom palette, auto - adjust in height...
// Through multiple constructors gives the opportunity to construct a default palette,
// or a palette with specific colors in array format.
// Supports one or two selected colors (foreground, background) and customizable colors
// for the indicators.
// Sets-gets colors through color index or color value.
// Notifies registered listeners for changes of selected colors.
// Finally by double-clicking a color a ColorChooser appears for more customized colors.
// New: Added open - save capability
// Status: Works with ScrollPanel

package gr.cti.eslate.colorPalette;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class ColorPalette extends JPanel implements MouseListener {

	private Color[] colors;

	int squareSize=14;

	int numberOfColors=115;

	int numberOfCustomColors=10;

	int customIndex, firstEmptyCustomIndex;

	int paletteIndex=0; // only in create mode

	int rows, columns;

	int selectedForegroundIndex, selectedBackgroundIndex;

	final String STR_FORMAT_VERSION="2"; //

	private int mode=0; // 0 : normal mode (fill only the custom palette)

	// 1: create mode (fill first the main palette and then the custom)

	private ArrayList<ActiveColorListener> activeColorListeners=new ArrayList<ActiveColorListener>();

	static JFrame dialogFrame=new JFrame();

	static transient FileDialog fileDialog=new FileDialog(dialogFrame);

	public boolean isPaletteModified=false;

	Dimension paletteDimension;

	boolean enableForegroundIndicator, enableBackgroundIndicator;

	Color foregroundIndicatorColor, backgroundIndicatorColor;

	ResourceBundle colorBundle=ResourceBundle.getBundle("gr.cti.eslate.colorPalette.InfoBundle",Locale.getDefault());

	// constructs a default color palette with the specified Dimension
	// red foreground and blue background indicators
	// having black as selected foreground color and white as background.

	public ColorPalette() {
		this(new Dimension(85,230));
	}

	public ColorPalette(Dimension palDimension) {
		colors=new Color[numberOfColors];
		columns=palDimension.width / squareSize - 1;

		if (colors.length % columns == 0)
			rows=colors.length / columns;
		else
			rows=colors.length / columns + 1;

		customIndex=numberOfColors - numberOfCustomColors;
		firstEmptyCustomIndex=customIndex;
		// adjust the width of the palette in order to have only full squares
		// Dimension fixedDimension = new Dimension(columns*squareSize, palDimension.height);
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap between
		// custom
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);

		createDefaultColors();
		this.addMouseListener(this);
		setLayout(null);
		// initialize indicators
		enableForegroundIndicator=true;
		foregroundIndicatorColor=Color.red;
		enableBackgroundIndicator=true;
		backgroundIndicatorColor=Color.blue;

		// initialize selected colors
		selectedForegroundIndex=12;
		selectedBackgroundIndex=0;
	}

	// constructs a custom color palette with the specified Dimension
	// without setting values for indicators or fore-back colors
	// but with the default color array
	public ColorPalette(Dimension palDimension,boolean custom) {
		colors=new Color[numberOfColors];
		columns=palDimension.width / squareSize;

		if (colors.length % columns == 0)
			rows=colors.length / columns;
		else
			rows=colors.length / columns + 1;

		customIndex=numberOfColors - numberOfCustomColors;
		firstEmptyCustomIndex=customIndex;
		// adjust the width of the palette in order to have only full squares
		// Dimension fixedDimension = new Dimension(columns*squareSize, palDimension.height);
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap between
		// custom
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);

		createDefaultColors();
		this.addMouseListener(this);
		setLayout(null);
	}

	// constructs a default color palette with the specified Dimension
	// red foreground and blue background indicators
	// and the specified array as colors
	public ColorPalette(Color[] cols,Dimension palDimension) {
		colors=new Color[cols.length];
		System.arraycopy(cols,0,colors,0,cols.length);

		numberOfColors=cols.length;
		columns=palDimension.width / squareSize;

		if (colors.length % columns == 0)
			rows=colors.length / columns;
		else
			rows=colors.length / columns + 1;

		customIndex=numberOfColors - numberOfCustomColors;
		firstEmptyCustomIndex=customIndex;
		// adjust the width of the palette in order to have only full squares
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap between
		// custom
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);

		this.addMouseListener(this);
		setLayout(null);
		// initialize indicators
		enableForegroundIndicator=true;
		foregroundIndicatorColor=Color.red;
		enableBackgroundIndicator=true;
		backgroundIndicatorColor=Color.blue;
	}

	public Dimension getDimension() {
		return paletteDimension;
	}

	private void createDefaultColors() {
		colors[0]=new Color(255,255,255,0);
		colors[1]=Color.white;
		colors[2]=Color.red;
		colors[3]=Color.green;
		colors[4]=Color.blue;
		colors[5]=Color.yellow;
		colors[6]=Color.magenta;
		colors[7]=Color.cyan;
		colors[8]=new Color(224,232,224);
		colors[9]=Color.lightGray;
		colors[10]=new Color(159,152,159);
		colors[11]=Color.gray;
		colors[12]=Color.darkGray;
		colors[13]=Color.black;
		colors[14]=new Color(207,0,48);
		colors[15]=new Color(255,239,0);
		colors[16]=new Color(0,151,79);
		colors[17]=new Color(0,175,223);
		colors[18]=new Color(32,16,111);
		colors[19]=new Color(207,0,112);
		colors[20]=new Color(239,167,128);
		colors[21]=new Color(240,191,143);
		colors[22]=new Color(255,208,144);
		colors[23]=new Color(255,240,175);
		colors[24]=new Color(208,224,160);
		colors[25]=new Color(191,216,160);
		colors[26]=new Color(160,207,160);
		colors[27]=new Color(159,208,207);
		colors[28]=new Color(159,208,239);
		colors[29]=new Color(159,176,208);
		colors[30]=new Color(159,160,192);
		colors[31]=new Color(159,143,191);
		colors[32]=new Color(175,151,191);
		colors[33]=new Color(192,159,191);
		colors[34]=new Color(239,168,192);
		colors[35]=new Color(239,168,160);
		colors[36]=new Color(224,119,80);
		colors[37]=new Color(239,159,95);
		colors[38]=new Color(240,191,96);
		colors[39]=new Color(255,239,112);
		colors[40]=new Color(191,216,127);
		colors[41]=new Color(159,200,127);
		colors[42]=new Color(111,184,127);
		colors[43]=new Color(96,184,176);
		colors[44]=new Color(95,191,239);
		colors[45]=new Color(111,144,192);
		colors[46]=new Color(111,120,175);
		colors[47]=new Color(111,95,159);
		colors[48]=new Color(143,103,159);
		colors[49]=new Color(175,111,159);
		colors[50]=new Color(224,120,160);
		colors[51]=new Color(224,120,127);
		colors[52]=new Color(207,0,48);
		colors[53]=new Color(223,112,0);
		colors[54]=new Color(239,167,0);
		colors[55]=new Color(159,200,0);
		colors[56]=new Color(95,176,32);
		colors[57]=new Color(0,151,79);
		colors[58]=new Color(0,159,159);
		colors[59]=new Color(0,175,223);
		colors[60]=new Color(0,119,175);
		colors[61]=new Color(0,79,144);
		colors[62]=new Color(32,16,111);
		colors[63]=new Color(96,0,111);
		colors[64]=new Color(143,0,111);
		colors[65]=new Color(207,0,112);
		colors[66]=new Color(207,0,95);
		colors[67]=new Color(143,0,32);
		colors[68]=new Color(159,79,0);
		colors[69]=new Color(160,119,0);
		colors[70]=new Color(176,168,0);
		colors[71]=new Color(111,143,0);
		colors[72]=new Color(63,127,31);
		colors[73]=new Color(0,104,48);
		colors[74]=new Color(0,111,111);
		colors[75]=new Color(0,120,159);
		colors[76]=new Color(0,80,127);
		colors[77]=new Color(0,55,96);
		colors[78]=new Color(31,8,79);
		colors[79]=new Color(64,0,79);
		colors[80]=new Color(96,0,79);
		colors[81]=new Color(143,0,80);
		colors[82]=new Color(143,0,63);
		colors[83]=new Color(111,0,31);
		colors[84]=new Color(112,63,0);
		colors[85]=new Color(127,88,0);
		colors[86]=new Color(128,128,0);
		colors[87]=new Color(80,104,0);
		colors[88]=new Color(47,95,16);
		colors[89]=new Color(0,79,47);
		colors[90]=new Color(0,80,80);
		colors[91]=new Color(0,88,112);
		colors[92]=new Color(0,63,95);
		colors[93]=new Color(79,0,63);
		colors[94]=new Color(111,0,47);
		colors[95]=new Color(207,191,160);
		colors[96]=new Color(160,151,128);
		colors[97]=new Color(128,112,96);
		colors[98]=new Color(95,80,64);
		colors[99]=new Color(63,55,48);
		colors[100]=new Color(207,168,127);
		colors[101]=new Color(175,143,95);
		colors[102]=new Color(144,112,64);
		colors[103]=new Color(127,88,47);
		colors[104]=new Color(96,71,16);
		// colors[105] = new Color(255, 255, 255, 0);
		// to place custom colors
		colors[105]=null;
		colors[106]=null;
		colors[107]=null;
		colors[108]=null;
		colors[109]=null;
		colors[110]=null;
		colors[111]=null;
		colors[112]=null;
		colors[113]=null;
		colors[114]=null;
		/*
		 * colors[0] = Color.white; colors[1] = Color.red; colors[2] = Color.green; colors[3] = Color.blue; colors[4] =
		 * Color.yellow; colors[5] = Color.magenta; colors[6] = Color.cyan; colors[7] = new Color(224, 232, 224);
		 * colors[8] = Color.lightGray; colors[9] = new Color(159, 152, 159); colors[10] = Color.gray; colors[11] =
		 * Color.darkGray; colors[12] = Color.black; colors[13] = new Color(207, 0, 48); colors[14] = new Color(255,239,
		 * 0); colors[15] = new Color(0, 151, 79); colors[16] = new Color(0, 175, 223); colors[17] = new Color(32, 16,
		 * 111); colors[18] = new Color(207, 0, 112); colors[19] = new Color(239,167,128); colors[20] = new Color(240,
		 * 191,143); colors[21] = new Color(255,208,144); colors[22] = new Color(255, 240, 175); colors[23] = new
		 * Color(208, 224, 160); colors[24] = new Color(191, 216, 160); colors[25] = new Color(160, 207, 160);
		 * colors[26] = new Color(159, 208, 207); colors[27] = new Color(159, 208, 239); colors[28] = new Color(159,
		 * 176, 208); colors[29] = new Color(159, 160, 192); colors[30] = new Color(159, 143, 191); colors[31] = new
		 * Color(175, 151, 191); colors[32] = new Color(192, 159, 191); colors[33] = new Color(239, 168, 192);
		 * colors[34] = new Color(239, 168, 160); colors[35] = new Color(224, 119, 80); colors[36] = new Color(239, 159,
		 * 95); colors[37] = new Color(240, 191, 96); colors[38] = new Color(255, 239, 112); colors[39] = new Color(191,
		 * 216, 127); colors[40] = new Color(159, 200, 127); colors[41] = new Color(111, 184, 127); colors[42] = new
		 * Color(96,184,176); colors[43] = new Color(95, 191, 239); colors[44] = new Color(111, 144, 192); colors[45] =
		 * new Color(111, 120, 175); colors[46] = new Color(111, 95, 159); colors[47] = new Color(143, 103, 159);
		 * colors[48] = new Color(175, 111, 159); colors[49] = new Color(224, 120, 160); colors[50] = new Color(224,
		 * 120, 127); colors[51] = new Color(207, 0, 48); colors[52] = new Color(223, 112, 0); colors[53] = new
		 * Color(239, 167, 0); colors[54] = new Color(159, 200, 0); colors[55] = new Color(95, 176, 32); colors[56] =
		 * new Color(0, 151, 79); colors[57] = new Color(0, 159, 159); colors[58] = new Color(0, 175, 223); colors[59] =
		 * new Color(0, 119, 175); colors[60] = new Color(0, 79, 144); colors[61] = new Color(32, 16, 111); colors[62] =
		 * new Color(96, 0, 111); colors[63] = new Color(143, 0, 111); colors[64] = new Color(207, 0, 112); colors[65] =
		 * new Color(207, 0, 95); colors[66] = new Color(143, 0, 32); colors[67] = new Color(159, 79, 0); colors[68] =
		 * new Color(160, 119, 0); colors[69] = new Color(176, 168, 0); colors[70] = new Color(111, 143, 0); colors[71] =
		 * new Color(63, 127, 31); colors[72] = new Color(0, 104, 48); colors[73] = new Color(0, 111, 111); colors[74] =
		 * new Color(0, 120, 159); colors[75] = new Color(0, 80, 127); colors[76] = new Color(0, 55, 96); colors[77] =
		 * new Color(31, 8, 79); colors[78] = new Color(64, 0, 79); colors[79] = new Color(96, 0, 79); colors[80] = new
		 * Color(143, 0, 80); colors[81] = new Color(143, 0, 63); colors[82] = new Color(111, 0, 31); colors[83] = new
		 * Color(112, 63, 0); colors[84] = new Color(127, 88, 0); colors[85] = new Color(128, 128, 0); colors[86] = new
		 * Color(80, 104, 0); colors[87] = new Color(47, 95, 16); colors[88] = new Color(0, 79, 47); colors[89] = new
		 * Color(0, 80, 80); colors[90] = new Color(0, 88, 112); colors[91] = new Color(0, 63, 95); colors[92] = new
		 * Color(79, 0, 63); colors[93] = new Color(111, 0, 47); colors[94] = new Color(207, 191, 160); colors[95] = new
		 * Color(160, 151, 128); colors[96] = new Color(128, 112, 96); colors[97] = new Color(95, 80, 64); colors[98] =
		 * new Color(63, 55, 48); colors[99] = new Color(207, 168, 127); colors[100] = new Color(175, 143, 95);
		 * colors[101] = new Color(144, 112, 64); colors[102] = new Color(127, 88, 47); colors[103] = new Color(96, 71,
		 * 16); colors[104] = new Color(255, 255, 255, 0); // to place custom colors colors[105] = null; colors[106] =
		 * null; colors[107] = null; colors[109] = null; colors[110] = null; colors[111] = null; colors[112] = null;
		 * colors[113] = null; colors[114] = null;
		 */
	}

	public ResourceBundle getBundle() {
		return colorBundle;
	}

	public void paint(Graphics g) {
		super.paint(g);
		// Draw the grid
		for (int i=0;i < columns;i++) {
			// g.setColor(Color.gray);
			g.setColor(Color.black);
			g.drawLine(i * squareSize,0,i * squareSize,paletteDimension.height - 1);
			// g.setColor(Color.lightGray);
			// g.drawLine(i*squareSize+1, 0, i*squareSize+1, paletteDimension.height);
			// g.setColor(Color.white);
			// g.drawLine(i*squareSize+2, 0, i*squareSize+2, paletteDimension.height);
		}
		int customRows=numberOfCustomColors / columns;
		for (int i=0;i <= rows - customRows;i++) {
			// g.setColor(Color.gray);
			g.setColor(Color.black);
			g.drawLine(0,i * squareSize,paletteDimension.width,i * squareSize);
			// g.setColor(Color.lightGray);
			// g.drawLine(0, i*squareSize+1, paletteDimension.width, i*squareSize+1);
			// g.setColor(Color.white);
			// g.drawLine(0, i*squareSize+2, paletteDimension.width, i*squareSize+2);
		}
		// draw the limit between custom and default palette
		// g.setColor(Color.gray);
		g.setColor(Color.black);
		g.drawLine(0,(rows - customRows) * squareSize + 3,paletteDimension.width,(rows - customRows) * squareSize + 3);
		// g.setColor(Color.lightGray);
		// g.drawLine(0, (rows-customRows)*squareSize+4, paletteDimension.width, (rows-customRows)*squareSize+4);
		// g.setColor(Color.white);
		// g.drawLine(0, (rows-customRows)*squareSize+5, paletteDimension.width, (rows-customRows)*squareSize+5);

		// draw the grid for the custom palette
		for (int i=rows - customRows + 1;i <= rows;i++) {
			// g.setColor(Color.gray);
			g.setColor(Color.black);
			g.drawLine(0,i * squareSize + 3,paletteDimension.width,i * squareSize + 3);

			// g.setColor(Color.lightGray);
			// g.drawLine(0, i*squareSize+4, paletteDimension.width, i*squareSize+4);
			// g.setColor(Color.white);
			// g.drawLine(0, i*squareSize+5, paletteDimension.width, i*squareSize+5);
		}

		// coloring the palette
		int colorIndex=0;
		for (int j=0;j < rows - customRows;j++)
			for (int i=0;i < columns;i++) {
				if (colorIndex < customIndex) {
					if (colors[colorIndex] == null) {
						g.setColor(Color.lightGray);
						// g.fillRect(i*squareSize+3,j*squareSize+3, squareSize-3, squareSize-3);
						g.fillRect(i * squareSize + 1,j * squareSize + 1,squareSize - 1,squareSize - 1);
					} else if (colors[colorIndex].getAlpha() == 0) {
						g.setColor(Color.white);
						// g.fillRect(i*squareSize+3,j*squareSize+3, squareSize-3, squareSize-3);
						g.fillRect(i * squareSize + 1,j * squareSize + 1,squareSize - 1,squareSize - 1);
						g.setColor(Color.black);
						String trans=colorBundle.getString("trans");
						int TLength=g.getFontMetrics().stringWidth(trans);
						g.drawString(trans,i * squareSize + squareSize / 2 - TLength / 2 - 1,j * squareSize + 12);
						g.drawString(trans,i * squareSize + squareSize / 2 - TLength / 2,j * squareSize + 12);

						// g.drawString("T", i*squareSize+5,j*squareSize+12);
						// g.drawString("T", i*squareSize+6,j*squareSize+12);
					} else {
						g.setColor(colors[colorIndex]);
						// g.fillRect(i*squareSize+3,j*squareSize+3, squareSize-3, squareSize-3);
						g.fillRect(i * squareSize + 1,j * squareSize + 1,squareSize - 1,squareSize - 1);
					}
					colorIndex++;
				}
			}
		// coloring the custom palette
		for (int j=rows - customRows;j < rows;j++)
			for (int i=0;i < columns;i++) {
				if (colors[colorIndex] == null) {
					g.setColor(Color.lightGray);
					// g.fillRect(i*squareSize+3,j*squareSize+6, squareSize-3, squareSize-3);
					g.fillRect(i * squareSize + 1,j * squareSize + 4,squareSize - 1,squareSize - 1);
				} else {
					g.setColor(colors[colorIndex]);
					// g.fillRect(i*squareSize+3,j*squareSize+6, squareSize-3, squareSize-3);
					g.fillRect(i * squareSize + 1,j * squareSize + 4,squareSize - 1,squareSize - 1);
					colorIndex++;
				}
			}

		// indicate foreground color
		if (enableForegroundIndicator) {
			Point corner=findCornerFromIndex(selectedForegroundIndex);
			g.setColor(foregroundIndicatorColor);
			if (selectedForegroundIndex >= customIndex)
				// g.drawRect(corner.x+1, corner.y+4, squareSize-1, squareSize-1);
				g.drawRect(corner.x,corner.y + 2,squareSize - 1,squareSize - 1);
			else
				// g.drawRect(corner.x+1, corner.y+1, squareSize-1, squareSize-1);
				g.drawRect(corner.x,corner.y,squareSize - 2,squareSize - 2);
		}

		// indicate background color
		if (enableBackgroundIndicator) {
			Point corner=findCornerFromIndex(selectedBackgroundIndex);
			g.setColor(backgroundIndicatorColor);
			if (selectedBackgroundIndex >= customIndex)
				// g.drawRect(corner.x+1, corner.y+4, squareSize-1, squareSize-1);
				g.drawRect(corner.x,corner.y + 2,squareSize - 1,squareSize - 1);
			else
				// g.drawRect(corner.x+1, corner.y+1, squareSize-1, squareSize-1);
				g.drawRect(corner.x,corner.y,squareSize - 2,squareSize - 2);
		}
	}

	// find the coordinates of the corner of the square that corresponds to index (of color table)
	private Point findCornerFromIndex(int index) {
		int row=index / columns;
		int column=index % columns;
		return new Point(column * squareSize + 1,row * squareSize + 1);
	}

	private int findIndexFromSquare(int x,int y) {
		int column=x / squareSize;
		int row=y / squareSize;
		int index=row * columns + column;
		if (index >= numberOfColors)
			return -1;
		return index;
	}

	// signals the existance of a foreground indicator with the specified color
	public void setForegroundIndicator(Color color) {
		enableForegroundIndicator=true;
		foregroundIndicatorColor=color;
	}

	// signals the existance of a background indicator with the specified color
	public void setBackgroundIndicator(Color color) {
		enableBackgroundIndicator=true;
		backgroundIndicatorColor=color;
	}

	// returns the selected foreground color
	public Color getSelectedForegroundColor() {
		if (selectedForegroundIndex < numberOfColors)
			return colors[selectedForegroundIndex];
		else
			return Color.black;

	}

	// returns the selected foreground index
	public int getSelectedForegroundIndex() {
		if (selectedForegroundIndex < numberOfColors)
			return selectedForegroundIndex;
		else
			return -1;
	}

	// returns the index of the color or -1 if the color does not exist in the array
	public int getColorIndex(Color color) {
		for (int i=0;i < numberOfColors;i++)
			if (color.equals(colors[i]))
				return i;
		return -1;
	}

	// returns the selected background index
	public int getSelectedBackgroundIndex() {
		if (selectedBackgroundIndex < numberOfColors)
			return selectedBackgroundIndex;
		else
			return -1;
	}

	// returns the selected background color
	public Color getSelectedBackgroundColor() {
		if (selectedBackgroundIndex < numberOfColors)
			return colors[selectedBackgroundIndex];
		else
			return null;
	}

	// sets the foreground color that corresponds to the specified index
	public boolean setSelectedForegroundIndex(int index) {
		if (index < numberOfColors) {
			selectedForegroundIndex=index;
			repaint();
			fireActiveColorChanged(new ActiveColorEvent(this,true));
			return true;
		} else
			return false;
	}

	// sets the background color that corresponds to the specified index
	public boolean setSelectedBackgroundIndex(int index) {
		if (index < numberOfColors) {
			selectedBackgroundIndex=index;
			fireActiveColorChanged(new ActiveColorEvent(this,false));
			repaint();
			return true;
		} else
			return false;
	}

	// sets the foreground color to the specified color
	public boolean setSelectedForegroundColor(Color color) {
		for (int i=0;i < numberOfColors;i++)
			if (color.equals(colors[i])) {
				selectedForegroundIndex=i;
				fireActiveColorChanged(new ActiveColorEvent(this,true));
				repaint();
				return true;
			}
		return false;
	}

	// sets the background color to the specified color
	public boolean setSelectedBackgroundColor(Color color) {
		for (int i=0;i < numberOfColors;i++)
			if (color.equals(colors[i])) {
				selectedBackgroundIndex=i;
				fireActiveColorChanged(new ActiveColorEvent(this,false));
				return true;
			}
		return false;
	}

	// adds a color to the custom palette and return its index
	public int addColor(Color color) {
		if (color == null)
			return -1;
		isPaletteModified=true;
		if (mode == 0) { // normal mode
			colors[firstEmptyCustomIndex]=color;
			int index=firstEmptyCustomIndex;
			firstEmptyCustomIndex++;
			if (firstEmptyCustomIndex >= numberOfColors)
				firstEmptyCustomIndex=customIndex;
			repaint();
			return index;
		} else {
			colors[paletteIndex]=color;
			int index=paletteIndex;
			paletteIndex++;
			if (paletteIndex >= customIndex)
				mode=0;
			repaint();
			return index;
		}
	}

	public int getColorsCount() {
		return colors.length;
	}

	/*
	 * public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
	 * System.out.println("colorPalette read Ext "); ESlateFieldMap fieldMap = (ESlateFieldMap)in.readObject();
	 * numberOfColors = fieldMap.get("NumberOfColors", 115); colors = new Color[numberOfColors]; Color[] openedArray =
	 * (Color[])fieldMap.get("ColorArray");
	 * 
	 * System.arraycopy(openedArray,0, colors, 0, openedArray.length);
	 * 
	 * numberOfCustomColors = fieldMap.get("NumberOfCustomColors", 10); firstEmptyCustomIndex =
	 * fieldMap.get("FirstEmptyCustomIndex", 1); customIndex = fieldMap.get("CustomIndex", 1); mode =
	 * fieldMap.get("Mode", 0); paletteIndex = fieldMap.get("PaletteIndex", 0);
	 * 
	 * columns = paletteDimension.width/squareSize -1; if (colors.length % columns == 0) rows = colors.length / columns;
	 * else rows = colors.length / columns +1;
	 * 
	 * customIndex = numberOfColors - numberOfCustomColors; firstEmptyCustomIndex = customIndex;
	 * 
	 * 
	 * //paletteDimension = (Dimension)fieldMap.get("Dimension");
	 * 
	 * 
	 * repaint(); }
	 * 
	 * public void writeExternal(ObjectOutput out) throws IOException { ESlateFieldMap fieldMap = new
	 * ESlateFieldMap(STR_FORMAT_VERSION); System.out.println("colorPalette writeExt "); fieldMap.put("ColorArray",
	 * colors); fieldMap.put("NumberOfColors", numberOfColors); System.out.println("colorPalette numberOfCol:
	 * "+numberOfColors);
	 * 
	 * fieldMap.put("NumberOfCustomColors", numberOfCustomColors); fieldMap.put("FirstEmptyCustomIndex",
	 * firstEmptyCustomIndex); fieldMap.put("CustomIndex", customIndex); fieldMap.put("Mode", mode);
	 * fieldMap.put("PaletteIndex", paletteIndex); fieldMap.put("Dimension", paletteDimension);
	 * out.writeObject(fieldMap); out.flush(); }
	 * 
	 */
	public ArrayList paletteToList() {
		ArrayList<Object> list=new ArrayList<Object>();
		list.add(colors);
		list.add(new Integer(numberOfColors));
		list.add(new Integer(numberOfCustomColors));
		list.add(new Integer(firstEmptyCustomIndex));
		list.add(new Integer(customIndex));
		list.add(new Integer(mode));
		list.add(new Integer(paletteIndex));
		list.add(new Integer(selectedForegroundIndex));
		list.add(new Integer(selectedBackgroundIndex));
		return list;
	}

	public void restorePalette(ArrayList list) {
		if (list == null)
			return;
		Color[] openedArray=(Color[]) list.get(0);
		numberOfColors=((Integer) list.get(1)).intValue();
		numberOfCustomColors=((Integer) list.get(2)).intValue();
		firstEmptyCustomIndex=((Integer) list.get(3)).intValue();
		customIndex=((Integer) list.get(4)).intValue();
		mode=((Integer) list.get(5)).intValue();
		paletteIndex=((Integer) list.get(6)).intValue();

		colors=new Color[numberOfColors];
		System.arraycopy(openedArray,0,colors,0,openedArray.length);
		// setting indexes out of bounds because no color must be selected
		try {
			selectedForegroundIndex=((Integer) list.get(7)).intValue();// numberOfColors+1;
			selectedBackgroundIndex=((Integer) list.get(8)).intValue();// numberOfColors+1;
		} catch (Exception ex) {
			selectedForegroundIndex=numberOfColors + 1;
			selectedBackgroundIndex=numberOfColors + 1;
		}
		fireActiveColorChanged(new ActiveColorEvent(this,true));
		fireActiveColorChanged(new ActiveColorEvent(this,false));
		if (colors.length % columns == 0)
			rows=colors.length / columns;
		else
			rows=colors.length / columns + 1;
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap between
		// custom
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);
		enableForegroundIndicator=false;
		enableBackgroundIndicator=false;
		((JViewport) getParent()).setViewPosition(new Point(0,0));
		revalidate();
		repaint();
	}

	// saves a color palette to a *.cpl file
	public void save() {
		// creates a fileDialog in order to select the name and location to save
		String fileName=new String("");
		fileDialog.setTitle(colorBundle.getString("savePalette"));
		fileDialog.setMode(FileDialog.SAVE);
		String directory=null;
		if (fileName == null || fileName.length() == 0) {
			File file;
			int counter=1;
			String cpaExtension=".cpa";
			while (true) {
				fileName=colorBundle.getString("palette") + counter + cpaExtension;
				file=new File(fileName);
				if (!file.exists())
					break;
				else
					counter++;
			}
		} else if (fileName.indexOf(System.getProperty("file.separator")) != -1) {
			directory=fileName.substring(0,fileName.lastIndexOf(System.getProperty("file.separator")));
			fileName=fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator")) + 1);
		}
		if (directory != null)
			fileDialog.setDirectory(directory);
		fileDialog.setFile(fileName);
		fileDialog.setVisible(true);
		if (fileDialog.getFile() != null) {
			// creates a fileStream and stores an object stream that contains the color array
			fileName=fileDialog.getDirectory() + fileDialog.getFile();
			try {
				FileOutputStream fout=new FileOutputStream(fileName);
				ObjectOutputStream objectStream=new ObjectOutputStream(fout);
				objectStream.writeObject(colors);
				objectStream.writeInt(numberOfColors);
				objectStream.writeInt(numberOfCustomColors);
				objectStream.writeInt(firstEmptyCustomIndex);
				objectStream.writeInt(customIndex);
				objectStream.writeInt(mode);
				objectStream.writeInt(paletteIndex);
				objectStream.flush();
				fout.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			isPaletteModified=false;
		} else {
			System.out.println("Save Palette Failed");
		}
	}

	// opens a palette from *.cpl files
	public void open() {
		String fileName=new String("");
		fileDialog.setTitle(colorBundle.getString("openPalette"));
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setFile("*.cpa");
		fileDialog.setVisible(true);
		if (fileDialog.getFile() != null) {
			fileName=fileDialog.getDirectory() + fileDialog.getFile();
			try {
				FileInputStream fin=new FileInputStream(fileName);
				ObjectInputStream inStream=new ObjectInputStream(fin);
				// restore values
				Color[] openedArray=(Color[]) inStream.readObject();
				numberOfColors=inStream.readInt();
				numberOfCustomColors=inStream.readInt();
				firstEmptyCustomIndex=inStream.readInt();
				customIndex=inStream.readInt();
				mode=inStream.readInt();
				paletteIndex=inStream.readInt();
				colors=new Color[numberOfColors];
				System.arraycopy(openedArray,0,colors,0,openedArray.length);
				// setting indexes out of bounds because no color must be selected
				selectedForegroundIndex=numberOfColors + 1;
				selectedBackgroundIndex=numberOfColors + 1;
				fireActiveColorChanged(new ActiveColorEvent(this,true));
				fireActiveColorChanged(new ActiveColorEvent(this,false));
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,colorBundle.getString("noFile"),colorBundle.getString("alert"),JOptionPane.ERROR_MESSAGE);
			}
			if (colors.length % columns == 0)
				rows=colors.length / columns;
			else
				rows=colors.length / columns + 1;
			Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap
			// between custom
			paletteDimension=fixedDimension;
			setPreferredSize(fixedDimension);
			setMaximumSize(fixedDimension);
			setMinimumSize(fixedDimension);
			enableForegroundIndicator=false;
			enableBackgroundIndicator=false;
			((JViewport) getParent()).setViewPosition(new Point(0,0));
			revalidate();
			repaint();

		} else
			System.out.println("Load File Failed");
	}

	public void newPalette(int numberOfCols,int numberOfCustomCols) {
		colors=new Color[numberOfCols];
		numberOfColors=numberOfCols;
		numberOfCustomColors=numberOfCustomCols;
		customIndex=numberOfColors - numberOfCustomColors;
		firstEmptyCustomIndex=customIndex;
		// setting indexes out of bounds because no color must be selected
		selectedForegroundIndex=numberOfColors + 1;
		selectedBackgroundIndex=numberOfColors + 1;
		fireActiveColorChanged(new ActiveColorEvent(this,true));
		fireActiveColorChanged(new ActiveColorEvent(this,false));
		enableForegroundIndicator=false;
		enableBackgroundIndicator=false;

		if (colors.length % columns == 0)
			rows=colors.length / columns;
		else
			rows=colors.length / columns + 1;
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap between
		// custom
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);
		mode=1;
		paletteIndex=0;
		((JViewport) getParent()).setViewPosition(new Point(0,0));
		revalidate();
		repaint();
		isPaletteModified=true;
	}

	// Listener implementations
	public void mouseClicked(MouseEvent e) {
		;
	}

	public void mouseEntered(MouseEvent e) {
		;
	}

	public void mouseExited(MouseEvent e) {
		;
	}

	public void mousePressed(MouseEvent e) {
		// when double clicked
		if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && e.getY() < paletteDimension.height) {
			Color color=JColorChooser.showDialog(new JFrame(),colorBundle.getString("colorChooser"),getSelectedForegroundColor());
			if (color != null) {
				int colorIndex=getColorIndex(color);
				if (colorIndex == -1)
					colorIndex=addColor(color);
				if (!enableForegroundIndicator)
					enableForegroundIndicator=true;
				setSelectedForegroundIndex(colorIndex);
				return;
			}
		}

		if (e.getClickCount() == 2 && SwingUtilities.isRightMouseButton(e) && e.getY() < paletteDimension.height) {
			Color color=JColorChooser.showDialog(new JFrame(),colorBundle.getString("colorChooser"),getSelectedForegroundColor());
			if (color != null) {
				int colorIndex=getColorIndex(color);
				if (colorIndex == -1)
					colorIndex=addColor(color);
				if (!enableBackgroundIndicator)
					enableBackgroundIndicator=true;
				setSelectedBackgroundIndex(colorIndex);
				return;
			}
		}

		// when pressed
		Graphics g=this.getGraphics();
		g.setClip(0,0,paletteDimension.width + 3,paletteDimension.height + 3);

		if (SwingUtilities.isLeftMouseButton(e) && e.getY() < paletteDimension.height) {
			int index=findIndexFromSquare(e.getX(),e.getY());
			if (index != -1) {
				if (colors[index] != null) {
					if (!enableForegroundIndicator)
						enableForegroundIndicator=true;
					selectedForegroundIndex=index;
					fireActiveColorChanged(new ActiveColorEvent(this,true));
				}
			}
		}
		if (SwingUtilities.isRightMouseButton(e) && e.getY() < paletteDimension.height) {
			int index=findIndexFromSquare(e.getX(),e.getY());
			if (index != -1) {
				if (colors[index] != null) {
					if (!enableBackgroundIndicator)
						enableBackgroundIndicator=true;
					selectedBackgroundIndex=index;
					fireActiveColorChanged(new ActiveColorEvent(this,false));
				}
			}
		}
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		;
	}

	protected void fireActiveColorChanged(ActiveColorEvent e) {
		ArrayList listeners;
		synchronized (this) {
			listeners=(ArrayList) activeColorListeners.clone();
		}
		for (int i=0;i < listeners.size();i++)
			((ActiveColorListener) listeners.get(i)).activeColorChanged(e);
	}

	public synchronized void addActiveColorListener(ActiveColorListener acl) {
		if (activeColorListeners.indexOf(acl) == -1)
			activeColorListeners.add(acl);
	}

	public synchronized void removeActiveColorListener(ActiveColorListener acl) {
		if (activeColorListeners.indexOf(acl) != -1)
			activeColorListeners.remove(acl);
	}
}
