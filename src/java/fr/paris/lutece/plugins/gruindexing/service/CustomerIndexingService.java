/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.gruindexing.service;

import java.util.List;

import javax.inject.Inject;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexingService;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.gruindexing.business.IIndexCustomerDAO;

/**
 * This class represents a service to index a customer
 *
 */
public class CustomerIndexingService implements IIndexingService<Customer>
{
    private final IIndexCustomerDAO _indexCustomerDAO;

    /**
     * Constructor
     * 
     * @param indexCustomerDAO
     *            the index customer DAO
     */
    @Inject
    public CustomerIndexingService( IIndexCustomerDAO indexCustomerDAO )
    {
        _indexCustomerDAO = indexCustomerDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void index( Customer customer ) throws IndexingException
    {
        _indexCustomerDAO.insert( customer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexList( List<Customer> listCustomer ) throws IndexingException
    {
        _indexCustomerDAO.insert( listCustomer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIndex( Customer customer ) throws IndexingException
    {
        _indexCustomerDAO.delete( customer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllIndexes( ) throws IndexingException
    {
        _indexCustomerDAO.deleteAll( );
    }

}
