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
package fr.paris.lutece.plugins.grustorageelastic.service.threads;

import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.plugins.gru.business.customer.CustomerHome;
import fr.paris.lutece.plugins.grustorageelastic.business.indexer.ESHttp;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.AsynchronousService;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.IAsynchronousService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


// TODO: Auto-generated Javadoc
/**
 * The Class IndexerElasticSearchThread.
 */
public final class IndexerElasticSearchThread extends Thread
{
    /** The Constant THREAD_NAME. */
    private static final String THREAD_NAME = "grustorageelastic-idexer-es-thread";

    /** The _b running. */
    private static boolean _bRunning;

    /** The _sb logs. */
    private static StringBuilder _sbLogs;

    /** The _service. */
    private static AsynchronousService _service;

    /** The start time. */
    private static long _startTime;

    /** The end time. */
    private static long _endTime;
    private static IndexerElasticSearchThread _singleton;

    /**
     * Instantiates a new indexer elastic search thread.
     */
    private IndexerElasticSearchThread(  )
    {
        // TODO Auto-generated constructor stub
        setName( THREAD_NAME );
    }

    /**
     * Gets the single instance of IndexerElasticSearchThread.
     *
     * @param service the service
     * @return single instance of IndexerElasticSearchThread
     */
    public static IndexerElasticSearchThread getInstance( AsynchronousService service )
    {
        if ( _singleton == null )
        {
            _singleton = new IndexerElasticSearchThread(  );
            _singleton._service = service;
        }

        return _singleton;
    }

    /**
     * Sets the bool running.
     *
     * @param b the new bool running
     */
    private  static void setBoolRunning( boolean b )
    {
    	_bRunning = b;
    }
    /**
     * Constructor.
     *
     * @param service the service
     */

    /*   public IndexerElasticSearchThread( AsynchronousService service )
       {
           _service = service;
           setName( THREAD_NAME );
       }
    */

