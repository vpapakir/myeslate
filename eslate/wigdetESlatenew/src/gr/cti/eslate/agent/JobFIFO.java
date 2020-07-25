package gr.cti.eslate.agent;

import java.util.LinkedList;

import gr.cti.eslate.mapModel.geom.Heading;
import gr.cti.eslate.protocol.GeographicObject;

/**
 * A JobFIFO data structure.
 */
public class JobFIFO {
	/**
	 * Constructor.
	 * @param agent The agent that this fifo operates on.
	 */
	public JobFIFO(Agent agent) {
	    joblist=new LinkedList<Job>();
		isAlive=true;
		this.agent=agent;
	}

	/**
	 * Runs the fifo.
	 */
	public void run() {
		synchronized (joblist) {
			//Indicates a stop because a tick is needed
			boolean needTick=false;
			while (size()>0 && !needTick) {
//System.out.print(agent.getName()+" Jobs queued: ");
//for(int i=0;i<size();i++)
//System.out.print(at(i)+" # ");
//System.out.println();
				Job job=at(0);
				//Time consuming jobs have to be delayed
				if (agent.isTimeAware() && agent.tick==0 && Job.isTimeConsumingJob(job.type))
					needTick=true;
				else {
					//Time consuming jobs have to be ignored if there is no velocity
					if (agent.isTimeAware() && agent.getPotentialVelocity()==0 && Job.isMotionJob(job.type))
						consumeFirst();
					else {
						boolean consume=true;
						switch (job.type) {
							case Job.STOP:
								consume=true;
								agent.stopImpl();
								break;
							case Job.WAKE:
								consume=true;
								break;
							case Job.SLEEP:
								consume=agent.sleepImpl(job);
								break;
							case Job.GO:
								consume=agent.goImpl(job);
								break;
							case Job.MOVE:
								consume=agent.moveImpl(job);
								break;
							case Job.JUMP:
								agent.jumpImpl(job.param[0],job.param[1]);
								break;
							case Job.GOTO:
								consume=agent.goToImpl(job);
								break;
							case Job.LOOK:
								agent.lookImpl(job.param[0]);
								break;
							case Job.TURN:
								agent.turnImpl(job.param[0]);
								break;
							case Job.GOTO_ONHOST:
								consume=agent.goToOnHostImpl(job);
								break;
							case Job.GOTO_ONLINE_UNFINISHED:
								consume=agent.goToOnLineUnfinishedImpl(job);
								break;
						}
						if (consume) {
							Job was=consumeFirst();
							if (was.type==Job.GO) {
								//Produce an event when the agent stops
								if (agent.listeners!=null) {
									GeographicObject go=null;
									if (agent.lastTravelledOn!=null)
										if (agent.lastTravelledOn instanceof Heading.Line)
											go=((Heading.Line) agent.lastTravelledOn).line;
									agent.listeners.agentStopped(new AgentEvent(agent,go,null));
								}
							}
						}
					}
				}
			}
			//If no tick is needed, the agent has nothing to do, so stop it.
			if (!needTick)
				agent.stopImpl();
		}
	}

	public void put(Job o) {
		synchronized (joblist) {
			joblist.add(o);
			run();
		}
	}

	public void putFirst(Job o) {
		synchronized (joblist) {
			joblist.addFirst(o);
			run();
		}
	}
	
	public Job at(int i) {
		synchronized (joblist) {
		    if (i<0 || i>=joblist.size())
		        throw new ArrayIndexOutOfBoundsException("No such job.");
		    return joblist.get(0);
		}
	}

	public int size() {
		synchronized (joblist) {
			return joblist.size();
		}
	}

	private Job consumeFirst() {
		synchronized (joblist) {
		    if (joblist.size()==0)
		        return null;
			return joblist.removeFirst();
		}
	}

	public void clear() {
		synchronized (joblist) {
			joblist.clear();
		}
	}

	private LinkedList<Job> joblist;
	/**
	 * Indicates that the fifo is alive. Is set to false the thread dies.
	 * Should die when the agent dies.
	 */
	public boolean isAlive;
	/**
	 * The agent this fifo operates on.
	 */
	private Agent agent;
}
