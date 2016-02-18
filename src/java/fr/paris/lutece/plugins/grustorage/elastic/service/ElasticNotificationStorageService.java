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
package fr.paris.lutece.plugins.grustorage.elastic.service;

import fr.paris.lutece.plugins.grustorage.elastic.business.CustomerDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESCustomerDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ESNotificationDTO;
import fr.paris.lutece.plugins.grustorage.elastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorage.elastic.business.NotificationDemandDTO;
import fr.paris.lutece.plugins.grustorage.elastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grusupply.business.Customer;
import fr.paris.lutece.plugins.grusupply.business.Demand;
import fr.paris.lutece.plugins.grusupply.business.Notification;
import fr.paris.lutece.plugins.grusupply.service.INotificationStorageService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class ElasticNotificationStorageService implements INotificationStorageService
{
    /**
     * {@inheritDoc }
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
     * {@inheritDoc }
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
     * {@inheritDoc }
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
     * Build a cutomer to an esCustomerSTO
     * @param customer
     * @return
     */
    private static ESCustomerDTO buildCustomer( Customer customer )
    {
        if ( customer == null )
        {
            throw new NullPointerException(  );
        }

        ESCustomerDTO customerDTO = new ESCustomerDTO(  );
        customerDTO.setCustomerId( customer.getCustomerId(  ) );
        customerDTO.setName( customer.getName(  ) );
        customerDTO.setFirstName( customer.getFirstName(  ) );
        customerDTO.setEmail( customer.getEmail(  ) );
        customerDTO.setBirthday( customer.getBirthday(  ) );
        customerDTO.setCivility( customer.getCivility(  ) );
        customerDTO.setStreet( customer.getStreet(  ) );
        customerDTO.setCityOfBirth( customer.getCityOfBirth(  ) );
        customerDTO.setStayConnected( customer.getStayConnected(  ) );
        customerDTO.setCity( customer.getCity(  ) );
        customerDTO.setPostalCode( customer.getPostalCode(  ) );
        customerDTO.setTelephoneNumber( customer.getTelephoneNumber(  ) );
        customerDTO.setSuggest(  );

        return customerDTO;
    }

    /**
     * Buid a Notification to an esNotificationDTO
     * @param notif
     * @param demand
     * @return
     */
    private static ESNotificationDTO buildNotificationDto( Notification notif, Demand demand )
    {
        if ( ( notif == null ) || ( demand == null ) )
        {
            throw new NullPointerException(  );
        }

        ESNotificationDTO notifDTO = new ESNotificationDTO(  );
        NotificationDemandDTO nddto = new NotificationDemandDTO( String.valueOf( demand.getDemandId(  ) ),
                String.valueOf( demand.getDemandIdType(  ) ) );

        notifDTO.setDateNotification( notif.getDateNotification(  ) );
        notifDTO.setNotificationDemand( nddto );
        notifDTO.setUserEmail( notif.getUserEmail(  ) );
        notifDTO.setUserDashBoard( notif.getUserDashBoard(  ) );
        notifDTO.setUserSms( notif.getUserSms(  ) );
        notifDTO.setUserBackOffice( notif.getUserBackOffice(  ) );
        

        return notifDTO;
    }

    /**
     * Build a demand to an esDemandDTO
     * @param demand
     * @param customer
     * @return
     */
    private static ESDemandDTO buildDemandDTO( Demand demand, Customer customer )
    {
        if ( ( customer == null ) || ( demand == null ) )
        {
            throw new NullPointerException(  );
        }

        ESDemandDTO demandDTO = new ESDemandDTO(  );
        CustomerDemandDTO customerDemand = new CustomerDemandDTO( String.valueOf( 
                    demand.getCustomer(  ).getCustomerId(  ) ) );
        demandDTO.setCustomerDemand( customerDemand );
        demandDTO.setDemandId( String.valueOf( demand.getDemandId(  ) ) );
        demandDTO.setDemandIdType( String.valueOf( demand.getDemandIdType(  ) ) );
        demandDTO.setDemandMaxStep( String.valueOf( demand.getDemandMaxStep(  ) ) );
        demandDTO.setDemandUserCurrentStep( String.valueOf( demand.getDemandUserCurrentStep(  ) ) );
        demandDTO.setDemandState( String.valueOf( demand.getDemandState(  ) ) );
        demandDTO.setNotifType( demand.getNotifType(  ) );
        demandDTO.setCRMStatus( demand.getCRMStatus(  ) );
        demandDTO.setReference( demand.getReference(  ) );
        demandDTO.setDemandStatus( demand.getDemandStatus(  ) );
        demandDTO.setSuggest( customer );

        return demandDTO;
    }
}
