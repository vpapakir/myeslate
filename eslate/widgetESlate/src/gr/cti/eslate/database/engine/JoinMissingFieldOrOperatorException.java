package gr.cti.eslate.database.engine;


public class JoinMissingFieldOrOperatorException extends Exception {
    public String message;
    public JoinMissingFieldOrOperatorException() {};
    public JoinMissingFieldOrOperatorException(String msg) {
        super(msg);
        message = msg;
    }
}
