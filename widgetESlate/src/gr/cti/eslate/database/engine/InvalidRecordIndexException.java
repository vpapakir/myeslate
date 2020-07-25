package gr.cti.eslate.database.engine;


public class InvalidRecordIndexException extends Exception {
    public String message;
    public InvalidRecordIndexException() {};
    public InvalidRecordIndexException(String msg) {
        super(msg);
        message = msg;
    }
}
