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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * <p>
 * This class represents a content template for Elasticsearch request.
 * </p>
 * <p>
 * The template contains placeholders which are replaced by the {@code build} method.
 * </p>
 *
 */
public class ElasticSearchTemplate
{
    private static final String REGEXP_NEWLINE = "\\r\\n|\\r|\\n";

    private String _strTemplate;

    /**
     * Constructs an {@code ElasticSearchTemplate} instance from the specified path
     * 
     * @param pathTemplate
     *            the path containing the request content template
     */
    public ElasticSearchTemplate( Path pathTemplate )
    {
        try
        {
            _strTemplate = new String( Files.readAllBytes( pathTemplate ), StandardCharsets.UTF_8 ).replaceAll( REGEXP_NEWLINE, StringUtils.EMPTY );
        }
        catch( IOException e )
        {
            AppLogService.error( "Cannot open the template file : " + pathTemplate.getFileName( ), e );
            _strTemplate = StringUtils.EMPTY;
        }
    }

    /**
     * Builds the final request content by replacing the placeholders in the template with the specified values
     * 
     * @param mapPlaceholderValues
     *            the values used to replace the placeholders in the template
     * @return the final request content
     */
    public String build( Map<String, String> mapPlaceholderValues )
    {
        StrSubstitutor sub = new StrSubstitutor( mapPlaceholderValues );

        return sub.replace( _strTemplate );
    }
}
