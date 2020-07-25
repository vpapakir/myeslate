package gr.cti.eslate.database.engine;


public class InvalidDataFormatException extends Exception {
    public String message;
    public InvalidDataFormatException() {};
    public InvalidDataFormatException(String msg) {
        super(msg);
        message = msg;
    }
}
