// IconPalette by Drossos Nikos @ 4/2000

package gr.cti.eslate.iconPalette;

import gr.cti.eslate.imageEditor.Utilities;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

//import Acme.JPM.Encoders.*;

public class IconPalette extends JPanel implements MouseListener {

	private ImageIcon[] icons;

	private ImageIcon[] originalIcons;

	int squareSize=35;

	int numberOfIcons=20;

	int rows, columns;

	int selectedIconIndex, firstEmptyIndex;

	private Vector<IconPlacedListener> iconPlacedListeners=new Vector<IconPlacedListener>();

	static JFrame dialogFrame=new JFrame();

	static transient FileDialog fileDialog=new FileDialog(dialogFrame);

	Dimension paletteDimension;

	boolean enableIconIndicator;

	Color iconIndicatorColor;

	boolean isFocused=false;

	public boolean isPaletteModified=false;

	ResourceBundle iconBundle=ResourceBundle.getBundle("gr.cti.eslate.iconPalette.IconPaletteBundle",Locale.getDefault());

	// constructs a default icon palette with the specified Dimension
	// red icon indicator
	public IconPalette(Dimension palDimension) {
		icons=new ImageIcon[numberOfIcons];
		originalIcons=new ImageIcon[numberOfIcons];

		columns=palDimension.width / squareSize;

		if (icons.length % columns == 0)
			rows=icons.length / columns;
		else
			rows=icons.length / columns + 1;

		// adjust the width of the palette in order to have only full squares
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize);
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);

		createDefaultIcons();
		System.arraycopy(icons,0,originalIcons,0,icons.length);
		firstEmptyIndex=findFirstEmptyIndex();
		this.addMouseListener(this);
		this.addMouseMotionListener(motionListener);
		setLayout(null);
		// initialize indicators
		enableIconIndicator=true;
		iconIndicatorColor=Color.red;
	}

	// constructs a default icon palette with the specified Dimension
	// red icon indicator
	// and the specified array as icons
	public IconPalette(ImageIcon[] icos,Dimension palDimension) {
		icons=new ImageIcon[icons.length];
		originalIcons=new ImageIcon[numberOfIcons];

		System.arraycopy(icos,0,icons,0,icos.length);
		System.arraycopy(icons,0,originalIcons,0,icons.length);

		numberOfIcons=icos.length;
		columns=palDimension.width / squareSize;

		if (icons.length % columns == 0)
			rows=icons.length / columns;
		else
			rows=icons.length / columns + 1;

		// adjust the width of the palette in order to have only full squares
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize);
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);

		this.addMouseListener(this);
		setLayout(null);
		// initialize indicators
		enableIconIndicator=true;
		iconIndicatorColor=Color.red;
	}

	public Dimension getDimension() {
		return paletteDimension;
	}

	private int findFirstEmptyIndex() {
		for (int i=0;i < numberOfIcons;i++)
			if (icons[i] == null)
				return i;
		return -1;
	}

	// loads an icon
	public ImageIcon loadImageIcon(String filename,String description) {
		try {
			URL u=this.getClass().getResource(filename);
			if (u != null)
				return new ImageIcon(u,description);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}// loadImageIcon

	private void createDefaultIcons() {
		icons[0]=loadImageIcon("Images/newFile.gif"," ");
		icons[1]=loadImageIcon("Images/pen.gif"," ");
		icons[2]=loadImageIcon("Images/line.gif"," ");
		icons[3]=loadImageIcon("Images/rect.gif"," ");
		icons[4]=loadImageIcon("Images/ellipse.gif"," ");
		icons[5]=loadImageIcon("Images/wand.gif"," ");
		icons[6]=loadImageIcon("Images/menuPen.gif"," ");
		icons[7]=loadImageIcon("Images/select.gif"," ");
		icons[8]=loadImageIcon("Images/wand.gif"," ");
		icons[9]=loadImageIcon("Images/open.gif"," ");
		icons[10]=loadImageIcon("Images/iconDim.gif"," ");
		icons[11]=loadImageIcon("Images/delete.gif"," ");
		icons[12]=loadImageIcon("Images/invertSelection.gif"," ");
		icons[13]=loadImageIcon("Images/backColor.gif"," ");
		icons[14]=loadImageIcon("Images/delsel.gif"," ");
		icons[15]=loadImageIcon("Images/crop.gif"," ");
		icons[16]=null;
		icons[17]=null;
		icons[18]=null;
		icons[19]=null;
	}

	public void paint(Graphics g) {
		super.paint(g);
		// Draw the grid
		for (int i=0;i <= columns;i++) {
			g.setColor(Color.gray);
			g.drawLine(i * squareSize,0,i * squareSize,paletteDimension.height);
			g.setColor(Color.lightGray);
			g.drawLine(i * squareSize + 1,0,i * squareSize + 1,paletteDimension.height);
			g.setColor(Color.white);
			g.drawLine(i * squareSize + 2,0,i * squareSize + 2,paletteDimension.height);
		}
		for (int i=0;i <= rows;i++) {
			g.setColor(Color.gray);
			g.drawLine(0,i * squareSize,paletteDimension.width,i * squareSize);
			g.setColor(Color.lightGray);
			g.drawLine(0,i * squareSize + 1,paletteDimension.width,i * squareSize + 1);
			g.setColor(Color.white);
			g.drawLine(0,i * squareSize + 2,paletteDimension.width,i * squareSize + 2);
		}

		// coloring the palette
		int iconIndex=0;
		for (int j=0;j < rows;j++)
			for (int i=0;i < columns;i++) {
				if (iconIndex < numberOfIcons) {
					if (icons[iconIndex] == null) {
						g.setColor(Color.lightGray);
						g.fillRect(i * squareSize + 3,j * squareSize + 3,squareSize - 3,squareSize - 3);
					} else {
						g.drawImage(icons[iconIndex].getImage(),i * squareSize + 3,j * squareSize + 3,null);
					}
					iconIndex++;
				}
			}

		// indicate icon
		if (enableIconIndicator) {
			Point corner=findCornerFromIndex(selectedIconIndex);
			g.setColor(iconIndicatorColor);
			g.drawRect(corner.x + 1,corner.y + 1,squareSize - 1,squareSize - 1);
		}
	}

	// find the coordinates of the corner of the square that corresponds to index (of icon table)
	private Point findCornerFromIndex(int index) {
		int row=index / columns;
		int column=index % columns;
		return new Point(column * squareSize + 1,row * squareSize + 1);
	}

	private int findIndexFromSquare(int x,int y) {
		int column=x / squareSize;
		int row=y / squareSize;
		int index=row * columns + column;
		if (index >= numberOfIcons)
			return -1;
		return index;
	}

	// signals the existance of an icon indicator with the specified color
	public void setIconIndicator(Color color) {
		enableIconIndicator=true;
		iconIndicatorColor=color;
	}

	// returns the selected icon
	public ImageIcon getSelectedIcon() {
		if (selectedIconIndex < numberOfIcons)
			return originalIcons[selectedIconIndex];
		else
			return null;

	}

	// returns the selected foreground index
	public int getSelectedIconIndex() {
		if (selectedIconIndex < numberOfIcons)
			return selectedIconIndex;
		else
			return -1;
	}

	// returns the index of the icon or -1 if the icon does not exist in the array
	public int getIconIndex(ImageIcon ico) {
		for (int i=0;i < numberOfIcons;i++)
			if (ico.equals(icons[i]))
				return i;
		return -1;
	}

	// sets the icon that corresponds to the specified index
	public boolean setSelectedIconIndex(int index) {
		if (index < numberOfIcons) {
			selectedIconIndex=index;
			repaint();
			return true;
		} else
			return false;
	}

	// sets the specified icon
	public boolean setSelectedIcon(ImageIcon ico) {
		for (int i=0;i < numberOfIcons;i++)
			if (ico.equals(originalIcons[i])) {
				selectedIconIndex=i;
				repaint();
				return true;
			}
		return false;
	}

	// adds an icon to the palette and return its index
	public int addIcon(ImageIcon ico) {
		if (ico == null)
			return -1;
		originalIcons[firstEmptyIndex]=ico;

		if (ico.getIconWidth() >= squareSize || ico.getIconHeight() >= squareSize) {
			Image im=ico.getImage();
			im=im.getScaledInstance(squareSize - 3,squareSize - 3,Image.SCALE_DEFAULT);
			ico=new ImageIcon(im);
		}

		icons[firstEmptyIndex]=ico;

		int index=firstEmptyIndex;
		firstEmptyIndex++;
		if (firstEmptyIndex >= numberOfIcons)
			firstEmptyIndex=0;
		repaint();
		isPaletteModified=true;
		return index;
	}

	public int getIconsCount() {
		// return icons.length;
		return numberOfIcons;
	}

	// stores icons to an ArrayList as gifs
	public ArrayList saveIconsToStream() {
		ArrayList<byte[]> iconStreams=new ArrayList<byte[]>();
		try {
			for (int i=0;i < numberOfIcons;i++) {
				if (originalIcons[i] != null) {
					ByteArrayOutputStream imageStream=new ByteArrayOutputStream();
					// RestorableImageIcon iconToSave = new RestorableImageIcon(originalIcons[i].getImage());
					NewRestorableImageIcon iconToSave=new NewRestorableImageIcon(originalIcons[i].getImage());
					// iconToSave.saveImage(RestorableImageIcon.GIF, imageStream);
					iconToSave.saveImage(NewRestorableImageIcon.GIF,imageStream);

					// GifEncoder ge = new GifEncoder(originalIcons[i].getImage(),imageStream);
					// ge.encode();
					imageStream.flush();
					imageStream.close();
					iconStreams.add(imageStream.toByteArray());
					imageStream.reset();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return iconStreams;
	}

	public void restorePalette(int numberOfIcos,ArrayList iconsList) {
		newPalette(numberOfIcos);
		for (int i=0;i < iconsList.size();i++) {
			byte[] imageByte=(byte[]) iconsList.get(i);
			ImageIcon icon=new ImageIcon(imageByte);
			addIcon(icon);
		}
	}

	// saves an icon palette to a *.ipa file
	public void save() {
		// creates a fileDialog in order to select the name and location to save
		String fileName=new String("");
		fileDialog.setTitle(iconBundle.getString("savePalette"));
		fileDialog.setMode(FileDialog.SAVE);
		String directory=null;
		if (fileName == null || fileName.length() == 0) {
			File file;
			int counter=1;
			String ipaExtension=".ipa";
			while (true) {
				fileName=iconBundle.getString("palette") + counter + ipaExtension;
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
			// creates a fileStream and stores an object stream that contains the icon array
			fileName=fileDialog.getDirectory() + fileDialog.getFile();
			String extension=fileName.substring(fileName.length() - 4);
			if (!extension.equals(".ipa"))
				fileName=fileName.concat(".ipa");

			try {
				FileOutputStream fout=new FileOutputStream(fileName);
				ObjectOutputStream objectStream=new ObjectOutputStream(fout);
				objectStream.writeObject(icons);
				objectStream.writeObject(originalIcons);
				objectStream.writeInt(numberOfIcons);
				objectStream.writeInt(firstEmptyIndex);
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

	// opens a palette from *.ipa files
	public void open() {
		String fileName=new String("");
		fileDialog.setTitle(iconBundle.getString("openPalette"));
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setFile("*.ipa");
		fileDialog.setVisible(true);
		if (fileDialog.getFile() != null) {
			fileName=fileDialog.getDirectory() + fileDialog.getFile();
			try {
				FileInputStream fin=new FileInputStream(fileName);
				ObjectInputStream inStream=new ObjectInputStream(fin);
				// restore values
				ImageIcon[] openedArray=(ImageIcon[]) inStream.readObject();
				ImageIcon[] openedArrayOriginal=(ImageIcon[]) inStream.readObject();
				numberOfIcons=inStream.readInt();
				firstEmptyIndex=inStream.readInt();
				icons=new ImageIcon[numberOfIcons];
				originalIcons=new ImageIcon[numberOfIcons];
				System.arraycopy(openedArray,0,icons,0,openedArray.length);
				System.arraycopy(openedArrayOriginal,0,originalIcons,0,openedArrayOriginal.length);
				// setting indexes out of bounds because no icon must be selected
				selectedIconIndex=numberOfIcons + 1;
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,iconBundle.getString("noFile"),iconBundle.getString("alert"),JOptionPane.ERROR_MESSAGE);
			}
			if (icons.length % columns == 0)
				rows=icons.length / columns;
			else
				rows=icons.length / columns + 1;
			Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize + 4); // 4 is for the gap
			// between custom
			paletteDimension=fixedDimension;
			setPreferredSize(fixedDimension);
			setMaximumSize(fixedDimension);
			setMinimumSize(fixedDimension);
			enableIconIndicator=false;
			((JViewport) getParent()).setViewPosition(new Point(0,0));
			revalidate();
			repaint();

		} else
			System.out.println("Load File Failed");
	}

	public void newPalette(int numberOfIcos) {
		icons=new ImageIcon[numberOfIcos];
		originalIcons=new ImageIcon[numberOfIcos];
		numberOfIcons=numberOfIcos;
		firstEmptyIndex=0;
		// setting indexes out of bounds because no icon must be selected
		selectedIconIndex=numberOfIcons + 1;
		enableIconIndicator=false;

		if (icons.length % columns == 0)
			rows=icons.length / columns;
		else
			rows=icons.length / columns + 1;
		Dimension fixedDimension=new Dimension(columns * squareSize,rows * squareSize);
		paletteDimension=fixedDimension;
		setPreferredSize(fixedDimension);
		setMaximumSize(fixedDimension);
		setMinimumSize(fixedDimension);
		((JViewport) getParent()).setViewPosition(new Point(0,0));
		revalidate();
		repaint();
		isPaletteModified=true;
	}

	public void setFocused(boolean flag) {
		isFocused=flag;
		if (isFocused) {
			fireIconPlaced(new IconPlacedEvent(this,null,null,false));
			registerKeyboardAction(deleteIconListener,KeyStroke.getKeyStroke("DELETE"),WHEN_IN_FOCUSED_WINDOW);
		} else
			unregisterKeyboardAction(KeyStroke.getKeyStroke("DELETE"));
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
		setFocused(true);
		if (SwingUtilities.isLeftMouseButton(e) && e.getY() < paletteDimension.height) {
			int index=findIndexFromSquare(e.getX(),e.getY());
			if (index != -1) {
				if (icons[index] != null) {
					if (!enableIconIndicator)
						enableIconIndicator=true;
					selectedIconIndex=index;
				} else
					selectedIconIndex=-1;
			}
		}
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		if (selectedIconIndex == -1)
			return;
		Point p=e.getPoint();
		SwingUtilities.convertPointToScreen(p,IconPalette.this);
		IconPalette.this.getTopLevelAncestor().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		IconPalette.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if (IconPalette.this.contains(e.getPoint()))
			return;
		fireIconPlaced(new IconPlacedEvent(this,originalIcons[selectedIconIndex],p,false));
	}

	public void deleteSelectedIcon() {
		if (selectedIconIndex == -1 || icons[selectedIconIndex] == null)
			return;
		icons[selectedIconIndex]=null;
		originalIcons[selectedIconIndex]=null;
		for (int i=selectedIconIndex + 1;i < numberOfIcons;i++)
			if (icons[i] != null) {
				originalIcons[i - 1]=originalIcons[i];
				icons[i - 1]=icons[i];
				icons[i]=null;
				originalIcons[i]=null;
			}
		// numberOfIcons--;
		repaint();
		isPaletteModified=true;
		firstEmptyIndex=findFirstEmptyIndex();
	}

	ActionListener deleteIconListener=new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			deleteSelectedIcon();
		}
	};

	MouseMotionListener motionListener=new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
			if (selectedIconIndex == -1)
				return;
			BufferedImage selectedImage=Utilities.makeBufferedImage(icons[selectedIconIndex].getImage(),BufferedImage.TYPE_INT_ARGB);
			Cursor cursor=Toolkit.getDefaultToolkit().createCustomCursor(selectedImage,new Point(13,23),"selectedImage");
			IconPalette.this.setCursor(cursor);
			if (IconPalette.this.contains(e.getPoint()))
				return;
			setCursorToCanvas(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			Point p=e.getPoint();
			SwingUtilities.convertPointToScreen(p,IconPalette.this);
			fireIconPlaced(new IconPlacedEvent(this,originalIcons[selectedIconIndex],p,true));

		}
	};

	private void setCursorToCanvas(Cursor cursor) {
		Container c=IconPalette.this.getParent().getParent().getParent().getParent().getParent().getParent().getParent();

		JPanel hostPanel=((JPanel) c.getComponent(1));
		JRootPane r=((JRootPane) hostPanel.getComponent(0));
		r.getGlassPane().setCursor(cursor);
		JScrollPane s=((JScrollPane) r.getContentPane().getComponent(0));
		s.getViewport().getView().setCursor(cursor);
	}

	protected void fireIconPlaced(IconPlacedEvent e) {
		Vector listeners;
		synchronized (this) {
			listeners=(Vector) iconPlacedListeners.clone();
		}
		for (int i=0;i < listeners.size();i++)
			((IconPlacedListener) listeners.elementAt(i)).iconPlaced(e);
	}

	public synchronized void addIconPlacedListener(IconPlacedListener ipl) {
		if (iconPlacedListeners.indexOf(ipl) == -1)
			iconPlacedListeners.addElement(ipl);
	}

	public synchronized void removeIconPlacedListener(IconPlacedListener ipl) {
		if (iconPlacedListeners.indexOf(ipl) != -1)
			iconPlacedListeners.removeElement(ipl);
	}

}
