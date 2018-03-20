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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.gruindexing.business.IIndexCustomerDAO;
import fr.paris.lutece.plugins.gruindexing.util.ElasticSearchParameterUtil;
import fr.paris.lutece.plugins.libraryelastic.business.bulk.AbstractSubRequest;
import fr.paris.lutece.plugins.libraryelastic.business.bulk.BulkRequest;
import fr.paris.lutece.plugins.libraryelastic.business.bulk.IndexSubRequest;
import fr.paris.lutece.plugins.libraryelastic.business.search.BoolQuery;
import fr.paris.lutece.plugins.libraryelastic.business.search.MatchLeaf;
import fr.paris.lutece.plugins.libraryelastic.business.search.SearchRequest;
import fr.paris.lutece.plugins.libraryelastic.util.Elastic;
import fr.paris.lutece.plugins.libraryelastic.util.ElasticClientException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import java.util.Collection;

/**
 * DAO and indexer implementation with Elasticsearch for Customer
 */
public class ElasticSearchCustomerDAO implements IIndexCustomerDAO
{
    private static final String KEY_SOURCE = "_source";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_CUSTOMER_CIVILITY = "civility";
    private static final String KEY_CUSTOMER_LAST_NAME = "last_name";
    private static final String KEY_CUSTOMER_FAMILY_NAME = "family_name";
    private static final String KEY_CUSTOMER_FIRST_NAME = "first_name";
    private static final String KEY_CUSTOMER_EMAIL = "email";
    private static final String KEY_CUSTOMER_MOBILE_PHONE_NUMBER = "mobile_phone_number";
    private static final String KEY_CUSTOMER_FIXED_PHONE_NUMBER = "fixed_phone_number";
    private static final String KEY_CUSTOMER_BIRTHDATE = "birthday";
    private static final String KEY_CUSTOMER_CONNECTION_ID = "connection_id";
    private static final String KEY_SUGGEST = "suggest";

    private static final String FILE_CUSTOMER_INDEXING_TEMPLATE = "/WEB-INF/plugins/gruindexing/elasticsearch_customer_indexing.template";
    private static final String FILE_MAPPING = "/WEB-INF/plugins/gruindexing/mapping.json";

    private final Elastic _elastic;
    private final ElasticSearchTemplate _esTemplateCustomerIndexing;
    private final ElasticSearchMapping _esMapping;

