package gr.cti.eslate.database.engine;


public class IllegalCalculatedFieldException extends Exception {
    public String message;
    public IllegalCalculatedFieldException() {};
    public IllegalCalculatedFieldException(String msg) {
        super(msg);
        message = msg;
    }
}
