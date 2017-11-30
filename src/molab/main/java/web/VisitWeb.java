package molab.main.java.web;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import molab.main.java.component.VisitListComponent;
import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Visit;
import molab.main.java.util.Apptry;
import molab.main.java.util.DB;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/visit")
public class VisitWeb implements ServletContextAware {
	
	@RequestMapping(value = "/list/{developerId}/")
	public ModelAndView list(@PathVariable int developerId) {
		ModelAndView mav = new ModelAndView("snippet/visit_list");
		List<VisitListComponent> list = DB.loadVisitList(developerId);
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping(value = "/detail/{applicationId}/")
	public ModelAndView detail(@PathVariable int applicationId) {
		ModelAndView mav = new ModelAndView("snippet/visit_detail");
		T_Application application = DB.loadApplication(applicationId);
		mav.addObject("application", application);
		List<T_Visit> list = DB.loadVisitDetail(applicationId);
		if(list != null && list.size() > 0) {
			Map<String, T_Visit> visits = new TreeMap<String, T_Visit>();
			for(T_Visit visit : list) {
				// format login time
				long time = visit.getLoginTime();
				visits.put(Apptry.getLocaleTimestamp(time), visit);
				// format duration time
				visit.setDurationTime(visit.getDurationTime() / 1000);
			}
			mav.addObject("visits", visits);
		}
		return mav;
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
