package gr.cti.eslate.database.engine;


public class UnableToReadFileException extends Exception {
    public String message;
    public UnableToReadFileException() {};
    public UnableToReadFileException(String msg) {
        super(msg);
        message = msg;
    }
}
