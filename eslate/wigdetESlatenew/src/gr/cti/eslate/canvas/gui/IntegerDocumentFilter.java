package gr.cti.eslate.canvas.gui;

import javax.swing.text.*;

public class IntegerDocumentFilter extends DocumentFilter
{
  int currentValue = 0;

  public IntegerDocumentFilter()
  {
  }

  public void insertString
    (DocumentFilter.FilterBypass fb, int offset, String string,
     AttributeSet attr)
    throws BadLocationException
  {
    if (string == null) {
      return ;
    }else{
      replace(fb, offset, 0, string, attr);
    }
  }

  public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
    throws BadLocationException
  {
    replace(fb, offset, length, "", null);
  }

  public void replace
    (DocumentFilter.FilterBypass fb, int offset, int length, String text,
     AttributeSet attrs)
    throws BadLocationException
  {
    Document doc = fb.getDocument();
    int currentLength = doc.getLength();
    String currentContent = doc.getText(0, currentLength);
    String before = currentContent.substring(0, offset);
    String after = currentContent.substring(length + offset, currentLength);
    String newValue = before + (text == null ? "" : text) + after;
    currentValue = checkInput(newValue, offset);
    fb.replace(offset, length, text, attrs);
  }

  private int checkInput(String proposedValue, int offset)
    throws BadLocationException
  {
    int newValue = 0;
    if (proposedValue.length() > 0) {
      try {
        newValue = Integer.parseInt(proposedValue);
      } catch (NumberFormatException e) {
        throw new BadLocationException(proposedValue, offset);
      }
    }
    return newValue;
  }
}
