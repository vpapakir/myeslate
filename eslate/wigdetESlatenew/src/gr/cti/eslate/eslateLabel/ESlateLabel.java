package gr.cti.eslate.eslateLabel;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MaleSingleIFMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.NoSuchPlugException;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.protocol.ActorInterface;
import gr.cti.eslate.protocol.ActorNameEvent;
import gr.cti.eslate.protocol.ActorNameListener;
import gr.cti.eslate.protocol.AnimatedPropertyDescriptor;
import gr.cti.eslate.protocol.AnimatedPropertyStructure;
import gr.cti.eslate.protocol.AnimationSession;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.sharedObject.View;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;

public class ESlateLabel extends JLabel implements ESlatePart, Externalizable,
        ActorInterface {

    private ESlateHandle handle;

    private static final int FORMAT_VERSION = 1;

    private StringSO stringSO;

    static final long serialVersionUID = 0xad4134855712b7e9L;

    SharedObjectPlug plug;

    private boolean plugsUsed;

    private ResourceBundle bundleMessages;

    private static final String version = "2.0.7";

    private AttributedString attributedString;

    private static Rectangle paintViewR = new Rectangle();

    private static Rectangle paintIconR = new Rectangle();

    private static Rectangle paintTextR = new Rectangle();

    int preferredHeight;

    Graphics2D gg;

    boolean multilineMode;

    PerformanceTimer loadTimer;

    PerformanceTimer saveTimer;

    PerformanceTimer constructorTimer;

    PerformanceTimer initESlateAspectTimer;

    PerformanceListener perfListener;

    private int plugCount = 1;

    // Number of animated variables.
    private int varCount = 4;

    // Array of component variables values to export through actor plug.
    private IntBaseArray varValues;

    // Structure to hold the property IDs and names of the variables.
    private AnimatedPropertyStructure animatedPropertyStructure;

    // Actor's name listeners.
    private ArrayList actorNameListeners = new ArrayList();

    private MaleSingleIFMultipleConnectionProtocolPlug animationPlug;

    private ESlateInfo getInfo() {
        String info[] = { bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                bundleMessages.getString("copyright") };
        return new ESlateInfo(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(bundleMessages
                        .getString("componentName"))))).append(" ").append(
                bundleMessages.getString("version")).append(" ")
                .append("1.1.6"))), info);
    }

    public ESlateLabel() {
        preferredHeight = 20;
        multilineMode = false;
        perfListener = null;
        bundleMessages = ResourceBundle
                .getBundle("gr.cti.eslate.eslateLabel.BundleMessages", Locale
                        .getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(false);
        setPreferredSize(new Dimension(100, 60));
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
        setText(bundleMessages.getString("E-Slate"));
        getESlateHandle();
        setPlugsUsed(true);
        setPreferredSize(new Dimension(150,100));
    }

    public void updatePreferredHeight(int forcedWidth, Graphics g) {
        preferredHeight = getNumberOfRows(g, forcedWidth)
                *(getFontMetrics(getFont()).getHeight()+2);
    }

    public void updatePreferredHeight(Graphics g) {
        updatePreferredHeight(getWidth(), g);
    }

    public void setText(String s) {
        if (getText()==s)
            return;
        if (isEnabled()) {
            super.setText(s);
            if (stringSO!=null)
                stringSO.setString(s);
            if (multilineMode)
                if (getText().length()>0) {
                    attributedString = new AttributedString(s);
                    attributedString
                            .addAttribute(TextAttribute.FONT, getFont());
                    attributedString.addAttribute(TextAttribute.FOREGROUND,
                            getForeground());
                    preferredHeight = getNumberOfRows(gg, getWidth())
                            *(getFontMetrics(getFont()).getHeight()+2);
                } else {
                    return;
                }
        }
        repaint();
    }

    public void setFont(Font f) {
        super.setFont(f);
        if (! multilineMode)
            return;
        if (getText().length()>0) {
            attributedString = new AttributedString(getText());
            attributedString.addAttribute(TextAttribute.FONT, getFont());
            attributedString.addAttribute(TextAttribute.FOREGROUND,
                    getForeground());
            preferredHeight = getNumberOfRows(gg, getWidth())
                    *(getFontMetrics(getFont()).getHeight()+2);
            repaint();
        }
    }

    public void setForeground(Color c) {
        super.setForeground(c);
        if (! multilineMode)
            return;
        if (getText().length()>0) {
            attributedString = new AttributedString(getText());
            attributedString.addAttribute(TextAttribute.FONT, getFont());
            attributedString.addAttribute(TextAttribute.FOREGROUND, c);
            preferredHeight = getNumberOfRows(gg, getWidth())
                    *(getFontMetrics(getFont()).getHeight()+2);
            repaint();
        }
    }

    public void setMultilineMode(boolean b) {
        if (multilineMode==b)
            return;
        multilineMode = b;
        if (multilineMode&&getText().length()>0) {
            attributedString = new AttributedString(getText());
            attributedString.addAttribute(TextAttribute.FONT, getFont());
            attributedString.addAttribute(TextAttribute.FOREGROUND,
                    getForeground());
            preferredHeight = getNumberOfRows(gg, getWidth())
                    *(getFontMetrics(getFont()).getHeight()+2);
        }
        repaint();
        revalidate();
    }

    public boolean isMultilineMode() {
        return multilineMode;
    }

    public Dimension getPreferredSize() {
        if (multilineMode)
            return new Dimension(super.getPreferredSize().width,
                    preferredHeight);
        return super.getPreferredSize();
    }

    public Dimension getMaximumSize() {
        if (multilineMode)
            return new Dimension(1000, 1000);
        return super.getMaximumSize();
    }

    int getNumberOfRows(Graphics g, int forcedWidth) {
        if (! multilineMode)
            return 1;
        Insets insets = getInsets();
        Graphics2D g2 = (Graphics2D) g;
        if (g2==null)
            return 1;
        FontMetrics fm = g2.getFontMetrics();
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = forcedWidth-(insets.left+insets.right);
        if (getHeight()==0)
            paintViewR.height = getPreferredSize().height
                    -(insets.top+insets.bottom);
        else
            paintViewR.height = getHeight()-(insets.top+insets.bottom);
        Icon icon = isEnabled() ? getIcon() : getDisabledIcon();
        if (attributedString!=null) {
            int width;
            if (icon!=null) {
                boolean textIsEmpty = getText()==null||getText().equals("");
                int gap = ! textIsEmpty&&icon!=null ? getIconTextGap() : 0;
                width = paintViewR.width-(icon.getIconWidth()+gap);
            } else {
                width = paintViewR.width;
            }
            AttributedCharacterIterator characterIterator = attributedString
                    .getIterator();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            LineBreakMeasurer measurer = new LineBreakMeasurer(
                    characterIterator, fontRenderContext);
            int numberOfRows;
            for (numberOfRows = 0; measurer.getPosition()<characterIterator
                    .getEndIndex(); numberOfRows++) {
                TextLayout textLayout = measurer.nextLayout(width);
            }

            return numberOfRows;   
        }
        return 1;
        
    }

    public int getVisibleLineCount() {
        int lineNum = 0;
        if (! multilineMode)
            return 1;
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        gg = g2;
        Insets insets = getInsets();
        FontMetrics fm = getFontMetrics(getFont());
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = getWidth()-(insets.left+insets.right);
        paintViewR.height = getHeight()-(insets.top+insets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        Icon icon = isEnabled() ? getIcon() : getDisabledIcon();
        String clippedText = layoutCL(this, fm, getText(), icon, paintViewR,
                paintIconR, paintTextR, getWidth());
        if (attributedString!=null) {
            int width = paintTextR.width;
            int x = paintTextR.x;
            int y = paintTextR.y;
            if (y<insets.top)
                y = insets.top;
            AttributedCharacterIterator characterIterator = attributedString
                    .getIterator();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            LineBreakMeasurer measurer = new LineBreakMeasurer(
                    characterIterator, fontRenderContext);
            g2.setColor(getForeground());
            while (measurer.getPosition()<characterIterator.getEndIndex()) {
                int lineStart = measurer.getPosition();
                TextLayout textLayout = measurer.nextLayout(width);
                y = (int) (y+textLayout.getAscent());
                if ( y+textLayout.getDescent()+textLayout.getLeading()
                        +textLayout.getAscent()>=(getSize().height-insets.bottom)) {
                    String s = getText();
                    s = s.substring(lineStart, lineStart
                            +textLayout.getCharacterCount());
                    if (lineStart+textLayout.getCharacterCount()<characterIterator
                            .getEndIndex())
                        s = s.concat("...");
                    TextLayout tL = new TextLayout(s, g2.getFont(),
                            fontRenderContext);
                    return ++lineNum;
                }
                lineNum++;
                y = (int) ( y+(textLayout.getDescent()+textLayout
                        .getLeading()));
            }
        }
        return lineNum;
    }

    // Who is using this?

    public int getLineCount(int forcedWidth, Graphics g) {
        int lineNum = 0;
        if (! multilineMode)
            return 1;
        Graphics2D g2 = (Graphics2D) g;
        gg = g2;
        Insets insets = getInsets();
        FontMetrics fm = getFontMetrics(getFont());
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = forcedWidth-(insets.left+insets.right);
        paintViewR.height = 0x7fffffff-(insets.top+insets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        Icon icon = isEnabled() ? getIcon() : getDisabledIcon();
        String clippedText = layoutCL(this, fm, getText(), icon, paintViewR,
                paintIconR, paintTextR, forcedWidth);
        if (attributedString!=null) {
            int width = paintTextR.width;
            int x = paintTextR.x;
            int y = paintTextR.y;
            if (y<insets.top)
                y = insets.top;
            AttributedCharacterIterator characterIterator = attributedString
                    .getIterator();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            LineBreakMeasurer measurer = new LineBreakMeasurer(
                    characterIterator, fontRenderContext);
            g2.setColor(getForeground());
            while (measurer.getPosition()<characterIterator.getEndIndex()) {
                int lineStart = measurer.getPosition();
                TextLayout textLayout = measurer.nextLayout(width);
                y = (int) ( y+textLayout.getAscent());
                if ( y+textLayout.getDescent()+textLayout.getLeading()
                        +textLayout.getAscent()>=(0x7fffffff-insets.bottom)) {
                    String s = getText();
                    s = s.substring(lineStart, lineStart
                            +textLayout.getCharacterCount());
                    if (lineStart+textLayout.getCharacterCount()<characterIterator
                            .getEndIndex())
                        s = s.concat("...");
                    TextLayout tL = new TextLayout(s, g2.getFont(),
                            fontRenderContext);
                    return ++lineNum;
                }
                lineNum++;
                y = (int) (y+(textLayout.getDescent()+textLayout.getLeading()));
            }
        }
        return lineNum;
    }

    public void paintComponent(Graphics g) {
        if (! multilineMode||getText().length()==0) {
            super.paintComponent(g);
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        gg = g2;
        Insets insets = getInsets();
        FontMetrics fm = g.getFontMetrics();
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = getWidth()-(insets.left+insets.right);
        paintViewR.height = getHeight()-(insets.top+insets.bottom);
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRect(paintViewR.x, paintViewR.y, paintViewR.width,
                    paintViewR.height);
        }
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        Icon icon = isEnabled() ? getIcon() : getDisabledIcon();
        String clippedText = layoutCL(this, fm, getText(), icon, paintViewR,
                paintIconR, paintTextR, getWidth());
        if (icon!=null)
            icon.paintIcon(this, g2, paintIconR.x, paintIconR.y);
        if (attributedString!=null) {
            int width = paintTextR.width;
            int x = paintTextR.x;
            int y = paintTextR.y;
            if (y<insets.top)
                y = insets.top;
            AttributedCharacterIterator characterIterator = attributedString
                    .getIterator();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            LineBreakMeasurer measurer = new LineBreakMeasurer(
                    characterIterator, fontRenderContext);
            g2.setColor(getForeground());
            while (measurer.getPosition()<characterIterator.getEndIndex()) {
                int lineStart = measurer.getPosition();
                TextLayout textLayout = measurer.nextLayout(width);
                y = (int) (y+textLayout.getAscent());
                if (y+textLayout.getDescent()+textLayout.getLeading()
                        +textLayout.getAscent()>=(getSize().height-insets.bottom)) {
                    String s = getText();
                    if (s==null||s.equals(""))
                        return;
                    s = s.substring(lineStart, lineStart
                            +textLayout.getCharacterCount());
                    if (lineStart+textLayout.getCharacterCount()<characterIterator
                            .getEndIndex())
                        s = s.concat("...");
                    TextLayout tL = new TextLayout(s, g2.getFont(),
                            fontRenderContext);
                    tL.draw(g2, x, y);
                    return;
                }
                textLayout.draw(g2, x, y);
                y = (int) (y+(textLayout.getDescent()+textLayout.getLeading()));
            }
        }
    }

    protected String layoutCL(JLabel label, FontMetrics fontMetrics,
            String text, Icon icon, Rectangle viewR, Rectangle iconR,
            Rectangle textR, int customWidth) {
        return layoutCompoundLabel(label, fontMetrics, text, icon, label
                .getVerticalAlignment(), label.getHorizontalAlignment(), label
                .getVerticalTextPosition(), label.getHorizontalTextPosition(),
                viewR, iconR, textR, label.getIconTextGap(), customWidth);
    }

    public String layoutCompoundLabel(JComponent c, FontMetrics fm,
            String text, Icon icon, int verticalAlignment,
            int horizontalAlignment, int verticalTextPosition,
            int horizontalTextPosition, Rectangle viewR, Rectangle iconR,
            Rectangle textR, int textIconGap, int customWidth) {
        boolean orientationIsLeftToRight = true;
        int hAlign = horizontalAlignment;
        int hTextPos = horizontalTextPosition;
        if (c!=null&&! c.getComponentOrientation().isLeftToRight())
            orientationIsLeftToRight = false;
        switch (horizontalAlignment) {
        case 10: // '\n'
            hAlign = orientationIsLeftToRight ? 2 : 4;
            break;

        case 11: // '\013'
            hAlign = orientationIsLeftToRight ? 4 : 2;
            break;
        }
        switch (horizontalTextPosition) {
        case 10: // '\n'
            hTextPos = orientationIsLeftToRight ? 2 : 4;
            break;

        case 11: // '\013'
            hTextPos = orientationIsLeftToRight ? 4 : 2;
            break;
        }
        return layoutCompoundLabelImpl(c, fm, text, icon, verticalAlignment,
                hAlign, verticalTextPosition, hTextPos, viewR, iconR, textR,
                textIconGap, customWidth);
    }

    private String layoutCompoundLabelImpl(JComponent c, FontMetrics fm,
            String text, Icon icon, int verticalAlignment,
            int horizontalAlignment, int verticalTextPosition,
            int horizontalTextPosition, Rectangle viewR, Rectangle iconR,
            Rectangle textR, int textIconGap, int customWidth) {
        if (icon!=null) {
            iconR.width = icon.getIconWidth();
            iconR.height = icon.getIconHeight();
        } else {
            iconR.width = iconR.height = 0;
        }
        boolean textIsEmpty = text==null||text.equals("");
        View v = null;
        if (textIsEmpty) {
            textR.width = textR.height = 0;
            text = "";
        } else {
            textR.width = computeStringWidth(fm, text);
            textR.height = fm.getHeight()*getNumberOfRows(gg, customWidth);
        }
        int gap = ! textIsEmpty&&icon!=null ? textIconGap : 0;
        if (! textIsEmpty) {
            int availTextWidth;
            if (horizontalTextPosition==0)
                availTextWidth = viewR.width;
            else
                availTextWidth = viewR.width-(iconR.width+gap);
            if (textR.width>availTextWidth) {
                String clipString = "...";
                int totalWidth = computeStringWidth(fm, clipString);
                int nChars = 0;
                do {
                    if (nChars>=text.length())
                        break;
                    totalWidth += fm.charWidth(text.charAt(nChars));
                    if (totalWidth>availTextWidth)
                        break;
                    nChars++;
                } while (true);
                text = String.valueOf(text.substring(0, nChars))
                        +String.valueOf(clipString);
                textR.width = computeStringWidth(fm, text);
            }
        }
        if (verticalTextPosition==1) {
            if (horizontalTextPosition!=0)
                textR.y = 0;
            else
                textR.y = - (textR.height+gap);
        } else if (verticalTextPosition==0)
            textR.y = iconR.height/2-textR.height/2;
        else if (horizontalTextPosition!=0)
            textR.y = iconR.height-textR.height;
        else
            textR.y = iconR.height+gap;
        if (horizontalTextPosition==2)
            textR.x = - (textR.width+gap);
        else if (horizontalTextPosition==0)
            textR.x = iconR.width/2-textR.width/2;
        else
            textR.x = iconR.width+gap;
        int labelR_x = Math.min(iconR.x, textR.x);
        int labelR_width = Math.max(iconR.x+iconR.width, textR.x+textR.width)
                -labelR_x;
        int labelR_y = Math.min(iconR.y, textR.y);
        int labelR_height = Math
                .max(iconR.y+iconR.height, textR.y+textR.height)
                -labelR_y;
        int dy;
        if (verticalAlignment==1)
            dy = viewR.y-labelR_y;
        else if (verticalAlignment==0)
            dy = (viewR.y+viewR.height/2)-(labelR_y+labelR_height/2);
        else
            dy = (viewR.y+viewR.height)-(labelR_y+labelR_height);
        int dx;
        if (horizontalAlignment==2)
            dx = viewR.x-labelR_x;
        else if (horizontalAlignment==4)
            dx = (viewR.x+viewR.width)-(labelR_x+labelR_width);
        else
            dx = (viewR.x+viewR.width/2)-(labelR_x+labelR_width/2);
        textR.x += dx;
        textR.y += dy;
        iconR.x += dx;
        iconR.y += dy;
        return text;
    }

    public static int computeStringWidth(FontMetrics fm, String str) {
        return fm.stringWidth(str);
    }

    public ESlateHandle getESlateHandle() {
        if (handle==null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);
            handle = ESlate.registerPart(this);
            handle.addESlateListener(new ESlateAdapter() {

                public void handleDisposed(HandleDisposalEvent e) {
                    stringSO = null;
                    PerformanceManager pm = PerformanceManager
                            .getPerformanceManager();
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
                }

            });
            try {
                handle.setUniqueComponentName(bundleMessages
                        .getString("ESlateLabel"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle
                    .addPrimitiveGroup("gr.cti.eslate.scripting.logo.LabelPrimitives");
            handle.setInfo(getInfo());
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    public NewRestorableImageIcon toRestorableImageIcon(Icon icon) {

        if (icon==null) {
            return null;
        }
        if (icon.getIconHeight()==- 1||icon.getIconHeight()==- 1)
            return null;
        if (icon instanceof NewRestorableImageIcon)
            return new NewRestorableImageIcon(((NewRestorableImageIcon) icon)
                    .getImage());
        if (icon instanceof ImageIcon) {
            return new NewRestorableImageIcon(((ImageIcon) icon).getImage());
        }
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon
                .getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        icon.paintIcon(this, g, 0, 0);
        g.dispose();
        return new NewRestorableImageIcon(b);
    }

    public void readExternal(ObjectInput in) throws ClassNotFoundException,
            IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
        Object firstObj = in.readObject();
        StorageStructure fieldMap = (StorageStructure) firstObj;
        if (fieldMap.containsKey("MultiLineMode"))
            setMultilineMode(fieldMap.get("MultiLineMode", isMultilineMode()));
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));
        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions",
                getDebugGraphicsOptions()));
        setIconTextGap(fieldMap.get("IconTextGap", getIconTextGap()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setHorizontalAlignment(fieldMap.get("HorizontalAlignment",
                getHorizontalAlignment()));
        setHorizontalTextPosition(fieldMap.get("HorizontalTextPosition",
                getHorizontalTextPosition()));
        setVerticalAlignment(fieldMap.get("VerticalAlignment",
                getVerticalAlignment()));
        setVerticalTextPosition(fieldMap.get("VerticalTextPosition",
                getVerticalTextPosition()));
        setText(fieldMap.get("Text", getText()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setMaximumSize(fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize(fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize(fieldMap.get("PreferredSize", getPreferredSize()));
        setDisabledIcon(fieldMap.get("DisabledIcon", getDisabledIcon()));
        setIcon(fieldMap.get("Icon", getIcon()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));
        if (fieldMap.containsKey("Border"))
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");
                setBorder(bd.getBorder());
            } catch (Throwable throwable) {
            }
        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(1);
        fieldMap.put("MultiLineMode", isMultilineMode());
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());
        if (! (getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("HorizontalAlignment", getHorizontalAlignment());
        fieldMap.put("HorizontalTextPosition", getHorizontalTextPosition());
        fieldMap.put("VerticalAlignment", getVerticalAlignment());
        fieldMap.put("VerticalTextPosition", getVerticalTextPosition());
        fieldMap.put("Text", getText());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        if (! (getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (! (getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());
        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
        fieldMap.put("DisabledIcon", toRestorableImageIcon(getDisabledIcon()));
        fieldMap.put("Icon", toRestorableImageIcon(getIcon()));
        fieldMap.put("PlugsUsed", getPlugsUsed());

        if (getBorder()!=null&&! (getBorder() instanceof UIResource))
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(
                        getBorder(), this);
                fieldMap.put("Border", bd);
            } catch (Throwable throwable) {
            }
        else if (getBorder()==null)
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(
                        getBorder(), this);
                fieldMap.put("Border", bd);
            } catch (Throwable throwable1) {
            }
        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener==null) {
            perfListener = new PerformanceAdapter() {

                public void performanceManagerStateChanged(PropertyChangeEvent e) {
                    boolean enabled = ((Boolean) e.getNewValue())
                            .booleanValue();
                    if (enabled)
                        attachTimers();
                }

            };
            pm.addPerformanceListener(perfListener);
        }
    }

    private void attachTimers() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();
        if (! pmEnabled&&perfListener==null)
            createPerformanceManagerListener(pm);
        if (! pmEnabled)
            return;
        boolean timersCreated = loadTimer!=null;
        if (! timersCreated) {
            PerformanceTimerGroup compoTimerGroup = pm
                    .getPerformanceTimerGroup(this);
            constructorTimer = (PerformanceTimer) pm
                    .createPerformanceTimerGroup(compoTimerGroup,
                            bundleMessages.getString("ConstructorTimer"), true);
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                    compoTimerGroup, bundleMessages.getString("LoadTimer"),
                    true);
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                    compoTimerGroup, bundleMessages.getString("SaveTimer"),
                    true);
            initESlateAspectTimer = (PerformanceTimer) pm
                    .createPerformanceTimerGroup(compoTimerGroup,
                            bundleMessages.getString("InitESlateAspectTimer"),
                            true);
            pm.registerPerformanceTimerGroup(0, constructorTimer, this);
            pm.registerPerformanceTimerGroup(1, loadTimer, this);
            pm.registerPerformanceTimerGroup(2, saveTimer, this);
            pm.registerPerformanceTimerGroup(3, initESlateAspectTimer, this);
        }
    }

    private void createPlugs() {
        if (handle==null)
            return;
        stringSO = new StringSO(handle);
        try {
            SharedObjectListener sol = new SharedObjectListener() {

                public synchronized void handleSharedObjectEvent(
                        SharedObjectEvent e) {
                    setText(((StringSO) e.getSharedObject()).getString());
                }

            };
            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages,
                    "Title", new Color(139, 117, 0),
                    gr.cti.eslate.sharedObject.StringSO.class, stringSO, sol);
            plug.addConnectionListener(new ConnectionListener() {

                public void handleConnectionEvent(ConnectionEvent e) {
                    if (e.getType()==0) {
                        StringSO so = (StringSO) ((SharedObjectPlug) e
                                .getPlug()).getSharedObject();
                        setText(so.getString());
                    }
                }

            });
            handle.addPlug(plug);
        } catch (InvalidPlugParametersException invalidplugparametersexception) {
        } catch (PlugExistsException plugexistsexception) {
        }

        createProtocolPlug();
    }

    private void destroyPlugs() {
        try {
            if (plug!=null)
                handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug = null;
        stringSO = null;

        destroyProtocolPlug();
    }

    public void setPlugsUsed(boolean create) {
        if (plugsUsed==create)
            return;
        if (plugsUsed==create)
            return;
        if (handle!=null) {
            plugsUsed = create;
            if (create)
                createPlugs();
            else
                destroyPlugs();
        } else {
            plugsUsed = false;
        }
    }

    public boolean getPlugsUsed() {
        return plugsUsed;
    }

    /**
     * Create multiple protocol plug.
     */
    private void createProtocolPlug() {
        try {
            animationPlug = new MaleSingleIFMultipleConnectionProtocolPlug(
                    handle, null, bundleMessages.getString("animationPlug"),
                    new Color(0, 100, 255));
            animationPlug.setNameLocaleIndependent(animationPlug
                    .getInternalName()
                    +plugCount);
            animationPlug.setName(animationPlug.getName()+plugCount);
            animationPlug.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    plugCount++;
                    e.getOwnPlug().removeConnectionListener(this);
                    createProtocolPlug();
                }
            });
            animationPlug.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent(DisconnectionEvent e) {
                    try {
                        handle.removePlug(e.getOwnPlug());
                    } catch (NoSuchPlugException nspe) {
                    }
                }
            });
            try {
                handle.addPlug(animationPlug);
            } catch (PlugExistsException e) {
                System.err.println("Plug exists");
                e.printStackTrace();
            }
        } catch (InvalidPlugParametersException e) {
        } catch (PlugExistsException e) {
        }
    }

    private void destroyProtocolPlug() {
        try {
            if (animationPlug!=null)
                handle.removePlug(animationPlug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        animationPlug = null;
    }

    /**
     * @return The values of the variables of the actor.
     */
    public IntBaseArray getVarValues() {
        if (varValues==null)
            varValues = new IntBaseArray(varCount);
        else
            varValues.clear();
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, ESlateLabel.this);
        Rectangle bounds = internalFrame.getBounds();
        varValues.add(bounds.x);
        varValues.add(bounds.y);
        varValues.add(bounds.width);
        varValues.add(bounds.height);
        return varValues;
    }

    /**
     * Sets the values of the actor's variables.
     * 
     * @param varValues
     *            The values of the actor's variables.
     * @param animationSession
     *            The animation session to set the values.
     */
    public void setVarValues(IntBaseArray varValues,
            AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, ESlateLabel.this);
        Rectangle bounds = internalFrame.getBounds();
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        int i = 0;
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(0))
                        .getPropertyID())) {
            x = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(1))
                        .getPropertyID())) {
            y = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(2))
                        .getPropertyID())) {
            width = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(3))
                        .getPropertyID())) {
            height = varValues.get(i);
        }
        internalFrame.setBounds(x, y, width, height);
    }

    /**
     * Get the animated property structure.
     * 
     * @return The animated property structure.
     */
    public AnimatedPropertyStructure getAnimatedPropertyStructure() {
        if (animatedPropertyStructure==null) {
            animatedPropertyStructure = new AnimatedPropertyStructure();
            AnimatedPropertyDescriptor aniProDesc1 = new AnimatedPropertyDescriptor(
                    0, bundleMessages.getString("x"));
            AnimatedPropertyDescriptor aniProDesc2 = new AnimatedPropertyDescriptor(
                    1, bundleMessages.getString("y"));
            AnimatedPropertyDescriptor aniProDesc3 = new AnimatedPropertyDescriptor(
                    2, bundleMessages.getString("width"));
            AnimatedPropertyDescriptor aniProDesc4 = new AnimatedPropertyDescriptor(
                    3, bundleMessages.getString("height"));
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc1);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc2);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc3);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc4);
        }
        return animatedPropertyStructure;
    }

    /**
     * Get plugs number.
     * 
     * @return The plug's number.
     */
    public int getPlugCount() {
        return plugCount;
    }

    /**
     * Actor is active (on stage).
     * 
     * @param animationSession
     *            The actor's animation session.
     */
    public void onStage(AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, ESlateLabel.this);
        internalFrame.setVisible(true);
    }

    /**
     * Actor is inactive (off stage).
     * 
     * @param animationSession
     *            The actor's animation session.
     */
    public void offStage(AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, ESlateLabel.this);
        internalFrame.setVisible(false);
    }

    /**
     * Get actor name.
     * @return The actor's name.
     */
    public String getActorName() {
        return bundleMessages.getString("actorName");
    }

    /**
     * Add a listener for actor name events.
     * @param	listener	The listener to add.
     */
    public void addActorNameListener(ActorNameListener listener) {
        synchronized (actorNameListeners) {
            if (! actorNameListeners.contains(listener)) {
                actorNameListeners.add(listener);
            }
        }
    }

    /**
     * Remove a listener from actor's name events.
     * @param	listener	The listener to remove.
     */
    public void removeActorNameListener(ActorNameListener listener) {
        synchronized (actorNameListeners) {
            int ind = actorNameListeners.indexOf(listener);
            if (ind>=0) {
                actorNameListeners.remove(ind);
            }
        }
    }

    /**
     * Fires all listeners registered for milestone events.
     * @param	actorName  The actor's name that changed.
     */
    public void fireActorNameListeners(String actorName) {
        ArrayList listeners;
        synchronized (actorNameListeners) {
            listeners = (ArrayList) (actorNameListeners.clone());
        }
        int size = listeners.size();
        for (int i = 0; i<size; i++) {
            ActorNameListener l = (ActorNameListener) (listeners.get(i));
            ActorNameEvent e = new ActorNameEvent(this, actorName);
            l.actorNameChanged(e);
        }
    }
}
