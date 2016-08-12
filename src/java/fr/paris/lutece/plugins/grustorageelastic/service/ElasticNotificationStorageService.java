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
package fr.paris.lutece.plugins.grustorageelastic.service;

import fr.paris.lutece.plugins.grustorageelastic.business.CustomerDemandDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ESCustomerDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ESNotificationDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.business.NotificationDemandDTO;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grusupply.business.Customer;
import fr.paris.lutece.plugins.grusupply.business.Demand;
import fr.paris.lutece.plugins.grusupply.business.Notification;
import fr.paris.lutece.plugins.grusupply.service.INotificationStorageService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import javax.ws.rs.core.Response;


/**
 * The Class ElasticNotificationStorageService.
 */
public class ElasticNotificationStorageService implements INotificationStorageService
{
    /**
     * {@inheritDoc }.
     *
     * @param notification the notification
     */
    @Override
    public void store( Notification notification )
    {
        if ( notification == null )
        {
            throw new NullPointerException(  );
        }

        ObjectMapper mapper = new ObjectMapper(  );
        String jsonNotif = "";

        try
        {
            ESNotificationDTO notifDto = buildNotificationDto( notification, notification.getDemand(  ) );

            jsonNotif = mapper.writeValueAsString( notifDto );
            ElasticConnexion.sentToElasticPOST( ElasticConnexion.getESParam( 
                    GRUElasticsConstants.PATH_ELK_TYPE_NOTIFICATION, "" ), jsonNotif );
        }
        catch ( JsonGenerationException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( JsonMappingException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
    }

    /**
     * {@inheritDoc }.
     *
     * @param user the user
     */
    @Override
    public void store( Customer user )
    {
        if ( user == null )
        {
            throw new NullPointerException(  );
        }

        ObjectMapper mapper = new ObjectMapper(  );
        String jsonUser;

        try
        {
            ESCustomerDTO cutomerDTO = buildCustomer( user );
            jsonUser = mapper.writeValueAsString( cutomerDTO );
            ElasticConnexion.sentToElasticPOST( ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_USER,
                    user.getCustomerId(  ) ), jsonUser );
        }
        catch ( JsonGenerationException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( JsonMappingException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
    }

    /**
     * {@inheritDoc }.
     *
     * @param demand the demand
     */
    @Override
    public void store( Demand demand )
    {
        if ( demand == null )
        {
            throw new NullPointerException(  );
        }

        ESDemandDTO demandDTO = buildDemandDTO( demand, demand.getCustomer(  ) );

        ObjectMapper mapper = new ObjectMapper(  );
        String jsonDemand;

        try
        {
            jsonDemand = mapper.writeValueAsString( demandDTO );
            ElasticConnexion.sentToElasticPOST( ElasticConnexion.getESParam( 
                    GRUElasticsConstants.PATH_ELK_TYPE_DEMAND, demand.getReference(  ) ), jsonDemand );
        }
        catch ( JsonGenerationException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( JsonMappingException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
        catch ( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
    }

    /**
     * Build a cutomer to an esCustomerSTO.
     *
     * @param customer the customer
     * @return the ES customer dto
     */
    private ESCustomerDTO buildCustomer( Customer customer )
    {
        ESCustomerDTO customerDTO = new ESCustomerDTO(  );

        try
        {
            customerDTO.setCustomerId( customer.getCustomerId(  ) );
            customerDTO.setName( manageNullValue( customer.getName(  ) ) );
            customerDTO.setFirstName( manageNullValue( customer.getFirstName(  ) ) );
            customerDTO.setEmail( manageNullValue( customer.getEmail(  ) ) );
            customerDTO.setBirthday( manageNullValue( customer.getBirthday(  ) ) );
            customerDTO.setCivility( manageNullValue( customer.getCivility(  ) ) );
            customerDTO.setStreet( manageNullValue( customer.getStreet(  ) ) );
            customerDTO.setCityOfBirth( manageNullValue( customer.getCityOfBirth(  ) ) );
            customerDTO.setStayConnected( customer.getStayConnected(  ) );
            customerDTO.setCity( manageNullValue( customer.getCity(  ) ) );
            customerDTO.setPostalCode( manageNullValue( customer.getPostalCode(  ) ) );
            customerDTO.setTelephoneNumber( manageNullValue( customer.getTelephoneNumber(  ) ) );
            customerDTO.setFixedTelephoneNumber( manageNullValue( customer.getFixedTelephoneNumber(  ) ) );
            customerDTO.setSuggest(  );
        }
        catch ( NullPointerException ex )
        {
            error( "Demand OR Notofocation parsing fail", ex );
        }

        return customerDTO;
    }

    /**
     * Buid a Notification to an esNotificationDTO.
     *
     * @param notif the notif
     * @param demand the demand
     * @return the ES notification dto
     */
    private ESNotificationDTO buildNotificationDto( Notification notif, Demand demand )
    {
        ESNotificationDTO notifDTO = new ESNotificationDTO(  );

        try
        {
            NotificationDemandDTO nddto = new NotificationDemandDTO( String.valueOf( demand.getDemandId(  ) ),
                    String.valueOf( demand.getDemandTypeId(  ) ) );

            notifDTO.setDateNotification( notif.getDateNotification(  ) );
            notifDTO.setNotificationDemand( nddto );
            notifDTO.setUserEmail( notif.getUserEmail(  ) );
            notifDTO.setUserDashBoard( notif.getUserDashBoard(  ) );
            notifDTO.setUserSms( notif.getUserSms(  ) );
            notifDTO.setUserBackOffice( notif.getUserBackOffice(  ) );
        }
        catch ( NullPointerException ex )
        {
            error( "Demand OR Notofocation parsing fail", ex );
        }

        return notifDTO;
    }

    /**
     * Build a demand to an esDemandDTO.
     *
     * @param demand the demand
     * @param customer the customer
     * @return the ES demand dto
     */
    private ESDemandDTO buildDemandDTO( Demand demand, Customer customer )
    {
        ESDemandDTO demandDTO = new ESDemandDTO(  );

        try
        {
            CustomerDemandDTO customerDemand = new CustomerDemandDTO( String.valueOf( 
                        demand.getCustomer(  ).getCustomerId(  ) ) );
            demandDTO.setCustomerDemand( customerDemand );
            demandDTO.setDemandId( String.valueOf( demand.getDemandId(  ) ) );
            demandDTO.setDemandTypeId( String.valueOf( demand.getDemandTypeId(  ) ) );
            demandDTO.setDemandMaxStep( String.valueOf( demand.getDemandMaxStep(  ) ) );
            demandDTO.setDemandUserCurrentStep( String.valueOf( demand.getDemandUserCurrentStep(  ) ) );
            demandDTO.setDemandState( String.valueOf( demand.getDemandStatus(  ) ) );
            demandDTO.setNotifType( demand.getNotifType(  ) );
            demandDTO.setCRMStatus( demand.getCRMStatus(  ) );
            demandDTO.setReference( demand.getReference(  ) );
            demandDTO.setDemandStatus( demand.getDemandStatus(  ) );
            demandDTO.setSuggest( customer );
        }
        catch ( NullPointerException ex )
        {
            error( "Demand OR Notofocation parsing fail", ex );
        }

        return demandDTO;
    }

    /**
     * Build an error response.
     *
     * @param strMessage The error message
     * @param ex An exception
     * @return The response
     */
    private Response error( String strMessage, Throwable ex )
    {
        if ( ex != null )
        {
            AppLogService.error( strMessage, ex );
        }
        else
        {
            AppLogService.error( strMessage );
        }

        String strError = "{ \"status\": \"Error : " + strMessage + "\" }";

        return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( strError ).build(  );
    }

    /**
     * Manages the case the specified String is {@code null}
     * @param strValue the String to manage
     * @return the correct String when the specified String is {@code null}, {@code strValue} otherwise
     */
    private static String manageNullValue( String strValue )
    {
        return ( strValue == null ) ? StringUtils.EMPTY : strValue;
    }
}