package gr.cti.eslate.database.engine;


public class InvalidDelimeterException extends Exception {
    public String message;
    public InvalidDelimeterException() {};
    public InvalidDelimeterException(String msg) {
        super(msg);
        message = msg;
    }
}
