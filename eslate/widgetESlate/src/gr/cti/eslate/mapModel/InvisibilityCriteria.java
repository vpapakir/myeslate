package gr.cti.eslate.mapModel;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Enumeration;


public class InvisibilityCriteria implements Externalizable {

   protected Layer layer=null;
   protected MapView map=null;
   ArrayList invisibilityConditions;
   String queryString;
   static final long serialVersionUID=3000L;
   //For restoring only
   String layerGUID;

	protected InvisibilityCriteria(MapView map,Layer layer){
		this();
		this.layer=layer;
		this.map=map;
	}

   public InvisibilityCriteria(){
	  invisibilityConditions= new ArrayList();
	  queryString = "";
	  layer = null;
   }

   private String add(InvisibilityCondition condition, boolean shouldUpdate){
		 String s = "";
		 if (conditionNameExists(condition)){

			invisibilityConditions.add(new InvisibilityCondition(this, condition.getName()+"_1",condition.getQueryString(),condition.isStorable()));
			s = condition.getName()+"_1";
		 }else{

			invisibilityConditions.add(condition);
			//createQueryString();
			//layerView.updateInvisibleRecords();
			s = condition.getName();
		 }
		 if (shouldUpdate)
			createQueryString();
		 return s;
   }

   public String add(String name, String qString, boolean storable){
		 String s = "";
		 InvisibilityCondition condition = new InvisibilityCondition(this,name,qString,storable);
		 s = add(condition, true);
		 return s;
   }

	public boolean remove(String name){
		InvisibilityCondition condition=getCondition(name);
		if (condition!=null) {
			invisibilityConditions.remove(condition);
			createQueryString();
			return true;
		} else
			return false;
	}

	protected void createQueryString() {
		if (invisibilityConditions!=null) {
			if (invisibilityConditions.size()>0) {
				StringBuffer query=new StringBuffer();
				query.append("(");
				query.append(((InvisibilityCondition) invisibilityConditions.get(0)).getQueryString());
				query.append(")");
				for (int i=1;i<invisibilityConditions.size();i++) {
					query.append(" ");
					query.append(Map.bundleMessages.getString("OR"));
					query.append(" (");
					query.append(((InvisibilityCondition) invisibilityConditions.get(i)).getQueryString());
					query.append(")");
				}
				String tmp=new String(query);
				if (!tmp.equals(queryString)) {
					queryString=tmp;
					map.updateInvisibleRecords(layer);
				}
			} else if (invisibilityConditions.size()==0) {
				map.clearInvisibleRecords(layer);
			}
		}
	}

   public String getQueryString(){
	  return queryString;
   }


   public void flush(){
	  for (int i=0;i<invisibilityConditions.size();i++)
		 invisibilityConditions.remove(i);
   }

   private boolean conditionNameExists(InvisibilityCondition condition){
	  for (int i=0;i<invisibilityConditions.size();i++){
		 if (((InvisibilityCondition) invisibilityConditions.get(i)).getName().equals(condition.getName()))
			return true;
	  }
	  return false;
   }

   public InvisibilityCondition getCondition(String name){
	  InvisibilityCondition condition = null;
	  for (int i=0;i<invisibilityConditions.size();i++){
		 if (((InvisibilityCondition) invisibilityConditions.get(i)).getName().equals(name))
			condition = (InvisibilityCondition) invisibilityConditions.get(i);
	  }
	  return condition;
   }

   @SuppressWarnings("unused")
   private ArrayList getInvisibilityConditions() {
	  return invisibilityConditions;
   }

	/**
	 * The number of conditions in this criteria.
	 */
	public int getNumberOfConditions() {
		if (invisibilityConditions==null)
			return 0;
		else
			return invisibilityConditions.size();
	}

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		for (Enumeration e = ht.keys(); e.hasMoreElements();) {
			String str=(String) e.nextElement();
			if (!str.equals("&#__LayerGUID__#&")) {
				InvisibilityCondition c = (InvisibilityCondition) ht.get(str);
				c.criteria = (InvisibilityCriteria) this;
				add(c,false);
			}
		}
		layerGUID=ht.get("&#__LayerGUID__#&",(String)null);
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		for (int i =0;i<invisibilityConditions.size();i++){
			if (((InvisibilityCondition) invisibilityConditions.get(i)).isStorable()){
			   ht.put(((InvisibilityCondition) invisibilityConditions.get(i)).getName(),(InvisibilityCondition) invisibilityConditions.get(i));
			}
		}
		ht.put("&#__LayerGUID__#&",layer.getGUID());
		out.writeObject(ht);
	}

}


