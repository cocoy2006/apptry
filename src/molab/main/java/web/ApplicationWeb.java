package molab.main.java.web;

import javax.servlet.ServletContext;

import molab.main.java.entity.T_Application;
import molab.main.java.service.ApplicationService;
import molab.main.java.util.Apptry;
import molab.main.java.util.DB;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/application")
public class ApplicationWeb implements ServletContextAware {
	
	@RequestMapping(value = "/{id}/")
	public ModelAndView load(@PathVariable int id) {
		ModelAndView mav = new ModelAndView("snippet/application_detail");
		T_Application application = DB.loadApplication(id);
		mav.addObject("application", application);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/remove/{id}/{packageName}/")
	public String remove(@PathVariable int id, @PathVariable String packageName) {
		String result = ApplicationService.remove(id, packageName);
		return new Gson().toJson(result);
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
