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
package fr.paris.lutece.plugins.grustorageelastic.business.indexer;

import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;


/**
 * The Class ESHttp.
 */
public final class ESHttp
{
    /** The _base url. */
    private static String _baseUrl;

    /** The _type. */
    private static String _type;

    /** The _indice. */
    private static String _indice;

    /**
     * Instantiates a new ES http.
     */
    private ESHttp(  )
    {
    }

    /**
     * Execute.
     *
     * @param method the method
     * @param targetURL the target url
     * @param urlParameters the url parameters
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String execute( String method, String targetURL, String urlParameters )
        throws IOException
    {
        HttpURLConnection connection = null;

        try
        {
            //Create connection
            URL url = new URL( targetURL );
            connection = (HttpURLConnection) url.openConnection(  );
            connection.setRequestMethod( method.toUpperCase(  ) );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );

            connection.setRequestProperty( "Content-Length", Integer.toString( urlParameters.getBytes(  ).length ) );
            connection.setRequestProperty( "Content-Language", "fr-FR" );

            connection.setUseCaches( false );
            connection.setDoOutput( true );

            //Send request
            OutputStreamWriter wr = new OutputStreamWriter( connection.getOutputStream(  ), StandardCharsets.UTF_8 );
            wr.write( urlParameters );
            wr.flush(  );
            wr.close(  );

            //Get Response  
            InputStream is = connection.getInputStream(  );
            BufferedReader rd = new BufferedReader( new InputStreamReader( is ) );
            StringBuilder response = new StringBuilder(  ); // or StringBuffer if not Java 5+ 
            String line;

            while ( ( line = rd.readLine(  ) ) != null )
            {
                response.append( line );
                response.append( '\r' );
            }

            rd.close(  );

            return response.toString(  );
        }
        finally
        {
            if ( connection != null )
            {
                connection.disconnect(  );
            }
        }
    }

    /**
     * Adds the.
     *
     * 
     * @param data the data
  
     * @throws InterruptedException the interrupted exception
     */
    public static void add( String data ) throws InterruptedException
    {
        try
        {
        	  
             execute( "POST", baseUrl( ), data );
        }
        catch ( IOException e )
        {
        	   Thread.sleep( 5000 );

            AppLogService.error( "ERROR BULK DATA : " + e.getMessage( ) );
            AppLogService.error( e.getMessage() , e );
            AppLogService.info( "data : " + data );
/*ElasticSearch send error 400 after 350 000 documents. Done thread sleep to wait for*/
          //      add( data );
        }
    }

    /**
     * Base url.
     *

     * @return the string
     */
    private static String baseUrl( )
    {
        _baseUrl = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
            GRUElasticsConstants.PATH_ELK_BULK;
        AppLogService.info( "URL Bulk " + _baseUrl );

        return _baseUrl;
    }

    /**
     * Index exist.
     *
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static boolean indexExist(  ) throws IOException
    {
        boolean res = false;

        String strUrl = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
            AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE );

        AppLogService.debug( "url :" + strUrl );

        URL url = new URL( strUrl );
        HttpURLConnection http = (HttpURLConnection) url.openConnection(  );

        try
        {
            if ( http.getResponseCode(  ) == 200 )
            {
                res = true;
            }

            AppLogService.debug( "url :" + strUrl + "response : " + http.getResponseCode(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ) );
        }

        return res;
    }

    /**
     * Gets the indice.
     *
     * @return the indice
     */
    public static String getIndice(  )
    {
        return _indice;
    }

    /**
     * Sets the indice.
     *
     * @param indice the new indice
     */
    public static void setIndice( String indice )
    {
        _indice = indice;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public static String getType(  )
    {
        return _type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public static void setType( String type )
    {
        _type = type;
    }
}
