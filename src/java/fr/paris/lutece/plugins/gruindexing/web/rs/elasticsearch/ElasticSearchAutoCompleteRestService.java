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
package fr.paris.lutece.plugins.gruindexing.web.rs.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.paris.lutece.plugins.gruindexing.business.elasticsearch.ElasticSearchTemplate;
import fr.paris.lutece.plugins.gruindexing.util.ElasticSearchParameterUtil;
import fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.AutocompletePlaceholderFilterChain;
import fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.AutocompletePlaceholderFilterChainFactory;
import fr.paris.lutece.plugins.libraryelastic.util.Elastic;
import fr.paris.lutece.plugins.libraryelastic.util.ElasticClientException;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * The Class GRUElasticRestService.
 */
@Path( RestConstants.BASE_PATH + ElasticSearchAutoCompleteRestService.PATH_SERVICE )
public class ElasticSearchAutoCompleteRestService
{
    public static final String PATH_SERVICE = "elasticsearch/";
    public static final String PATH_AUTOCOMPLETION = "autocomplete";

    private static final String FILE_AUTOCOMPLETE_TEMPLATE = "/WEB-INF/plugins/gruindexing/elasticsearch_autocomplete.template";

    // Keys
    private static final String KEY_SOURCE = "_source";
    private static final String KEY_AUTOCOMPLETE = "autocomplete";

    private final ElasticSearchTemplate _esTemplateAutocomplete;

    /**
     * Constructor
     */
    public ElasticSearchAutoCompleteRestService( )
    {
        super( );

        _esTemplateAutocomplete = new ElasticSearchTemplate( Paths.get( AppPathService.getWebAppPath( ) + FILE_AUTOCOMPLETE_TEMPLATE ) );
    }

    /**
     * Autocomplete.
     *
     * @param request
     *            the request
     * @return the string
     */
    @GET
    @Path( PATH_AUTOCOMPLETION )
    @Produces( MediaType.APPLICATION_JSON )
    public String autocomplete( @Context HttpServletRequest request )
    {
        String retour = StringUtils.EMPTY;

        try
        {
            Map<String, String> mapPlaceholderValues = new HashMap<>( );

            AutocompletePlaceholderFilterChain placeholderFilterChain = AutocompletePlaceholderFilterChainFactory.getInstance( ).createFilterChain( );
            placeholderFilterChain.doFilter( request, mapPlaceholderValues );

            Elastic elastic = null;
            if ( StringUtils.isNotEmpty( ElasticSearchParameterUtil.PROP_URL_ELK_LOGIN ) && StringUtils.isNotEmpty( ElasticSearchParameterUtil.PROP_URL_ELK_PWD ) )
            {
                elastic = new Elastic( ElasticSearchParameterUtil.PROP_URL_ELK_SERVER , ElasticSearchParameterUtil.PROP_URL_ELK_LOGIN, ElasticSearchParameterUtil.PROP_URL_ELK_PWD );
            }
            else
            {
                elastic = new Elastic( ElasticSearchParameterUtil.PROP_URL_ELK_SERVER  );
            }
            String jsonRetour = elastic.suggest( ElasticSearchParameterUtil.PROP_PATH_ELK_INDEX, _esTemplateAutocomplete.build( mapPlaceholderValues ) );

            JsonNode node = ElasticSearchParameterUtil.setJsonToJsonTree( jsonRetour );
            retour = getInfoAutocomplete( node );
        }
        catch( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }
        catch( ElasticClientException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
        }

        return retour;
    }

    /**
     * Method which permit to find and format the result of an autocomplete.
     *
     * @param nodeESAutocomplete
     *            the autocomplete node from Elasticsearch
     * @return the info autocomplete
     * @throws JsonProcessingException
     *             if the JSON result cannot be processed
     */
    private static String getInfoAutocomplete( JsonNode nodeESAutocomplete ) throws JsonProcessingException
    {
        List<JsonNode> listPayloads = nodeESAutocomplete.findValues( KEY_SOURCE );

        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode nodeRoot = new ObjectNode( factory );
        ArrayNode autocomplete = new ArrayNode( factory );

        autocomplete.addAll( listPayloads );

        nodeRoot.set( KEY_AUTOCOMPLETE, autocomplete );

        return mapper.writeValueAsString( nodeRoot );
    }
}
