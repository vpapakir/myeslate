package gr.cti.eslate.database.engine;


public class UnionTablesNotIdenticalException extends Exception {
    public String message;
    public UnionTablesNotIdenticalException() {};
    public UnionTablesNotIdenticalException(String msg) {
        super(msg);
        message = msg;
    }
}
