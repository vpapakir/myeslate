package gr.cti.eslate.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * Editor kit customizer.
 *
 * @version     2.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class KitCustomizer extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The class of teh editor kit being customized.
   */
  Class kitClass;
  /**
   * Localized resources.
   */
  private ResourceBundle resources;
  /**
   * The names of the colorings supported by the editor kit.
   */
  private String[] cNames;
  private boolean ignoreEvents = false;

  // UI elements.
  private JList list;
  private JComboBox fonts;
  private JComboBox fontSizes;
  private JCheckBox bold;
  private JCheckBox italic;
  private JButton fgColor;
  private JButton bgColor;
  private JLabel sample;

  /**
   * A cached copy of the names of all available fonts.
   */
  static Vector<String> fontNames = null;

  /**
   * Make a cached copy of the names of all available fonts.
   */
  static {
    if (fontNames == null) {
      fontNames = new Vector<String>();
      GraphicsEnvironment ge =
        GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] fNames = ge.getAvailableFontFamilyNames();
      int nFonts = fNames.length;
      for (int i=0; i<nFonts; i++) {
        if (!fNames[i].equals("")) {
          fontNames.addElement(fNames[i]);
        }
      }
    }
  }

  /**
   * Create an editor kit customizer.
   * @param     kitClass        The class of the editor kit.
   */
  public KitCustomizer(Class kitClass)
  {
    this.kitClass = kitClass;
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.editor.EditorResource", Locale.getDefault()
    );

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    cNames = FontOptions.getColoringNames(kitClass);
    int n = cNames.length;
    String[] cDisplayNames = new String[n];
    for (int i=0; i<n; i++) {
      String name = cNames[i];
      cDisplayNames[i] = resources.getString(name);
    }
    HashMap<String, String> hm = new HashMap<String, String>();
    for (int i=0; i<n; i++) {
      hm.put(cDisplayNames[i], cNames[i]);
    }
    Arrays.sort(cDisplayNames);
    for (int i=0; i<n; i++) {
      cNames[i] = hm.get(cDisplayNames[i]);
    }
    hm.clear();
    int selectedIndex = 0;
    for (int i=0; i<n; i++) {
      String name = cNames[i];
      if (name.equals("default")) {
        selectedIndex = i;
      }
    }
    list = new JList(cDisplayNames);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(selectedIndex);
    list.addListSelectionListener(new ListSelectionListener(){
      public void valueChanged(ListSelectionEvent e)
      {
        listListSelectionListener(e);
      }
    });
    JScrollPane scrollPane = new JScrollPane(list);
    add(scrollPane);

    JPanel props = new JPanel();
    props.setLayout(new BoxLayout(props, BoxLayout.Y_AXIS));

    JPanel fBox = new JPanel();
    fBox.setBorder(new TitledBorder(resources.getString("font")));
    fBox.setLayout(new BoxLayout(fBox, BoxLayout.Y_AXIS));

    JPanel fontSelector = new JPanel();
    fontSelector.setLayout(new BoxLayout(fontSelector, BoxLayout.X_AXIS));
    fonts = new JComboBox(fontNames);
    fonts.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontsItemStateChanged(evt);
      }
    });
    fontSelector.add(fonts);
    fontSizes = new JComboBox(new Object[] {
      new Integer(8), new Integer(9), new Integer(10), new Integer(11),
      new Integer(12), new Integer(14), new Integer(16), new Integer(18),
      new Integer(20), new Integer(22), new Integer(24), new Integer(26),
      new Integer(28), new Integer(36), new Integer(48), new Integer(72)
    });
    fontSizes.setEditable(true);
    Dimension s = new Dimension(48, fontSizes.getPreferredSize().height);
    fontSizes.setMinimumSize(s);
    fontSizes.setMaximumSize(s);
    fontSizes.setPreferredSize(s);
    fontSizes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontSizesItemStateChanged(evt);
      }
    });
    fontSelector.add(fontSizes);
    fBox.add(fontSelector);

    JPanel styleSelector = new JPanel();
    styleSelector.setLayout(new BoxLayout(styleSelector, BoxLayout.X_AXIS));
    bold = new JCheckBox(resources.getString("bold"));
    bold.setFocusPainted(false);
    bold.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        boldActionPerformed(evt);
      }
    });
    italic = new JCheckBox(resources.getString("italic"));
    italic.setFocusPainted(false);
    italic.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        italicActionPerformed(evt);
      }
    });
    styleSelector.add(Box.createHorizontalGlue());
    styleSelector.add(bold);
    styleSelector.add(Box.createHorizontalGlue());
    styleSelector.add(italic);
    styleSelector.add(Box.createHorizontalGlue());
    fBox.add(styleSelector);

    props.add(fBox);

    JPanel fgBox = new JPanel();
    fgBox.setBorder(new TitledBorder(resources.getString("fgColor")));
    fgBox.setLayout(new BorderLayout());
    fgColor = new JButton();
    fgColor.setFocusPainted(false);
    fgColor.setPreferredSize(new Dimension(32, 32));
    fgColor.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt)
      {
        fgColorActionPerformed(evt);
      }
    });
    fgBox.add(fgColor, BorderLayout.CENTER);

    props.add(fgBox);

    JPanel bgBox = new JPanel();
    bgBox.setBorder(new TitledBorder(resources.getString("bgColor")));
    bgBox.setLayout(new BorderLayout());
    bgColor = new JButton();
    bgColor.setFocusPainted(false);
    bgColor.setPreferredSize(new Dimension(32, 32));
    bgColor.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt)
      {
        bgColorActionPerformed(evt);
      }
    });
    bgBox.add(bgColor, BorderLayout.CENTER);

    props.add(bgBox);

    JPanel exBox = new JPanel();
    exBox.setBorder(new TitledBorder(resources.getString("sample")));
    exBox.setLayout(new BorderLayout());
    sample = new JLabel(resources.getString("abcd"), JLabel.CENTER);
    sample.setOpaque(true);
    exBox.add(sample, BorderLayout.CENTER);

    props.add(exBox);

    add(props);

    updateDisplay();
  }

  /**
   * Update's the customizer's display according to the currently selected
   * options.
   */
  private void updateDisplay()
  {
    ignoreEvents = true;

    String coloring = getColoring();
    FontAndColor fac = FontOptions.getFontAndColor(kitClass, coloring);

    Font font = fac.font;

    String family = font.getFamily();
    int nFonts = fontNames.size();
    int selectedFamily = -1;
    for (int i=0; i<nFonts; i++) {
      String f = fontNames.get(i);
      if (f.equalsIgnoreCase(family)) {
        selectedFamily = i;
        break;
      }
    }
    if (selectedFamily >= 0) {
      fonts.setSelectedIndex(selectedFamily);
    }

    fontSizes.setSelectedItem(new Integer(fac.font.getSize()));

    int style = font.getStyle();
    bold.setSelected((style & Font.BOLD) != 0);
    italic.setSelected((style & Font.ITALIC) != 0);

    Color fg = fac.fore;
    fgColor.setBackground(fg);

    Color bg = fac.back;
    bgColor.setBackground(bg);

    sample.setFont(font);
    sample.setForeground(fg);
    sample.setBackground(bg);

    ignoreEvents = false;
  }

  /**
   * Invoked when a new coloring is selected by the user.
   * @param     evt     The event generated by the selection.
   */
  private void listListSelectionListener(ListSelectionEvent evt)
  {
    updateDisplay();
  }

  /**
   * Invoked when a new font is selected by the user.
   * @param     evt     The event generated by the selection.
   */
  private void fontsItemStateChanged(ItemEvent evt)
  {
    if (!ignoreEvents) {
      String family = (String)(fonts.getSelectedItem());
      String coloring = getColoring();
      int style = 0;
      if (bold.isSelected()) {
        style |= Font.BOLD;
      }
      if (italic.isSelected()) {
        style |= Font.ITALIC;
      }
      if (style == 0) {
        style = Font.PLAIN;
      }
      int size = getFontSize();
      Font font = new Font(family, style, size);
      FontOptions.setFont(kitClass, coloring, font);

      updateDisplay();
    }
  }

  /**
   * Invoked when a new font size is selected by the user.
   * @param     evt     The event generated by the selection.
   */
  private void fontSizesItemStateChanged(ItemEvent evt)
  {
    if (!ignoreEvents) {
      int size = getFontSize();
      String coloring = getColoring();
      Font font = FontOptions.getFont(kitClass, coloring);
      font = font.deriveFont((float)size);
      FontOptions.setFont(kitClass, coloring, font);

      updateDisplay();
    }
  }

  /**
   * Invoked when a new foreground color is selected by the user.
   * @param     evt     The event generated by the selection.
   */
  private void fgColorActionPerformed(ActionEvent evt)
  {
    if (!ignoreEvents) {
      Color fg = fgColor.getBackground();
      fg = JColorChooser.showDialog(this, resources.getString("fgColor"), fg);
      String coloring = getColoring();
      FontOptions.setForeColor(kitClass, coloring, fg);
      fgColor.setBackground(fg);
      updateDisplay();
    }
  }

  /**
   * Invoked when a new background color is selected by the user.
   * @param     evt     The event generated by the selection.
   */
  private void bgColorActionPerformed(ActionEvent evt)
  {
    if (!ignoreEvents) {
      Color bg = bgColor.getBackground();
      bg = JColorChooser.showDialog(this, resources.getString("bgColor"), bg);
      String coloring = getColoring();
      FontOptions.setBackColor(kitClass, coloring, bg);
      bgColor.setBackground(bg);
      updateDisplay();
    }
  }

  /**
   * Invoked when the "bold" button is pressed by the user.
   * @param     evt     The event generated by the press.
   */
  private void boldActionPerformed(ActionEvent evt)
  {
    if (!ignoreEvents) {
      String coloring = getColoring();
      FontAndColor fac = FontOptions.getFontAndColor(kitClass, coloring);
      Font font = fac.font;
      int style = font.getStyle();
      System.out.print(style+" ");
      if (bold.isSelected()) {
        style |= Font.BOLD;
      }else{
        style &= (~Font.BOLD);
      }
      font = font.deriveFont(style);
      System.out.print(font.getStyle()+" ");
      FontOptions.setFont(kitClass, coloring, font);
      fac = FontOptions.getFontAndColor(kitClass, coloring);

      updateDisplay();
    }
  }

  /**
   * Invoked when the "italic" button is pressed by the user.
   * @param     evt     The event generated by the press.
   */
  private void italicActionPerformed(ActionEvent evt)
  {
    if (!ignoreEvents) {
      String coloring = getColoring();
      FontAndColor fac = FontOptions.getFontAndColor(kitClass, coloring);
      Font font = fac.font;
      int style = font.getStyle();
      if (italic.isSelected()) {
        style |= Font.ITALIC;
      }else{
        style &= (~Font.ITALIC);
      }
      font = font.deriveFont(style);
      FontOptions.setFont(kitClass, coloring, font);

      updateDisplay();
    }
  }

  /**
   * Returns the font size that is currently selected in the font size combo
   * box.
   * @return    The requested font size.
   */
  private int getFontSize()
  {
    return Integer.parseInt(fontSizes.getSelectedItem().toString());
  }

  /**
   * Returns the name of the currently selected coloring.
   * @return    The requeste name.
   */
  private String getColoring()
  {
    int fontNum = list.getSelectedIndex();
    return cNames[fontNum];
  }

}
