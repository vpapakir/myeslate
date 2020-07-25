package gr.cti.eslate.database.engine;


public class UnableToSaveException extends Exception {
    public String message;
    public UnableToSaveException() {};
    public UnableToSaveException(String msg) {
        super(msg);
        message = msg;
    }
}