    /**
     * default constructor
     */
    public ElasticSearchCustomerDAO( )
    {
        super( );
        _elastic = new Elastic( ElasticSearchParameterUtil.PROP_URL_ELK_SERVER );
        _esTemplateCustomerIndexing = new ElasticSearchTemplate( Paths.get( AppPathService.getWebAppPath( ) + FILE_CUSTOMER_INDEXING_TEMPLATE ) );
        _esMapping = new ElasticSearchMapping( Paths.get( AppPathService.getWebAppPath( ) + FILE_MAPPING ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Customer> selectByFilter( Map<String, String> mapFilter )
    {
        List<Customer> listCustomer = new ArrayList<>( );

        SearchRequest search = buildSearchRequest( mapFilter );

        try
        {
            String strESResult = _elastic.search( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, search );
            JsonNode jsonESResult = ElasticSearchParameterUtil.setJsonToJsonTree( strESResult );
            List<JsonNode> listJsonCustomers = jsonESResult.findValues( KEY_SOURCE );

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
            AppLogService.error( "Error during searching customers from Elasticsearch", ex );
        }

        return listCustomer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Customer> selectByName( String strFirstName, String strLastName )
    {
        Map<String, String> mapFilter = new HashMap<String, String>( );

        if ( StringUtils.isNotEmpty( strFirstName ) )
        {
            mapFilter.put( KEY_CUSTOMER_FIRST_NAME, strFirstName );
        }

        if ( StringUtils.isNotEmpty( strLastName ) )
        {
            mapFilter.put( KEY_CUSTOMER_LAST_NAME, strLastName );
        }

        return selectByFilter( mapFilter );
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
            SearchRequest search = buildSearchRequest( mapFields );

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
     * {@inheritDoc }
     */
    @Override
    public void insert( Customer customer ) throws IndexingException
    {
        try
        {
            _elastic.create( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, ElasticSearchParameterUtil.PROP_PATH_ELK_TYPE_USER, customer.getId( ),
                    buildCustomerIndex( customer ) );
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( List<Customer> listCustomer ) throws IndexingException
    {
        if ( listCustomer != null && !listCustomer.isEmpty( ) )
        {
            try
            {
                BulkRequest bulkRequest = new BulkRequest( );

                Map<AbstractSubRequest, Object> mapSubRequest = new HashMap<AbstractSubRequest, Object>( );
                for ( Customer customer : listCustomer )
                {
                    mapSubRequest.put( new IndexSubRequest( customer.getId( ) ), buildCustomerIndex( customer ) );
                }
                bulkRequest.setMapSubAction( mapSubRequest );

                _elastic.createByBulk( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, ElasticSearchParameterUtil.PROP_PATH_ELK_TYPE_USER, bulkRequest );

            }
            catch( ElasticClientException ex )
            {
                AppLogService.error( ex + " :" + ex.getMessage( ), ex );
                throw new IndexingException( ex.getMessage( ), ex );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( Customer customer ) throws IndexingException
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
     * Builds the index request from the specified customer
     * 
     * @param customer
     *            the customer to index
     * @return the index request
     */
    private String buildCustomerIndex( Customer customer )
    {
        Map<String, String> mapPlaceholderValues = new HashMap<>( );
        mapPlaceholderValues.put( KEY_CUSTOMER_ID, customer.getId( ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_CONNECTION_ID, customer.getConnectionId( ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_LAST_NAME, manageNullValue( customer.getLastname( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_FIRST_NAME, manageNullValue( customer.getFirstname( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_FAMILY_NAME, manageNullValue( customer.getFamilyname( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_EMAIL, manageNullValue( customer.getEmail( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_BIRTHDATE, manageNullValue( customer.getBirthDate( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_CIVILITY, manageNullValue( Integer.toString( customer.getIdTitle( ) ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_MOBILE_PHONE_NUMBER, manageNullValue( customer.getMobilePhone( ) ) );
        mapPlaceholderValues.put( KEY_CUSTOMER_FIXED_PHONE_NUMBER, manageNullValue( customer.getFixedPhoneNumber( ) ) );

        for ( String strAttribute : customer.getAttributeNames( ) )
        {
            mapPlaceholderValues.put( strAttribute, customer.getAttribute( strAttribute ) );
        }

        return _esTemplateCustomerIndexing.build( mapPlaceholderValues );

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

        if ( node != null )
        {
            List<String> listAttributesKeys = new ArrayList<>( );
            Collection<String> listCustomerKeys = new ArrayList<>( );
            listCustomerKeys.add( KEY_CUSTOMER_ID );
            listCustomerKeys.add( KEY_CUSTOMER_CONNECTION_ID );
            listCustomerKeys.add( KEY_CUSTOMER_CIVILITY );
            listCustomerKeys.add( KEY_CUSTOMER_LAST_NAME );
            listCustomerKeys.add( KEY_CUSTOMER_FAMILY_NAME );
            listCustomerKeys.add( KEY_CUSTOMER_FIRST_NAME );
            listCustomerKeys.add( KEY_CUSTOMER_EMAIL );
            listCustomerKeys.add( KEY_CUSTOMER_FIXED_PHONE_NUMBER );
            listCustomerKeys.add( KEY_CUSTOMER_MOBILE_PHONE_NUMBER );
            listCustomerKeys.add( KEY_CUSTOMER_BIRTHDATE );
            listCustomerKeys.add( KEY_SUGGEST );
            node.fieldNames( ).forEachRemaining( key -> {
                if ( !listCustomerKeys.contains( key ) )
                {
                    listAttributesKeys.add( key );
                }
            } );
            customer.setId( findNodeValue( node, KEY_CUSTOMER_ID ) );
            customer.setConnectionId( findNodeValue( node, KEY_CUSTOMER_CONNECTION_ID ) );
            customer.setIdTitle( node.findValue( KEY_CUSTOMER_CIVILITY ) != null ? node.findValue( KEY_CUSTOMER_CIVILITY ).asInt( ) : 0 );
            customer.setLastname( findNodeValue( node, KEY_CUSTOMER_LAST_NAME ) );
            customer.setFamilyname( findNodeValue( node, KEY_CUSTOMER_FAMILY_NAME ) );
            customer.setFirstname( findNodeValue( node, KEY_CUSTOMER_FIRST_NAME ) );
            customer.setEmail( findNodeValue( node, KEY_CUSTOMER_EMAIL ) );
            customer.setFixedPhoneNumber( findNodeValue( node, KEY_CUSTOMER_FIXED_PHONE_NUMBER ) );
            customer.setMobilePhone( findNodeValue( node, KEY_CUSTOMER_MOBILE_PHONE_NUMBER ) );
            customer.setBirthDate( findNodeValue( node, KEY_CUSTOMER_BIRTHDATE ) );

            for ( String strAttributeName : listAttributesKeys )
            {
                customer.addAttributes( strAttributeName, findNodeValue( node, strAttributeName ) );
            }
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

    /**
     * Finds a node value
     * 
     * @param node
     *            the node containing the value
     * @param strKey
     *            the key associated to the value
     * @return the value of an empty String if not found
     */
    private static String findNodeValue( JsonNode node, String strKey )
    {
        String strResult = StringUtils.EMPTY;
        JsonNode nodeValue = node.findValue( strKey );

        if ( nodeValue != null )
        {
            strResult = nodeValue.asText( );
        }

        return strResult;
    }

    /**
     * Builds a search request for Elasticsearch.
     *
     * @param mapFields
     *            the fields to search
     * @return the search request
     */
    private static SearchRequest buildSearchRequest( Map<String, String> mapFields )
    {
        SearchRequest root = new SearchRequest( );
        BoolQuery query = new BoolQuery( );

        for ( Entry<String, String> searchParam : mapFields.entrySet( ) )
        {
            query.addMust( new MatchLeaf( searchParam.getKey( ), searchParam.getValue( ) ) );
        }

        root.setSearchQuery( query );

        if ( StringUtils.isNotBlank( ElasticSearchParameterUtil.PROPERTY_SIZE_ELK_SEARCH ) )
        {
            root.setSize( Integer.parseInt( ElasticSearchParameterUtil.PROPERTY_SIZE_ELK_SEARCH ) );
        }

        return root;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteAll( ) throws IndexingException
    {
        try
        {
            _elastic.deleteIndex( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX );
            _elastic.createMappings( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, _esMapping.get( ) );
        }
        catch( ElasticClientException e )
        {
            throw new IndexingException( "Error during the deletion of the ElasticSearch index", e );
        }
    }

}
