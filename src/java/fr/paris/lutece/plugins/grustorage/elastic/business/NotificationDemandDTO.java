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
package fr.paris.lutece.plugins.grustorage.elastic.business;

import org.codehaus.jackson.annotate.JsonProperty;


public class NotificationDemandDTO
{
    private String _strDemandId;
    private String _strDemandIdType;

    public NotificationDemandDTO(  )
    {
        super(  );
    }

    public NotificationDemandDTO( String strDemandId, String strDemandIdType )
    {
        super(  );
        this._strDemandId = strDemandId;
        this._strDemandIdType = strDemandIdType;
    }

    /**
     * Returns the Demand Id
     * @return the Demand ID
     */
    @JsonProperty( "demand_id" )
    public String getDemandId(  )
    {
        return _strDemandId;
    }

    /**
     * Sets the Demand ID
     * @param _strDemandId
     */
    @JsonProperty( "demand_id" )
    public void setDemandId( String strDemandId )
    {
        this._strDemandId = strDemandId;
    }

    /**
     * Return the Demand ID Type
     * @return
     */
    @JsonProperty( "demand_type_id" )
    public String getDemandIdType(  )
    {
        return _strDemandIdType;
    }

    /**
     * Sets the Demand ID Type
     * @param _strDemandIdType
     */
    @JsonProperty( "demand_type_id" )
    public void setDemandIdType( String strDemandIdType )
    {
        this._strDemandIdType = strDemandIdType;
    }
}
