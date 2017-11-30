package molab.main.java.util.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import molab.main.java.entity.T_Emulator;
import molab.main.java.util.Apptry;
import molab.main.java.util.DB;
import molab.main.java.util.Init;
import molab.main.java.util.log.BehaviorLog;

public class TimeoutTimer extends ITimer {
	
	private static final Logger LOG = Logger.getLogger(TimeoutTimer.class.getName());
	private Timer timer;
	
	public TimeoutTimer(final String emulatorId) {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				LOG.log(Level.INFO, "Timeout happen,  " + timer.toString() + " is going to run.");
				// stop timeout timer first.
				timer.cancel();
				BehaviorLog bl = (BehaviorLog) Init.getBehaviorLogs().getLog(emulatorId);
				if(bl != null) {
					// release emulator
					T_Emulator emulator = bl.getEmulator();
					DB.releaseEmulator(emulator);
					// write behavior log file
					bl.logout();
					// remove behavior log from logs
					Init.getBehaviorLogs().removeLog(emulatorId);
				}
				// remove timeout timer from timers
				Init.getTimeoutTimers().removeTimer(emulatorId);
			}
			
		}, Apptry.getTrialTime() * 1000L + 5000); // plus 5s in case mistake
	}
	
	public void cancel() {
		timer.cancel();
	}
}
