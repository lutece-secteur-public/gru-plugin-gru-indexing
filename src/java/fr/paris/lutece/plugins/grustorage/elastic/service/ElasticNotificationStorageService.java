/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grustorage.elastic.service;


import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fr.paris.lutece.plugins.grusupply.business.Demand;
import fr.paris.lutece.plugins.grusupply.business.Notification;
import fr.paris.lutece.plugins.grustorage.elastic.business.CustomerDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESBackofficeNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESDashboardNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESSMSNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESEmailNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.NotificationDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grusupply.business.Customer;
import fr.paris.lutece.plugins.grusupply.service.INotificationStorageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ElasticNotificationStorageService implements INotificationStorageService{

	// Jersey Http Request
    private Client _client = Client.create(  );
    
    /**
     * {@inheritDoc }
     */
	@Override
	public void store( Notification notification ) 
	{
		if(notification == null) throw new NullPointerException( );
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonNotif;
		try 
		{
			ESNotificationDTO notifDto = buildNotificationDto(notification, notification.getDemand());
			
			jsonNotif = mapper.writeValueAsString(notifDto);
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_NOTIFICATION, notification.getDemand().getDemandId( ) ), jsonNotif);
		} 
		catch (JsonGenerationException | JsonMappingException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		} 
		catch (IOException ex) 
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
	}
    /**
     * {@inheritDoc }
     */
	@Override
	public void store( Customer user )
	{
		if(user == null) throw new NullPointerException( );
		
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonUser;
		
		try 
		{
			jsonUser = mapper.writeValueAsString(user);		
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_USER,  user.getCustomerId( ) ), jsonUser);
		} 
		catch (JsonGenerationException | JsonMappingException ex )
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
		catch (IOException ex) 
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
	}
    /**
     * {@inheritDoc }
     */
	@Override
	public void store( Demand demand ) 
	{
		if(demand == null) throw new NullPointerException( );
		
		ESDemandDTO demandDTO = buildDemandDTO(demand, demand.getCustomer());
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonDemand;
		
		try 
		{
			jsonDemand = mapper.writeValueAsString(demandDTO);
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, demand.getCustomer().getCustomerId() ), jsonDemand );
		} 
		catch (JsonGenerationException | JsonMappingException ex)
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		} 
		catch (IOException ex) 
		{
			AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
		}
	}
	
	
	private static ESNotificationDTO buildNotificationDto( Notification notif, Demand demand)
	{
	    ESNotificationDTO notifDTO = new ESNotificationDTO();
		NotificationDemandDTO nddto = new NotificationDemandDTO( String.valueOf(demand.getDemandId()), String.valueOf(demand.getDemandIdType()));
		
		notifDTO.setDateSollicitation(notif.getDateSollicitation());
		notifDTO.setNotificationDemand( nddto );
		notifDTO.setUserEmail(new ESEmailNotificationDTO(notif.getUserEmail() ) );
		notifDTO.setUserDashBoard(new ESDashboardNotificationDTO(notif.getUserDashBoard()));
		notifDTO.setUserSms(new ESSMSNotificationDTO(notif.getUserSms()));
		notifDTO.setUserBackOffice(new ESBackofficeNotificationDTO(notif.getUserBackOffice()));
	    
		return notifDTO;
	}
	/**
	 * 
	 * @param demand
	 * @param customer
	 * @return
	 */
	private static ESDemandDTO buildDemandDTO( Demand demand, Customer customer )
	{
		ESDemandDTO demandDTO = new ESDemandDTO();
		CustomerDemandDTO customerDemand = new CustomerDemandDTO(String.valueOf(demand.getCustomer().getCustomerId()));
		demandDTO.setCustomerDemand(customerDemand);
		demandDTO.setDemandId(demand.getDemandId());
		demandDTO.setDemandIdType(demand.getDemandIdType());
		demandDTO.setDemandMaxStep(demand.getDemandMaxStep());
		demandDTO.setDemandUserCurrentStep(demand.getDemandUserCurrentStep());
		demandDTO.setDemandState(demand.getDemandState());
		demandDTO.setNotifType(demand.getNotifType());
		demandDTO.setDateDemand(demand.getDateDemand());
		demandDTO.setCRMStatus(demand.getCRMStatus());
		demandDTO.setReference(demand.getReference());
		demandDTO.setSuggest(customer);
		
		return demandDTO;
	}	
	
	/**
	 * Function which set URI to elasticsearch connexion
	 * @param strPath
	 * @param strSpecif
	 * @return
	 */
	private static String getESParam( String strPath, String strSpecif ){
		return AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
			   AppPropertiesService.getProperty( strPath)+
			   strSpecif;
	}
	private static String getESParam( String strPath, int strSpecif )
	{
		return getESParam(strPath, String.valueOf(strSpecif));
	}
	/**
	 * Fonction which insert data into elasticSearch
	 * @param uri
	 * @param json
	 * @return
	 */
	private String sentToElastic( String uri, String json)
	{
		WebResource resource = _client.resource( uri );
		ClientResponse response = resource.put( ClientResponse.class, json );
		return response.getEntity( String.class );			
	}
	
	
    /**
     *  Web service method which permit to send autocompletion request to elasticsearch
     * @param strQuery autocompletion request for elasticsearch
     * @return the response of the
     */
 /*   
  * Elle sera implementer dans un autre module
  * @GET
    @Path( GruSupplyConstants.PATH_ELASTIC_AUTOCOMPLETION )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public String autocomplete( @QueryParam( "query" ) String strQuery )
    {
        //String autocompleteRequest = GRUElasticsConstants.FIELD_USER_SUGGEST + strQuery +
          //  GRUElasticsConstants.FIELD__COMPLETION;

       // return ElasticSearchHttpRequest.getInstance().autocomplete(strQuery);

		return GruSupplyConstants.STATUS_201;
    }

*/


    
    
}