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

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationDemandDTO.
 */
public class NotificationDemandDTO
{
    /** The _str demand id. */
    private String _strDemandId;

    /** The _str demand type id. */
    private String _strDemandTypeId;

    /**
     * Instantiates a new notification demand dto.
     */
    public NotificationDemandDTO(  )
    {
        super(  );
    }

    /**
     * Instantiates a new notification demand dto.
     *
     * @param strDemandId the str demand id
     * @param strDemandTypeId the str demand type id
     */
    public NotificationDemandDTO( String strDemandId, String strDemandTypeId )
    {
        super(  );
        this._strDemandId = strDemandId;
        this._strDemandTypeId = strDemandTypeId;
    }

    /**
     * Gets the demand id.
     *
     * @return the demand id
     */
    @JsonProperty( "demand_id" )
    public String getDemandId(  )
    {
        return _strDemandId;
    }

    /**
     * Sets the demand id.
     *
     * @param strDemandId the new demand id
     */
    @JsonProperty( "demand_id" )
    public void setDemandId( String strDemandId )
    {
        this._strDemandId = strDemandId;
    }

    /**
     * Gets the demand type id.
     *
     * @return the demand type id
     */
    @JsonProperty( "demand_type_id" )
    public String getDemandTypeId(  )
    {
        return _strDemandTypeId;
    }

    /**
     * Sets the demand type id.
     *
     * @param strDemandTypeId the new demand type id
     */
    @JsonProperty( "demand_type_id" )
    public void setDemandTypeId( String strDemandTypeId )
    {
        this._strDemandTypeId = strDemandTypeId;
    }
}
