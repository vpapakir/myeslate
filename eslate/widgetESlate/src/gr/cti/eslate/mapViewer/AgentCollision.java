package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IAgent;
import gr.cti.typeArray.ObjectBaseArray;

/**
 * Keeps track of agents that have met, in order to avoid
 * informing again with an event.
 * @author  Giorgos Vasiliou
 * @version 1.0, 17-Feb-2003
 * @since   2.3.2
 */
class AgentCollision {
	/**
	 * Adds a pair of agent as agents that have met.
	 */
	void add(IAgent a1,IAgent a2) {
		if (agents1==null) {
			agents1=new ObjectBaseArray();
			agents2=new ObjectBaseArray();
		}
		if (!hasPair(a1,a2)) {
			agents1.add(a1);
			agents2.add(a2);
		}
	}
	/**
	 * Checks if the pair has been added, which means that the
	 * listeners have already been informed.
	 */
	boolean hasPair(IAgent a1,IAgent a2) {
		if (agents1==null)
			return false;
		for (int i=agents1.size()-1;i>-1;i--)
			if ((agents1.get(i)==a1 && agents2.get(i)==a2) || (agents1.get(i)==a2 && agents2.get(i)==a1))
				return true;
		return false;
	}
	/**
	 * Removes a pair.
	 */
	void remove(IAgent a1,IAgent a2) {
		if (agents1==null)
			return;
		for (int i=agents1.size()-1;i>-1;i--)
			if ((agents1.get(i)==a1 && agents2.get(i)==a2) || (agents1.get(i)==a2 && agents2.get(i)==a1)) {
				agents1.remove(i);
				agents2.remove(i);
			}
	}

	private ObjectBaseArray agents1,agents2;
}