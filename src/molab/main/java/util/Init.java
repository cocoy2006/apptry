package molab.main.java.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import molab.main.java.util.log.BehaviorLogs;
import molab.main.java.util.log.ILogs;
import molab.main.java.util.timer.ITimers;
import molab.main.java.util.timer.TimeoutTimers;

public class Init {
	
	private static final Logger LOG = Logger.getLogger(Init.class.getName());
	private static BehaviorLogs logs = new BehaviorLogs();
	private static TimeoutTimers timers = new TimeoutTimers();

	public void init() {
		LOG.log(Level.INFO, "Apptry : Executing init, initial behavior logs and timeout timers.");
	}
	
	public static ILogs getBehaviorLogs() {
		return logs;
	}
	
	public static ITimers getTimeoutTimers() {
		return timers;
	}
}
