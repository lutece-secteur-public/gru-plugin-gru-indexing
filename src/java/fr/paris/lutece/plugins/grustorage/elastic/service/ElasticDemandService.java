package fr.paris.lutece.plugins.grustorage.elastic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import fr.paris.lutece.plugins.gru.business.demand.BaseDemand;
import fr.paris.lutece.plugins.gru.business.demand.Demand;
import fr.paris.lutece.plugins.gru.business.demand.Email;
import fr.paris.lutece.plugins.gru.business.demand.Notification;
import fr.paris.lutece.plugins.gru.business.demand.Sms;
import fr.paris.lutece.plugins.gru.business.demand.UserDashboard;
import fr.paris.lutece.plugins.gru.service.demand.IDemandService;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ElasticDemandService implements IDemandService{
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try 
		{
			json = ElasticConnexion.formatExactSearch( "demand_id", strDemandId);
			retour = ElasticConnexion.sentToElasticPOST( uri, json);
			
			// JSON Parsing
			JsonNode jsonRetour =  mapper.readTree(retour);
			JsonNode jnode = jsonRetour.findValue("_source");
	
			String tmp = mapper.writeValueAsString(jnode);
			ESDemandDTO demandDTO = mapper.readValue( tmp, ESDemandDTO.class );
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
		String retourES = "";
		String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, GRUElasticsConstants.PATH_ELK_SEARCH );
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try 
		{		
			json = ElasticConnexion.formatExactSearch( "utilisateur.user_cid", strCustomerId);
			retourES = ElasticConnexion.sentToElasticPOST( uri, json);
			
			// JSON Parsing
			JsonNode jsonRetour =  mapper.readTree(retourES);
			List<JsonNode> listDemand = jsonRetour.findValues("_source");
			
			for(JsonNode jnode: listDemand)
			{
				if(jnode != null)
				{
					String tmp = mapper.writeValueAsString(jnode);
					ESDemandDTO demandDTO = mapper.readValue( tmp, ESDemandDTO.class );
					base.add(buildBaseDemand(demandDTO));					
				}
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

		if(demand == null) throw new NullPointerException();
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
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	private static Demand buildDemand(ESDemandDTO demand) throws JsonGenerationException, JsonMappingException, IOException
	{
		// create demand
		Demand base = new Demand(buildBaseDemand(demand));
		
		// create Notifications
		String json;
		String uri = ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_NOTIFICATION, GRUElasticsConstants.PATH_ELK_SEARCH);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		json = ElasticConnexion.formatExactSearch( "demande.demand_id", demand.getDemandId());
		String retourES = ElasticConnexion.sentToElasticPOST( uri, json);
		
		JsonNode jsonRetour =  mapper.readTree(retourES);
		List<JsonNode> listNotification = jsonRetour.findValues("_source");
		
		for(JsonNode jnode: listNotification)
		{
			if(jnode != null)
			{
				String tmp = mapper.writeValueAsString(jnode);
				ESNotificationDTO notificationDTO = mapper.readValue( tmp, ESNotificationDTO.class );
				base.getNotifications().add(buildNotification(notificationDTO));
			}
			
		}
		
		// create action
		
		return base;
	}
	
	/**
	 * Build a Demand from a demandDTO
	 * @param demand
	 * @return
	 */
	private static Notification buildNotification(ESNotificationDTO notification)
	{

		if(notification == null) throw new NullPointerException();
		Notification retour = new Notification();
		Email email = new Email();
		if(notification.getUserEmail() != null)
		{
			email.setSenderName(notification.getUserEmail().getSenderName());
			email.setRecipient(notification.getUserEmail().getRecipient());
			email.setSubject(notification.getUserEmail().getSubject());
			email.setMessage(notification.getUserEmail().getMessage());
		}
		
		Sms sms = new Sms();
		if(notification.getUserEmail() != null)
		{
			sms.setMessage(notification.getUserSms().getMessage());
			sms.setPhoneNumber(String.valueOf(notification.getUserSms().getPhoneNumber()));
		}
		
		UserDashboard uDash = new UserDashboard();
		if(notification.getUserEmail() != null)
		{
			uDash.setStatusText(notification.getUserDashBoard().getStatusText());
			uDash.setSenderName(notification.getUserDashBoard().getSenderName());
			uDash.setSubject(notification.getUserDashBoard().getSubject());
			uDash.setMessage(notification.getUserDashBoard().getMessage());
		}
		
		retour.setTimestamp(1L);
		retour.setTitle("PAS TROUVE");
		retour.setSource("PAS TROUVE");
		retour.setEmail(email);
		retour.setSms(sms);
		retour.setUserDashboard(uDash);
		return retour;
	}
}
