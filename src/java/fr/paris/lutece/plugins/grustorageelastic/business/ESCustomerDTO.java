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
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grustorageelastic.business;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.HashMap;


/**
 * This is the business class for the object Customer
 */
@JsonPropertyOrder( {"user_cid",
    "email",
    "last_name",
    "first_name",
    "stayConnected",
    "street",
    "telephoneNumber",
    "city",
    "cityOfBirth",
    "birthday",
    "civility",
    "postalCode",
    "suggest"
} )
public class ESCustomerDTO
{
    // Variables declarations 
    private int _nCustomerId;
    private String _strName;
    private String _strFirstName;
    private String _strEmail;
    private String _strBirthday;
    private String _strCivility;
    private String _strStreet;
    private String _strCityOfBirth;
    private boolean _bStayConnected;
    private String _strCity;
    private String _strPostalCode;
    private String _strTelephoneNumber;
    private ESSuggestDTO _oSuggest;

    public ESCustomerDTO(  )
    {
        super(  );
    }

    public ESCustomerDTO( int nCustomerId, String strName, String strFirstName, String strEmail, String strBirthday,
        String strCivility, String strStreet, String strCityOfBirth, boolean bStayConnected, String strCity,
        String strPostalCode, String strTelephoneNumber, ESSuggestDTO oSuggest )
    {
        super(  );
        this._nCustomerId = nCustomerId;
        this._strName = strName;
        this._strFirstName = strFirstName;
        this._strEmail = strEmail;
        this._strBirthday = strBirthday;
        this._strCivility = strCivility;
        this._strStreet = strStreet;
        this._strCityOfBirth = strCityOfBirth;
        this._bStayConnected = bStayConnected;
        this._strCity = strCity;
        this._strPostalCode = strPostalCode;
        this._strTelephoneNumber = strTelephoneNumber;
        this._oSuggest = oSuggest;
    }

    /**
     * Returns the CustomerId
     * @return The CustomerId
     */
    @JsonProperty( "user_cid" )
    public int getCustomerId(  )
    {
        return _nCustomerId;
    }

    /**
     * Sets the CustomerId
     * @param CustomerId The CustomerId
     */
    public void setCustomerId( int luserGuid )
    {
        _nCustomerId = luserGuid;
    }

    /**
     * Returns the Name
     * @return The Name
     */
    @JsonProperty( "last_name" )
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the FirstName
     * @return The FirstName
     */
    @JsonProperty( "first_name" )
    public String getFirstName(  )
    {
        return _strFirstName;
    }

    /**
     * Sets the FirstName
     * @param strFirstName The FirstName
     */
    public void setFirstName( String strFirstName )
    {
        _strFirstName = strFirstName;
    }

    /**
     * Returns the Email
     * @return The Email
     */
    @JsonProperty( "email" )
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the Email
     * @param strEmail The Email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Returns the Birthday
     * @return The Birthday
     */
    @JsonProperty( "birthday" )
    public String getBirthday(  )
    {
        return _strBirthday;
    }

    /**
     * Sets the Birthday
     * @param strBirthday The Birthday
     */
    public void setBirthday( String strBirthday )
    {
        _strBirthday = strBirthday;
    }

    /**
     * Returns the Civility
     * @return The Civility
     */
    @JsonProperty( "civility" )
    public String getCivility(  )
    {
        return _strCivility;
    }

    /**
     * Sets the Civility
     * @param strCivility The Civility
     */
    public void setCivility( String strCivility )
    {
        _strCivility = strCivility;
    }

    /**
     * Returns the Street
     * @return The Street
     */
    @JsonProperty( "street" )
    public String getStreet(  )
    {
        return _strStreet;
    }

    /**
     * Sets the Street
     * @param strStreet The Street
     */
    public void setStreet( String strStreet )
    {
        _strStreet = strStreet;
    }

    /**
     * Returns the CityOfBirth
     * @return The CityOfBirth
     */
    @JsonProperty( "cityOfBirth" )
    public String getCityOfBirth(  )
    {
        return _strCityOfBirth;
    }

    /**
     * Sets the CityOfBirth
     * @param strCityOfBirth The CityOfBirth
     */
    public void setCityOfBirth( String strCityOfBirth )
    {
        _strCityOfBirth = strCityOfBirth;
    }

    /**
     * Returns the StayConnected
     * @return The StayConnected
     */
    @JsonProperty( "stayConnected" )
    public boolean getStayConnected(  )
    {
        return _bStayConnected;
    }

    /**
     * Sets the StayConnected
     * @param StayConnected The StayConnected
     */
    public void setStayConnected( boolean stayConnected )
    {
        _bStayConnected = stayConnected;
    }

    /**
     * Returns the City
     * @return The City
     */
    @JsonProperty( "city" )
    public String getCity(  )
    {
        return _strCity;
    }

    /**
     * Sets the City
     * @param strCity The City
     */
    public void setCity( String strCity )
    {
        _strCity = strCity;
    }

    /**
     * Returns the PostalCode
     * @return The PostalCode
     */
    @JsonProperty( "postalCode" )
    public String getPostalCode(  )
    {
        return _strPostalCode;
    }

    /**
     * Sets the PostalCode
     * @param strPostalCode The PostalCode
     */
    public void setPostalCode( String strPostalCode )
    {
        _strPostalCode = strPostalCode;
    }

    /**
     * Returns the TelephoneNumber
     * @return The TelephoneNumber
     */
    @JsonProperty( "telephoneNumber" )
    public String getTelephoneNumber(  )
    {
        return _strTelephoneNumber;
    }

    /**
     * Sets the TelephoneNumber
     * @param strTelephoneNumber The TelephoneNumber
     */
    public void setTelephoneNumber( String strTelephoneNumber )
    {
        _strTelephoneNumber = strTelephoneNumber;
    }

    /**
     * Returns the suggest
     */
    @JsonProperty( "suggest" )
    public ESSuggestDTO getSuggest(  )
    {
        return _oSuggest;
    }

    /**
     * Sets the suggest
     */
    public void setSuggest(  )
    {
        ESSuggestDTO s = new ESSuggestDTO(  );

        // input
//        String[] input = { _strFirstName, _strName, _strTelephoneNumber, _strEmail };
        String[] input = { _strFirstName, _strName, _strFirstName+" "+_strName, _strName+" "+_strFirstName, _strTelephoneNumber  };
        s.setInput( input );
        // Output
        s.setOutput(  _strFirstName+ " " + _strName );

        // Payload
        ESPayload oPayload = new ESPayload(  );
        HashMap<String, String> payload = new HashMap<String, String>(  );

        payload.put( "user_cid", String.valueOf( _nCustomerId ) );
        payload.put( "last_name", _strName );
        payload.put( "first_name", _strFirstName );
        payload.put( "birthday", _strBirthday );
        payload.put( "telephoneNumber", _strTelephoneNumber );
        payload.put( "email", _strEmail );

        oPayload.setElements( payload );
        s.setPayload( oPayload );
        this._oSuggest = s;
    }
}
