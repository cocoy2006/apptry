package molab.main.java.entity;

@SuppressWarnings("serial")
public class T_Deployment extends BaseEntity {
	private int id;
	private int dispatcher_id;
	private int application_id;
	private int emulator_id;
	private String state;

	public T_Deployment() {}
	
	public T_Deployment(int dispatcher_id, int application_id, int emulator_id, String state) {
		this.dispatcher_id = dispatcher_id;
		this.application_id = application_id;
		this.emulator_id = emulator_id;
		this.state = state;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dispatcher_id
	 */
	public int getDispatcher_id() {
		return dispatcher_id;
	}

	/**
	 * @param dispatcherId the dispatcher_id to set
	 */
	public void setDispatcher_id(int dispatcherId) {
		dispatcher_id = dispatcherId;
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
	 * @return the emulator_id
	 */
	public int getEmulator_id() {
		return emulator_id;
	}

	/**
	 * @param emulatorId the emulator_id to set
	 */
	public void setEmulator_id(int emulatorId) {
		emulator_id = emulatorId;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

}