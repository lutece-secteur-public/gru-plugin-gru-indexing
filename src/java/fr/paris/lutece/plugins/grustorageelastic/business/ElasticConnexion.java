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
package fr.paris.lutece.plugins.grustorageelastic.business;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import java.util.Map;

/**
 * The Class ElasticConnexion.
 */
public final class ElasticConnexion
{
    /** The _client. */
    private static HttpAccess _clientHttp = new HttpAccess( );

    /** The _singleton. */
    private ElasticConnexion _singleton;

    /**
     * Gets the single instance of ElasticConnexion.
     *
     * @return single instance of ElasticConnexion
     */
    public ElasticConnexion getInstance( )
    {
        if ( _singleton == null )
        {
            return new ElasticConnexion( );
        }

        return _singleton;
    }

    /**
     * Gets the ES param.
     *
     * @param strPath
     *            the str path
     * @param strSpecif
     *            the str specif
     * @return the ES param
     */
    public static String getESParam( String strPath, String strSpecif )
    {
        String path = ( StringUtils.isBlank( strPath ) ) ? "" : AppPropertiesService.getProperty( strPath );
        String tmp = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER )
                + AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_PATH ) + path + strSpecif;

        return tmp;
    }

    /**
     * Gets the ES param.
     *
     * @param strPath
     *            the str path
     * @param strSpecif
     *            the str specif
     * @return the ES param
     */
    public static String getESParam( String strPath, int strSpecif )
    {
        return getESParam( strPath, String.valueOf( strSpecif ) );
    }

    /**
     * Sent to elastic put.
     *
     * @param uri
     *            the uri
     * @param json
     *            the json
     * @return the string
     */
    public static String sentToElasticPUT( String uri, String json ) throws HttpAccessException
    {
        return _clientHttp.doPutJSON( uri, json, null, null );
    }

    /**
     * Sent to elastic post.
     *
     * @param uri
     *            the uri
     * @param json
     *            the json
     * @throws HttpAccessException
     *             http access exception
     * @return the string
     */
    public static String sentToElasticPOST( String uri, String json ) throws HttpAccessException
    {
        return _clientHttp.doPostJSON( uri, json, null, null );
    }

    /**
     * Sent to elastic delete.
     *
     * @param uri
     *            the uri
     * @throws HttpAccessException
     *             http access exception
     * @return the string
     */
    public static String sentToElasticDELETE( String uri ) throws HttpAccessException
    {
        return _clientHttp.doDelete( uri, null, null, null, null );
    }

    /**
     * Format auto complete search.
     *
     * @param champ
     *            the champ
     * @return the string
     * @throws JsonGenerationException
     *             the json generation exception
     * @throws JsonMappingException
     *             the json mapping exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String formatAutoCompleteSearch( String champ ) throws JsonGenerationException, JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;

        ObjectNode root = new ObjectNode( factory );
        ObjectNode tmp = new ObjectNode( factory );
        ObjectNode completion = new ObjectNode( factory );
        completion.put( "field", "suggest" );
        completion.put( "size", "15" );

        tmp.put( "text", champ );
        tmp.set( "completion", completion );
        root.set( "user-suggest", tmp );

        return mapper.writeValueAsString( root );
    }

    /**
     * Format exact search.
     *
     * @param strKey
     *            the str key
     * @param strValue
     *            the str value
     * @return the string
     * @throws JsonGenerationException
     *             the json generation exception
     * @throws JsonMappingException
     *             the json mapping exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String formatExactSearch( String strKey, String strValue ) throws JsonGenerationException, JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = new ObjectNode( factory );
        ObjectNode tmp = new ObjectNode( factory );

        ObjectNode filtered = new ObjectNode( factory );
        ObjectNode query = new ObjectNode( factory );
        query.set( "match_all", new ObjectNode( factory ) );

        ObjectNode filter = new ObjectNode( factory );
        ObjectNode term = new ObjectNode( factory );

        term.put( strKey, strValue );
        filter.set( "term", term );

        filtered.set( "query", query );
        filtered.set( "filter", filter );

        tmp.set( "filtered", filtered );

        if ( StringUtils.isNotBlank( AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) ) )
        {
            root.put( "size", AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) );
        }

        root.set( "query", tmp );

        return mapper.writeValueAsString( root );
    }

    /**
     * Format exact search.
     *
     * @param map
     *            the map
     * @return the string
     * @throws JsonGenerationException
     *             the json generation exception
     * @throws JsonMappingException
     *             the json mapping exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String formatExactSearch( Map<String, String> map ) throws JsonGenerationException, JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = new ObjectNode( factory );
        ObjectNode query = new ObjectNode( factory );
        ObjectNode filtered = new ObjectNode( factory );
        ObjectNode filter = new ObjectNode( factory );
        ObjectNode bool = new ObjectNode( factory );

        ArrayNode must = new ArrayNode( factory );

        for ( String mapKey : map.keySet( ) )
        {
            ObjectNode tmp = new ObjectNode( factory );
            ObjectNode term = new ObjectNode( factory );
            term.put( mapKey, map.get( mapKey ) );
            tmp.set( "term", term );
            must.add( tmp );
        }

        bool.set( "must", must );
        filter.set( "bool", bool );
        filtered.set( "filter", filter );
        query.set( "filtered", filtered );

        if ( StringUtils.isNotBlank( AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) ) )
        {
            root.put( "size", AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) );
        }

        root.set( "query", query );

        return mapper.writeValueAsString( root );
    }

    /**
     * Format full text.
     *
     * @param map
     *            the map
     * @return the string
     * @throws JsonGenerationException
     *             the json generation exception
     * @throws JsonMappingException
     *             the json mapping exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String formatFullText( Map<String, String> map ) throws JsonGenerationException, JsonMappingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = new ObjectNode( factory );
        ObjectNode query = new ObjectNode( factory );
        ObjectNode bool = new ObjectNode( factory );
        ObjectNode and = new ObjectNode( factory );

        ArrayNode should = new ArrayNode( factory );

        for ( String mapKey : map.keySet( ) )
        {
            ObjectNode match = new ObjectNode( factory );
            ObjectNode tmp = new ObjectNode( factory );
            tmp.put( mapKey, map.get( mapKey ) );
            match.set( "match", tmp );
            should.add( match );
        }

        bool.set( "should", should );
        and.set( "bool", bool );
        query.set( "and", and );

        if ( StringUtils.isNotBlank( AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) ) )
        {
            root.put( "size", AppPropertiesService.getProperty( GRUElasticsConstants.SIZE_ELK_SEARCH_PARAM_VALUE ) );
        }

        root.set( "query", query );

        return mapper.writeValueAsString( root );
    }

    /**
     * Sets the json to json tree.
     *
     * @param strJson
     *            the str json
     * @return the json node
     */
    public static JsonNode setJsonToJsonTree( String strJson )
    {
        ObjectMapper mapper = new ObjectMapper( );
        JsonNode tmp = null;

        try
        {
            tmp = mapper.readTree( strJson );
        }
        catch( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return tmp;
    }
}
