package gr.cti.eslate.database.engine;


public class EmptyFileException extends Exception {
    public String message;
    public EmptyFileException() {};
    public EmptyFileException(String msg) {
        super(msg);
        message = msg;
    }
}
