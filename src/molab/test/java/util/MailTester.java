package molab.test.java.util;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import molab.main.java.util.mail.MailSenderInfo;
import molab.main.java.util.mail.SimpleMailSender;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class MailTester {
	
	private static final Logger LOG = Logger.getLogger(MailTester.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		
		mailInfo.setMailServerHost("smtp.exmail.qq.com");
		mailInfo.setMailServerPort("465");
		mailInfo.setValidate(true);
		mailInfo.setUserName("no-reply@apptry.cn");
		mailInfo.setPassword("apptry123*()");// 您的邮箱密码
		mailInfo.setFromAddress("no-reply@apptry.cn");
		mailInfo.setToAddress("cocoy2006@sina.com");
		mailInfo.setSubject("Apptry");

		
		/* first, we init the runtime engine.  Defaults are fine. */

        Velocity.init();

        /* lets make a Context and put data into it */

        VelocityContext context = new VelocityContext();

        context.put("user", "APPTRY");
		context.put("content", "Hello");

        /* lets render a template */

        StringWriter w = new StringWriter();

        Velocity.mergeTemplate("./m.vm", "UTF-8", context, w );

        String content = w.toString();
		
		
		mailInfo.setContent(content);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
		
		LOG.log(Level.INFO, content);
	}

}
