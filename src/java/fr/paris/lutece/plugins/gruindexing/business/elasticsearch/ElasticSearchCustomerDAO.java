/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.gruindexing.business.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.customer.ICustomerDAO;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexingService;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.gruindexing.business.ESCustomerDTO;
import fr.paris.lutece.plugins.gruindexing.util.ElasticSearchParameterUtil;
import fr.paris.lutece.plugins.libraryelastic.business.search.SearchRequest;
import fr.paris.lutece.plugins.libraryelastic.util.Elastic;
import fr.paris.lutece.plugins.libraryelastic.util.ElasticClientException;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * DAO and indexer implementation with Elasticsearch for Customer
 */
public class ElasticSearchCustomerDAO implements IIndexingService<Customer>, ICustomerDAO
{
    public static final String KEY_CUSTOMER_ID = "user_cid";
    public static final String KEY_CUSTOMER_CIVILITY = "civility";
    public static final String KEY_CUSTOMER_LAST_NAME = "last_name";
    public static final String KEY_CUSTOMER_FAMILY_NAME = "family_name";
    public static final String KEY_CUSTOMER_FIRST_NAME = "first_name";
    public static final String KEY_CUSTOMER_EMAIL = "email";
    public static final String KEY_CUSTOMER_MOBILE_PHONE_NUMBER = "telephoneNumber";
    public static final String KEY_CUSTOMER_FIXED_PHONE_NUMBER = "fixed_telephone_number";
    public static final String KEY_CUSTOMER_BIRTHDATE = "birthday";
    public static final String KEY_CUSTOMER_CONNECTION_ID = "connection_id";
    private Elastic _elastic;

    /**
     * default constructor
     */
    public ElasticSearchCustomerDAO( )
    {
        super( );
        _elastic = new Elastic( ElasticSearchParameterUtil.PROP_URL_ELK_SERVER );
    }

    /**
     * {@inheritDoc }.
     *
     * @param strQuery
     *            the str query
     * @return the list
     */
    @Override
    public List<Customer> loadByName( String strFirstName, String strLastName )
    {
        List<Customer> listCustomer = new ArrayList<Customer>( );

        Map<String, String> mapFields = new HashMap<String, String>( );
        if ( StringUtils.isNotEmpty( strFirstName ) )
        {
            mapFields.put( KEY_CUSTOMER_FIRST_NAME, strFirstName );
        }
        if ( StringUtils.isNotEmpty( strLastName ) )
        {
            mapFields.put( KEY_CUSTOMER_LAST_NAME, strLastName );
        }

        SearchRequest search = ElasticSearchParameterUtil.buildSearchRequest( mapFields );

        try
        {
            String strESResult = _elastic.search( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, search );
            JsonNode jsonESResult = ElasticSearchParameterUtil.setJsonToJsonTree( strESResult );
            List<JsonNode> listJsonCustomers = jsonESResult.findValues( "_source" );

            for ( JsonNode jsonCustomer : listJsonCustomers )
            {
                if ( jsonCustomer != null )
                {
                    listCustomer.add( buildCustomer( jsonCustomer ) );
                }
            }
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return listCustomer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Customer load( String strCustomerId )
    {
        Customer customer = null;

        Map<String, String> mapFields = new HashMap<String, String>( );
        mapFields.put( KEY_CUSTOMER_ID, strCustomerId );

        try
        {
            SearchRequest search = ElasticSearchParameterUtil.buildSearchRequest( mapFields );

            String strESResult = _elastic.search( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, search );
            JsonNode jsonESResult = ElasticSearchParameterUtil.setJsonToJsonTree( strESResult );

            List<JsonNode> listJsonCustomers = jsonESResult.findValues( "_source" );
            JsonNode jsonCustomer = null;
            if ( listJsonCustomers != null && !listJsonCustomers.isEmpty( ) )
            {
                jsonCustomer = listJsonCustomers.get( 0 );
            }

            if ( jsonCustomer != null )
            {
                customer = buildCustomer( jsonCustomer );
            }
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return customer;
    }

    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void index( Customer customer ) throws IndexingException
    {
        try
        {
            _elastic.create( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, ElasticSearchParameterUtil.PROP_PATH_ELK_TYPE_USER, customer.getId( ),
                    buildCustomer( customer ) );
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }

    }

    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void deleteIndex( Customer customer ) throws IndexingException
    {
        try
        {
            _elastic.deleteDocument( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, ElasticSearchParameterUtil.PROP_PATH_ELK_TYPE_USER, customer.getId( ) );
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }
    }

    /**
     * Build a customer to an esCustomerDTO.
     *
     * @param customer
     *            the customer
     * @return the ES customer dto
     */
    private ESCustomerDTO buildCustomer( Customer customer )
    {
        ESCustomerDTO customerDTO = new ESCustomerDTO( );

        customerDTO.setCustomerId( customer.getId( ) );
        customerDTO.setConnectionId( customer.getConnectionId( ) );
        customerDTO.setName( manageNullValue( customer.getLastname( ) ) );
        customerDTO.setFirstName( manageNullValue( customer.getFirstname( ) ) );
        customerDTO.setFamilyName(manageNullValue( customer.getFamilyname( ) ) );
        customerDTO.setEmail( manageNullValue( customer.getEmail( ) ) );
        customerDTO.setBirthday( manageNullValue( customer.getBirthDate( ) ) );
        customerDTO.setCivility( manageNullValue( Integer.toString( customer.getIdTitle( ) ) ) );
        customerDTO.setTelephoneNumber( manageNullValue( customer.getMobilePhone( ) ) );
        customerDTO.setFixedTelephoneNumber( manageNullValue( customer.getFixedPhoneNumber( ) ) );
        customerDTO.setSuggest( );

        return customerDTO;
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

            customer.setConnectionId( node.findValue( KEY_CUSTOMER_CONNECTION_ID ) != null ? node.findValue( KEY_CUSTOMER_CONNECTION_ID ).asText( )
                    : StringUtils.EMPTY );
            customer.setIdTitle( node.findValue( KEY_CUSTOMER_CIVILITY ) != null ? node.findValue( KEY_CUSTOMER_CIVILITY ).asInt( ) : 0 );
            customer.setLastname( node.findValue( KEY_CUSTOMER_LAST_NAME ) != null ? node.findValue( KEY_CUSTOMER_LAST_NAME ).asText( ) : StringUtils.EMPTY );
            customer.setFamilyname( node.findValue( KEY_CUSTOMER_FAMILY_NAME ) != null ? node.findValue( KEY_CUSTOMER_FAMILY_NAME ).asText( ) : StringUtils.EMPTY );
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
            AppLogService.error( "Parsing Customer fail " + node.toString( ) );
        }

        return customer;
    }

    /**
     * Manages the case the specified String is {@code null}
     * 
     * @param strValue
     *            the String to manage
     * @return the correct String when the specified String is {@code null}, {@code strValue} otherwise
     */
    private static String manageNullValue( String strValue )
    {
        return ( strValue == null ) ? StringUtils.EMPTY : strValue;
    }

}
