package gr.cti.eslate.database.engine;


public class IntersectionTablesNotIdenticalException extends Exception {
    public String message;
    public IntersectionTablesNotIdenticalException() {};
    public IntersectionTablesNotIdenticalException(String msg) {
        super(msg);
        message = msg;
    }
}
