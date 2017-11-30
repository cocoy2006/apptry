package molab.main.java.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import molab.main.java.component.VisitListComponent;
import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Deployment;
import molab.main.java.entity.T_Developer;
import molab.main.java.entity.T_Dispatcher;
import molab.main.java.entity.T_Emulator;
import molab.main.java.entity.T_Order;
import molab.main.java.entity.T_Product;
import molab.main.java.entity.T_Visit;

import org.apache.commons.httpclient.NameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DB {
	
	private static final Logger LOG = Logger.getLogger(DB.class.getName());
	private static ServletContext servletContext;

	public static void setServletContext(ServletContext sc) {
		servletContext = sc;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
		
	public static T_Application loadApplication(int id) {
		String url = Apptry.getDatabaseHost().concat("/application/")
			.concat(String.valueOf(id)).concat("/");
		String result = HC.ajaxGet(url);
		if(result != "") {
			T_Application application = new Gson().fromJson(result, T_Application.class);
			return application;
		}
		return null;
	}
	
	public static List<T_Application> loadApplications(int developerId) {
		String url = Apptry.getDatabaseHost().concat("/loadApplications/")
			.concat(String.valueOf(developerId)).concat("/");
		String result = HC.ajaxGet(url);
		if(result != "") {
			List<T_Application> list = new Gson().fromJson(result, new TypeToken<List<T_Application>>(){}.getType());
			return list;
		}
		return null;
	}
	
	public static int createApplication(T_Application application) {
		String url = Apptry.getDatabaseHost().concat("/application/create/");
		NameValuePair[] params = {
				new NameValuePair("md5", application.getMd5()),
				new NameValuePair("name", application.getName()),
				new NameValuePair("aliasName", application.getAliasName()),
				new NameValuePair("size", String.valueOf(application.getSize())),
				new NameValuePair("packageName", application.getPackageName()),
				new NameValuePair("version", application.getVersion()),
				new NameValuePair("os", application.getOs()),
				new NameValuePair("startActivity", application.getStartActivity())
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}
	
	public static int checkApplication(int developerId, String packageName) {
		String url = Apptry.getDatabaseHost().concat("/application/check/");
		NameValuePair[] params = {
				new NameValuePair("developerId", String.valueOf(developerId)),
				new NameValuePair("packageName", packageName)
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}
	
	public static String removeApplication(int id) {
		String url = Apptry.getDatabaseHost().concat("/application/remove/").concat(String.valueOf(id)).concat("/");
		String result = HC.ajaxGet(url);
		return result;
	}
	
	public static int signup(T_Developer user, String fromUrl) throws Exception {
		String url = Apptry.getDatabaseHost().concat("/user/signup/");
		NameValuePair[] params = {
				new NameValuePair("r_u", user.getUsername()),
				new NameValuePair("r_p", user.getPassword()),
				new NameValuePair("r_email", user.getEmail()),
				new NameValuePair("r_left_clicks", String.valueOf(user.getLeftClicks())),
				new NameValuePair("r_register_time", String.valueOf(user.getRegisterTime())),
				new NameValuePair("r_expiration", String.valueOf(user.getExpiration())),
				new NameValuePair("r_is_renewal", String.valueOf(user.getIsRenewal())),
				new NameValuePair("r_state", String.valueOf(user.getState()))
		};
		String result = HC.ajaxPost(url, params);
//		int data = Integer.parseInt(result);
//		if(data == Status.SUCCESS) {
//			Mail mail = new Mail(user.getUsername(), user.getEmail(), fromUrl);
//			mail.sendActivation();
//		}
//		return data;
		return Integer.parseInt(result);
	}
	
	public static int login(String username, String password) {
		String url = Apptry.getDatabaseHost().concat("/user/login");
		NameValuePair[] params = {
				new NameValuePair("l_u", username),
				new NameValuePair("l_p", password)
		};
		String result = HC.ajaxPost(url, params);
		return Integer.parseInt(result);
	}
	
	public static int checkUsername(String username) {
		String url = Apptry.getDatabaseHost().concat("/user/check/").concat(username);
		String result = HC.ajaxGet(url);
		return Integer.parseInt(result);
	}
	
	public static T_Developer loadDeveloper(String username) {
		String url = Apptry.getDatabaseHost().concat("/user/").concat(username);
		String result = HC.ajaxGet(url);
		T_Developer developer = null;
		if(result != "") {
			developer = new Gson().fromJson(result, T_Developer.class);
		}
		return developer;
	}
	
	public static int updateDeveloper(T_Developer developer) {
		String url = Apptry.getDatabaseHost().concat("/user/update/");
		NameValuePair[] params = {
				new NameValuePair("id", String.valueOf(developer.getId())),
				new NameValuePair("password", developer.getPassword()),
				new NameValuePair("leftClicks", String.valueOf(developer.getLeftClicks()))
		};
		String result = HC.ajaxPost(url, params);
		return Integer.parseInt(result);
	}
	
	public static int activeDeveloper(String username, String email) {
		String url = Apptry.getDatabaseHost().concat("/user/active/")
			.concat(username).concat("/").concat(email).concat("/");
		String result = HC.ajaxGet(url);
		return Integer.parseInt(result);
	}
	
	public static int createDeployment(T_Deployment deployment) {
		String url = Apptry.getDatabaseHost().concat("/deployment/create/");
		NameValuePair[] params = {
				new NameValuePair("dispatcherId", String.valueOf(deployment.getDispatcher_id())),
				new NameValuePair("applicationId", String.valueOf(deployment.getApplication_id())),
				new NameValuePair("emulatorId", String.valueOf(deployment.getEmulator_id())),
				new NameValuePair("state", deployment.getState())
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}
	
	public static int updateDeployment(T_Deployment deployment) {
		String url = Apptry.getDatabaseHost().concat("/deployment/update/");
		NameValuePair[] params = {
				new NameValuePair("id", String.valueOf(deployment.getId())),
				new NameValuePair("state", deployment.getState())
		};
		String result = HC.ajaxPost(url, params);
		return Integer.parseInt(result);
	}
	
	public static int createDispatcher(T_Dispatcher dispatcher) {
		String url = Apptry.getDatabaseHost().concat("/dispatcher/create/");
		NameValuePair[] params = {
				new NameValuePair("developerId", String.valueOf(dispatcher.getDeveloper_id())),
				new NameValuePair("applicationId", String.valueOf(dispatcher.getApplication_id())),
				new NameValuePair("time", String.valueOf(dispatcher.getTime())),
				new NameValuePair("state", String.valueOf(dispatcher.getState()))
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}
	
	public static int updateDispatcher(T_Dispatcher dispatcher) {
		String url = Apptry.getDatabaseHost().concat("/dispatcher/update/");
		NameValuePair[] params = {
				new NameValuePair("id", String.valueOf(dispatcher.getId())),
				new NameValuePair("state", String.valueOf(dispatcher.getState()))
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}
	
	public static int occupyEmulator(T_Emulator emulator) {
		LOG.log(Level.INFO, "Apptry: Shift emulator @" + emulator.getSerialNumber() + "'s state to BUSY.");
		emulator.setState(Status.BUSY);
		return updateEmulator(emulator);
	}
	
	public static int releaseEmulator(T_Emulator emulator) {
		LOG.log(Level.INFO, "Apptry: Shift emulator @" + emulator.getSerialNumber() + "'s state to IDLE.");
		emulator.setState(Status.IDLE);
		return updateEmulator(emulator);
	}
	
	public static int updateEmulator(T_Emulator emulator) {
		String url = Apptry.getDatabaseHost().concat("/emulator/update/");
		NameValuePair[] params = {
				new NameValuePair("id", String.valueOf(emulator.getId())),
				new NameValuePair("monthClicks", String.valueOf(emulator.getMonthClicks())),
				new NameValuePair("state", String.valueOf(emulator.getState()))
		};
		String result = HC.ajaxPost(url, params);
		return Integer.parseInt(result);
	}
	
	public static T_Product loadProduct(int id) {
		String url = Apptry.getDatabaseHost().concat("/product/")
			.concat(String.valueOf(id)).concat("/");
		String result = HC.ajaxGet(url);
		if(result != "") {
			T_Product product = new Gson().fromJson(result, T_Product.class);
			return product;
		}
		return null;
	}
	
	public static long createOrder(T_Order order) {
		String url = Apptry.getDatabaseHost().concat("/order/create/");
		NameValuePair[] params = {
				new NameValuePair("id", String.valueOf(order.getId())),
				new NameValuePair("productId", String.valueOf(order.getProduct_id())),
				new NameValuePair("developerId", String.valueOf(order.getDeveloper_id())),
				new NameValuePair("time", String.valueOf(order.getTime())),
				new NameValuePair("state", String.valueOf(order.getState()))
		};
		String id = HC.ajaxPost(url, params);
		return Long.parseLong(id);
	}
	
	public static T_Order loadOrder(long id) {
		String url = Apptry.getDatabaseHost().concat("/order/").concat(String.valueOf(id)).concat("/");
		String result = HC.ajaxGet(url);
		if(result != "") {
			T_Order order = new Gson().fromJson(result, T_Order.class);
			return order;
		}
		return null;
	}
	
	public static List<T_Order> loadOrders(int developerId) {
		String url = Apptry.getDatabaseHost().concat("/order/loadAll/")
			.concat(String.valueOf(developerId)).concat("/");
		String result = HC.ajaxGet(url);
		if(result != "") {
			List<T_Order> list = new Gson().fromJson(result, new TypeToken<List<T_Order>>(){}.getType());
			return list;
		}
		return null;
	}
	
	public static int updateOrder(String url) {
		url = Apptry.getDatabaseHost().concat(url);
		String result = HC.ajaxGet(url);
		return Integer.parseInt(result);
	}
	
	public static int updateOrder(T_Order order) {
		String url = Apptry.getDatabaseHost().concat("/order/update/").concat(String.valueOf(order.getId())).concat("/")
			.concat(String.valueOf(order.getState())).concat("/");
		String result = HC.ajaxGet(url);
		return Integer.parseInt(result);
	}
	
	public static int createVisit(T_Visit visit) {
		String url = Apptry.getDatabaseHost().concat("/visit/create/");
		NameValuePair[] params = {
				new NameValuePair("applicationId", String.valueOf(visit.getApplication_id())),
				new NameValuePair("ipAddress", visit.getIpAddress()),
				new NameValuePair("macAddress", visit.getMacAddress()),
				new NameValuePair("fromUrl", visit.getFromUrl()),
				new NameValuePair("loginTime", String.valueOf(visit.getLoginTime())),
				new NameValuePair("durationTime", String.valueOf(visit.getDurationTime())),
				new NameValuePair("userAgent", visit.getUserAgent())
		};
		String id = HC.ajaxPost(url, params);
		return Integer.parseInt(id);
	}

	public static List<VisitListComponent> loadVisitList(int developerId) {
		String url = Apptry.getDatabaseHost().concat("/visit/list/");
		NameValuePair[] params = {
				new NameValuePair("developerId", String.valueOf(developerId))
		};
		String result = HC.ajaxPost(url, params);
		if(result != "") {
			List<VisitListComponent> list = new Gson().fromJson(result, new TypeToken<List<VisitListComponent>>(){}.getType());
			return list;
		}
		return null;
	}
	
	public static List<T_Visit> loadVisitDetail(int applicationId) {
		String url = Apptry.getDatabaseHost().concat("/visit/detail/");
		NameValuePair[] params = {
				new NameValuePair("applicationId", String.valueOf(applicationId))
		};
		String result = HC.ajaxPost(url, params);
		if(result != "") {
			List<T_Visit> list = new Gson().fromJson(result, new TypeToken<List<T_Visit>>(){}.getType());
			return list;
		}
		return null;
	}
}