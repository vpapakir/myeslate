package gr.cti.eslate.eslateTextArea;

import gr.cti.eslate.base.container.FilePropertyEditor;

public class TextFilePropertyEditor extends FilePropertyEditor {

    public TextFilePropertyEditor() {
        super();
        setDescriptions(new String[]{"txt"});
        setExtensions(new String[]{"txt"});
    }
}
