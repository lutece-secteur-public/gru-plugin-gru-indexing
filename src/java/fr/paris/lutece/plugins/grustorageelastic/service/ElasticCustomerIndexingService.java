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

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexingService;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.grustorageelastic.business.ESCustomerDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * The Class ElasticCustomerIndexingService.
 */
public class ElasticCustomerIndexingService implements IIndexingService<Customer>
{
    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void index( Customer customer ) throws IndexingException
    {
        ESCustomerDTO customerDTO = buildCustomer( customer );

        ObjectMapper mapper = new ObjectMapper( );
        mapper.setSerializationInclusion( Include.NON_NULL );

        String jsonUser;

        try
        {
            jsonUser = mapper.writeValueAsString( customerDTO );
            ElasticConnexion.sentToElasticPOST( ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_USER, customerDTO.getCustomerId( ) ), jsonUser );
        }
        catch( JsonGenerationException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }
        catch( JsonMappingException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }
        catch( IOException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }
        catch( HttpAccessException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }

    }

    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void deleteIndex( Customer customer ) throws IndexingException
    {
        try
        {
            ElasticConnexion.sentToElasticDELETE( ElasticConnexion.getESParam( GRUElasticsConstants.PATH_ELK_TYPE_USER, customer.getId( ) ) );
        }
        catch( HttpAccessException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage( ), ex );
            throw new IndexingException( ex.getMessage( ), ex );
        }
    }

    /**
     * Build a customer to an esCustomerSTO.
     *
     * @param customer
     *            the customer
     * @return the ES customer dto
     */
    private ESCustomerDTO buildCustomer( Customer customer )
    {
        ESCustomerDTO customerDTO = new ESCustomerDTO( );

        customerDTO.setCustomerId( customer.getId( ) );
        customerDTO.setConnectionId( customer.getAccountGuid( ) );
        customerDTO.setName( manageNullValue( customer.getLastname( ) ) );
        customerDTO.setFirstName( manageNullValue( customer.getFirstname( ) ) );
        customerDTO.setEmail( manageNullValue( customer.getEmail( ) ) );
        customerDTO.setBirthday( manageNullValue( customer.getBirthDate( ) ) );
        customerDTO.setCivility( manageNullValue( Integer.toString( customer.getIdTitle( ) ) ) );
        customerDTO.setTelephoneNumber( manageNullValue( customer.getMobilePhone( ) ) );
        customerDTO.setFixedTelephoneNumber( manageNullValue( customer.getFixedPhoneNumber( ) ) );
        customerDTO.setSuggest( );

        return customerDTO;
    }

    /**
     * Manages the case the specified String is {@code null}
     * 
     * @param strValue
     *            the String to manage
     * @return the correct String when the specified String is {@code null}, {@code strValue} otherwise
     */
    private static String manageNullValue( String strValue )
    {
        return ( strValue == null ) ? StringUtils.EMPTY : strValue;
    }
}
