package gr.cti.eslate.database.engine;


public class UnableToAddNewRecordException extends Exception {
    public String message;
    public UnableToAddNewRecordException() {};
    public UnableToAddNewRecordException(String msg) {
        super(msg);
        message = msg;
    }
}
