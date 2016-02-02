package fr.paris.lutece.plugins.grustorage.elastic.web.rs;

import java.io.IOException;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;

@Path( RestConstants.BASE_PATH + GRUElasticsConstants.PLUGIN_NAME )
public class GRUElasticRestService {
	
    /**
     *  Web service method which permit to send autocompletion request to elasticsearch
     * @param strQuery autocompletion request for elasticsearch
     * @return the response of the
     */
    @GET
    @Path( GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public String autocomplete( @QueryParam( "query" ) String strQuery )
    {
        String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_SUGGEST, "" );
        String json = "";
		try 
		{
			json = ElasticConnexion.formatAutoCompleteSearch(strQuery);
		}
		catch (IOException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
        return ElasticConnexion.sentToElasticPOST( uri, json);
    }
	
    /**
     *  Web service method which permit to send autocompletion request to elasticsearch
     * @param strQuery autocompletion request for elasticsearch
     * @return the response of the
     */
    @GET
    @Path( GRUElasticsConstants.PATH_ELASTIC_FIND_USER )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public String searchUser( @QueryParam( "query" ) String strFirstNameUser, String strLastNameUser )
    {
        String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_SEARCH, "" );
        String json = "";
        HashMap<String, String> mapChamps = new HashMap<>();
        mapChamps.put("first_name", strFirstNameUser);
        mapChamps.put("last_name", strFirstNameUser);
        
		try 
		{
			json = ElasticConnexion.formatFullText(mapChamps);
		}
		catch (IOException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
        return ElasticConnexion.sentToElasticPOST( uri, json);
    }
}
