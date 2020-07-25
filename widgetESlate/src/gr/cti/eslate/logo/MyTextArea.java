package gr.cti.eslate.logo;

import java.awt.*;
import java.util.*;
import javax.swing.text.*;

import jeditSyntax.*;

/**
 * Text area used for the input and output area of the Logo component.
 *
 * @version     2.0.1, 2-Jun-2006
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class MyTextArea extends JEditTextArea
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Plain text file.
   */
  public final static String PLAIN_TEXT = "text/plain2";
  /**
   * Java file.
   */
  public final static String JAVA = "text/x-java";
  /**
   * Logo file.
   */
  public final static String LOGO = "text/x-logo";
  /**
   * JavaScript file.
   */
  public final static String JAVASCRIPT = "text/x-javascript";

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


  String fontName = Logo.defaultFontName;
  int fontSize = Logo.defaultFontSize;
  private boolean lineNumbersVisible = false;

  /**
   * Create a text area.
   * @param     type    The type of text being edited in the text area.
   *                    This should be one of PLAIN_TEXT, Editor.JAVA,
   *                    JAVASCRIPT, or LOGO. Anything else is
   *                    equivalent to PLAIN_TEXT.
   */
  public MyTextArea(String type)
  {
    super();

    TextAreaPainter tp = getPainter();
    tp.setEOLMarkersPainted(false);
    // If we don't do this, then all instances share the same document!!!
    setDocument(new SyntaxDocument());
    setFont(new Font(fontName, Font.PLAIN, fontSize));
    if (JAVA.equals(type)) {
      setTokenMarker(new JavaTokenMarker());
    }else{
      if (JAVASCRIPT.equals(type)) {
        setTokenMarker(new JavaScriptTokenMarker());
      }else{
        if (LOGO.equals(type)) {
          setTokenMarker(new LogoTokenMarker());
        }
      }
    }
  }

  /**
   * Specifies whether line numbers should be visible. 
   * Currently a no-op.
   * @param     visible True if yes, false if no.
   */
  public void setLineNumbersVisible(boolean visible)
  {
  }

  /**
   * Checks whether line numbers are visible.
   * @return    True if yes, false if no. Currnetly always returns false.
   */
  public boolean isLineNumbersVisible()
  {
    return lineNumbersVisible;
  }

  /**
   * Appends the given text to the end of the document.
   * @param     txt     The text to append.
   */
  public void append(String txt)
  {
    SyntaxDocument document = getDocument();
    try {
      document.beginCompoundEdit();
      document.insertString(document.getLength(), txt, null);
    } catch (BadLocationException ble) {
      ble.printStackTrace();
    } finally {
      document.endCompoundEdit();
    }
  }

  /**
   * Changes the font of the text area.
   * @param     font    The new font of the text area.
   */
  public void setFont(Font font)
  {
    TextAreaPainter tp = getPainter();
    tp.setFont(font);
    fontName = font.getName();
    fontSize = font.getSize();
  }

  /**
   * Returns the font of the text area.
   * @return    The requested font. The returned Font instance is created on
   *            the fly from the font name and size.
   */
  public Font getFont()
  {
    TextAreaPainter tp = getPainter();
    return tp.getFont();
  }

  public void appendSelected(String s)
  {
    SyntaxDocument document = getDocument();
    int oldEnd = document.getLength();
    setCaretPosition(oldEnd);
    append(s);
    int newEnd = document.getLength();
    setCaretPosition(newEnd);
    select(oldEnd, newEnd);
  }

  public void appendScrolling(String s)
  { //15-4-1999
    append(s);                                    //first append
    setCaretPosition(getDocument().getLength());  //then scroll (don't reverse these actions, else the area might not scroll at all [in case the old text was already just fitting the area])
  }


  public void changeSelection(String s)
  {
    setSelectedText(s);
  }

  public void moveToNextLineStart()
  {
    int nextline = getCurrentLine() + 1;
    try {
      setCaretPosition(getLineStartOffset(nextline));
    } catch (Exception e) {
      //setRows(getRows()+1); //this doesn't work, just add append a "\n"
      //System.out.println("Appending a new line");
      append("\n");
      try {
        setCaretPosition(getLineStartOffset(nextline));
      } catch (Exception ex) {
        System.err.println(ex + "\nCouldn't append a new line!");
      }
    }
  }

  public void moveToStartOfLineAfterSelection()
  {
    try {
      setCaretPosition(getSelectionEnd() - 1);  //11-1-1999: must have -1, so that if two whole lines are selected we don't move to next-next line
    }
    catch (Exception e) {
      //try{setCaretPosition(getSelectionEnd()-1);}catch(Exception ex){}
      System.out.println("moveToStartOfLineAfterSelection: no selection: just moving to start of next line");
    } //30-9-1998:catching exceptions:if a LanguageException was thrown by Logo, there is no selection any more
    moveToNextLineStart();
  }

  //noexceptions-start//
  int getCurrentLine()
  {
    //try {
      return getLineOfOffset(getCaretPosition());
    //} catch (BadLocationException e) { //9-1-1998: seems to be unable to get the current line if we are at end of 1st line which is also end of document...
      //System.err.println(e); //don't print this: occurs when at end of doc
      //try { //...retrying with caretpos-1 works
        //return getLineOfOffset(getCaretPosition() - 1);
      //} catch (BadLocationException ex) {
        //System.err.println("Error at getCurrentLine(): returning 1st line as the current one");
        //return 0;
      //}
    //}
  }

  int getCurrentLineStart()
  {
    //try {
      return getLineStartOffset(getCurrentLine());
    //} catch (BadLocationException e) {
      //System.err.println(e + "\nError at getCurrentLineStart");
      //return getCaretPosition();
    //}
  }

  int getCurrentLineEnd()
  {
    //try {
      return getLineEndOffset(getCurrentLine());
    //} catch (BadLocationException e) {
      //System.err.println(e + "\nError at getCurrentLineEnd");
      //return getCaretPosition();
    //}
  }

  int getLineStart(int line)
  {
    //try {
      return getLineStartOffset(line);
    //} catch (BadLocationException e) {
      //System.err.println(e + "\nError at getLineStart");
      //return 0;
    //}
  }

  int getLineEnd(int line)
  {
    //try {
      return getLineEndOffset(line);
    //} catch (BadLocationException e) {
      //System.err.println(e + "\nError at getLineEnd");
      //return 0;
    //}
  }
  //noexceptions-end//

  public void selectCurrentLine()
  {
    try {
      int start = getCurrentLineStart();
      int end = getCurrentLineEnd();
      setCaretPosition(start);
      select(start, end-1);
//11-1-1999: need -1 cause else setCaretPosition(getSelectionEnd()) at moveToStartOfLineAfterSelection fails, however placed -1 there (needed when selection two whole lines, cause cursor moved to the next next line
    }
    catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error at selectCurrentLine: " + e);  //!!!
      append("\n");
    } //1-9-1998: add lines at end
  }


  public void unSelect()
  {
    try {
      int pos = getCaretPosition();
      select(pos, pos);
    } //unSelect //11-1-1999: fixed-bug:was moving one pos behind!!!
    catch (Exception e) { //in case we were at pos 0
      setCaretPosition(0);
    } //1-9-1998: ???
  }

  public void insertAtNewLineSelected(String s)
  {
    try {
      int pos = getLineStartOffset(getLineOfOffset(getCaretPosition()));
      insert(s + '\n', pos);  //20-10-1998: changed CR[=="\n"] to '\n'
      select(pos, pos + s.length());
    } catch (BadLocationException e) {
      System.err.println(e);
      appendSelected(s);
    } //31-8-1998
  }

  /**
   * Returns a list of the names of all available fonts.
   * @return    A list of the names of all available fonts.
   */
  @SuppressWarnings("unchecked")
  public static Vector getAvailableFontNames()
  {
    return fontNames;
  }

  /**
   * Replaces the selected text with new text.
   * @param     text    The new text.
   */
  public void replaceSelection(String text)
  {
    setSelectedText(text);
  }

  /**
   * Sets the color of the caret.
   * @param     color   The color of the caret.
   */
  public void setCaretColor(Color color)
  {
    TextAreaPainter tp = getPainter();
    tp.setCaretColor(color);
  }

  /**
   * Appends the given text at the given position.
   * @param     txt     The text to insert.
   * @param     pos     The position where the text will be inserted.
   */
  public void insert(String txt, int pos) throws BadLocationException
  {
    SyntaxDocument document = getDocument();
    try {
      document.beginCompoundEdit();
      document.insertString(pos, txt, null);
    } finally {
      document.endCompoundEdit();
    }
  }

}
