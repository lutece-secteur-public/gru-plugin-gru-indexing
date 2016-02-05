package fr.paris.lutece.plugins.grustorage.elastic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import fr.paris.lutece.plugins.gru.service.search.CustomerResult;
import fr.paris.lutece.plugins.gru.service.search.ISearchService;
import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ElasticSearchService implements ISearchService
{
	/**
     * {@inheritDoc }
     */	
	@Override
	public List<CustomerResult> searchCustomer(String strQuery) {
		List<CustomerResult> listCustomer = new ArrayList<>();
        String uri = ElasticConnexion.getESParam( "", GRUElasticsConstants.PATH_ELK_SEARCH);
        String[] res = strQuery.split(" ");
        String json = "";

        // Gets the name/firstname enter by autocomplete
        HashMap<String, String> mapChamps = new HashMap<>();
        mapChamps.put("first_name", res[0]);
        mapChamps.put("last_name", res[1]);
        
		try 
		{
			json = ElasticConnexion.formatFullText(mapChamps);
		}
		catch (IOException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
        String jsonRetour = ElasticConnexion.sentToElasticPOST( uri, json);
        JsonNode retour = ElasticConnexion.setJsonToJsonTree(jsonRetour);

        List<JsonNode> tmp = retour.findValues("_source");
        
        for(JsonNode node : tmp)
        {
        	if(node != null) listCustomer.add(buildCustomerResult(node));
        }
		return listCustomer;
	}

	/**
     * {@inheritDoc }
     */
	@Override
	public boolean isAutoComplete()
	{
		return true;
	}

	/**
     * {@inheritDoc }
     */
	@Override
	public String getAutoCompleteUrl() 
	{
		return GRUElasticsConstants.PLUGIN_NAME+GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION;
	}
	
	/**
	 * Build a CustomerResult from a node
	 * @param node
	 * @return
	 */
	private static CustomerResult buildCustomerResult(JsonNode node)
	{
		CustomerResult customer = new CustomerResult();
		customer.setId(node.findValue("user_cid").asInt());
		customer.setLastname(node.findValue("last_name").asText());
		customer.setFirstname(node.findValue("first_name").asText());
		customer.setEmail(node.findValue("email").asText());
		customer.setMobilePhone(node.findValue("telephoneNumber").asText());
		return customer;
	}
}
