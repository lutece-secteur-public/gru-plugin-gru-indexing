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

import fr.paris.lutece.plugins.gru.service.search.CustomerResult;
import fr.paris.lutece.plugins.gru.service.search.ISearchService;
import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.codehaus.jackson.JsonNode;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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

        // Gets the name/firstname enter by autocomplete
        HashMap<String, String> mapChamps = new HashMap<String, String>(  );
        mapChamps.put( "first_name", res[0] );
        mapChamps.put( "last_name", res[1] );

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
     * Build a CustomerResult from a node
     * @param node
     * @return
     */
    private static CustomerResult buildCustomerResult( JsonNode node )
    {
        if ( node == null )
        {
            throw new NullPointerException(  );
        }

        CustomerResult customer = new CustomerResult(  );
        customer.setId( node.findValue( "user_cid" ).asInt(  ) );
        customer.setLastname( node.findValue( "last_name" ).asText(  ) );
        customer.setFirstname( node.findValue( "first_name" ).asText(  ) );
        customer.setEmail( node.findValue( "email" ).asText(  ) );
        customer.setMobilePhone( node.findValue( "telephoneNumber" ).asText(  ) );

        return customer;
    }
}
