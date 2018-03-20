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
package fr.paris.lutece.plugins.gruindexing.business;

import java.util.List;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.customer.ICustomerDAO;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;

/**
 * This interface provides Data Access methods for {@code Customer} objects, using an index
 */
public interface IIndexCustomerDAO extends ICustomerDAO
{
    /**
     * Inserts the specified customer into the index
     *
     * @param customer
     *            The customer to index
     * @throws IndexingException
     *             if there is an exception during the indexing
     */
    void insert( Customer customer ) throws IndexingException;

    /**
     * Inserts the specified list of customers into the index
     *
     * @param listCustomer
     *            The list of customers to index
     * @throws IndexingException
     *             if there is an exception during the indexing
     */
    void insert( List<Customer> listCustomer ) throws IndexingException;

    /**
     * Deletes the specified customer from the index
     *
     * @param customer
     *            The customer to delete
     * @throws IndexingException
     *             if there is an exception during the deletion
     */
    void delete( Customer customer ) throws IndexingException;

    /**
     * Deletes all the customers from the index
     *
     * @throws IndexingException
     *             if there is an exception during the deletion
     */
    void deleteAll( ) throws IndexingException;
}
