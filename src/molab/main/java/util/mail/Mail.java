package molab.main.java.util.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import molab.main.java.util.SpringContextUtil;

import org.springframework.mail.SimpleMailMessage;

public class Mail {
	
	private static final Logger LOG = Logger.getLogger(Mail.class.getName());

//	private static String STMP = "smtp.exmail.qq.com";
//	private static String PORT = "465";
//	private static String USERNAME = "no-reply@apptry.cn";
//	private static String PASSWORD = "apptry123*()";
//	private static String MAIL_ADDRESS = "no-reply@apptry.cn";
//	private static String MAIL_TITLE = "Apptry";
	
//	private static final String BLANK = "&nbsp;&nbsp;&nbsp;&nbsp;";
	
//	private int id;
	private String username;
	private String email;
	private String fromUrl;
	
	public Mail() {}
	
	public Mail(String username, String email, String fromUrl) {
//		this.id = id;
		this.username = username;
		this.email = email;
		this.fromUrl = fromUrl;
//		initParameters();
	}
	
	public Mail(String username, String email) {
		this.username = username;
		this.email = email;
//		initParameters();
	}
	
//	private void initParameters() {
//		Properties props = Apptry.getProperties();
//		STMP = props.getProperty("mailStmp");
//		PORT = props.getProperty("mailPort");
//		USERNAME = props.getProperty("mailUsername");
//		PASSWORD = props.getProperty("mailPassword");
//		MAIL_ADDRESS = props.getProperty("mailAddress");
//		MAIL_TITLE = props.getProperty("mailTitle");
//	}
	
	public boolean sendIframe(String applicationId, String packageName, String version) throws Exception {
		try {
			// data to fill
			String src = parseUrl().concat("index#").concat(applicationId);
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("packageName", packageName);
	        model.put("version", version);
	        model.put("src", src);
	        // to address
	        String to = username.concat("<").concat(email).concat(">");
	        // send email
	        send(to, "iframe.vm", model);
	        LOG.log(Level.INFO, "Iframe邮件成功发送给开发者:" + username);
			return true;
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	public boolean sendActivation() throws Exception {
		try {
			String url = parseUrl().concat("user/active/").concat(username).concat("/").concat(email).concat("/");
	        Map<String, Object> model = new HashMap<String, Object>();
	        model.put("url", url);
	        String to = username.concat("<").concat(email).concat(">");
	        send(to, "activation.vm", model);
	        LOG.log(Level.INFO, "激活邮件成功发送给开发者:" + username);
	        return true;
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	public boolean sendNewPassword(String newPassword) throws Exception {
		try {
	        Map<String, Object> model = new HashMap<String, Object>();
	        model.put("newPassword", newPassword);
	        String to = username.concat("<").concat(email).concat(">");
	        send(to, "pwdreset.vm", model);
	        LOG.log(Level.INFO, "新密码成功发送给开发者:" + username);
	        return true;
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			throw e;
		}
	}
	
	public boolean send(String to, String vm, Map<String, Object> model) throws Exception {
		try {
			SimpleMailMessage message = (SimpleMailMessage) SpringContextUtil.getBean("mailMessage");
			message.setSubject("Apptry");
	        message.setTo(to);
	        MailEngine engine = (MailEngine) SpringContextUtil.getBean("mailEngine");
	        engine.send(message, vm, model);
			return true;
		} catch(Exception e) {
			LOG.log(Level.SEVERE, "邮件发送失败:" + e.getMessage());
			throw e;
		}
	}
	
//	public boolean send() {
//		MailSenderInfo mailInfo = createSender(MAIL_TITLE, content());
//		SimpleMailSender sms = new SimpleMailSender();
//		sms.sendHtmlMail(mailInfo);
		
//		SimpleMailMessage message = (SimpleMailMessage) SpringContextUtil.getBean("mailMessage");
//        message.setTo("Apptry<cocoy2006@sina.com>");
//        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("url", "www.apptry.cn");
//        MailEngine engine = (MailEngine) SpringContextUtil.getBean("mailEngine");
//        engine.send(message, "activation.vm", model);
//		return true;
//	}
	
//	public void send(MailSenderInfo mailInfo) {
//		SimpleMailSender sms = new SimpleMailSender();
//		sms.sendHtmlMail(mailInfo);
//	}
	
//	public String passwordResetSender() {
//		String newPassword = randomString(8);
//		if(newPassword == null) return null;
//		send(createSender(MAIL_TITLE, passwordResetContent(newPassword)));
//		return newPassword;
//	}
	
//	private String content() {
//		String url = parseUrl()//.concat("/").concat(String.valueOf(id))
//			.concat("/").concat(username).concat("/").concat(email).concat("/");
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append("Hi, <font color='#005EAC'>").append(username).append("</font><br/><br/>")
//			.append(BLANK).append("感谢您选择").append(MAIL_TITLE).append("<br/><br/>")
//			.append(BLANK).append("请您点击以下链接以激活您的账号:<br/>")
//			.append(BLANK).append("<a href='").append(url).append("' color='red'>").append(url).append("</a><br/><br/>")
//			.append(BLANK).append("(如果无法点击，请完整的复制上面地址到您的浏览器地址栏中并打开)<br/><br/>")
//			.append(BLANK).append("您必须先验证通行证电子邮箱地址，方可保证账号安全，日后可以享有邮件找回密码等服务。<br/><br/>")
//			.append(BLANK).append("【注意】本邮件为系统自动发送的邮件，请不要回复本邮件。<br/><br/>");
//		return sb.toString();
//	}
	
	private String parseUrl() {
		String[] parts = fromUrl.split("/"); 
		return parts[0] + "//" + parts[2] + "/" + parts[3] + "/";
	}
	
//	private String passwordResetContent(String newPassword) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("Hi, <font color='#005EAC'>").append(username).append("</font><br/><br/>")
//			.append(BLANK).append("感谢您选择").append(MAIL_TITLE).append("<br/><br/>")
//			.append(BLANK).append("您的新密码是:<br/>")
//			.append(BLANK).append(newPassword)
//			.append(BLANK).append("(请您注意保管您的密码，及时登录系统修改)<br/><br/>")
//			.append(BLANK).append("【注意】本邮件为系统自动发送的邮件，请不要回复本邮件。<br/><br/>");
//		return sb.toString();
//	}
	
//	public MailSenderInfo createSender(String subject, String content) {
//		MailSenderInfo msi = new MailSenderInfo();
//		msi.setMailServerHost(STMP); 
//		msi.setMailServerPort(PORT);
//		msi.setValidate(true);
//		msi.setUserName(USERNAME);
//		msi.setPassword(PASSWORD);
//		msi.setFromAddress(MAIL_ADDRESS);
//		msi.setToAddress(email);
//		msi.setSubject(subject);
//		msi.setContent(content);
//		return msi;
//	}
}
