/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 21 Οκτ 2002
 * Time: 1:40:38 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

public interface ObjectComparator extends Comparator {
    public boolean execute(Object first, Object second);
}