    /**
    * {@inheritDoc }.
    */
    @Override
    public void run(  )
    {
        try
        {
            _service.setState( IAsynchronousService.STATE_RUNNING );
            _service.clearLogs(  );
            _service.setProgress( 0 );
          //  _bRunning = true;
            setBoolRunning( true );
            
            ESHttp.setIndice( AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE ) );
            ESHttp.setType( AppPropertiesService.getProperty( GRUElasticsConstants.ES_TYPE ) );

            try
            {
                selectRecordsFromDbUserTable(  );
            }
            catch ( Exception e )
            {
                AppLogService.error( e.getCause(  ) + " :" + e.getMessage(  ), e );

                _service.addToLog( e + " :" + e.getMessage(  ) );
                _service.setProgress( 100 );
                _service.setState( IAsynchronousService.STATE_ABORTED );
                setBoolRunning( false );
              
            }
        }
        catch ( Exception ex )
        {
            AppLogService.error( ex.getCause(  ) + " :" + ex.getMessage(  ), ex );
            _service.addToLog( ex + " :" + ex.getMessage(  ) );
            _service.setProgress( 100 );
            _service.setState( IAsynchronousService.STATE_ABORTED );
            setBoolRunning( false );
        }
    }

    /**
     * Gets the head index.
     *
     * @return the head index
     */
    private JSONObject getHeadIndex(  )
    {
        JSONObject head = new JSONObject(  );
        JSONObject index = new JSONObject(  );

        index.put( "_index", "teleservice" );
        index.put( "_type", "user" );
        index.put( "_id", 0 );

        head.put( "index", index );

        return head;
    }

    /**
     * Select records from db user table.
     *
     * @throws SQLException the SQL exception
     * @throws ClassNotFoundException the class not found exception
     * @throws InterruptedException the interrupted exception
     */
    private static void selectRecordsFromDbUserTable(  )
        throws SQLException, ClassNotFoundException, InterruptedException
    {
        List<Customer> listCustomer = CustomerHome.getCustomersList(  );

        if ( listCustomer.isEmpty(  ) )
        {
            _service.addToLog( "Data Base is empty !" );
        }
        else
        {
            int count = 0;
            int countnb = 0;
            String strBulk = "";

        

            List<JSONObject> portion = new ArrayList<JSONObject>(  );

            int size = listCustomer.size(  );
 
            long allTime = 0;
            long diffTime = 0;
      
            String strLogging = "";

            _startTime = Calendar.getInstance(  ).getTimeInMillis(  );
            AppLogService.info( "\n \n CHRONOMETRAGE  Size : " + size + " \n\n" );

            for ( int j = 0; j < size; j++ )
            {
                JSONObject headBulk = new JSONObject(  );
                JSONObject indexBulk = new JSONObject(  );

                JSONObject json = new JSONObject(  );
                JSONObject suggest = new JSONObject(  );
                JSONObject payload = new JSONObject(  );
                JSONArray input = new JSONArray(  );
                
            	Random r = new Random();
    			int cp = 0;
    			int bday = 0;
    			cp = 75000 + r.nextInt( 19000 );
    			bday = 1000 + r.nextInt( 951401617 );

                json.put( "user_cid", listCustomer.get( j ).getId(  ) );
                json.put( "email", listCustomer.get( j ).getEmail(  ) );
                json.put( "first_name", listCustomer.get( j ).getFirstname(  ) );
                json.put( "last_name", listCustomer.get( j ).getLastname(  ) );
                json.put( "stayConnected", String.valueOf( listCustomer.get( j ).getHasAccount(  ) ) );
                json.put( "street", "test" );
                json.put( "telephoneNumber", listCustomer.get( j ).getMobilePhone(  ) );
                json.put( "city", "PARIS" );
                json.put( "cityOfBirth", "PARIS" );
                json.put( "birthday", bday );
                json.put( "civility", ( listCustomer.get( j ).getIdTitle(  ) == 1 ) ? "M" : "F" );
                json.put( "postalCode", cp );
                input.add( listCustomer.get( j ).getFirstname(  ) );
                input.add( listCustomer.get( j ).getLastname(  ) );
                input.add( listCustomer.get( j ).getFirstname(  ) + " " + listCustomer.get( j ).getLastname(  ) );
                input.add( listCustomer.get( j ).getLastname(  ) + " " + listCustomer.get( j ).getFirstname(  ) );
                input.add( listCustomer.get( j ).getMobilePhone(  ) );
                suggest.put( "input", input );
                suggest.put( "output",
                    listCustomer.get( j ).getFirstname(  ) + " " + listCustomer.get( j ).getLastname(  ) );
                payload.put( "user_cid", listCustomer.get( j ).getId(  ) );
                payload.put( "last_name", listCustomer.get( j ).getLastname(  ) );
                payload.put( "first_name", listCustomer.get( j ).getFirstname(  ) );
                payload.put( "birthday", bday );
                payload.put( "telephoneNumber", listCustomer.get( j ).getMobilePhone(  ) );
                payload.put( "email", listCustomer.get( j ).getEmail(  ) );
                suggest.put( "payload", payload );
                json.put( "suggest", suggest );

                indexBulk.put( "_index", "teleservice" );
                indexBulk.put( "_type", "user" );
                indexBulk.put( "_id", listCustomer.get( j ).getId(  ) );
                headBulk.put( "index", indexBulk );

                portion.add( headBulk );
                portion.add( json );

                count++;
                countnb++;


                if ( count > 0 && (  count % 2000  == 0  ||   count+1   >= size )  )
                {
                  //  AppLogService.info( "\n\n\n\n" );
                 //   AppLogService.info( "Nombre = " + countnb );
                   
                 //   AppLogService.info( "Nombre Total= " + count );
                    _endTime = Calendar.getInstance(  ).getTimeInMillis(  );
                    diffTime = _endTime - _startTime ;
                    allTime += diffTime ;
                 //   AppLogService.info( "\n \n CHRONOMETRAGE " + diffTime + " ms" + " \n\n" );
                    
                    strLogging = "Speed bulking "+2000+" document(s) / "+diffTime+" ms , Total : "+count+" document(s) in "+( allTime / 1000 )+" seconde(s) \n";
                    _startTime = Calendar.getInstance(  ).getTimeInMillis(  );
                   // AppLogService.info( "\n\n\n\n" );

                    //for bulk and reset
                    strBulk = joinPortionForBulk( portion, "\n" );
                   
                    portion.clear(  );

                  /* if( count < 100 )
                   {
                	   AppLogService.info( strBulk );
                   } */
                   countnb = 0;
                   
                   
                   
                    try
                    {
                        if ( !ESHttp.indexExist(  ) )
                        {
                            _service.addToLog( "Index Does not exist !" );
                            AppLogService.debug( "ESHttp.indexExist( ) : " + String.valueOf( ESHttp.indexExist(  ) ) );
                            _service.setProgress( 100 );
                            _service.setState( IAsynchronousService.STATE_ABORTED );
                            _bRunning = false;

                            break;
                        }
                    }
                    catch ( HttpAccessException ex )
                    {
                        _service.addToLog( "Index Does not exist !" );
                        AppLogService.error( "IOException : " + ex.getMessage(  ) );
                        _service.addToLog( ex + " :" + ex.getMessage(  ) );
                        _service.setProgress( 100 );
                        _service.setState( IAsynchronousService.STATE_ABORTED );
                        _bRunning = false;
                    }

                    try
                    {
                        ESHttp.add( strBulk );
                        //Thread.sleep( 1200 );
                    }
                    catch ( InterruptedException e )
                    {
                        _service.addToLog( e + " :" + e.getMessage(  ) );
                    }

                  //  _service.addToLog( strBulk );
                    _service.addToLog( strLogging );
                    _service.setProgress( ( 100 * count ) / size );
                }
            }
        }

        if ( _service.getState(  ) != IAsynchronousService.STATE_ABORTED )
        {
            _service.setState( IAsynchronousService.STATE_FINISHED );
            _service.addToLog( "\nIndexation réussi avec succès. \nTotale : " + listCustomer.size(  ) + " documents." );
            _bRunning = false;
        }
    }

    /**
     * Join portion for bulk.
     *
     * @param list the list
     * @param delim the delim
     * @return the string
     */
    public static String joinPortionForBulk( List<JSONObject> list, String delim )
    {
        StringBuilder sb = new StringBuilder(  );

        String loopDelim = "";

        int size = list.size();
        for ( int i = 0; i < size; i++ )
        {
        	
        	if( i + 1 < size )
        	{
        		 sb.append( loopDelim );
        	}
        
             sb.append( list.get( i ).toJSONString(  ) );
             AppLogService.debug( sb.toString(  ) );
             loopDelim = delim;
        }
        
        /*
        for ( JSONObject s : list )
        {
            sb.append( loopDelim );
            sb.append( s.toJSONString(  ) );
            AppLogService.debug( sb.toString(  ) );
            loopDelim = delim;
        } */

        return sb.toString(  );
    }

    /**
     * Return the running state.
     *
     * @return the running state
     */
    public static boolean isRunning(  )
    {
        return _bRunning;
    }

    /**
     * Set the running state.
     *
     * @param bool the state running
     */
    public static void setRunning( boolean bool )
    {
        _bRunning = bool;
    }

    /**
     * Returns the process logs.
     *
     * @return The logs
     */
    public String getProcessLogs(  )
    {
        if ( _sbLogs != null )
        {
            return _sbLogs.toString(  );
        }

        return "Not processed yet";
    }
}
