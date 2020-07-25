package gr.cti.eslate.database.engine;


public class InvalidTypeConversionException extends Exception {
    public String message;
    public InvalidTypeConversionException() {};
    public InvalidTypeConversionException(String msg) {
        super(msg);
        message = msg;
    }
}

