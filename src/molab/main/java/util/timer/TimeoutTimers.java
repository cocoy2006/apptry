package molab.main.java.util.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeoutTimers implements ITimers {

	private static final Logger LOG = Logger.getLogger(TimeoutTimers.class.getName());
	private static Map<String, ITimer> timers = new HashMap<String, ITimer>();
	
	public void addTimer(String key, ITimer timer) {
		LOG.log(Level.INFO, "Apptry : Timeout timer E" + key + " added.");
		timers.put(key, timer);
	}
	
	public ITimer getTimer(String key) {
		return timers.get(key);
	}
	
	public void removeTimer(String key) {
		if(timers.containsKey(key)) {
			timers.remove(key);
			LOG.log(Level.INFO, "Apptry : Timeout timer E" + key + " removed.");
		}
	}

}
