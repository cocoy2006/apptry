package molab.main.java.web;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Developer;
import molab.main.java.util.Apptry;
import molab.main.java.util.Constants;
import molab.main.java.util.DB;
import molab.main.java.util.Status;
import molab.main.java.util.mail.Mail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/user")
public class DeveloperWeb implements ServletContextAware {
	
	@RequestMapping(value = "")
	public String index() {
		return "user";
	}
	
	@RequestMapping(value = "/")
	public String indexMain() {
		return "user";
	}
	
	@ResponseBody
	@RequestMapping(value = "/check/{username}")
	public String check(@PathVariable String username) {
		return new Gson().toJson(DB.checkUsername(username));
	}
	
	@ResponseBody
	@RequestMapping(value = "/signup/")
	public String signup(HttpServletRequest request) throws Exception {
		T_Developer developer = new T_Developer();
		developer.setUsername(request.getParameter("r_u"));
		developer.setPassword(request.getParameter("r_p"));
		developer.setEmail(request.getParameter("r_email"));
		developer.setLeftClicks(Status.DEFAULT_LEFT_CLICKS);
		developer.setRegisterTime(System.currentTimeMillis());
		developer.setExpiration(Status.DEFAULT_EXPIRATION);
		developer.setIsRenewal(Status.FALSE);
		developer.setState(Status.USER_NORMAL);
		
		String fromUrl = Apptry.parseFromUrl(request.getRequestURL(), request.getServletPath());
		int result = DB.signup(developer, fromUrl);
		if(result > 0) {
			developer.setId(result);
			request.getSession().setAttribute("developer", developer);
		}
		return new Gson().toJson(result);
	}
	
	@ResponseBody
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request) {
		String username = request.getParameter("l_u");
		String password = request.getParameter("l_p");
		int result = DB.login(username, password);
		if(result == Status.USER_NORMAL) {
			T_Developer developer = DB.loadDeveloper(username);
			request.getSession().setAttribute("developer", developer);
		}
		return new Gson().toJson(result);
	}
	
	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// release developer's session
		session.removeAttribute("developer");
		Apptry.gotoPage(request, response, "home");
	}
	
	@RequestMapping(value = "/active/{username}/{email:.*}/")
	public void active(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String username, @PathVariable("email") String email) {
		int result = DB.activeDeveloper(username, email);
		if(result == Status.SUCCESS) { // active success
			T_Developer developer = DB.loadDeveloper(username);
			request.getSession().setAttribute("developer", developer);
			Apptry.gotoPage(request, response, "user");
		} else { // active fail
			request.getSession().setAttribute("MESSAGE", Constants.MESSAGE_ERROR_SYSTEM);
			Apptry.gotoPage(request, response, "home");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/pwdreset")
	public String pwdreset(HttpServletRequest request) throws Exception {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		int result = Status.SUCCESS;
		T_Developer developer = DB.loadDeveloper(username);
		if(developer != null) { // username exist
			if(developer.getEmail().equalsIgnoreCase(email)) { // email exist and correct
				// build new password
				String newPassword = Apptry.randomString(Constants.DEFAULT_PASSWORD_LENGTH);
				// send to developer's email address
				Mail mail = new Mail(developer.getUsername(), developer.getEmail());
				mail.sendNewPassword(newPassword);
				// update developer
				developer.setPassword(Apptry.encodeMD5Hex(newPassword));
				DB.updateDeveloper(developer);
			} else {
				result = Status.EMAIL_ERROR;
			}
		} else {
			result = Status.USERNAME_ERROR;
		}
		return new Gson().toJson(result);
	}
	
	@RequestMapping(value = "/loadApplications/{id}/")
	public ModelAndView loadApplications(@PathVariable int id) {
		ModelAndView mav = new ModelAndView("snippet/application_list");
		List<T_Application> list = DB.loadApplications(id);
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping(value = "/loadStatisticsList/{id}/")
	public ModelAndView loadStatisticsList(@PathVariable int id) {
		ModelAndView mav = new ModelAndView("snippet/statistics_list");
		List<T_Application> list = DB.loadApplications(id);
		mav.addObject("list", list);
		return mav;
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
