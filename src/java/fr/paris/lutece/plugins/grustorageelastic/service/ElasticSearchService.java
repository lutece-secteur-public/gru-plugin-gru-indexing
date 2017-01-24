/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.gru.service.search.ISearchService;
import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

// TODO: Auto-generated Javadoc
/**
 * The Class ElasticSearchService.
 */
public class ElasticSearchService implements ISearchService
{
    private static final String KEY_CUSTOMER_ID = "user_cid";
    private static final String KEY_CUSTOMER_CIVILITY = "civility";
    private static final String KEY_CUSTOMER_LAST_NAME = "last_name";
    private static final String KEY_CUSTOMER_FIRST_NAME = "first_name";
    private static final String KEY_CUSTOMER_EMAIL = "email";
    private static final String KEY_CUSTOMER_MOBILE_PHONE_NUMBER = "telephoneNumber";
    private static final String KEY_CUSTOMER_FIXED_PHONE_NUMBER = "fixed_telephone_number";
    private static final String KEY_CUSTOMER_BIRTHDATE = "birthday";
    private static final String KEY_CUSTOMER_CONNECTION_ID = "connection_id";

    /**
     * {@inheritDoc }.
     *
     * @param strQuery
     *            the str query
     * @return the list
     */
    @Override
    public List<Customer> searchCustomer( String strFirstName, String strLastName )
    {
        List<Customer> listCustomer = new ArrayList<Customer>( );
        String uri = ElasticConnexion.getESParam( StringUtils.EMPTY, GRUElasticsConstants.PATH_ELK_SEARCH );
        String json = StringUtils.EMPTY;

        try
        {
            // Gets the name/firstname entered by autocomplete
            Map<String, String> mapFields = new HashMap<String, String>( );

            if ( StringUtils.isNotEmpty( strFirstName ) )
            {
                mapFields.put( KEY_CUSTOMER_FIRST_NAME, strFirstName );
            }

            if ( StringUtils.isNotEmpty( strLastName ) )
            {
                mapFields.put( KEY_CUSTOMER_LAST_NAME, strLastName );
            }

            json = ElasticConnexion.formatFullText( mapFields );
        }
        catch( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        try
        {
            String jsonRetour = ElasticConnexion.sentToElasticPOST( uri, json );
            JsonNode retour = ElasticConnexion.setJsonToJsonTree( jsonRetour );
            List<JsonNode> tmp = retour.findValues( "_source" );

            for ( JsonNode node : tmp )
            {
                if ( node != null )
                {
                    listCustomer.add( buildCustomer( node ) );
                }
            }
        }
        catch( HttpAccessException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return listCustomer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Customer searchCustomerById( String strCustomerId )
    {
        Customer customer = null;
        String uri = ElasticConnexion.getESParam( "", GRUElasticsConstants.PATH_ELK_SEARCH );
        String json = "";

        Map<String, String> mapFields = new HashMap<String, String>( );

        mapFields.put( KEY_CUSTOMER_ID, strCustomerId );

        try
        {
            json = ElasticConnexion.formatFullText( mapFields );

            String strESResult = ElasticConnexion.sentToElasticPOST( uri, json );
            JsonNode jsonESResult = ElasticConnexion.setJsonToJsonTree( strESResult );

            List<JsonNode> listJsonCustomers = jsonESResult.findValues( "_source" );
            JsonNode jsonCustomer = listJsonCustomers.get( 0 );

            if ( jsonCustomer != null )
            {
                customer = buildCustomer( jsonCustomer );
            }
        }
        catch( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }
        catch( HttpAccessException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return customer;
    }

    /**
     * {@inheritDoc }.
     *
     * @return true, if is auto complete
     */
    @Override
    public boolean isAutoComplete( )
    {
        return true;
    }

    /**
     * {@inheritDoc }.
     *
     * @return the auto complete url
     */
    @Override
    public String getAutoCompleteUrl( )
    {
        String tmp = AppPathService.getProdUrl( );

        return tmp.substring( 0, tmp.length( ) - 1 ) + RestConstants.BASE_PATH + GRUElasticsConstants.PLUGIN_NAME
                + GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION;
    }

    /**
     * Build a Customer from a node.
     *
     * @param node
     *            the node
     * @return the created customer
     */
    private Customer buildCustomer( JsonNode node )
    {
        Customer customer = new Customer( );

        try
        {
            customer.setId( node.findValue( KEY_CUSTOMER_ID ).asText( ) );

            customer.setAccountGuid( node.findValue( KEY_CUSTOMER_CONNECTION_ID ) != null ? node.findValue( KEY_CUSTOMER_CONNECTION_ID ).asText( )
                    : StringUtils.EMPTY );
            customer.setIdTitle( node.findValue( KEY_CUSTOMER_CIVILITY ) != null ? node.findValue( KEY_CUSTOMER_CIVILITY ).asInt( ) : 0 );
            customer.setLastname( node.findValue( KEY_CUSTOMER_LAST_NAME ) != null ? node.findValue( KEY_CUSTOMER_LAST_NAME ).asText( ) : StringUtils.EMPTY );
            customer.setFirstname( node.findValue( KEY_CUSTOMER_FIRST_NAME ) != null ? node.findValue( KEY_CUSTOMER_FIRST_NAME ).asText( ) : StringUtils.EMPTY );
            customer.setEmail( node.findValue( KEY_CUSTOMER_EMAIL ) != null ? node.findValue( KEY_CUSTOMER_EMAIL ).asText( ) : StringUtils.EMPTY );
            customer.setFixedPhoneNumber( node.findValue( KEY_CUSTOMER_FIXED_PHONE_NUMBER ) != null ? node.findValue( KEY_CUSTOMER_FIXED_PHONE_NUMBER )
                    .asText( ) : StringUtils.EMPTY );
            customer.setMobilePhone( node.findValue( KEY_CUSTOMER_MOBILE_PHONE_NUMBER ) != null ? node.findValue( KEY_CUSTOMER_MOBILE_PHONE_NUMBER ).asText( )
                    : StringUtils.EMPTY );
            customer.setBirthDate( node.findValue( KEY_CUSTOMER_BIRTHDATE ) != null ? node.findValue( KEY_CUSTOMER_BIRTHDATE ).asText( ) : StringUtils.EMPTY );
        }
        catch( NullPointerException ex )
        {
            error( "Parsing Customer fail" + node.toString( ), null );
        }

        return customer;
    }

    /**
     * Build an error response.
     *
     * @param strMessage
     *            The error message
     * @param ex
     *            An exception
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

        return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( strError ).build( );
    }
}
