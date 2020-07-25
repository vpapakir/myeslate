package gr.cti.eslate.database.engine;


public final class GreaterEqualHashComparator implements ObjectComparator {
    /**
    * Compare the operands based on their hash code.
    * @param first The first object.
    * @param second The second object.
    * @return true if the hash code of the first operand is greater than or equal to the
    * hash code of the second operand using the standard Java hashCode() method.
    */
    public boolean execute(Object first, Object second) {
        if (first == null) {
            if (second == null) return true;
            return false;
        }
        if (second == null) return true;
        return first.hashCode() >= second.hashCode();
    }
}
