package fr.paris.lutece.plugins.grustorageelastic.business.indexer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.util.AppLogService;

public class ESHttp {

	private static String _baseUrl;
	private static String _type;
	private static String _indice;
	private static Integer _port;
	private static String _host;
	private static String _protocol;
	private List<String> _bulk;

	private static String execute(String method, String targetURL, String urlParameters) throws IOException {
		HttpURLConnection connection = null;  
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method.toUpperCase());
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "fr-FR");  

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			//Send request
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
			wr.write(urlParameters);
				wr.flush();
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		}
		finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}

	public static String add(int id, String data) throws InterruptedException {
		try {
		return execute("PUT", baseUrl(String.valueOf(id)), data);
		}
		catch (IOException e) {
			Thread.sleep(2);
			System.out.println("id : " + String.valueOf(id));
			System.out.println("data : " + data);
			return add(id, data);
		}
	}

	private static String baseUrl(String string) {
		baseUrl();
		if (_baseUrl != null) {
			return _baseUrl + "/" + string;
		}
		else {
			return string + "/";
		}
	}
	
	public static boolean indexExist( ) throws IOException
	{
		boolean res= false ;
		
		String strUrl = getUrl( ) ;
		
		AppLogService.debug("url :" + strUrl );
		URL url = new URL( strUrl );
	    HttpURLConnection http = (HttpURLConnection)url.openConnection();
	    try 
	    {
		    if ( http.getResponseCode() == 200 )
		    {
		    	res = true;
		    }
		    AppLogService.debug("url :"+strUrl + "response : "+http.getResponseCode());
	    }
	    catch (IOException e )
	    {
			AppLogService.error( e.getMessage( ) );
		}
	    return res;

	}
	
	private static String getUrl( )
	{
		String strType ="";
		baseUrl();
		if ( getType()!=null )
		{
			strType = "/" + getType() ;
		}
		return _baseUrl.replaceAll( strType, StringUtils.EMPTY ) ;
	}
	private static void baseUrl() {
		StringBuilder sb = new StringBuilder();
		if (getProtocol() == null) {
			sb.append("http://");
		}
		else {
			sb.append(getProtocol() + "://");
		}
		
		if (getHost() == null) {
			return;
		}
		else {
			sb.append(getHost());
		}
		
		sb.append(":");
		if (getPort() == null) {
			sb.append("9200");
		}
		else {
			sb.append(String.valueOf(getPort()));
		}
		
		sb.append("/");
		if (getIndice() == null) {
			return;
		}
		else {
			sb.append(getIndice());
		}
		
		sb.append("/");
		if (getType() == null) {
			return;
		}
		else {
			sb.append(getType());
		}
		
		_baseUrl = sb.toString();
	}

	/**
	 * @return the _protocol
	 */
	public static String getProtocol() {
		return _protocol;
	}

	/**
	 * @param _protocol the _protocol to set
	 */
	public static void setProtocol(String protocol) {
		_protocol = protocol;
	}

	/**
	 * @return the _host
	 */
	public static String getHost() {
		return _host;
	}

	/**
	 * @param _host the _host to set
	 */
	public static void setHost(String host) {
		_host = host;
	}

	/**
	 * @return the _port
	 */
	public static Integer getPort() {
		return _port;
	}

	/**
	 * @param _port the _port to set
	 */
	public static void setPort(Integer port) {
		_port = port;
	}

	/**
	 * @return the _indice
	 */
	public static String getIndice() {
		return _indice;
	}

	/**
	 * @param _indice the _indice to set
	 */
	public static void setIndice(String indice) {
		_indice = indice;
	}

	/**
	 * @return the _type
	 */
	public static String getType() {
		return _type;
	}

	/**
	 * @param _type the _type to set
	 */
	public static void setType(String type) {
		_type = type;
	}
}
