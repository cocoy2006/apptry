package molab.main.java.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Repository
public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		Object obj = request.getSession().getAttribute("developer");
		if (obj == null) {
			response.sendRedirect("/" + request.getRequestURI().split("/")[1] + "/");
			return false;
		} else {
			return super.preHandle(request, response, handler);
		}
		
	}
	
}
