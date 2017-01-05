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
package fr.paris.lutece.plugins.grustorageelastic.web.rs;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/**
 * The Class GRUElasticRestService.
 */
@Path( RestConstants.BASE_PATH + GRUElasticsConstants.PLUGIN_NAME )
public class GRUElasticRestService
{
    /**
     * Autocomplete.
     *
     * @param strQuery the str query
     * @return the string
     */
    @GET
    @Path( GRUElasticsConstants.PATH_ELASTIC_AUTOCOMPLETION )
    @Produces( MediaType.APPLICATION_JSON )
    public String autocomplete( @QueryParam( "query" )
    String strQuery )
    {
        String uri = ElasticConnexion.getESParam( StringUtils.EMPTY, GRUElasticsConstants.PATH_ELK_SUGGEST );
        String json = StringUtils.EMPTY;
        String retour = StringUtils.EMPTY;

        try
        {
            json = ElasticConnexion.formatAutoCompleteSearch( strQuery );

            String jsonRetour = ElasticConnexion.sentToElasticPOST( uri, json );
            JsonNode node = ElasticConnexion.setJsonToJsonTree( jsonRetour );
            retour = getInfoAutocomplete( node );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }

        return retour;
    }

    /**
     * Method which permit to find and format the result of an autocomplete.
     *
     * @param nodeTree the node tree
     * @return the info autocomplete
     * @throws JsonGenerationException the json generation exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String getInfoAutocomplete( JsonNode nodeTree )
        throws JsonGenerationException, JsonMappingException, IOException
    {
        List<JsonNode> payload = nodeTree.findValues( GRUElasticsConstants.MARKER_PAYLOAD );

        ObjectMapper mapper = new ObjectMapper(  );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = new ObjectNode( factory );
        ArrayNode autocomplete = new ArrayNode( factory );

        for ( JsonNode node : payload )
        {
            ObjectNode item = new ObjectNode( factory );

            node = node.path( GRUElasticsConstants.MARKER_ELEMENTS );

            if ( !node.path( GRUElasticsConstants.MARKER_LAST_NAME ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_LAST_NAME,
                    node.get( GRUElasticsConstants.MARKER_LAST_NAME ).asText(  ) );
            }

            if ( !node.path( GRUElasticsConstants.MARKER_FIRST_NAME ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_FIRST_NAME,
                    node.get( GRUElasticsConstants.MARKER_FIRST_NAME ).asText(  ) );
            }

            if ( !node.path( GRUElasticsConstants.MARKER_REFERENCE ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_REFERENCE,
                    node.get( GRUElasticsConstants.MARKER_REFERENCE ).asText(  ) );
            }

            if ( !node.path( GRUElasticsConstants.MARKER_DEMAND_ID ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_DEMAND_ID,
                    node.get( GRUElasticsConstants.MARKER_DEMAND_ID ).asText(  ) );
            }

            if ( !node.path( GRUElasticsConstants.MARKER_DEMAND_TYPE_ID ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_DEMAND_TYPE_ID,
                    node.get( GRUElasticsConstants.MARKER_DEMAND_TYPE_ID ).asText(  ) );
            }

            if ( !node.path( GRUElasticsConstants.MARKER_USER_CID ).isMissingNode(  ) )
            {
                item.put( GRUElasticsConstants.MARKER_USER_CID,
                    node.get( GRUElasticsConstants.MARKER_USER_CID ).asText(  ) );
            }

            ObjectNode tmp = new ObjectNode( factory );
            tmp.set( GRUElasticsConstants.MARKER_ITEM, item );
            autocomplete.add( tmp );
        }

        root.set( GRUElasticsConstants.MARKER_AUTOCOMPLETE, autocomplete );

        String test = mapper.writeValueAsString( root );

        return test;
    }
}
