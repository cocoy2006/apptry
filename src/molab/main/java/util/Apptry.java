package molab.main.java.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import molab.main.java.component.DispatcherEmulatorComponent;
import molab.main.java.entity.T_Application;
import molab.main.java.entity.T_Deployment;

import org.apache.commons.httpclient.NameValuePair;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import brut.androlib.ApkDecoder;

import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;

public class Apptry {
	private static final String APPTRY_PROPERTIES = "/WEB-INF/classes/apptry.properties";
	private static final String VELOCITY_PROPERTIES = "/WEB-INF/classes/velocity.properties";
	private static final String DATABASE_HOST = "apptryDatabaseHost";
	private static final String YEEPAY_HOST = "yeepayHost";
	private static final String YEEPAY_PAY = "yeepayPay";
	private static final String YEEPAY_PAY_CALLBACK = "yeepayCallback";
	private static final String DISPATCHER_COUNT = "dispatcherCount";
	private static final String TRIAL_TIME = "trialTime";
	private static final Logger LOG = Logger.getLogger(Apptry.class.getName());

	private static ServletContext servletContext;

	public static void setServletContext(ServletContext sc) {
		servletContext = sc;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	public static String getDatabaseHost() {
		return getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), DATABASE_HOST);
	}
	
	public static String getYeepayHost() {
		return getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), YEEPAY_HOST);
	}
	
	public static String getYeepayPay() {
		return getYeepayHost().concat(getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), YEEPAY_PAY));
	}
	
	public static String getYeepayCallback() {
		return getYeepayHost().concat(getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), YEEPAY_PAY_CALLBACK));
	}
	
	public static int getDispatcherCount() {
		return Integer.parseInt(getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), DISPATCHER_COUNT));
	}
	
	public static int getTrialTime() {
		return Integer.parseInt(getProperty(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES), TRIAL_TIME));
	}
	
	public static String getProperty(String file, String key) {
		try {
			Properties props = loadProperties(file);
			return props.getProperty(key);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static String getProperty(InputStream is, String key) {
		try {
			Properties props = loadProperties(is);
			return props.getProperty(key);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static Properties getProperties() {
		try {
			return loadProperties(Apptry.getServletContext().getResourceAsStream(APPTRY_PROPERTIES));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static Properties getVelocityProperties() {
		try {
			return loadProperties(Apptry.getServletContext().getResourceAsStream(VELOCITY_PROPERTIES));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static Properties loadProperties(String file) {
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			return loadProperties(is);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

	public static Properties loadProperties(InputStream is) {
		Properties props = new Properties();
		try {
			props.load(is);
			return props;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static String getLogsDirectory() {
		return servletContext.getRealPath("/logs/");
	}
	
	public static String getBehaviorDirectory() {
		return getLogsDirectory().concat("/behavior/");
	}
	
	public static String getUploadDirectory() {
		return servletContext.getRealPath("/upload/");
	}
	
	public static String getApkDirectory() {
		return getUploadDirectory().concat("/application/apk/");
	}
	
	public static String getVelocityDirectory() {
		return servletContext.getRealPath("/velocity/");
	}
	
	public static String renameWithTimestamp(String originalName) {
		StringBuilder sb = new StringBuilder("");
		String name = getTimestamp(new Date());
		sb.append(name);
		String suffix = "";
		int indexOfPoint = originalName.lastIndexOf(".");
		if(indexOfPoint != -1) {
			suffix = originalName.substring(indexOfPoint, originalName.length());
		}
		sb.append(suffix);
		return sb.toString();
	}
	
	public static String getLocaleTimestamp(long time) {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(time));
	}
	
	public static String getTimestamp(Date date) {
		return new SimpleDateFormat("yyMMddHHmmssSSS").format(date);
	}
	
	public static String getToday() {
		return new SimpleDateFormat("yyMMdd").format(new Date());
	}
	
	public static T_Application parseApplication(MultipartFile file) {
		T_Application application = null;
		try {
			String name = file.getOriginalFilename();
			String aliasName = renameWithTimestamp(name);
			if(aliasName.endsWith(".apk")) {
				String apkPath = getApkDirectory().concat(aliasName);
				File apk = new File(apkPath);
				file.transferTo(apk);
				String md5 = MD5Util.getFileMD5(apk);
//				String url = Apptry.getDatabaseHost().concat("/application/load/").concat(md5);
//				String result = ajaxGet(url);
//				if("0".equals(result)) { // application does not exist
					application = new T_Application();
					application.setMd5(md5);
					application.setName(name);
					application.setSize(file.getSize());
					application.setAliasName(aliasName);
					// get more information from AndroidManifest.xml.
					ApkDecoder decoder = new ApkDecoder();
					String outDirPath = apkPath.substring(0, apkPath.length() - 4);
					File apkFile = new File(apkPath);
					decoder.setOutDir(new File(outDirPath));
			        decoder.setApkFile(apkFile);
			        decoder.decode();
			        
			        File amx = new File(outDirPath.concat("/AndroidManifest.xml"));
					ManifestData md = AndroidManifestParser.parse(new FileInputStream(amx));
					
					application.setOs(md.getMinSdkVersionString() != null ? md.getMinSdkVersionString() : "");
					application.setPackageName(md.getPackage() != null ? md.getPackage() : "");
					application.setStartActivity(md.getLauncherActivity() != null ? md.getLauncherActivity().getName() : "");
					application.setVersion(md.getVersionName());
					LOG.log(Level.INFO, application.getName() + "解析完成");
//				} else { // application already exists
//					application = new Gson().fromJson(result, T_Application.class);
//					LOG.log(Level.INFO, application.getName() + "已经存在");
//				}
			} else if(aliasName.endsWith(".ipa")) {}
			return application;
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
//	public static String install(int dispatcherId, int applicationId, String filePath, DispatcherEmulatorComponent emulator) {
//		String host = emulator.getIpAddress().concat(":").concat(String.valueOf(emulator.getPort()));
//		String url = "http://".concat(host).concat("/apptryADB/singleInstall/");
//		String result = "";
//		try {
//			result = HC.ajaxMultipart(url, new String[]{filePath}, new String[]{emulator.getSerialNumber()});
//			result = result.replaceAll("\"", "");
//			LOG.log(Level.INFO, "Apptry: Install " + filePath + 
//					" @" + host + "." + emulator.getSerialNumber() + " " + result);
//		} catch(Exception e) {
//			LOG.log(Level.SEVERE, e.getMessage());
//			result = Constants.APK_EXCEPTION;
//		}
//		return result;
//	}
	
	public static String install(String filePath, DispatcherEmulatorComponent emulator) {
		String host = emulator.getIpAddress().concat(":").concat(String.valueOf(emulator.getPort()));
		String url = "http://".concat(host).concat("/apptryADB/singleInstall/");
		String result = "";
		try {
			result = HC.ajaxMultipart(url, new String[]{filePath}, new String[]{emulator.getSerialNumber()});
			result = result.replaceAll("\"", "");
			LOG.log(Level.INFO, "Apptry: Reinstall " + filePath + 
					" @" + host + "." + emulator.getSerialNumber() + " " + result);
		} catch(Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			result = Constants.APK_EXCEPTION;
		}
		return result;
	}
	
	public static String uninstall(String packageName, DispatcherEmulatorComponent emulator) {
		String host = emulator.getIpAddress().concat(":").concat(String.valueOf(emulator.getPort()));
		String url = "http://".concat(host).concat("/apptryADB/singleUninstall/");
		NameValuePair[] params = {
				new NameValuePair("serialNumber", emulator.getSerialNumber()),
				new NameValuePair("packageName", packageName)
		};
		String result = HC.ajaxPost(url, params);
		LOG.log(Level.INFO, "Apptry: Uninstall " + packageName + 
				" @" + host + "." + emulator.getSerialNumber() + " " + result);
		return result;
	}
	
	public static String parseFromUrl(HttpServletRequest request) {
		return parseFromUrl(request.getRequestURL(), request.getServletPath());
	}
	
	public static String parseFromUrl(StringBuffer requestURL, String servletPath) {
		int index = requestURL.indexOf(servletPath);
		return requestURL.substring(0, index + 1);
	}
	
	public static void gotoPage(HttpServletResponse response, String url) {
		try {
			response.sendRedirect(url);
			return;
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public static void gotoPage(HttpServletRequest request, HttpServletResponse response, String page) {
		try {
			response.sendRedirect("/" + request.getRequestURI().split("/")[1] + "/" + page);
			return;
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * 产生随机的length位字符串
	 */
	public static String randomString(int length) {
		if (length < 1)
			return null;
		Random randGen = new Random();
		char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
				+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 产生随机的N位数
	 */
	public static String randomInteger(int length) {
		Random random = new Random();
		String str = random.nextInt((int)Math.pow(10, length)) + "";
		if(str.length() < length) {
			int diff = length - str.length();
			while(diff-- > 0) {
				str = "0" + str;
			}
		}
		return str;
	}
	
	public static String encodeMD5Hex(String data) {
		return DigestUtils.md5DigestAsHex(data.getBytes());
	}
	
	public static int buildYeepayPay() {
		String url = Apptry.getYeepayPay();
		NameValuePair[] params = {
				new NameValuePair("", String.valueOf("")),
				new NameValuePair("", String.valueOf(""))
		};
		String result = HC.ajaxPost(url, params);
		return Integer.parseInt(result);
	}

}