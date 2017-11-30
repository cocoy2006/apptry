package molab.main.java.util.log;

public interface ILogs {
	
	public void addLog(String key, ILog log);
	
	public ILog getLog(String key);
	
	public void removeLog(String key);
}
