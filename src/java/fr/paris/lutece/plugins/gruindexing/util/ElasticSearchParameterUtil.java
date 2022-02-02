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
package fr.paris.lutece.plugins.gruindexing.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Util class for elastic request
 */
public final class ElasticSearchParameterUtil
{
    // PROPERTIES KEYS
    public static final String URL_ELK_SERVER = "gru-indexing.urlElk";
    public static final String PATH_ELK_INDEX = "gru-indexing.index";
    public static final String PATH_ELK_TYPE_USER = "gru-indexing.typeUser";
    public static final String SIZE_ELK_SEARCH = "gru-indexing.sizeSearchParamValue";

    // PATHS
    public static final String PROP_URL_ELK_SERVER = AppPropertiesService.getProperty( URL_ELK_SERVER );
    public static final String PROP_PATH_ELK_INDEX = AppPropertiesService.getProperty( PATH_ELK_INDEX );
    public static final String PROPERTY_SIZE_ELK_SEARCH = AppPropertiesService.getProperty( SIZE_ELK_SEARCH );

    /**
     * Instantiates a new GRU elastics constants.
     */
    private ElasticSearchParameterUtil( )
    {
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
