package gr.cti.eslate.database.engine;


public class UnableToWriteFileException extends Exception {
    public String message;
    public UnableToWriteFileException() {};
    public UnableToWriteFileException(String msg) {
        super(msg);
        message = msg;
    }
}
