package molab.main.java.entity;


@SuppressWarnings("serial")
public class T_Visit extends BaseEntity {
	
	private int id;
	private int application_id;
	private String ipAddress;
	private String macAddress;
	private String fromUrl;
	private long loginTime;
	private long durationTime;
	private String userAgent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the application_id
	 */
	public int getApplication_id() {
		return application_id;
	}

	/**
	 * @param applicationId the application_id to set
	 */
	public void setApplication_id(int applicationId) {
		application_id = applicationId;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * @return the fromUrl
	 */
	public String getFromUrl() {
		return fromUrl;
	}

	/**
	 * @param fromUrl the fromUrl to set
	 */
	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	/**
	 * @return the loginTime
	 */
	public long getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the durationTime
	 */
	public long getDurationTime() {
		return durationTime;
	}

	/**
	 * @param durationTime the durationTime to set
	 */
	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}