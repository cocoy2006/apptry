package molab.main.java.util.timer;


public interface ITimers {

	public void addTimer(String key, ITimer iTimer);
	
	public ITimer getTimer(String key);
	
	public void removeTimer(String key);
	
}
