package molab.main.java.util.log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BehaviorLogs implements ILogs {
	
	private static final Logger LOG = Logger.getLogger(BehaviorLogs.class.getName());
	private static Map<String, ILog> logs = new HashMap<String, ILog>();

	public void addLog(String key, ILog log) {
		LOG.log(Level.INFO, "Apptry: Behavior log with E" + key + " added.");
		logs.put(key, log);
	}

	public ILog getLog(String key) {
		// TODO Auto-generated method stub
		return logs.get(key);
	}
	
	public void removeLog(String key) {
		if(logs.containsKey(key)) {
			logs.remove(key);
			LOG.log(Level.INFO, "Apptry: Behavior log with E" + key + " removed.");
		}
	}

}
