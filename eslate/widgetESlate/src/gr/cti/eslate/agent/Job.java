package gr.cti.eslate.agent;

import gr.cti.eslate.protocol.IAgentHost;
import gr.cti.eslate.protocol.MotionFeatures;

/**
 * Class that defines jobs for the job queue of the agent.
 */
class Job {
	int type;
	double param[];
	IAgentHost host;
	MotionFeatures mf;

	Job(int type,double[] param) {
		this.type=type;
		this.param=param;
	}

	Job(int type,double[] param,IAgentHost host) {
		this.type=type;
		this.param=param;
		this.host=host;
	}

	Job(int type,MotionFeatures mf) {
		this.type=type;
		this.mf=mf;
	}

	public String toString() {
		String jt="";
		if (type==MOVE)
			jt="MOVE";
		else if (type==JUMP)
			jt="JUMP";
		else if (type==GOTO)
			jt="GOTO";
		else if (type==LOOK)
			jt="LOOK";
		else if (type==TURN)
			jt="TURN";
		else if (type==GOTO_ONHOST)
			jt="GOTO_ONHOST";
		else if (type==GOTO_ONLINE_UNFINISHED)
			jt="GOTO_ONLINE_UNFINISHED";
		else if (type==GO)
			jt="GO";
		else if (type==STOP)
			jt="STOP";
		else if (type==SLEEP)
			jt="SLEEP";
		else if (type==WAKE)
			jt="WAKE";
		if (param!=null)
			for (int i=0;i<param.length;i++)
				jt=jt+" "+param[i];
		return jt+((mf!=null)?" continued motion":"");//+((host!=null)?(" on host "+host):"");
	}

	/**
	 * Declares if the given job type needs time ticks to execute.
	 * @param i the job to check.
	 * @return true, if the job is time consuming, false otherwise.
	 */
	static boolean isTimeConsumingJob(int i) {
		return i==Job.SLEEP || isMotionJob(i);
	}
	/**
	 * Declares if the given job type is a motion job.
	 * @param i the job to check.
	 * @return true, if the job is a motion job, false otherwise.
	 */
	static boolean isMotionJob(int i) {
		return i==Job.MOVE || i==Job.GOTO || i==Job.GOTO_ONHOST || i==Job.GOTO_ONLINE_UNFINISHED || i==Job.GO;
	}
	/**
	 * Job type constant. Moves for a given amount of meters.
	 */
	static final int MOVE=0;
	/**
	 * Job type constant. Jumps to the desired location.
	 */
	static final int JUMP=1;
	/**
	 * Job type constant. Goes to the desired location walking through all other locations.
	 */
	static final int GOTO=2;
	/**
	 * Job type constant. Looks to the given angle from north anti-clockwise.
	 */
	static final int LOOK=3;
	/**
	 * Job type constant. Turns the given degrees. Positive means anti-clockwise.
	 */
	static final int TURN=4;
	/**
	 * Job type constant. Goes to the desired location walking through all other locations on a specific host.
	 */
	static final int GOTO_ONHOST=5;
	/**
	 * Job type constant. Goes to the desired location walking through all other locations on a line.
	 * This job is the remainder of a goto job on a line which didn't have the time to finish.
	 */
	static final int GOTO_ONLINE_UNFINISHED=6;
	/**
	 * Job type constant. Starts walking.
	 */
	static final int GO=7;
	/**
	 * Job type constant. Stops continuing walking.
	 */
	static final int STOP=8;
	/**
	 * Job type constant. Makes the agent sleep (wait).
	 */
	static final int SLEEP=9;
	/**
	 * Job type constant. Wakes the agent up if it sleeps (waits).
	 */
	static final int WAKE=10;
}
