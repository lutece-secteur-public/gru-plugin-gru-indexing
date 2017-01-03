/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.plugins.grusupply.business.Customer;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.HashMap;


/**
 * The Class ESDemandDTO.
 */
@JsonPropertyOrder( {"utilisateur",
    "demand_id",
    "demand_type_id",
    "reference",
    "suggest"
} )
public class ESDemandDTO
{
    /** The _o customer demand. */
    // Variables declarations 
    private CustomerDemandDTO _oCustomerDemand;

    /** The _str demand id. */
    private String _strDemandId;

    /** The _str demand type id. */
    private String _strDemandTypeId;

    /** The _str reference. */
    private String _strReference;

    /** The _o suggest. */
    private ESSuggestDTO _oSuggest;

    /**
     * Instantiates a new ES demand dto.
     */
    public ESDemandDTO(  )
    {
        super(  );
    }

    /**
     * Instantiates a new ES demand dto.
     *
     * @param oCustomerDemand the o customer demand
     * @param strDemandId the str demand id
     * @param strDemandTypeId the str demand type id
     * @param strReference the str reference
     * @param oSuggest the o suggest
     */
    public ESDemandDTO( CustomerDemandDTO oCustomerDemand, String strDemandId, String strDemandTypeId,
        String strReference, ESSuggestDTO oSuggest )
    {
        super(  );
        this._oCustomerDemand = oCustomerDemand;
        this._strDemandId = strDemandId;
        this._strDemandTypeId = strDemandTypeId;
        this._strReference = strReference;
        this._oSuggest = oSuggest;
    }

    /**
     * Gets the customer demand.
     *
     * @return the customer demand
     */
    @JsonProperty( "utilisateur" )
    public CustomerDemandDTO getCustomerDemand(  )
    {
        return _oCustomerDemand;
    }

    /**
     * Sets the customer demand.
     *
     * @param oCustomerDemand the new customer demand
     */
    @JsonProperty( "utilisateur" )
    public void setCustomerDemand( CustomerDemandDTO oCustomerDemand )
    {
        this._oCustomerDemand = oCustomerDemand;
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
     * @param nDemandId the new demand id
     */
    @JsonProperty( "demand_id" )
    public void setDemandId( String nDemandId )
    {
        _strDemandId = nDemandId;
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
     * @param nDemandTypeId the new demand type id
     */
    @JsonProperty( "demand_type_id" )
    public void setDemandTypeId( String nDemandTypeId )
    {
        _strDemandTypeId = nDemandTypeId;
    }

    /**
     * Gets the reference.
     *
     * @return the reference
     */
    @JsonProperty( "demand_reference" )
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * Sets the reference.
     *
     * @param strReference the new reference
     */
    @JsonProperty( "demand_reference" )
    public void setReference( String strReference )
    {
        _strReference = strReference;
    }

    /**
     * Gets the suggest.
     *
     * @return the suggest
     */
    @JsonProperty( "suggest" )
    public ESSuggestDTO getSuggest(  )
    {
        return _oSuggest;
    }

    /**
     * Sets the suggest.
     *
     * @param suggest the new suggest
     */
    @JsonIgnore
    public void setSuggest( ESSuggestDTO suggest )
    {
        _oSuggest = suggest;
    }

    /**
     * Sets the suggest.
     *
     * @param customer the new suggest
     */
    public void setSuggest( Customer customer )
    {
        ESSuggestDTO s = new ESSuggestDTO(  );

        // input
        String[] input = { _strReference };
        s.setInput( input );
        // Output
        s.setOutput( customer.getName(  ) + " " + customer.getFirstName(  ) );

        // Payload
        ESPayload oPayload = new ESPayload(  );
        HashMap<String, String> payload = new HashMap<String, String>(  );

        payload.put( "user_cid", String.valueOf( customer.getCustomerId(  ) ) );
        payload.put( "last_name", customer.getName(  ) );
        payload.put( "first_name", customer.getFirstName(  ) );
        payload.put( "birthday", customer.getBirthday(  ) );
        payload.put( "telephoneNumber", customer.getTelephoneNumber(  ) );
        payload.put( "FixeTelephoneNumber", customer.getFixedTelephoneNumber(  ) );
        payload.put( "email", customer.getEmail(  ) );
        payload.put( "reference", _strReference );

        oPayload.setElements( payload );
        s.setPayload( oPayload );
        this._oSuggest = s;
    }
}
