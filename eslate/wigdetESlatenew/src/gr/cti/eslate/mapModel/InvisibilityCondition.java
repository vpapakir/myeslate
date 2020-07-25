package gr.cti.eslate.mapModel;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

//////////////////////INVISIBILITY CONDITION CLASS (N)///////////////////////////////

public class InvisibilityCondition implements Externalizable{

   String queryString="";
   String name="";
   boolean shouldBeStored = false;
   protected InvisibilityCriteria criteria;
   static final long serialVersionUID=3000L;

   public InvisibilityCondition(InvisibilityCriteria criteria,String name, String condition, boolean shouldBeStored){
	  queryString = condition;
	  this.name =  name;
	  this.shouldBeStored = shouldBeStored;
	  this.criteria = criteria;

   }

   public InvisibilityCondition(){
   }

	public void setQueryString(String s){
		if ((s==null && queryString==null) || (s!=null && s.equals(queryString)))
			return;
		queryString = s;
		criteria.createQueryString();
	}

   public String getQueryString(){
	  return queryString;
   }

   public void setStorable(boolean b){
	  shouldBeStored = b;
   }

   public boolean isStorable(){
	  return shouldBeStored;
   }

   public String getName(){
	  return name;
   }

   public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		name = ht.get("Name",getName());
		setStorable(ht.get("Storable",isStorable()));
		queryString = ht.get("QueryString",getQueryString());
		//criteria = (Invisibilityht.get("Criteria",criteria);
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("Name",getName());
		ht.put("Storable",isStorable());
		ht.put("QueryString",getQueryString());
		//ht.put("Criteria",criteria);
		out.writeObject(ht);
	}



}
