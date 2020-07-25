//8Nov1999//

package gr.cti.eslate.scripting;

public interface AsLogoProceduresMemory
{
 public String[] getLogoUserProcedures(); //get all the user procedures' names
 public String[] getLogoPrimitives(); //get all primitives' names

 public String getLogoUserProcedure(String name); //get the definition of a user procedure as a String, given its name (as a TO...END body)
 public void setLogoUserProcedure(String name, String definition); //refefine or make a new user procedure (using TO...END notation)

 public String getLogoUserProcedureLambda(String name); //same as getLogoUserProcedure, but returns a LambdaList
 public void setLogoUserProcedureLambda(String name,String lambdaDefinition); //same as setLogoUserProcedure, expects the procedure definition as a Lambda list
}
