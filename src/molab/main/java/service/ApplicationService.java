package molab.main.java.service;

import java.util.List;

import molab.main.java.component.DispatcherEmulatorComponent;
import molab.main.java.util.Apptry;
import molab.main.java.util.DB;
import molab.main.java.util.HC;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApplicationService {

	public static String remove(int id, String packageName) {
		// remove information from t_application, t_dispatcher and t_deployment first to ensure process complete.
		String result = DB.removeApplication(id);
		// load emulators where application belongs to
		String url = Apptry.getDatabaseHost().concat("/dispatcher/loadEmulatorsWithApplicationId/").concat(String.valueOf(id));
		List<DispatcherEmulatorComponent> emulators 
			= new Gson().fromJson(HC.ajaxGet(url), new TypeToken<List<DispatcherEmulatorComponent>>(){}.getType());
		// uninstall from emulator one by one
		for(DispatcherEmulatorComponent emulator : emulators) {
			Apptry.uninstall(packageName, emulator);
		}
		return result;
	}
}
