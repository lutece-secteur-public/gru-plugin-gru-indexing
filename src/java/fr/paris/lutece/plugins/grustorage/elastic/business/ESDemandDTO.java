/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
 * StringERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grustorage.elastic.business;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.paris.lutece.plugins.grusupply.business.Customer;


/**
 * This is the business class for the object Demand
 */
@JsonPropertyOrder({"utilisateur","demand_id","demand_id_type","demand_max_step","demand_user_current_step","demand_state","notification_type","date_demande","crm_status_id","reference","suggest"})
public class ESDemandDTO
{
    // Variables declarations 
	private CustomerDemandDTO _oCustomerDemand;
    private String _strDemandId;
    private String _strDemandIdType;
    private String _strDemandMaxStep;
    private String _strDemandUserCurrentStep;
    private String _strDemandState;
    private String _strNotifType;
    private String _strDateDemand;
    private int _nCRMStatus;
    private String _strReference;
    private ESSuggestDTO _oSuggest;

    

    /**
     * Return the Customer Cid for a Demand
     * @return
     */
    @JsonProperty("utilisateur")
    public CustomerDemandDTO getCustomerDemand() {
		return _oCustomerDemand;
	}
    /**
     * Sets the Customer Cid for a demand
     * @param _oCustomerDemand
     */
	public void setCustomerDemand(CustomerDemandDTO _oCustomerDemand) {
		this._oCustomerDemand = _oCustomerDemand;
	}

    /**
     * Returns the DemandId
     * @return The DemandId
     */
    @JsonProperty( "demand_id" )
    public String getDemandId(  )
    {
        return _strDemandId;
    }

    /**
     * Sets the DemandId
     * @param nDemandId The DemandId
     */
    public void setDemandId( String nDemandId )
    {
        _strDemandId = nDemandId;
    }

    /**
     * Returns the DemandIdType
     * @return The DemandIdType
     */
    @JsonProperty( "demand_id_type" )
    public String getDemandIdType(  )
    {
        return _strDemandIdType;
    }

    /**
     * Sets the DemandIdType
     * @param nDemandIdType The DemandIdType
     */
    public void setDemandIdType( String nDemandIdType )
    {
        _strDemandIdType = nDemandIdType;
    }

    /**
     * Returns the DemandMaxStep
     * @return The DemandMaxStep
     */
    @JsonProperty( "demand_max_step" )
    public String getDemandMaxStep(  )
    {
        return _strDemandMaxStep;
    }

    /**
     * Sets the DemandMaxStep
     * @param nDemandMaxStep The DemandMaxStep
     */
    public void setDemandMaxStep( String nDemandMaxStep )
    {
        _strDemandMaxStep = nDemandMaxStep;
    }

    /**
     * Returns the DemandUserCurrentStep
     * @return The DemandUserCurrentStep
     */
    @JsonProperty( "demand_user_max_step" )
    public String getDemandUserCurrentStep(  )
    {
        return _strDemandUserCurrentStep;
    }

    /**
     * Sets the DemandUserCurrentStep
     * @param nDemandUserCurrentStep The DemandUserCurrentStep
     */
    public void setDemandUserCurrentStep( String nDemandUserCurrentStep )
    {
        _strDemandUserCurrentStep = nDemandUserCurrentStep;
    }

    /**
     * Returns the DemandState
     * @return The DemandState
     */
    @JsonProperty( "demand_state" )
    public String getDemandState(  )
    {
        return _strDemandState;
    }

    /**
     * Sets the DemandState
     * @param nDemandState The DemandState
     */
    public void setDemandState( String nDemandState )
    {
        _strDemandState = nDemandState;
    }

    /**
     * Returns the NotifType
     * @return The NotifType
     */
    @JsonProperty( "notification_type" )
    public String getNotifType(  )
    {
        return _strNotifType;
    }

    /**
     * Sets the NotifType
     * @param strNotifType The NotifType
     */
    public void setNotifType( String strNotifType )
    {
        _strNotifType = strNotifType;
    }

    /**
     * Returns the DateDemand
     * @return The DateDemand
     */
    @JsonProperty( "date_demand" )
    public String getDateDemand(  )
    {
        return _strDateDemand;
    }

    /**
     * Sets the DateDemand
     * @param strDateDemand The DateDemand
     */
    public void setDateDemand( String strDateDemand )
    {
        _strDateDemand = strDateDemand;
    }

    /**
     * Returns the CRMStatus
     * @return The CRMStatus
     */
    @JsonProperty( "crm_status_id" )
    public int getCRMStatus(  )
    {
        return _nCRMStatus;
    }

    /**
     * Sets the CRMStatus
     * @param nCRMStatus The CRMStatus
     */
    public void setCRMStatus( int nCRMStatus )
    {
        _nCRMStatus = nCRMStatus;
    }

    /**
     * Returns the Reference
     * @return The Reference
     */
    @JsonProperty( "reference" )
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * Sets the Reference
     * @param strReference The Reference
     */
    public void setReference( String strReference )
    {
        _strReference = strReference;
    }
    /**
     * Return the Suggest
     * @return The suggest
     */
    @JsonProperty( "suggest" )
	public ESSuggestDTO getSuggest() {
		return _oSuggest;
	}

	/**
	 * Sets the suggest
	 */
	public void setSuggest(Customer customer) 
	{
		ESSuggestDTO s = new ESSuggestDTO( );
		// input
		String[ ] input = { _strReference };
		s.setInput(input);
		// Output
		s.setOutput(customer.getName()+" "+customer.getFirstName());
		// Payload
		ESPayload oPayload = new ESPayload();
		HashMap<String, String> payload = new HashMap<>();
		
		payload.put("user_cid", String.valueOf(customer.getCustomerId()));
		payload.put("birthday", customer.getBirthday());
		payload.put("telephoneNumber", customer.getTelephoneNumber());
		payload.put("email", customer.getEmail());
		payload.put("reference", _strReference);
		
    	oPayload.setElements(payload);
    	s.setPayload(oPayload);
    	this._oSuggest = s;
	}
    
}
