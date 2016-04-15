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
package fr.paris.lutece.plugins.grustorageelastic.web.rs;

public class AsynchronousService implements IAsynchronousService
{
    private int _nState;
    private int _nProgress;
    private StringBuilder _sbLogs;

    /**
     * {@inheritDoc }
     */
    @Override
    public void setState( int nState )
    {
        _nState = nState;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setProgress( int nProgress )
    {
        _nProgress = nProgress;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addToLog( String strLog )
    {
        if ( _sbLogs == null )
        {
            _sbLogs = new StringBuilder(  );
        }

        _sbLogs.append( strLog );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clearLogs(  )
    {
        _sbLogs = new StringBuilder(  );
    }

    /**
     * Get the progression value
     * @return The progression value
     */
    @Override
    public int getProgress(  )
    {
        return _nProgress;
    }

    /**
     * Get the logs
     * @return The logs
     */
    @Override
    public String getLogs(  )
    {
        if ( _sbLogs == null )
        {
            return "No logs";
        }

        return _sbLogs.toString(  );
    }

    /**
     * Return true if the process is currently running
     * @return otherwise false;
     */
    @Override
    public boolean isRunning(  )
    {
        return _nState == STATE_RUNNING;
    }
    
    @Override
    public int getState(  )
    {
        return _nState ;
    }
    
    
}
