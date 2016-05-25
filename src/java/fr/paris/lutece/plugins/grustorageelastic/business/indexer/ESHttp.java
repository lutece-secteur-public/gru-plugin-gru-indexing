package fr.paris.lutece.plugins.grustorageelastic.business.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ESHttp {

	private static String _baseUrl;
	private static String _type;
	private static String _indice;

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

		_baseUrl = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) + AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE )+ "/"+AppPropertiesService.getProperty( GRUElasticsConstants.ES_TYPE ) ;
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
		
		String strUrl = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
	            AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE ) ;
		
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
