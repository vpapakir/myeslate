package gr.cti.eslate.database.engine;


public class InvalidCellAddressException extends Exception {
    public String message;
    public InvalidCellAddressException() {};
    public InvalidCellAddressException(String msg) {
        super(msg);
        message = msg;
    }
}


