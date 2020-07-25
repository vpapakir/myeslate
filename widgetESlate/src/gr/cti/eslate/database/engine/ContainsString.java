package gr.cti.eslate.database.engine;


/**
 * ContainsString is a query comparator that returns true
 * if the first operand contains the second operand.
 */
public final class ContainsString extends StringComparator {
    /**
    * Return true if the first operand contains the second operand.
    * @return first.indexOf(second) != -1
    */
    public boolean execute(String first, String second) {
        if ( first.indexOf(second) == -1)
            return false;
        return true;
    }
}
