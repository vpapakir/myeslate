package gr.cti.eslate.database.engine;


public class InvalidOperandExpressionException extends Exception {
    public String message;
    public InvalidOperandExpressionException() {};
    public InvalidOperandExpressionException(String msg) {
        super(msg);
        Thread.currentThread().dumpStack();
        message = msg;
    }
}
