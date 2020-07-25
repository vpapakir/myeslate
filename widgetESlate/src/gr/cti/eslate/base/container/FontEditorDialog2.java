package gr.cti.eslate.base.container;

import gr.cti.eslate.jeditlist.JEditList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MutableAttributeSet;


class FontEditorDialog2 extends JDialog {
    protected int m_option = JOptionPane.CLOSED_OPTION;
    protected OpenList m_lstFontName;
    protected OpenList m_lstFontSize;
    protected MutableAttributeSet m_attributes;
    protected JCheckBox m_chkBold;
    protected JCheckBox m_chkItalic;
  //GT  protected JCheckBox m_chkUnderline;

  //GT  protected JCheckBox m_chkStrikethrough;
  //GT  protected JCheckBox m_chkSubscript;
  //GT  protected JCheckBox m_chkSuperscript;

  //GT  protected JComboBox m_cbColor;
    protected JLabel m_preview;
    Font currentFont = null;
    ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.FontEditorDialog2Bundle", Locale.getDefault());
    FontPropertyEditor editor = null;
    JButton cancelButton, okButton;
    boolean isInitializing = false, isInitialized = false;

    public FontEditorDialog2(java.awt.Frame parent, String[] names, String[] sizes, Font font, FontPropertyEditor ed) {
        super(parent, true);
System.out.println("parent: " + parent);
        setTitle(bundle.getString("ChooseFont"));
        currentFont = font;
        editor = ed;

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel p = new JPanel(new GridLayout(1, 2, 10, 2));
        p.setBorder(new TitledBorder(new EtchedBorder(), bundle.getString("Font")));
        m_lstFontName = new OpenList(names, bundle.getString("Name:"));
        p.add(m_lstFontName);

        m_lstFontSize = new OpenList(sizes, bundle.getString("Size:"));
        p.add(m_lstFontSize);
        getContentPane().add(p);

//        p = new JPanel(new GridLayout(1, 3, 10, 5));
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(0,0,5,0)), bundle.getString("Effects")));
        p.add(Box.createHorizontalStrut(5));
        m_chkBold = new JCheckBox(bundle.getString("Bold"));
        p.add(m_chkBold);
        p.add(Box.createHorizontalStrut(13));
        m_chkItalic = new JCheckBox(bundle.getString("Italic"));
        p.add(m_chkItalic);
        p.add(Box.createGlue());
    /*    m_chkUnderline = new JCheckBox("Underline");
        p.add(m_chkUnderline);
        m_chkStrikethrough = new JCheckBox("Strikeout");
        p.add(m_chkStrikethrough);
        m_chkSubscript = new JCheckBox("Subscript");
        p.add(m_chkSubscript);
        m_chkSuperscript = new JCheckBox("Superscript");
        p.add(m_chkSuperscript);
    */
        getContentPane().add(p);

    /*    getContentPane().add(Box.createVerticalStrut(5));
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalStrut(10));
        p.add(new JLabel("Color:"));
        p.add(Box.createHorizontalStrut(20));
        m_cbColor = new JComboBox();

        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r=0; r<values.length; r++) {
          for (int g=0; g<values.length; g++) {
            for (int b=0; b<values.length; b++) {
              Color c = new Color(values[r], values[g], values[b]);
              m_cbColor.addItem(c);
            }
          }
        }

        m_cbColor.setRenderer(new ColorComboRenderer());
        p.add(m_cbColor);
    */
        p.add(Box.createHorizontalStrut(10));
        getContentPane().add(p);

        p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new EtchedBorder(), bundle.getString("Preview")));
        m_preview = new JLabel(bundle.getString("PreviewFont"), JLabel.CENTER);
        m_preview.setBackground(Color.white);
        m_preview.setForeground(Color.black);
        m_preview.setOpaque(true);
        m_preview.setBorder(new LineBorder(Color.black));
        m_preview.setPreferredSize(new Dimension(120, 40));
        p.add(m_preview, BorderLayout.CENTER);
        getContentPane().add(p);

        p = new JPanel(new FlowLayout());
        JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
        okButton = new JButton(bundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        cancelButton = new JButton(bundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(bundle.getString("Apply"));
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

        p1.add(okButton);
        p1.add(cancelButton);
        p1.add(applyButton);
        p.add(p1);
        getContentPane().add(Box.createVerticalStrut(5));
        getContentPane().add(p);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_option = JOptionPane.OK_OPTION;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            m_option = JOptionPane.CANCEL_OPTION;
            setVisible(false);
          }
        });

        applyButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              editor.updateFont(currentFont);
          }
        });

        pack();
        setResizable(false);

        ListSelectionListener listListener = new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
              if (e.getValueIsAdjusting() || isInitializing) return;
              updatePreview();
          }
        };
        m_lstFontName.addListSelectionListener(listListener);
        m_lstFontSize.addListSelectionListener(listListener);

        ActionListener lst = new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              updatePreview();
          }
        };
        m_chkBold.addActionListener(lst);
        m_chkItalic.addActionListener(lst);

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                isInitializing = true;
                if (!isInitialized && currentFont != null) {
                    /* Initialize the dialog with the currentFont settings, when it shows.
                     */
                    m_chkBold.setSelected(currentFont.isBold());
                    m_chkItalic.setSelected(currentFont.isItalic());
                    m_lstFontName.setSelValueIgnoreCase(new String(currentFont.getFamily()));
                    m_lstFontSize.setSelValue(new Integer(currentFont.getSize()).toString());
                    updatePreview();
                    m_lstFontName.m_list.requestFocus();
                    isInitialized = true;
                }
                isInitializing = false;
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        getRootPane().setDefaultButton(okButton);
    //GT    m_cbColor.addActionListener(lst);
    }

  /* GT
      public void setAttributes(AttributeSet a) {
      m_attributes = new SimpleAttributeSet(a);
      String name = StyleConstants.getFontFamily(a);
      m_lstFontName.setSelected(name);
      int size = StyleConstants.getFontSize(a);
      m_lstFontSize.setSelectedInt(size);
      m_chkBold.setSelected(StyleConstants.isBold(a));
      m_chkItalic.setSelected(StyleConstants.isItalic(a));
      m_chkUnderline.setSelected(StyleConstants.isUnderline(a));
      m_chkStrikethrough.setSelected(
        StyleConstants.isStrikeThrough(a));
      m_chkSubscript.setSelected(StyleConstants.isSubscript(a));
      m_chkSuperscript.setSelected(StyleConstants.isSuperscript(a));
      m_cbColor.setSelectedItem(StyleConstants.getForeground(a));
      updatePreview();
    }

    public AttributeSet getAttributes() {
      if (m_attributes == null)
        return null;
      StyleConstants.setFontFamily(m_attributes,
        m_lstFontName.getSelected());
      StyleConstants.setFontSize(m_attributes,
        m_lstFontSize.getSelectedInt());
      StyleConstants.setBold(m_attributes,
        m_chkBold.isSelected());
      StyleConstants.setItalic(m_attributes,
        m_chkItalic.isSelected());
      StyleConstants.setUnderline(m_attributes,
        m_chkUnderline.isSelected());
      StyleConstants.setStrikeThrough(m_attributes,
        m_chkStrikethrough.isSelected());
      StyleConstants.setSubscript(m_attributes,
        m_chkSubscript.isSelected());
      StyleConstants.setSuperscript(m_attributes,
        m_chkSuperscript.isSelected());
      StyleConstants.setForeground(m_attributes,
        (Color)m_cbColor.getSelectedItem());
      return m_attributes;
    }
  */
    public int getOption() { return m_option; }

    protected void updatePreview() {
        String name = m_lstFontName.getSelValue();
        int size = m_lstFontSize.getSelInt();
        if (size <= 0)
          return;
        int style = Font.PLAIN;
        if (m_chkBold.isSelected())
          style |= Font.BOLD;
        if (m_chkItalic.isSelected())
          style |= Font.ITALIC;

        // Bug Alert! This doesn't work if only style is changed.
        Font fn = new Font(name, style, size);
        m_preview.setFont(fn);

    //GT    Color c = (Color)m_cbColor.getSelectedItem();
    //GT    m_preview.setForeground(c);
        m_preview.repaint();
        currentFont = fn;
    }

    public Font getFont() {
        return currentFont;
    }

}

