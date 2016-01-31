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
package fr.paris.lutece.plugins.grustorage.elastic.business;

import org.codehaus.jackson.annotate.JsonProperty;

import fr.paris.lutece.plugins.grusupply.business.DashboardNotification;


/**
 * This is the business class for the object DashboardNotification
 */
public class ESDashboardNotificationDTO
{
    // Variables declarations 
    private String _strStatusText;
    private String _strMessage;
    private String _strSubject;
    private String _strSenderName;
    private String _strData;

    public ESDashboardNotificationDTO(DashboardNotification dashNotif) {
		super();
		this._strStatusText = dashNotif.getStatusText();
		this._strMessage = dashNotif.getMessage();
		this._strSubject = dashNotif.getSubject();
		this._strSenderName = dashNotif.getSenderName();
		this._strData = dashNotif.getData();
	}

	/**
     * Returns the StatusText
     * @return The StatusText
     */
    @JsonProperty( "status_text" )
    public String getStatusText(  )
    {
        return _strStatusText;
    }

    /**
     * Sets the StatusText
     * @param strStatusText The StatusText
     */
    @JsonProperty( "status_text" )
    public void setStatusText( String strStatusText )
    {
        _strStatusText = strStatusText;
    }

    /**
     * Returns the Message
     * @return The Message
     */
    @JsonProperty( "message" )
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * Sets the Message
     * @param strMessage The Message
     */
    @JsonProperty( "message" )
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
     * Returns the Subject
     * @return The Subject
     */
    @JsonProperty( "subject" )
    public String getSubject(  )
    {
        return _strSubject;
    }

    /**
     * Sets the Subject
     * @param strSubject The Subject
     */
    @JsonProperty( "subject" )
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }

    /**
     * Returns the SenderName
     * @return The SenderName
     */
    @JsonProperty( "sender_name" )
    public String getSenderName(  )
    {
        return _strSenderName;
    }

    /**
     * Sets the SenderName
     * @param strSenderName The SenderName
     */
    @JsonProperty( "sender_name" )
    public void setSenderName( String strSenderName )
    {
        _strSenderName = strSenderName;
    }

    /**
     * Returns the Data
     * @return The Data
     */
    @JsonProperty( "data" )
    public String getData(  )
    {
        return _strData;
    }

    /**
     * Sets the Data
     * @param strData The Data
     */
    @JsonProperty( "data" )
    public void setData( String strData )
    {
        _strData = strData;
    }
}
