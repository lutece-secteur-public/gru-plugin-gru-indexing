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
    "demand_max_step",
    "demand_user_current_step",
    "demand_state",
    "demand_status",
    "notification_type",
    "crm_status_id",
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

    /** The _str demand max step. */
    private String _strDemandMaxStep;

    /** The _str demand user current step. */
    private String _strDemandUserCurrentStep;

    /** The _str demand state. */
    private String _strDemandState;

    /** The _n demand status. */
    private int _nDemandStatus;

    /** The _str notif type. */
    private String _strNotifType;

    /** The _n crm status. */
    private int _nCRMStatus;

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
     * @param strDemandMaxStep the str demand max step
     * @param strDemandUserCurrentStep the str demand user current step
     * @param strDemandState the str demand state
     * @param strNotifType the str notif type
     * @param nCRMStatus the n crm status
     * @param strReference the str reference
     * @param oSuggest the o suggest
     */
    public ESDemandDTO( CustomerDemandDTO oCustomerDemand, String strDemandId, String strDemandTypeId,
        String strDemandMaxStep, String strDemandUserCurrentStep, String strDemandState, String strNotifType,
        int nCRMStatus, String strReference, ESSuggestDTO oSuggest )
    {
        super(  );
        this._oCustomerDemand = oCustomerDemand;
        this._strDemandId = strDemandId;
        this._strDemandTypeId = strDemandTypeId;
        this._strDemandMaxStep = strDemandMaxStep;
        this._strDemandUserCurrentStep = strDemandUserCurrentStep;
        this._strDemandState = strDemandState;
        this._strNotifType = strNotifType;
        this._nCRMStatus = nCRMStatus;
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
     * Gets the demand max step.
     *
     * @return the demand max step
     */
    @JsonProperty( "demand_max_step" )
    public String getDemandMaxStep(  )
    {
        return _strDemandMaxStep;
    }

    /**
     * Sets the demand max step.
     *
     * @param nDemandMaxStep the new demand max step
     */
    @JsonProperty( "demand_max_step" )
    public void setDemandMaxStep( String nDemandMaxStep )
    {
        _strDemandMaxStep = nDemandMaxStep;
    }

    /**
     * Gets the demand user current step.
     *
     * @return the demand user current step
     */
    @JsonProperty( "demand_user_max_step" )
    public String getDemandUserCurrentStep(  )
    {
        return _strDemandUserCurrentStep;
    }

    /**
     * Sets the demand user current step.
     *
     * @param nDemandUserCurrentStep the new demand user current step
     */
    @JsonProperty( "demand_user_max_step" )
    public void setDemandUserCurrentStep( String nDemandUserCurrentStep )
    {
        _strDemandUserCurrentStep = nDemandUserCurrentStep;
    }

    /**
     * Gets the demand state.
     *
     * @return the demand state
     */
    @JsonProperty( "demand_state" )
    public String getDemandState(  )
    {
        return _strDemandState;
    }

    /**
     * Sets the demand state.
     *
     * @param nDemandState the new demand state
     */
    @JsonProperty( "demand_state" )
    public void setDemandState( String nDemandState )
    {
        _strDemandState = nDemandState;
    }

    /**
     * Gets the notif type.
     *
     * @return the notif type
     */
    @JsonProperty( "notification_type" )
    public String getNotifType(  )
    {
        return _strNotifType;
    }

    /**
     * Sets the notif type.
     *
     * @param strNotifType the new notif type
     */
    @JsonProperty( "notification_type" )
    public void setNotifType( String strNotifType )
    {
        _strNotifType = strNotifType;
    }

    /**
     * Gets the demand status.
     *
     * @return the demand status
     */
    @JsonProperty( "demand_status" )
    public int getDemandStatus(  )
    {
        return _nDemandStatus;
    }

    /**
     * Sets the demand status.
     *
     * @param nDemandStatus the new demand status
     */
    @JsonProperty( "demand_status" )
    public void setDemandStatus( int nDemandStatus )
    {
        this._nDemandStatus = nDemandStatus;
    }

    /**
     * Gets the CRM status.
     *
     * @return the CRM status
     */
    @JsonProperty( "crm_status_id" )
    public int getCRMStatus(  )
    {
        return _nCRMStatus;
    }

    /**
     * Sets the CRM status.
     *
     * @param nCRMStatus the new CRM status
     */
    @JsonProperty( "crm_status_id" )
    public void setCRMStatus( int nCRMStatus )
    {
        _nCRMStatus = nCRMStatus;
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
