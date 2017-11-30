package molab.main.java.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import molab.main.java.entity.T_Developer;
import molab.main.java.entity.T_Emulator;
import molab.main.java.entity.T_Visit;
import molab.main.java.util.Apptry;
import molab.main.java.util.Constants;
import molab.main.java.util.DB;
import molab.main.java.util.useragent.UserAgentParser;

public class BehaviorLog implements ILog {
	
	private static final Logger LOG = Logger.getLogger(BehaviorLog.class.getName());
	private static final String BEHAVIOR_LOG_PATH = Apptry.getBehaviorDirectory();
	private static final String[] LOCALHOSTS = {"0:0:0:0:0:0:0:1", "localhost"};
	private static final String LOCALHOST = "127.0.0.1";
	private long loginTime;
	private long logoutTime;
	
	private HttpServletRequest request;
	private T_Developer developer;
	private int applicationId;
	private T_Emulator emulator;
	
	public BehaviorLog(HttpServletRequest request, T_Developer developer, int applicationId, T_Emulator emulator) {
//		LOG.log(Level.INFO, "Login:"+getBasePath(request));
		this.request = request;
		this.developer = developer;
		this.applicationId = applicationId;
		this.emulator = emulator;
		login();
	}
	
	public T_Emulator getEmulator() {
		return this.emulator;
	}
	
	public void login() {
		this.loginTime = System.currentTimeMillis();
	}
	
	public void logout() {
//		LOG.log(Level.INFO, "Logout:"+getBasePath(request));
		this.logoutTime = System.currentTimeMillis();
		writeTo();
		writeTo(new File(buildLog(applicationId, emulator.getId())));
	}
	
	public long duration() {
		return this.logoutTime - this.loginTime;
	}
	
	public void writeTo() {
		LOG.log(Level.INFO, "Start writing to DB. ");
		T_Visit visit = new T_Visit();
		String ip = getIpAddress(request);
		for(String localhost : LOCALHOSTS) {
			if(localhost.equalsIgnoreCase(ip)) {
				ip = LOCALHOST;
			}
		}
		visit.setApplication_id(applicationId);
		visit.setIpAddress(ip);
		visit.setMacAddress(getMACAddress(ip));
		visit.setFromUrl(getBasePath(request));
		visit.setLoginTime(loginTime);
		visit.setDurationTime(duration());
//		UserAgent ua = UserAgent.parseUserAgentString(getUserAgent(request));
//		visit.setUserAgent(ua.getBrowser().getName());
		String userAgent = getUserAgent(request);
		if(userAgent != null) {
			UserAgentParser uap = new UserAgentParser(userAgent);
			visit.setUserAgent(uap.getBrowserOperatingSystem() + " " + uap.getBrowserName() + " " + uap.getBrowserVersion());
		} else {
			visit.setUserAgent(Constants.UNKNOWN);
		}
		DB.createVisit(visit);
		LOG.log(Level.INFO, "Write DB done.");
	}
	
	public void writeTo(File log) {
		LOG.log(Level.INFO, "Start writing to: " + log.getName());
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(log);
			bw = new BufferedWriter(fw);
//			StringBuffer sb = new StringBuffer();
//			sb.append(new Browser(request).toString())
//				.append("Action\r\n")
//				.append("Login:").append(Apptry.getTimestamp(new Date(loginTime))).append("\r\n")
//				.append("Logout:").append(Apptry.getTimestamp(new Date(logoutTime))).append("\r\n")
//				.append("Duration:").append(String.valueOf(duration())).append("ms\r\n");
//			bw.write(sb.toString());
			
			// browser related
//			bw.write(new Browser(request).toString());
			bw.write("Browser");
			bw.newLine();
			String ip = getIpAddress(request);
			bw.write("IPAddress:".concat(ip));
			bw.newLine();
			bw.write("MACAddress:".concat(getMACAddress(ip)));
			bw.newLine();
			bw.write("FromURL:".concat(getBasePath(request)));
			bw.newLine();
			bw.write("UserAgent:".concat(getUserAgent(request)));
			bw.newLine();
			// user related
			bw.write("Action");
			bw.newLine();
			bw.write("Login:".concat(Apptry.getTimestamp(new Date(loginTime))));
			bw.newLine();
			bw.write("Logout:".concat(Apptry.getTimestamp(new Date(logoutTime))));
			bw.newLine();
			bw.write("Duration:".concat(String.valueOf(duration())).concat("ms"));
			bw.newLine();
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				LOG.log(Level.SEVERE, e.getMessage());
			}			
		}
		LOG.log(Level.INFO, "Write " + log.getName() + " done.");
	}
	
	public String buildLog(int applicationId, int emulatorId) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String path = BEHAVIOR_LOG_PATH
			.concat(String.valueOf(year)).concat(File.separator)
			.concat(String.valueOf(month)).concat(File.separator)
			.concat(String.valueOf(day)).concat(File.separator);
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
			LOG.log(Level.INFO, path.concat(" created successfully."));
		}
		path = path.concat(Apptry.getTimestamp(new Date()))
			.concat(".D").concat(String.valueOf(developer.getId()))
			.concat(".A").concat(String.valueOf(applicationId))
			.concat(".E").concat(String.valueOf(emulatorId))
			.concat(".log");
		return path;
			
	}
	
//	class Browser {
//		
//		private HttpServletRequest request;
//		
//		public Browser(HttpServletRequest request) {
//			this.request = request;
//		}
//		
//		@Override
//		public String toString() {
//			StringBuffer sb = new StringBuffer("Browser\r\n");
//			String ip = getIpAddress(request);
//			sb.append("IPAddress:").append(ip).append("\r\n");
//			sb.append("MACAddress:").append(getMACAddress(ip)).append("\r\n");
//			sb.append("FromURL:").append(getBasePath(request)).append("\r\n");
//			sb.append("UserAgent:").append(request.getHeader("User-Agent")).append("\r\n");
//			return sb.toString();
//		}
//		
//	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String getMACAddress(String ip) {
		String macAddress = "";
		try {
            InetAddress address = InetAddress.getByName(ip);
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
					/*
					 * Extract each array of mac address and convert it to hexa
					 * with the following format 08-00-27-DC-4A-9E.
					 */
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < mac.length; i++) {
						if (i != 0) {
							sb.append("-");
						}
						// mac[i] & 0xFF 是为了把byte转化为正整数
						String s = Integer.toHexString(mac[i] & 0xFF);
						sb.append(s.length() == 1 ? 0 + s : s);
					}
					// 把字符串所有小写字母改为大写成为正规的mac地址并返回
					macAddress = sb.toString().toUpperCase();
                } else {
                	macAddress = "Error: Address doesn't exist or is not accessible.";
                }
            } else {
            	macAddress = "Error: Network Interface not found.";
            }
        } catch (UnknownHostException e) {
        	macAddress = "Error: " + e.getMessage();
        } catch (SocketException e) {
        	macAddress = "Error: " + e.getMessage();
        }
        return macAddress;
    }
	
	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + path + "/";
	}
	
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}
}
