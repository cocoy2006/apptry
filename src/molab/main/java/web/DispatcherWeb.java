package molab.main.java.web;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import molab.main.java.component.DispatcherEmulatorComponent;
import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Deployment;
import molab.main.java.entity.T_Developer;
import molab.main.java.entity.T_Dispatcher;
import molab.main.java.util.Apptry;
import molab.main.java.util.Constants;
import molab.main.java.util.DB;
import molab.main.java.util.HC;
import molab.main.java.util.Status;
import molab.main.java.util.fileupload.FileUploadListener;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/dispatcher")
public class DispatcherWeb implements ServletContextAware {
	private static final Logger LOG = Logger.getLogger(DispatcherWeb.class.getName());
	
	@RequestMapping(value = "/newUpload")
	public void newUpload(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
			// force set percentDone to 100%
			FileUploadListener listener = (FileUploadListener) session.getAttribute("uploadProgressListener");
			if(listener != null) {
				listener.setPercentDone(100);
			}
			// parse application
			T_Application application = Apptry.parseApplication(file);
			if(application != null) {
				session.setAttribute("application", application);
			} else {
				session.setAttribute("MESSAGE", Constants.MESSAGE_APP_PARSE_EXCEPTION);
				LOG.log(Level.INFO, "Exception happened while parsing app.");
			}
		} catch (Exception e) {
			session.setAttribute("MESSAGE", Constants.MESSAGE_APP_PARSE_EXCEPTION);
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/newSubmit")
	public String newSubmit(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			// load session attributes
			T_Developer developer = (T_Developer) session.getAttribute("developer");
			T_Application application = (T_Application) session.getAttribute("application");
			if(application != null) {
				String aliasName = application.getAliasName();
				String filePath = Apptry.getApkDirectory().concat(aliasName);
				int applicationId = DB.checkApplication(developer.getId(), application.getPackageName());
				if(applicationId > 0) { // application exists under the developer and result is the id
					LOG.log(Level.INFO, "Apptry: application exists and reinstall it.");
					// update application information first
//					DB.updateApplication(application); name, aliasName, size, version, os, startActivity
					// load emulators where application belongs to and reinstall it directly
					String url = Apptry.getDatabaseHost().concat("/dispatcher/loadEmulatorsWithApplicationId/").concat(String.valueOf(applicationId));
					List<DispatcherEmulatorComponent> emulators 
						= new Gson().fromJson(HC.ajaxGet(url), new TypeToken<List<DispatcherEmulatorComponent>>(){}.getType());
					// install apk to emulator one by one
					for(DispatcherEmulatorComponent emulator : emulators) {
						String result = Apptry.install(filePath, emulator);
						// TODO: update deployment
					}
				} else { // application not exist
					LOG.log(Level.INFO, "Apptry: application does not exist and install it as new one.");
					applicationId = DB.createApplication(application);
					if (applicationId != -1) {
						LOG.log(Level.INFO, "Apptry: application id is " + applicationId);
						// build object dispatcher to record the process
						T_Dispatcher dispatcher = new T_Dispatcher(developer.getId(), applicationId, System.currentTimeMillis(), Status.START);
						int dispatcherId = DB.createDispatcher(dispatcher);
						if(dispatcherId != -1) {
							LOG.log(Level.INFO, "Apptry: dispatcher id is " + dispatcherId);
							dispatcher.setId(dispatcherId);
//							session.setAttribute("dispatcherId", dispatcherId);
							// load dispatcher emulators
							int num = Apptry.getDispatcherCount();
//							session.setAttribute("dispatcherCount", num);
							String url = Apptry.getDatabaseHost().concat("/dispatcher/loadEmulators/").concat(String.valueOf(num));
							List<DispatcherEmulatorComponent> emulators 
								= new Gson().fromJson(HC.ajaxGet(url), new TypeToken<List<DispatcherEmulatorComponent>>(){}.getType());
							// install apk to emulator one by one
							for(DispatcherEmulatorComponent emulator : emulators) {
								T_Deployment deployment = new T_Deployment(dispatcherId, applicationId, emulator.getId(), Constants.APK_INSTALLING);
								int id = DB.createDeployment(deployment);
								deployment.setId(id);
								String result = Apptry.install(filePath, emulator);
								deployment.setState(result);
								DB.updateDeployment(deployment);
							}
							dispatcher.setState(Status.END);
							DB.updateDispatcher(dispatcher);
						}
					}
				}
				session.setAttribute("applicationId", applicationId);
				return String.valueOf(applicationId);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/percentDone", method = RequestMethod.GET)
	public String loadPercent(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		FileUploadListener listener = (FileUploadListener) session.getAttribute("uploadProgressListener");
		if(listener == null) {
			return "-1";
		}
		double percent = listener.getPercentDone();
		return String.valueOf(percent);
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
