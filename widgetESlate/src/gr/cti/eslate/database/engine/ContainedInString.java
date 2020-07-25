package gr.cti.eslate.database.engine;


/**
 * ContainedInString is a string query comparator that returns true
 * if the first operand is contained in the second operand.
 */
public class ContainedInString extends StringComparator {
  /**
   * Return true if the first operand is contained into the second operand.
   * @return second.indexOf(first) != -1
   */
    public boolean execute(String first, String second) {
        if (second.indexOf(first) == -1)
            return false;
        return true;
    }
}
