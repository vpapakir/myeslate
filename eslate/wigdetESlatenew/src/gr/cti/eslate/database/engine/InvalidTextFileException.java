package gr.cti.eslate.database.engine;


public class InvalidTextFileException extends Exception {
    public String message;
    public InvalidTextFileException() {};
    public InvalidTextFileException(String msg) {
        super(msg);
        message = msg;
    }
}
