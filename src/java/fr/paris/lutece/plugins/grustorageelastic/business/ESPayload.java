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

import fr.paris.lutece.portal.service.util.AppLogService;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

import java.util.HashMap;


// TODO: Auto-generated Javadoc
/**
 * The Class ESPayload.
 */
public class ESPayload
{
    /** The _o elements. */
    private HashMap<String, String> _oElements;

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public HashMap<String, String> getElements(  )
    {
        return _oElements;
    }

    /**
     * Sets the elements.
     *
     * @param oElements the o elements
     */
    public void setElements( HashMap<String, String> oElements )
    {
        this._oElements = oElements;
    }

    /**
     * To json.
     *
     * @return the string
     */
    public String toJson(  )
    {
        String strRetour = "";

        if ( _oElements == null )
        {
            throw new NullPointerException(  );
        }

        try
        {
            ObjectMapper mapper = new ObjectMapper(  );
            JsonNodeFactory factory = JsonNodeFactory.instance;

            ObjectNode root = new ObjectNode( factory );

            for ( String mapKey : _oElements.keySet(  ) )
            {
                root.put( mapKey, _oElements.get( mapKey ) );
            }

            strRetour = mapper.writeValueAsString( root );
        }
        catch ( NullPointerException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }

        return strRetour;
    }
}