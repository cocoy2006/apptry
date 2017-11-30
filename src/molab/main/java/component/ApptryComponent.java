package molab.main.java.component;

import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Deployment;
import molab.main.java.entity.T_Developer;
import molab.main.java.entity.T_Emulator;
import molab.main.java.entity.T_Server;

public class ApptryComponent {

	private T_Application application;
	private T_Deployment deployment;
	private T_Developer developer;
	private T_Emulator emulator;
	private T_Server server;

	public T_Application getApplication() {
		return application;
	}

	public void setApplication(T_Application application) {
		this.application = application;
	}

	public T_Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(T_Deployment deployment) {
		this.deployment = deployment;
	}
	
	/**
	 * @return the developer
	 */
	public T_Developer getDeveloper() {
		return developer;
	}

	/**
	 * @param developer the developer to set
	 */
	public void setDeveloper(T_Developer developer) {
		this.developer = developer;
	}
	
	public T_Emulator getEmulator() {
		return emulator;
	}

	public void setEmulator(T_Emulator emulator) {
		this.emulator = emulator;
	}

	public T_Server getServer() {
		return server;
	}

	public void setServer(T_Server server) {
		this.server = server;
	}

}
