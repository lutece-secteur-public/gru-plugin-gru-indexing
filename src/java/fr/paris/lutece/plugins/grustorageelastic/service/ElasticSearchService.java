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
package fr.paris.lutece.plugins.grustorageelastic.service;

import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.plugins.gru.service.search.CustomerResult;
import fr.paris.lutece.plugins.gru.service.search.ISearchService;
import fr.paris.lutece.plugins.grustorageelastic.business.ESCustomerDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grusupply.service.StorageService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ElasticSearchService implements ISearchService
{
    /**
    * {@inheritDoc }
    */
    @Override
    public List<CustomerResult> searchCustomer( String strQuery )
    {
    	List<CustomerResult> listCustomer = new ArrayList<CustomerResult>(  );
        String uri = ElasticConnexion.getESParam( "", GRUElasticsConstants.PATH_ELK_SEARCH );
        String[] res = strQuery.split( " " );
        String json = "";

        // Gets the name/firstname entered by autocomplete
        Map<String, String> mapChamps = new HashMap<String, String>(  );
        
        if ( res.length >= 1 )
        {
            mapChamps.put( "first_name", res[0] );
        }

        if ( res.length >= 2 )
        {
            mapChamps.put( "last_name", res[1] );
        }

        try
        {
            json = ElasticConnexion.formatFullText( mapChamps );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }

        String jsonRetour = ElasticConnexion.sentToElasticPOST( uri, json );
        JsonNode retour = ElasticConnexion.setJsonToJsonTree( jsonRetour );

        List<JsonNode> tmp = retour.findValues( "_source" );

        for ( JsonNode node : tmp )
        {
            if ( node != null )
            {
                listCustomer.add( buildCustomerResult( node ) );
            }
        }
        return listCustomer;
    }
    
    
    private fr.paris.lutece.plugins.grusupply.business.Customer searchCustomer( int nCid )
    {
        String uri = ElasticConnexion.getESParam( "", GRUElasticsConstants.PATH_ELK_SEARCH );
        String json = "";

        // Gets the name/firstname entered by autocomplete
        Map<String, String> mapChamps = new HashMap<String, String>(  );
        
        mapChamps.put( "user_cid", String.valueOf( nCid ) );
        try
        {
            json = ElasticConnexion.formatFullText( mapChamps );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }

        String jsonRetour = ElasticConnexion.sentToElasticPOST( uri, json );
        JsonNode retour = ElasticConnexion.setJsonToJsonTree( jsonRetour );

        JsonNode tmp = retour.findValue( "_source" );

        return buildCustomer( tmp );
    }
    

    /**
    * {@inheritDoc }
    */
    @Override
    public boolean isAutoComplete(  )
    {
        return true;
    }

    /**
    * {@inheritDoc }
    */
    @Override
    public String getAutoCompleteUrl(  )
    {
        String tmp = AppPathService.getProdUrl(  );

        return tmp.substring( 0, tmp.length(  ) - 1 ) + RestConstants.BASE_PATH + GRUElasticsConstants.PLUGIN_NAME +
        GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION;
    }
    
    /**
     * {@inheritDoc }
     */
     @Override
    public void updateCustomer ( Customer user )
    {
    	 fr.paris.lutece.plugins.grusupply.business.Customer grusupplyCustomer = searchCustomer( user.getId( ) );
    	
		 grusupplyCustomer.setName( user.getLastname(  ) );
		 grusupplyCustomer.setFirstName( user.getFirstname(  ) );
		 grusupplyCustomer.setEmail( user.getEmail(  ) );
		 grusupplyCustomer.setTelephoneNumber( user.getMobilePhone(  ) );
		 grusupplyCustomer.setFixedTelephoneNumber( user.getFixedPhoneNumber(  ) );
             	 
		 StorageService.instance(  ).store( grusupplyCustomer );
    }

    /**
     * Build a CustomerResult from a node
     * @param node
     * @return
     */
    private CustomerResult buildCustomerResult( JsonNode node )
    {

        CustomerResult customer = new CustomerResult(  );


        try{
            customer.setId( node.findValue( "user_cid" ).asInt(  ) );
            customer.setLastname( node.findValue( "last_name" ).asText(  ) );
            customer.setFirstname( node.findValue( "first_name" ).asText(  ) );
            customer.setEmail( node.findValue( "email" ).asText(  ) );
            customer.setMobilePhone( node.findValue( "telephoneNumber" ).asText(  ) );
        }
        catch(NullPointerException ex)
        {
        	error( "Parsing Customer fail"+ node.toString(), null );
        }
        return customer;
    }
    
    private fr.paris.lutece.plugins.grusupply.business.Customer buildCustomer( JsonNode node )
    {
    	fr.paris.lutece.plugins.grusupply.business.Customer customer = new fr.paris.lutece.plugins.grusupply.business.Customer(  );


        try{
            customer.setCustomerId( node.findValue( "user_cid" ).asInt(  ) );
            customer.setName( node.findValue( "last_name" ).asText(  ) );
            customer.setFirstName( node.findValue( "first_name" ).asText(  ) );
            customer.setEmail( node.findValue( "email" ).asText(  ) );
            customer.setTelephoneNumber( node.findValue( "telephoneNumber" ).asText(  ) );
            customer.setFixedTelephoneNumber( node.findValue( "fixed_telephone_number" ).asText(  ) );
            customer.setBirthday( node.findValue( "birthday" ).asText(  ) );
            customer.setCity( node.findValue( "city" ).asText(  ) );
            customer.setStreet( node.findValue( "street" ).asText(  ) );
            customer.setCityOfBirth( node.findValue( "cityOfBirth" ).asText(  ) );
            customer.setCivility( node.findValue( "civility" ).asText(  ) );
            customer.setPostalCode( node.findValue( "postalCode" ).asText(  ) );
            customer.setStayConnected( node.findValue( "stayConnected" ).asBoolean( ) );
        }
        catch(NullPointerException ex)
        {
        	error( "Parsing Customer fail"+ node.toString(), null );
        }
        return customer;
    }
    /**
     * Build an error response
     * @param strMessage The error message
     * @param ex An exception
     * @return The response
     */
    protected Response error( String strMessage, Throwable ex )
    {
        if ( ex != null )
        {
            AppLogService.error( strMessage, ex );
        }
        else
        {
            AppLogService.error( strMessage );
        }

        String strError = "{ \"status\": \"Error : " + strMessage + "\" }";

        return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( strError ).build(  );
    }
}
