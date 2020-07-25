package gr.cti.eslate.canvas.gui;

import javax.swing.*;
import javax.swing.text.*;

public class IntegerTextField extends JTextField
{
  private static final long serialVersionUID = 1L;

  public IntegerTextField()
  {
    super();
    Document doc = getDocument();
    DocumentFilter filter = new IntegerDocumentFilter();
    ((AbstractDocument)doc).setDocumentFilter(filter);
    setDocument(doc);
  }
}
