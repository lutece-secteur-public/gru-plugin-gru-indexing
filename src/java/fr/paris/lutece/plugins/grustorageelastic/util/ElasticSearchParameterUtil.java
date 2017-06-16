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
package fr.paris.lutece.plugins.grustorageelastic.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.libraryelastic.business.search.BoolQuery;
import fr.paris.lutece.plugins.libraryelastic.business.search.MatchLeaf;
import fr.paris.lutece.plugins.libraryelastic.business.search.SearchRequest;
import fr.paris.lutece.plugins.libraryelastic.business.suggest.CompletionSuggestRequest;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Util class for elastic request
 */
public final class ElasticSearchParameterUtil
{
    // MARKERS
    public static final String MARKER_AUTOCOMPLETE = "autocomplete";
    public static final String MARKER_ITEM = "item";
    public static final String MARKER_ELEMENTS = "elements";
    public static final String MARKER_PAYLOAD = "payload";

    // CONSTANTS
    private static final String SIZE_ELK_SEARCH_PARAM_VALUE = "grustorage-elastics.sizeSearchParamValue";

    // PROPERTIES KEYS
    private static final String URL_ELK_SERVER = "grustorage-elastics.urlElk";
    private static final String PATH_ELK_INDEX = "grustorage-elastics.index";
    private static final String PATH_ELK_TYPE_USER = "grustorage-elastics.typeUser";

    // PATHS
    public static final String PROP_URL_ELK_SERVER = AppPropertiesService.getProperty( URL_ELK_SERVER );
    public static final String PROP_PATH_ELK_INDEX = AppPropertiesService.getProperty( PATH_ELK_INDEX );
    public static final String PROP_PATH_ELK_TYPE_USER = AppPropertiesService.getProperty( PATH_ELK_TYPE_USER );

    /**
     * Instantiates a new GRU elastics constants.
     */
    private ElasticSearchParameterUtil( )
    {
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
    public static CompletionSuggestRequest buildAutoCompleteSearch( String champ )
    {
        CompletionSuggestRequest suggest = new CompletionSuggestRequest( );
        suggest.setMatchType( "text" );
        suggest.setMatchValue( champ );
        suggest.setSize( 15 );

        return suggest;
    }

    /**
     * Build a search requet for elasticsearch.
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
    public static SearchRequest buildSearchRequest( Map<String, String> map )
    {
        SearchRequest root = new SearchRequest( );
        BoolQuery query = new BoolQuery( );
        for ( Entry<String, String> searchParam : map.entrySet( ) )
        {
            query.addShould( new MatchLeaf( searchParam.getKey( ), searchParam.getValue( ) ) );
        }

        root.setSearchQuery( query );

        if ( StringUtils.isNotBlank( AppPropertiesService.getProperty( SIZE_ELK_SEARCH_PARAM_VALUE ) ) )
        {
            root.setSize( Integer.parseInt( AppPropertiesService.getProperty( SIZE_ELK_SEARCH_PARAM_VALUE ) ) );
        }

        return root;
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
