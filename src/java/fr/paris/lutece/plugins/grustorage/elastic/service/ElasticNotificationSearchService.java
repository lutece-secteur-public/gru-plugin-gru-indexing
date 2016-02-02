package fr.paris.lutece.plugins.grustorage.elastic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import fr.paris.lutece.plugins.gru.business.demand.BaseDemand;
import fr.paris.lutece.plugins.gru.business.demand.Demand;
import fr.paris.lutece.plugins.gru.service.demand.IDemandService;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ElasticNotificationSearchService implements IDemandService{
	/**
     * {@inheritDoc }
     */
	@Override
	public Demand getDemand(String strDemandId, String strDemandTypeId, AdminUser user) 
	{
		Demand demand = new Demand();
		String json;
		String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, GRUElasticsConstants.PATH_ELK_SEARCH);
		String retour = "";
		try 
		{
			HashMap<String, String> mapChamps = new HashMap<>();
			mapChamps.put("demand.demand_id", strDemandId);
			mapChamps.put("demand.demand_type_id", strDemandTypeId);
			json = ElasticConnexion.formatFullText( mapChamps);
			retour = ElasticConnexion.sentToElasticPOST( uri, json);
			
			// JSON Parsing
			ObjectMapper mapper = new ObjectMapper();
			ESDemandDTO demandDTO = null;
		

			JsonNode jsonRetour =  mapper.readTree(retour);
			JsonNode jnode = jsonRetour.findValue("_source");
	
			demandDTO = mapper.readValue( jnode.asText(), ESDemandDTO.class);
			demand = buildDemand(demandDTO);
		} 
		catch (IOException ex) 
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
		return demand;
	}

	/**
     * {@inheritDoc }
     */
	@Override
	public List<BaseDemand> getDemands(String strCustomerId, AdminUser user) 
	{
		List<BaseDemand> base = new ArrayList<>();
		String json;
		String retour = "";
		String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, GRUElasticsConstants.PATH_ELK_SEARCH );
	
		try 
		{		
			json = ElasticConnexion.formatExactSearch( "user_cid", strCustomerId);
			retour = ElasticConnexion.sentToElasticPOST( uri, json);
			
			// JSON Parsing
			ObjectMapper mapper = new ObjectMapper();
			ESDemandDTO demandDTO = null;

			JsonNode jsonRetour =  mapper.readTree(retour);
			List<JsonNode> listDemand = jsonRetour.findValues("_source");
			
			for(JsonNode jnode: listDemand)
			{
				demandDTO = mapper.readValue( jnode.asText(), ESDemandDTO.class);
				base.add(buildBaseDemand(demandDTO));
			}
		} 
		catch (IOException ex) 
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
		
		return base;
	}

	
	/**
	 * Build a baseDemand from a demand
	 * @param demand
	 * @return
	 */
	private static BaseDemand buildBaseDemand(ESDemandDTO demand)
	{
		BaseDemand base = new BaseDemand();
		base.setId(demand.getDemandId());
		base.setDemandTypeId(demand.getDemandIdType());
		base.setReference(demand.getReference());
		base.setStatus(demand.getCRMStatus());
		
		return base;
	}
	
	/**
	 * Build a Demand from a demandDTO
	 * @param demand
	 * @return
	 */
	private static Demand buildDemand(ESDemandDTO demand)
	{
		
		Demand base = new Demand();
		base.setId(demand.getDemandId());
		base.setDemandTypeId(demand.getDemandIdType());
		base.setReference(demand.getReference());
		base.setStatus(demand.getCRMStatus());
		
		return base;
	}
}
