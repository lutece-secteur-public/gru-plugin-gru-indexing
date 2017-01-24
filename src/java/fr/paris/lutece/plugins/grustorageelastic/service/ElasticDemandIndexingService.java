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

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexingService;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.grustorageelastic.business.CustomerDemandDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ESDemandDTO;
import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;


/**
 * The Class ElasticDemandIndexingService.
 */
public class ElasticDemandIndexingService implements IIndexingService<Demand> 
{
    /**
     * {@inheritDoc }.
     *
     * @param demand the demand
     */
    @Override
    public void index( Demand demand )
    {
        if ( demand == null )
        {
            throw new NullPointerException(  );
        }

        ESDemandDTO demandDTO = buildDemandDTO( demand );

        ObjectMapper mapper = new ObjectMapper(  );
        mapper.setSerializationInclusion( Include.NON_NULL );

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
        catch ( HttpAccessException ex )
        {
            AppLogService.error( ex + " :" + ex.getMessage(  ), ex );
        }
    }

    /**
     * {@inheritDoc }.
     *
     * @param demand the demand
     * @throws IndexingException indexing exception
     */
    @Override
    public void deleteIndex( Demand demand ) throws IndexingException
    {
        
    }
    
    /**
     * Build a demand to an esDemandDTO.
     *
     * @param demand the demand
     * @param customer the customer
     * @return the ES demand dto
     */
    private ESDemandDTO buildDemandDTO( Demand demand )
    {
        ESDemandDTO demandDTO = new ESDemandDTO(  );

        if ( ( demand.getCustomer(  ) != null ) && StringUtils.isNotBlank( demand.getCustomer(  ).getId(  ) ) )
        {
            CustomerDemandDTO customerDemand = new CustomerDemandDTO( String.valueOf( 
                        demand.getCustomer(  ).getId(  ) ) );
            demandDTO.setCustomerDemand( customerDemand );
        }
    
        demandDTO.setDemandId( demand.getId(  ) );
        demandDTO.setDemandTypeId( demand.getTypeId(  ) );
        demandDTO.setReference( demand.getReference(  ) );

        return demandDTO;
    }

}
