package molab.main.java.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HC {
	
	private static final Logger LOG = Logger.getLogger(HC.class.getName());

	private static ServletContext servletContext;

	public static void setServletContext(ServletContext sc) {
		servletContext = sc;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
		
	public static String ajaxGet(String url) {
		LOG.log(Level.INFO, "HttpClient GET Request:" + url);
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.log(Level.SEVERE, "Method failed: " + getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			String response = new String(responseBody);
			LOG.log(Level.INFO, "HttpClient GET Response:" + response);
			return response;
		} catch (HttpException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public static String ajaxPost(String url, NameValuePair[] params) {
		LOG.log(Level.INFO, "HttpClient POST Request:" + url);
		StringBuffer parameters = new StringBuffer();
		for(int i = 0; i < params.length; i++) {
			parameters.append(params[i].getName() + "=" + params[i].getValue() + ",");
		}
		LOG.log(Level.INFO, "HttpClient POST Request Parameters: " + parameters.toString());
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		postMethod.setRequestBody(params);
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if(statusCode != HttpStatus.SC_OK) {
				LOG.log(Level.SEVERE, "Method failed: " + postMethod.getStatusLine());
			}
			byte[] responseBody = postMethod.getResponseBody();
			String response = new String(responseBody);
			LOG.log(Level.INFO, "HttpClient POST Response:" + response);
			return response;
		} catch (HttpException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return null;
	}
	
	public static String ajaxMultipart(String url, String[] filePaths, String[] serialNumbers) throws IOException {
		LOG.log(Level.INFO, "HttpClient Multipart Request:" + url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = "";
		try {
            HttpPost httppost = new HttpPost(url);
            MultipartEntity mEntity = new MultipartEntity(  
                    HttpMultipartMode.BROWSER_COMPATIBLE, null,  
                    Charset.forName("UTF-8"));
            StringBuffer parameters = new StringBuffer();
            for(String filePath : filePaths) {
            	parameters.append("filePath=" + filePath + ", ");
            	mEntity.addPart("file", new FileBody(new File(filePath)));
            }
            for(String serialNumber : serialNumbers) {
            	parameters.append("serialNumber=" + serialNumber + ", ");
            	mEntity.addPart("serialNumber", new StringBody(serialNumber));
            }
            LOG.log(Level.INFO, "HttpClient Multipart Request Parameters: " + parameters.toString());
            httppost.setEntity(mEntity);

            LOG.log(Level.INFO, "Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
            	LOG.log(Level.INFO, "Response status line: " + response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                	result = EntityUtils.toString(resEntity, "UTF-8");
                    LOG.log(Level.INFO, "Response: " + result);
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
		return result;
	}
}