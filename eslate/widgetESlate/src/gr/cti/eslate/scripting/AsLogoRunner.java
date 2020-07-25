//7Nov1999//

package gr.cti.eslate.scripting;

public interface AsLogoRunner
{
 public void queueLogoStatement(String s); //asynchronous, returns immediately, queues statement in a FIFO to execute later
 public void doLogoStatement(String s);    //blocking, either places command in FIFO and waits for its turn to execute and return, or spawns another thread to run the command
 public String doLogoOperation(String s);  //blocking, returns result as a LogoObject.toString()
}
