package molab.main.java.web;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import molab.main.java.entity.T_Order;
import molab.main.java.entity.T_Product;
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
@RequestMapping(value = "/order")
public class OrderWeb implements ServletContextAware {
	
//	private static final Logger LOG = Logger.getLogger(OrderWeb.class.getName());
	
	@RequestMapping(value = "/loadAll/{developerId}/")
	public ModelAndView loadOrders(@PathVariable int developerId) {
		ModelAndView mav = new ModelAndView("snippet/order_list");
		List<T_Order> list = DB.loadOrders(developerId);
		mav.addObject("list", list);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/continue/{id}/")
	public String continuePay(HttpServletRequest request, @PathVariable long id) {
		T_Order order = DB.loadOrder(id);
		if(order != null) {
			int productId = order.getProduct_id();
			T_Product product = DB.loadProduct(productId);
			if(product != null) {
				String fromUrl = Apptry.parseFromUrl(request);
				String p2_Order = String.valueOf(order.getId());
				String p3_Amt = String.valueOf(product.getAmount());
				String p5_Pid = product.getName();
				String p8_Url = Apptry.getYeepayCallback();
				String url = Apptry.getYeepayPay();
				return url.concat("?fromUrl=").concat(fromUrl).concat("&p2_Order=").concat(p2_Order)
					.concat("&p3_Amt=").concat(p3_Amt).concat("&p5_Pid=").concat(p5_Pid).concat("&p8_Url=").concat(p8_Url);
			}
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/update/{id}/{newState}/")
	public String cancelOrder(HttpServletRequest request) {
		int result = DB.updateOrder(request.getServletPath());
		return new Gson().toJson(result);
	}
	
	public void setServletContext(ServletContext sc) {
		Apptry.setServletContext(sc);
	}
	
}