class OpenList extends JPanel implements ListSelectionListener, ActionListener {
    protected JLabel m_title;
    protected JTextField m_text;
    protected JEditList m_list;
    protected JScrollPane m_scroll;
    java.util.Vector listSelectionListeners = new java.util.Vector();

    public OpenList(String[] data, String title) {
        setLayout(null);
        m_title = new JLabel(title, JLabel.LEFT);
        add(m_title);
        m_text = new JTextField();
        m_text.addActionListener(this);
        add(m_text);
        m_list = new JEditList();
        m_list.setListData(data);
        m_list.setUndoAvailable(false);
        m_list.setCutAvailable(false);
        m_list.setCopyAvailable(false);
        m_list.setPasteAvailable(false);
        m_list.setSelectAllAvailable(false);
        m_list.setAppendAvailable(false);
        m_list.setDeleteAvailable(false);

        m_list.setVisibleRowCount(4);
        m_list.addListSelectionListener(this);
        m_scroll = new JScrollPane(m_list);
        add(m_scroll);
    }

    public OpenList(String title, int numCols) {
        setLayout(null);
        m_title = new JLabel(title, JLabel.LEFT);
        add(m_title);
        m_text = new JTextField(numCols);
        m_text.addActionListener(this);
        add(m_text);
        m_list = new JEditList();
        m_list.setVisibleRowCount(4);
        m_list.addListSelectionListener(this);
        m_scroll = new JScrollPane(m_list);
        add(m_scroll);
    }

