package molab.test.java.util;

import molab.main.java.util.useragent.UserAgentParser;

public class UserAgentTester {

	private static String a = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/32.0.1700.107 Chrome/32.0.1700.107 Safari/537.36";
	private static String d = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserAgentParser uapa = new UserAgentParser(a);
		System.out.println(uapa.getBrowserOperatingSystem());
		System.out.println(uapa.getBrowserName());
		System.out.println(uapa.getBrowserVersion());
	}

}
