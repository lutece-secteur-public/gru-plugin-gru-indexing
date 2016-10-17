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

import fr.paris.lutece.plugins.grubusiness.business.notification.BackofficeNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.SMSNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.UserDashboardNotification;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


/**
 * The Class ESNotificationDTO.
 */
@JsonPropertyOrder( {"demande",
    "user_backoffice",
    "date_sollicitation",
    "user_email",
    "user_dashboard",
    "user_sms"
} )
public class ESNotificationDTO
{
    /** The _o notification demand. */
    private NotificationDemandDTO _oNotificationDemand;

    /** The _l date notification. */
    private long _lDateNotification;

    /** The _email notification. */
    private EmailNotification _emailNotification;

    /** The _dash board notification. */
    private UserDashboardNotification _userDashboardNotification;

    /** The _sms notification. */
    private SMSNotification _smsNotification;

    /** The _back office notification. */
    private BackofficeNotification _backOfficeNotification;

    /**
     * Instantiates a new ES notification dto.
     */
    public ESNotificationDTO(  )
    {
        super(  );
    }

    /**
     * Instantiates a new ES notification dto.
     *
     * @param oNotificationDemand the o notification demand
     * @param lDateSollicitation the l date sollicitation
     * @param emailNotification the email notification
     * @param userDashboardNotification the dash board notification
     * @param smsNotification the sms notification
     * @param backOfficeNotification the back office notification
     */
    public ESNotificationDTO( NotificationDemandDTO oNotificationDemand, long lDateSollicitation,
        EmailNotification emailNotification, UserDashboardNotification userDashboardNotification,
        SMSNotification smsNotification, BackofficeNotification backOfficeNotification )
    {
        super(  );
        this._oNotificationDemand = oNotificationDemand;
        this._lDateNotification = lDateSollicitation;
        this._emailNotification = emailNotification;
        this._userDashboardNotification = userDashboardNotification;
        this._smsNotification = smsNotification;
        this._backOfficeNotification = backOfficeNotification;
    }

    /**
     * Gets the notification demand.
     *
     * @return the notification demand
     */
    @JsonProperty( "demande" )
    public NotificationDemandDTO getNotificationDemand(  )
    {
        return _oNotificationDemand;
    }

    /**
     * Sets the notification demand.
     *
     * @param oNotificationDemand the new notification demand
     */
    @JsonProperty( "demande" )
    public void setNotificationDemand( NotificationDemandDTO oNotificationDemand )
    {
        this._oNotificationDemand = oNotificationDemand;
    }

    /**
     * Gets the date notification.
     *
     * @return the date notification
     */
    @JsonProperty( "notification_date" )
    public long getDateNotification(  )
    {
        return _lDateNotification;
    }

    /**
     * Sets the date notification.
     *
     * @param lDateSollicitation the new date notification
     */
    @JsonProperty( "notification_date" )
    public void setDateNotification( long lDateSollicitation )
    {
        this._lDateNotification = lDateSollicitation;
    }

    /**
     * Gets the user email.
     *
     * @return the user email
     */
    @JsonProperty( "user_email" )
    public EmailNotification getUserEmail(  )
    {
        return _emailNotification;
    }

    /**
     * Sets the user email.
     *
     * @param userEmail the new user email
     */
    @JsonProperty( "user_email" )
    public void setUserEmail( EmailNotification userEmail )
    {
        _emailNotification = userEmail;
    }

    /**
     * Gets the user dash board.
     *
     * @return the user dash board
     */
    @JsonProperty( "user_dashboard" )
    public UserDashboardNotification getUserDashBoard(  )
    {
        return _userDashboardNotification;
    }

    /**
     * Sets the user dash board.
     *
     * @param userDashBoard the new user dash board
     */
    @JsonProperty( "user_dashboard" )
    public void setUserDashBoard( UserDashboardNotification userDashboardNotification )
    {
        _userDashboardNotification = userDashboardNotification;
    }

    /**
     * Gets the user sms.
     *
     * @return the user sms
     */
    @JsonProperty( "user_sms" )
    public SMSNotification getUserSms(  )
    {
        return _smsNotification;
    }

    /**
     * Sets the user sms.
     *
     * @param userSms the new user sms
     */
    @JsonProperty( "user_sms" )
    public void setUserSms( SMSNotification userSms )
    {
        _smsNotification = userSms;
    }

    /**
     * Gets the user back office.
     *
     * @return the user back office
     */
    @JsonProperty( "user_backoffice" )
    public BackofficeNotification getUserBackOffice(  )
    {
        return _backOfficeNotification;
    }

    /**
     * Sets the user back office.
     *
     * @param userBackOffice the new user back office
     */
    @JsonProperty( "user_backoffice" )
    public void setUserBackOffice( BackofficeNotification userBackOffice )
    {
        _backOfficeNotification = userBackOffice;
    }
}
