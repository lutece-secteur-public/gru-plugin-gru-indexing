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
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;


import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;



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

    private static  HttpAccess _clientHttp = new HttpAccess(  );
    /**
     * Instantiates a new ES http.
     */
    private ESHttp(  )
    {
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
    	
        
  		try {
  			 _clientHttp.doPostJSON(baseUrl( ), data, null, null);
  		} catch (HttpAccessException e) {
  			
  		  AppLogService.error( "ERROR BULK DATA : " + e.getMessage( ) );
          AppLogService.error( e.getMessage() , e );
          AppLogService.info( "data : " + data );
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
     * @throws HttpAccessException 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static boolean indexExist(  ) throws HttpAccessException
    {
        boolean res = false;

        String strUrl = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
            AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_PATH );

        AppLogService.debug( "url :" + strUrl );

      
		String strResponse;
		try {
			strResponse = _clientHttp.doGet(strUrl);
			
			  if ( JSONUtils.mayBeJSON( strResponse ) )
		        {
				  JSONObject  responseJsonObject = (JSONObject) JSONSerializer.toJSON( strResponse );		  
				
				  if( responseJsonObject.containsKey( AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_PATH ).replace("/", "") ) )
				  {
					res = true;
				  }				  
			  
		        }
			  
		} catch (HttpAccessException e) {
			// TODO Auto-generated catch block
			   AppLogService.error( "Grustorageelastic - Error" ,e);
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
