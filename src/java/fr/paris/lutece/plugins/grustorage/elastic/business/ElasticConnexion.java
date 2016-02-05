package fr.paris.lutece.plugins.grustorage.elastic.business;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.mysql.jdbc.StringUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ElasticConnexion {

	private ElasticConnexion _singleton = null;
    private static Client _client = Client.create();
	
	private ElasticConnexion( )
	{
		_singleton = new ElasticConnexion();
	}
	
	/**
	 * Function which make a singleton
	 * @return
	 */
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
		String path = (StringUtils.isNullOrEmpty(strPath)) ? "":AppPropertiesService.getProperty( strPath);
		return AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
			   AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_PATH ) +
			   path + strSpecif;
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
	public static String sentToElasticPUT( String uri, String json)
	{
		WebResource resource = _client.resource( uri );
		ClientResponse response = resource.put( ClientResponse.class, json );
		return response.getEntity( String.class );			
	}
	/**
	 * Fonction which insert data into elasticSearch
	 * @param uri
	 * @param json
	 * @return
	 */
	public static String sentToElasticPOST( String uri, String json)
	{
		WebResource resource = _client.resource( uri );
		ClientResponse response = resource.post( ClientResponse.class, json );
		return response.getEntity( String.class );			
	}	
	/**
	 * Method which permit to search with autocompletion
	 * @param champ
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String formatAutoCompleteSearch( String champ ) throws JsonGenerationException, JsonMappingException, IOException
	{
        ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = JsonNodeFactory.instance;
		
		ObjectNode root = new ObjectNode( factory );
		ObjectNode tmp =  new ObjectNode( factory );
		ObjectNode completion =  new ObjectNode( factory );
		completion.put( "field", "suggest" );
		completion.put( "fuzzy", new ObjectNode( factory ) );
		
		tmp.put( "text", champ );
		tmp.put( "completion", completion );
		root.put( "user-suggest", tmp );
		
		return mapper.writeValueAsString(root);
	}
	
	/**
	 * Method which permit to search by exact request
	 * @param key
	 * @param value
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String formatExactSearch( String strKey, String strValue) throws JsonGenerationException, JsonMappingException, IOException
	{
        ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		ObjectNode tmp = new ObjectNode( factory );
		
		ObjectNode filtered =  new ObjectNode( factory );
		ObjectNode query =  new ObjectNode( factory );
		query.put("match_all",  new ObjectNode( factory ) );
		
		ObjectNode filter =  new ObjectNode( factory );
		ObjectNode term =  new ObjectNode( factory );
		
		term.put(strKey, strValue);
		filter.put( "term", term );
		
		filtered.put( "query", query );
		filtered.put( "filter", filter );
		
		tmp.put("filtered", filtered);
		root.put( "query", tmp );
		
		return mapper.writeValueAsString(root);
	}
	/**
	 * Function which make a full text search with any key/value 
	 * @param map Have to be Key/value
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String formatFullText( Map<String, String> map) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		ObjectNode query = new ObjectNode( factory );
		ObjectNode bool = new ObjectNode( factory );
		
		ArrayNode should = new ArrayNode(factory);
		
		for(String mapKey : map.keySet())
		{
			ObjectNode match =  new ObjectNode( factory );
			ObjectNode tmp =  new ObjectNode( factory );
			tmp.put(mapKey, map.get(mapKey));
			match.put("match", tmp);
			should.add(match);
		}
		bool.put("should", should);
		query.put("bool", bool);
		root.put("query", query);
		
		return mapper.writeValueAsString(root);
	}
	
	/**
	 * Function which make a boolean text search with any key/value 
	 * @param map Have to be Key/value
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String formatBooleanText( Map<String, String> map) throws JsonGenerationException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		ObjectNode bool = new ObjectNode( factory );
		ObjectNode filter = new ObjectNode( factory );
		ObjectNode filtered = new ObjectNode( factory );
		ObjectNode tmptmp = new ObjectNode( factory );
		
		ArrayNode must = new ArrayNode(factory);
		
		for(String mapKey : map.keySet())
		{
			ObjectNode match =  new ObjectNode( factory );
			ObjectNode tmp =  new ObjectNode( factory );
			tmp.put(mapKey, map.get(mapKey));
			match.put("term", tmp);
			must.add(match);
		}
		bool.put("must", must);
		filter.put("bool", bool);
		tmptmp.put("filter", filter);
		filtered.put("filtered", tmptmp);
		root.put("query", filtered);
		
		return mapper.writeValueAsString(root);
	}
	/**
	 * Format a JSON String into a JSON node
	 * @param strJson
	 * @return
	 */
	public static JsonNode setJsonToJsonTree(String strJson)
	{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tmp = null;
		try {

	        tmp = mapper.readTree(strJson);
		} catch (IOException ex) {
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
		return tmp;
	}
}
