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
			jsonNotif = mapper.writeValueAsString(notification);
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_NOTIFICATION, notification.getDemandId( ) ), jsonNotif);
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
	public void store( Customer _user )
	{
		if(_user == null) throw new NullPointerException( );
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonUser;
		
		try 
		{
			jsonUser = mapper.writeValueAsString(_user);		
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_USER, _user.getCustomerId( ) ), jsonUser);
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
	public void store( Demand _demand ) 
	{
		if(_demand == null) throw new NullPointerException( );
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonDemand;
		
		try 
		{
			jsonDemand = mapper.writeValueAsString(_demand);
			sentToElastic( getESParam( GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, _demand.getUserCid( ) ), jsonDemand );
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
	 * Function which set URI to elasticsearch connexion
	 * @param strPath
	 * @param strSpecif
	 * @return
	 */
	private static String getESParam( String strPath, int strSpecif ){
		return AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
			   AppPropertiesService.getProperty( strPath)+
			   String.valueOf( strSpecif );
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