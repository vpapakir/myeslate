package gr.cti.eslate.logo;

import virtuoso.logo.*;

public class ProcedureCall{
 public Procedure procDef; //the procedure's definition!!!
 public LogoList procArgs; //called with these values
 public int time;          //call time (counts only 1st level calls)

 public ProcedureCall(Procedure procDef, LogoList procArgs, int time){
  this.procDef=procDef;
  this.procArgs=procArgs;
  this.time=time;
 }

}