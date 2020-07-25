package gr.cti.eslate.database.engine;


public class TableNotExpandableException extends Exception {
    public String message;
    public TableNotExpandableException() {};
    public TableNotExpandableException(String msg) {
        super(msg);
        message = msg;
    }
}