  /*  public void appendResultSet(ResultSet results, int index,
     boolean toTitleCase)
    {
      m_text.setText("");
      DefaultListModel model = new DefaultListModel();
      try {
        while (results.next()) {
          String str = results.getString(index);
          if (toTitleCase)
            str = Utils.titleCase(str);
          model.addElement(str);
        }
      }
      catch (SQLException ex) {
        System.err.println("appendResultSet: "+ex.toString());
      }
      m_list.setModel(model);
      if (model.getSize() > 0)
        m_list.setSelectedIndex(0);
    }
  */
    public void setSelValue(String sel) {
        m_list.setSelectedValue(sel, true);
        m_text.setText(sel);
    }

    public void setSelValueIgnoreCase(String value) {
        ListModel model = m_list.getModel();
        String valuelc = value.toLowerCase();
        String realValue = null;
        int index = -1;
        for (int i=0; i<model.getSize(); i++) {
            if (((String) model.getElementAt(i)).toLowerCase().equals(valuelc)) {
                realValue = (String) model.getElementAt(i);
                index = i;
                break;
            }
        }
//        System.out.println("index: " + index);
        if (index != -1) {
            m_list.setSelectedIndex(index);
            m_list.ensureIndexIsVisible(index);
            m_text.setText(realValue);
        }
    }

    public String getSelValue() { return m_text.getText(); }

    public void setSelInt(int value) {
        setSelValue(Integer.toString(value));
    }

    public int getSelInt() {
        try {
          return Integer.parseInt(getSelValue());
        }
        catch (NumberFormatException ex) { return -1; }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        Object obj = m_list.getSelectedValue();
        if (obj != null)
          m_text.setText(obj.toString());
        for (int i=0; i<listSelectionListeners.size(); i++)
            ((ListSelectionListener) listSelectionListeners.elementAt(i)).valueChanged(e);
    }

    public void actionPerformed(ActionEvent e) {
        ListModel model = m_list.getModel();
        String key = m_text.getText().toLowerCase();
        for (int k=0; k<model.getSize(); k++) {
          String data = (String)model.getElementAt(k);
          if (data.toLowerCase().startsWith(key)) {
            m_list.setSelectedValue(data, true);
            break;
          }
        }
    }

    public void addListSelectionListener(ListSelectionListener lst) {
        listSelectionListeners.addElement(lst);
    }

    public void removedListSelectionListener(ListSelectionListener lst) {
        listSelectionListeners.removeElement(lst);
    }

    public Dimension getPreferredSize() {
        Insets ins = getInsets();
        Dimension d1 = m_title.getPreferredSize();
        Dimension d2 = m_text.getPreferredSize();
        Dimension d3 = m_scroll.getPreferredSize();
        int w = Math.max(Math.max(d1.width, d2.width), d3.width);
        int h = d1.height + d2.height + d3.height;
        return new Dimension(w+ins.left+ins.right,
          h+ins.top+ins.bottom);
    }

    public Dimension getMaximumSize() {
        Insets ins = getInsets();
        Dimension d1 = m_title.getMaximumSize();
        Dimension d2 = m_text.getMaximumSize();
        Dimension d3 = m_scroll.getMaximumSize();
        int w = Math.max(Math.max(d1.width, d2.width), d3.width);
        int h = d1.height + d2.height + d3.height;
        return new Dimension(w+ins.left+ins.right,
          h+ins.top+ins.bottom);
    }

    public Dimension getMinimumSize() {
        Insets ins = getInsets();
        Dimension d1 = m_title.getMinimumSize();
        Dimension d2 = m_text.getMinimumSize();
        Dimension d3 = m_scroll.getMinimumSize();
        int w = Math.max(Math.max(d1.width, d2.width), d3.width);
        int h = d1.height + d2.height + d3.height;
        return new Dimension(w+ins.left+ins.right,
          h+ins.top+ins.bottom);
    }

    public void doLayout() {
        Insets ins = getInsets();
        Dimension d = getSize();
        int x = ins.left;
        int y = ins.top;
        int w = d.width-ins.left-ins.right;
        int h = d.height-ins.top-ins.bottom;

        Dimension d1 = m_title.getPreferredSize();
        m_title.setBounds(x, y, w, d1.height);
        y += d1.height;
        Dimension d2 = m_text.getPreferredSize();
        m_text.setBounds(x, y, w, d2.height);
        y += d2.height;
        m_scroll.setBounds(x, y, w, h-y);
    }
}

/*
class ColorComboRenderer extends JPanel implements ListCellRenderer {
    protected Color m_color = Color.black;
    protected Color m_focusColor =
      (Color) UIManager.get("List.selectionBackground");
    protected Color m_nonFocusColor = Color.white;

    public Component getListCellRendererComponent(JList list, Object obj, int row, boolean sel, boolean hasFocus) {
        if (hasFocus || sel)
          setBorder(new CompoundBorder(
            new MatteBorder(2, 10, 2, 10, m_focusColor),
            new LineBorder(Color.black)));
        else
          setBorder(new CompoundBorder(
            new MatteBorder(2, 10, 2, 10, m_nonFocusColor),
            new LineBorder(Color.black)));

        if (obj instanceof Color)
          m_color = (Color) obj;
        return this;
    }

    public void paintComponent(Graphics g) {
        setBackground(m_color);
        super.paintComponent(g);
    }
}
*/
