package fr.paris.lutece.plugins.grustorage.elastic.web.rs;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;

@Path( RestConstants.BASE_PATH + GRUElasticsConstants.PLUGIN_NAME )
public class GRUElasticRestService
{

	@GET
    @Path( GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION )
    @Produces( MediaType.APPLICATION_JSON )
    public String autocomplete( @QueryParam( "query" ) String strQuery )
    {
        String uri = ElasticConnexion.getESParam( "", GRUElasticsConstants.PATH_ELK_SUGGEST);
        String json = "";
        String retour = "";
		try 
		{
			json = ElasticConnexion.formatAutoCompleteSearch(strQuery);
	        String jsonRetour =  ElasticConnexion.sentToElasticPOST( uri, json);
	        JsonNode node = ElasticConnexion.setJsonToJsonTree(jsonRetour);
 	        retour = getInfoAutocomplete(node);
		}
		catch (IOException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
		return retour;
    }
    
    /**
     * Method which permit to find and format the result of an autocomplete
     * @param nodeTree
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
	private static String getInfoAutocomplete(JsonNode nodeTree) throws JsonGenerationException, JsonMappingException, IOException
	{
        List<JsonNode> payload = nodeTree.findValues("payload");
        
        ObjectMapper mapper = new ObjectMapper();
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode root = new ObjectNode( factory );
		ArrayNode autocomplete = new ArrayNode( factory );

		for(JsonNode node : payload)
        {
        	ObjectNode item = new ObjectNode( factory );
        	item.put("last_name", node.findValue("last_name"));
        	item.put("first_name", node.findValue("first_name"));
        	
        	ObjectNode tmp = new ObjectNode( factory );
        	tmp.put("item", item);
        	autocomplete.add(tmp);
        }
        root.put("autocomplete", autocomplete);
        String test = mapper.writeValueAsString(root);
        return test;
	}
}