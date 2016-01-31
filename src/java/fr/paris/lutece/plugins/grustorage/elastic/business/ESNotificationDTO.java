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
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * This is the business class for the object Notification
 */
@JsonPropertyOrder({"demande","user_backoffice","date_sollicitation","user_email","user_dashboard","user_sms"})
public class ESNotificationDTO
{
    private NotificationDemandDTO _oNotificationDemand;
    private String _strDateSollicitation;
    private ESEmailNotificationDTO _emailNotification;
    private ESDashboardNotificationDTO _dashBoardNotification;
    private ESSMSNotificationDTO _smsNotification;
    private ESBackofficeNotificationDTO _backOfficeNotification;

    /**
     * Returns the Demand of Notification
     * @return
     */
    @JsonProperty( "demande" )
	public NotificationDemandDTO getNotificationDemand() {
		return _oNotificationDemand;
	}

    /**
     * Sets the Demand of Notification
     * @param _oNotificationDemand
     */
	public void setNotificationDemand(NotificationDemandDTO _oNotificationDemand) {
		this._oNotificationDemand = _oNotificationDemand;
	}

    /**
     * Returns the Date of Sollicitation
     * @return
     */
    @JsonProperty( "date_sollicitation" )
    public String getDateSollicitation() {
		return _strDateSollicitation;
	}
    /**
     * Sets the date of sollicitation 
     * @param _strDateSollicitation
     */
	public void setDateSollicitation(String strDateSollicitation) {
		this._strDateSollicitation = strDateSollicitation;
	}

	/**
     * Returns the EmailNotification
     * @return The EmailNotification
     */
    @JsonProperty( "user_email" )
    public ESEmailNotificationDTO getUserEmail(  )
    {
        return _emailNotification;
    }

    /**
     * Sets the EmailNotification
     * @param UserEmail The EmailNotification
     */
    public void setUserEmail( ESEmailNotificationDTO UserEmail )
    {
        _emailNotification = UserEmail;
    }

    /**
     * Returns the UserDashBoard
     * @return The UserDashBoard
     */
    @JsonProperty( "user_dashboard" )
    public ESDashboardNotificationDTO getUserDashBoard(  )
    {
        return _dashBoardNotification;
    }

    /**
     * Sets the UserDashBoard
     * @param UserDashBoard The UserDashBoard
     */
    public void setUserDashBoard( ESDashboardNotificationDTO UserDashBoard )
    {
        _dashBoardNotification = UserDashBoard;
    }

    /**
     * Returns the UserSms
     * @return The UserSms
     */
    @JsonProperty( "user_sms" )
    public ESSMSNotificationDTO getUserSms(  )
    {
        return _smsNotification;
    }

    /**
     * Sets the UserSms
     * @param UserSms The UserSms
     */
    public void setUserSms( ESSMSNotificationDTO UserSms )
    {
        _smsNotification = UserSms;
    }

    /**
     * Returns the UserBackOffice
     * @return The UserBackOffice
     */
    @JsonProperty( "user_backoffice" )
    public ESBackofficeNotificationDTO getUserBackOffice(  )
    {
        return _backOfficeNotification;
    }

    /**
     * Sets the UserBackOffice
     * @param UserBackOffice The UserBackOffice
     */
    public void setUserBackOffice( ESBackofficeNotificationDTO UserBackOffice )
    {
        _backOfficeNotification = UserBackOffice;
    }
}
