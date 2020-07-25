package gr.cti.eslate.database.engine;


public class JoinMissingFieldException extends Exception {
    public String message;
    public JoinMissingFieldException() {};
    public JoinMissingFieldException(String msg) {
        super(msg);
        message = msg;
    }
}
