package gr.cti.eslate.database.engine;


public class InvalidFieldException extends Exception {
    public InvalidFieldException() {};
    public InvalidFieldException(String msg) {
        super(msg);
    }
}
