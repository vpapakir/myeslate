package gr.cti.eslate.database.engine;


public final class LessHashComparator implements ObjectComparator {
    /**
    * Compare the operands based on their hash code.
    * @param first The first object.
    * @param second The second object.
    * @return true if the hash code of the first operand is less than the hash code of
    * the second operand using the standard Java hashCode() method.
    */
    public boolean execute(Object first, Object second) {
        if (first == null) {
            if (second == null) return false;
            return true;
        }
        if (second == null) return false;
        return first.hashCode() < second.hashCode();
    }
}
