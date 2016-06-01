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
package fr.paris.lutece.plugins.grustorageelastic.service;

import fr.paris.lutece.plugins.grustorageelastic.service.threads.IndexerElasticSearchThread;
import fr.paris.lutece.plugins.grustorageelastic.util.ElasticIndexerException;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.AsynchronousService;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.IAsynchronousService;


/**
 * Class ElasticIndexerService
 *
 *
 */
public class ElasticIndexerService extends AsynchronousService implements IAsynchronousService, IElasticIndexerService
{
    private static ElasticIndexerService _singleton;
    private static IndexerElasticSearchThread _threadElastic;

    /**
     *
     * @return IElasticIndexerService
     */
    public static ElasticIndexerService getService(  )
    {
        if ( _singleton == null )
        {
            _singleton = new ElasticIndexerService(  );
        }

        return _singleton;
    }

    @Override
    public void indexerES(  ) throws ElasticIndexerException
    {
        if ( ( ( _threadElastic != null ) && _threadElastic.isRunning(  ) ) )
        {
            throw new ElasticIndexerException( "Indexer Elastic Search",
                "The _thread of indexing elastic is already running" );
        }

        // Start the thread
        // _threadElastic = new IndexerElasticSearchThread( this );
        _threadElastic = IndexerElasticSearchThread.getInstance( this );

        _threadElastic.setPriority( Thread.MAX_PRIORITY );

        try
        {
            _threadElastic.start(  );
        }
        catch ( Exception e )
        {
            IndexerElasticSearchThread.interrupted(  );
            _threadElastic = null;
        }
    }

    @Override
    public void killThreads(  )
    {
        if ( _threadElastic != null )
        {
            IndexerElasticSearchThread.interrupted(  );
            _threadElastic = null;
        }
    }
}
