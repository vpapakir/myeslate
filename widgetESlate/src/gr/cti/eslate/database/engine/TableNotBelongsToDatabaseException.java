package gr.cti.eslate.database.engine;


public class TableNotBelongsToDatabaseException extends Exception {
    public String message;
    public TableNotBelongsToDatabaseException() {};
    public TableNotBelongsToDatabaseException(String msg) {
        super(msg);
        message = msg;
    }
}
