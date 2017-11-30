package molab.main.java.web;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import molab.main.java.component.ApptryComponent;
import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Developer;
import molab.main.java.entity.T_Emulator;
import molab.main.java.entity.T_Order;
import molab.main.java.entity.T_Product;
import molab.main.java.entity.T_Server;
import molab.main.java.util.Apptry;
import molab.main.java.util.Constants;
import molab.main.java.util.DB;
import molab.main.java.util.HC;
import molab.main.java.util.Init;
import molab.main.java.util.Status;
import molab.main.java.util.log.BehaviorLog;
import molab.main.java.util.mail.Mail;
import molab.main.java.util.timer.TimeoutTimer;

import org.apache.commons.httpclient.NameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class Index implements ServletContextAware {
	
	@RequestMapping(value = "/")
	public String main() {
		return "home";
	}
	
	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/screen")
	public String screen() {
		return "screen";
	}
	
	@RequestMapping(value = "/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping(value = "/calculator")
	public String calculator() {
		return "calculator";
	}
	
	@RequestMapping(value = "/signup")
	public String signup() {
		return "signup";
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/dashboard")
	public String dashboard() {
		return "snippet/dashboard";
	}
	
	@RequestMapping(value = "/buy")
	public String buy() {
		return "snippet/buy";
	}
	
	@RequestMapping(value = "/apps")
	public String apps() {
		return "apps";
	}
	
	@RequestMapping(value = "/pwdreset")
	public String pwdreset() {
		return "pwdreset";
	}
	
	@RequestMapping(value = "/{applicationId}")
	public ModelAndView apptry(HttpServletRequest request, HttpSession session, @PathVariable int applicationId) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("applicationId", applicationId);
		// get information from apptryDB server
		String apptryDatabaseHost = Apptry.getDatabaseHost();		
		String url = apptryDatabaseHost.concat("/try/").concat(String.valueOf(applicationId));
		String apptry = HC.ajaxGet(url);
		if("".equals(apptry)) {
			mav.setViewName("redirect:screen#".concat(String.valueOf(applicationId)));
			session.setAttribute("MESSAGE", Constants.MESSAGE_EMULATORS_BUSY);
		} else {
			ApptryComponent ac = new Gson().fromJson(apptry, ApptryComponent.class);
			T_Developer developer = ac.getDeveloper();
			int leftClicks = developer.getLeftClicks();
			if(leftClicks <= 0) {
				mav.setViewName("redirect:screen#".concat(String.valueOf(applicationId)));
				session.setAttribute("MESSAGE", Constants.MESSAGE_LEFT_CLICKS_NOT_ENOUGH);
			} else {
				mav.setViewName("apptry");
				T_Application application = ac.getApplication();				
				T_Emulator emulator = ac.getEmulator();
				T_Server server = ac.getServer();
				
				mav.addObject("packageName", application.getPackageName());
				mav.addObject("startActivity", application.getStartActivity());
				mav.addObject("serialNumber", emulator.getSerialNumber());
				
				session.setAttribute("packageName", application.getPackageName());
				session.setAttribute("serialNumber", emulator.getSerialNumber());
				session.setAttribute("ipAddress", server.getIpAddress());
				session.setAttribute("port", server.getPort());
				// write objects to session
				session.setAttribute("application", application);
				session.setAttribute("developer", developer);
				session.setAttribute("emulator", emulator);
				session.setAttribute("server", server);
				// create behavior timer
				String emulatorId = String.valueOf(emulator.getId());
				BehaviorLog behavior = new BehaviorLog(request, developer, applicationId, emulator);
				Init.getBehaviorLogs().addLog(emulatorId, behavior);
				TimeoutTimer timer = new TimeoutTimer(emulatorId);
				Init.getTimeoutTimers().addTimer(emulatorId, timer);
			}
		}
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/logout")
	public String logout(/*HttpServletRequest request, HttpServletResponse response, */HttpSession session) {
		T_Emulator emulator = (T_Emulator) session.getAttribute("emulator");
		String emulatorId = String.valueOf(emulator.getId());
		// handle behavior log and remove it from logs
		BehaviorLog bl = (BehaviorLog) Init.getBehaviorLogs().getLog(emulatorId);
		if(bl != null) {
			bl.logout();
			Init.getBehaviorLogs().removeLog(emulatorId);
		}
		// handle timeout timer and remove it from timers
		TimeoutTimer tt = (TimeoutTimer) Init.getTimeoutTimers().getTimer(emulatorId);
		if(tt != null) {
			tt.cancel();
			Init.getTimeoutTimers().removeTimer(emulatorId);
		}
		// release emulator, shift its state from busy to idle
		DB.releaseEmulator(emulator);
		return String.valueOf(Status.SUCCESS);
	}
	
	@ResponseBody
	@RequestMapping(value = "/execute/")
	public String execute(HttpServletRequest request, HttpSession session) {
		String ipAddress = session.getAttribute("ipAddress").toString();
		String serverPort = session.getAttribute("port").toString();
		String serialNumber = session.getAttribute("serialNumber").toString();
		String command = request.getParameter("command");
		// send request and receive response
		String url = "http://" + ipAddress;
		if(!"".equals(serverPort)) {
			url += ":" + serverPort;
		}
		url += "/apptryADB/execute/";
		NameValuePair[] params = {
				new NameValuePair("serialNumber", serialNumber),
				new NameValuePair("command", command)
		};
		return HC.ajaxPost(url, params);
	}
	
	@ResponseBody
	@RequestMapping(value = "/hasAttribute/{attr}/")
	public String hasAttribute(HttpSession session, @PathVariable String attr) {
		if(session.getAttribute(attr) != null) {
			return new Gson().toJson(session.getAttribute(attr));
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/removeAttribute/{attr}/")
	public String removeAttribute(HttpSession session, @PathVariable String attr) {
		if(session.getAttribute(attr) != null) {
			session.removeAttribute(attr);
			return String.valueOf(Status.SUCCESS);
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/hasApplication/")
	public String hasApplication(HttpSession session) {
		if(session.getAttribute("application") != null) {
			return new Gson().toJson((T_Application) session.getAttribute("application"));
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/sendIframe/")
	public String sendIframe(HttpServletRequest request) {
		try {
			// get developer's information from session
			T_Developer developer = (T_Developer) request.getSession().getAttribute("developer");
			String username = developer.getUsername();
			String email = developer.getEmail();
			// get parameters from request
			String applicationId = request.getParameter("applicationId");
			String packageName = request.getParameter("packageName");
			String version = request.getParameter("version");
			
			String fromUrl = Apptry.parseFromUrl(request.getRequestURL(), request.getServletPath());
			Mail mail = new Mail(username, email, fromUrl);
			mail.sendIframe(applicationId, packageName, version);
			return new Gson().toJson(Status.SUCCESS);
		} catch(Exception e) {
			return new Gson().toJson(Status.ERROR_SYSTEM);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/pay/{productId}/")
	public String pay(HttpServletRequest request, @PathVariable int productId) {
		// build order and return order_id
		T_Order order = new T_Order();
		long orderId = Long.parseLong(Apptry.getTimestamp(new Date()).concat(Apptry.randomInteger(Constants.DEFAULT_ORDER_RANDOM_LENGTH)));
		order.setId(orderId);
		order.setProduct_id(productId);
		T_Developer developer = (T_Developer) request.getSession().getAttribute("developer");
		order.setDeveloper_id(developer.getId());
		order.setTime(System.currentTimeMillis());
		order.setState(Status.ORDER_WAITING);
		long returnId = DB.createOrder(order);
		if(orderId == returnId) {
			// load product
			T_Product product = DB.loadProduct(productId);
			// build pay request and waiting for yeepay's result
			String fromUrl = Apptry.parseFromUrl(request);
			String p2_Order = String.valueOf(orderId);
			String p3_Amt = String.valueOf(product.getAmount());
			String p5_Pid = product.getName();
			String p8_Url = Apptry.getYeepayCallback();
			String url = Apptry.getYeepayPay();
			return url.concat("?fromUrl=").concat(fromUrl).concat("&p2_Order=").concat(p2_Order)
				.concat("&p3_Amt=").concat(p3_Amt).concat("&p5_Pid=").concat(p5_Pid).concat("&p8_Url=").concat(p8_Url);
		}
		return "";
	}
	
	@RequestMapping(value = "/pay/callback/{orderId}/")
	public void payCallback() {
		
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
