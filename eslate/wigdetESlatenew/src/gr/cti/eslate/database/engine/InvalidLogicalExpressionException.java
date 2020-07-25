package gr.cti.eslate.database.engine;


public class InvalidLogicalExpressionException extends Exception {
    public String message;
    public InvalidLogicalExpressionException() {};
    public InvalidLogicalExpressionException(String msg) {
        super(msg);
        message = msg;
    }
}
