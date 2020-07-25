package gr.cti.eslate.base.container;


public class DuplicateEntryException extends Exception {
    int entryType = -1;

    public DuplicateEntryException() {};
    public DuplicateEntryException(String msg) {
        super(msg);
    }
    public DuplicateEntryException(int entry) {
        super();
        entryType = entry;
    }
}
