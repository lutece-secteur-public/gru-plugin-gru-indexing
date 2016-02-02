package fr.paris.lutece.plugins.grustorage.elastic.business;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ElasticConnexion {

	private ElasticConnexion _singleton = null;
    private static Client _client = Client.create();
	
	private ElasticConnexion( )
	{
		_singleton = new ElasticConnexion();
	}
	
	public ElasticConnexion getInstance( ){
		if(_singleton == null)
		{
			return new ElasticConnexion();
		}
		return _singleton;
	}
	
	
	/**
	 * Function which set URI to elasticsearch connexion
	 * @param strPath
	 * @param strSpecif
	 * @return
	 */
	public static String getESParam( String strPath, String strSpecif )
	{
		return AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
			   AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_PATH ) +
			   AppPropertiesService.getProperty( strPath)+
			   strSpecif;
	}
	/**
	 * @param strPath
	 * @param strSpecif
	 * @return
	 */
	public static String getESParam( String strPath, int strSpecif )
	{
		return getESParam(strPath, String.valueOf(strSpecif));
	}
	/**
	 * Fonction which insert data into elasticSearch
	 * @param uri
	 * @param json
	 * @return
	 */
	public static String sentToElastic( String uri, String json)
	{
		WebResource resource = _client.resource( uri );
		ClientResponse response = resource.put( ClientResponse.class, json );
		return response.getEntity( String.class );			
	}
	
	/**
	 * Method which permit to search with autocompletion
	 * @param champ
	 * @return
	 */
	public static String formatAutoCompleteSearch( String champ )
	{
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		ObjectNode tmp =  new ObjectNode( factory );
		ObjectNode completion =  new ObjectNode( factory );
		completion.put( "field", "suggest" );
		completion.put( "fuzzy", "{}" );
		
		tmp.put( "text", champ );
		tmp.put( "completion", completion );
		root.put( "user-suggest", tmp );

		return root.asText();
	}
	
	/**
	 * Method which permit to search by exact request
	 * @param key
	 * @param value
	 * @return
	 */
	public static String formatExactSearch( String key, String value )
	{
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		
		ObjectNode filtered =  new ObjectNode( factory );
		ObjectNode query =  new ObjectNode( factory );
		query.put("match_all",  new ObjectNode( factory ) );
		
		ObjectNode filter =  new ObjectNode( factory );
		ObjectNode term =  new ObjectNode( factory );
		term.put( key, value );
		filter.put( "term", term );
		
		filtered.put( "query", query );
		filtered.put( "filter", filter );
		
		root.put( "query", filtered );
		
		return root.asText( );
	}
}
