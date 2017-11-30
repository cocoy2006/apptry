package molab.main.java.component;

import molab.main.java.entity.T_Application;

public class VisitListComponent {

	private T_Application application;
	private long totalClicks;
	private long monthClicks;
	
	/**
	 * @return the application
	 */
	public T_Application getApplication() {
		return application;
	}
	/**
	 * @param application the application to set
	 */
	public void setApplication(T_Application application) {
		this.application = application;
	}
	/**
	 * @return the totalClicks
	 */
	public long getTotalClicks() {
		return totalClicks;
	}
	/**
	 * @param totalClicks the totalClicks to set
	 */
	public void setTotalClicks(long totalClicks) {
		this.totalClicks = totalClicks;
	}
	/**
	 * @return the monthClicks
	 */
	public long getMonthClicks() {
		return monthClicks;
	}
	/**
	 * @param monthClicks the monthClicks to set
	 */
	public void setMonthClicks(long monthClicks) {
		this.monthClicks = monthClicks;
	}
	
}