/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 21 Οκτ 2002
 * Time: 6:59:19 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

public class LessEqualHashComparator implements ObjectComparator {
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
            return true;
        }
        if (second == null) return false;
        return first.hashCode() <= second.hashCode();
    }
}
