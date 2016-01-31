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

import fr.paris.lutece.plugins.grusupply.business.BackofficeNotification;


/**
 * This is the business class for the object UserDashboard
 */
public class ESBackofficeNotificationDTO
{
    // Variables declarations 
    private String _strMessage;
    private String _strStatusText;
    private String _strNotifiedDashboard;
    private String _strDisplayDashboard;
    private String _strViewDashboard;
    private String _strNotifiedEmail;
    private String _strDisplayEmail;
    private String _strViewEmail;
    private int _nNotifiedSms;
    private int _nDisplaySms;
    private String _strViewSms;

    
    
    public ESBackofficeNotificationDTO(BackofficeNotification backNotif) {
		super();
		this._strMessage = backNotif.getMessage();
		this._strStatusText = backNotif.getStatusText();
		this._strNotifiedDashboard = backNotif.getNotifiedDashboard();
		this._strDisplayDashboard = backNotif.getDisplayDashboard();
		this._strViewDashboard = backNotif.getViewDashboard();
		this._strNotifiedEmail = backNotif.getNotifiedEmail();
		this._strDisplayEmail = backNotif.getDisplayEmail();
		this._strViewEmail = backNotif.getViewEmail();
		this._nNotifiedSms = backNotif.getDisplaySms();
		this._nDisplaySms = backNotif.getDisplaySms();
		this._strViewSms = backNotif.getViewSms();
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
     * Returns the NotifiedDashboard
     * @return The NotifiedDashboard
     */
    @JsonProperty( "notified_on_dashboard" )
    public String getNotifiedDashboard(  )
    {
        return _strNotifiedDashboard;
    }

    /**
     * Sets the NotifiedDashboard
     * @param strNotifiedDashboard The NotifiedDashboard
     */
    @JsonProperty( "notified_on_dashboard" )
    public void setNotifiedDashboard( String strNotifiedDashboard )
    {
        _strNotifiedDashboard = strNotifiedDashboard;
    }

    /**
     * Returns the DisplayDashboard
     * @return The DisplayDashboard
     */
    @JsonProperty( "display_level_dashboard_notification" )
    public String getDisplayDashboard(  )
    {
        return _strDisplayDashboard;
    }

    /**
     * Sets the DisplayDashboard
     * @param strDisplayDashboard The DisplayDashboard
     */
    @JsonProperty( "display_level_dashboard_notification" )
    public void setDisplayDashboard( String strDisplayDashboard )
    {
        _strDisplayDashboard = strDisplayDashboard;
    }

    /**
     * Returns the ViewDashboard
     * @return The ViewDashboard
     */
    @JsonProperty( "view_dashboard_notification" )
    public String getViewDashboard(  )
    {
        return _strViewDashboard;
    }

    /**
     * Sets the ViewDashboard
     * @param strViewDashboard The ViewDashboard
     */
    @JsonProperty( "view_dashboard_notification" )
    public void setViewDashboard( String strViewDashboard )
    {
        _strViewDashboard = strViewDashboard;
    }

    /**
     * Returns the NotifiedEmail
     * @return The NotifiedEmail
     */
    @JsonProperty( "notified_by_email" )
    public String getNotifiedEmail(  )
    {
        return _strNotifiedEmail;
    }

    /**
     * Sets the NotifiedEmail
     * @param strNotifiedEmail The NotifiedEmail
     */
    @JsonProperty( "notified_by_email" )
    public void setNotifiedEmail( String strNotifiedEmail )
    {
        _strNotifiedEmail = strNotifiedEmail;
    }

    /**
     * Returns the DisplayEmail
     * @return The DisplayEmail
     */
    @JsonProperty( "display_level_email_notification" )
    public String getDisplayEmail(  )
    {
        return _strDisplayEmail;
    }

    /**
     * Sets the DisplayEmail
     * @param strDisplayEmail The DisplayEmail
     */
    @JsonProperty( "display_level_email_notification" )
    public void setDisplayEmail( String strDisplayEmail )
    {
        _strDisplayEmail = strDisplayEmail;
    }

    /**
     * Returns the ViewEmail
     * @return The ViewEmail
     */
    @JsonProperty( "view_email_notification" )
    public String getViewEmail(  )
    {
        return _strViewEmail;
    }

    /**
     * Sets the ViewEmail
     * @param strViewEmail The ViewEmail
     */
    @JsonProperty( "view_email_notification" )
    public void setViewEmail( String strViewEmail )
    {
        _strViewEmail = strViewEmail;
    }

    /**
     * Returns the NNotifiedSms
     * @return The NNotifiedSms
     */
    @JsonProperty( "notified_by_sms" )
    public int getNotifiedSms(  )
    {
        return _nNotifiedSms;
    }

    /**
     * Sets the NotifiedSms
     * @param nNotifiedSms The NotifiedSms
     */
    @JsonProperty( "notified_by_sms" )
    public void setNotifiedSms( int nNotifiedSms )
    {
        _nNotifiedSms = nNotifiedSms;
    }

    /**
     * Returns the DisplaySms
     * @return The DisplaySms
     */
    @JsonProperty( "display_level_sms_notification" )
    public int getDisplaySms(  )
    {
        return _nDisplaySms;
    }

    /**
     * Sets the DisplaySms
     * @param nDisplaySms The DisplaySms
     */
    @JsonProperty( "display_level_sms_notification" )
    public void setDisplaySms( int nDisplaySms )
    {
        _nDisplaySms = nDisplaySms;
    }

    /**
     * Returns the ViewSms
     * @return The ViewSms
     */
    @JsonProperty( "view_sms_notification" )
    public String getViewSms(  )
    {
        return _strViewSms;
    }

    /**
     * Sets the ViewSms
     * @param strViewSms The ViewSms
     */
    @JsonProperty( "view_sms_notification" )
    public void setViewSms( String strViewSms )
    {
        _strViewSms = strViewSms;
    }
}
