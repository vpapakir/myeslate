package gr.cti.eslate.database.engine;


public class InvalidKeyFieldException extends Exception {
    public String message;
    public InvalidKeyFieldException() {};
    public InvalidKeyFieldException(String msg) {
        super(msg);
        message = msg;
    }
}
