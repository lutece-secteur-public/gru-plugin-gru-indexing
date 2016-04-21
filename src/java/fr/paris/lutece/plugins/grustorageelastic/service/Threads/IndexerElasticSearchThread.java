package fr.paris.lutece.plugins.grustorageelastic.service.Threads;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.plugins.gru.business.customer.CustomerHome;
import fr.paris.lutece.plugins.grustorageelastic.business.indexer.ESHttp;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.AsynchronousService;
import fr.paris.lutece.plugins.grustorageelastic.web.rs.IAsynchronousService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class IndexerElasticSearchThread extends Thread
{
	

    private static final String THREAD_NAME = "grustorageelastic-idexer-es-thread";
    private static boolean _bRunning;
    private static StringBuilder _sbLogs;
    private static AsynchronousService _service;

    /**
     * Constructor
     * @param service the service
     * @param request the request
     * @param locale the locale
     */
    public IndexerElasticSearchThread( AsynchronousService service )
    {
        _service = service;
        setName( THREAD_NAME );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void run(  )
    {
        try
        {
            _service.setState( IAsynchronousService.STATE_RUNNING );
            _service.clearLogs(  );
            _service.setProgress( 0 );
            _bRunning = true;

    		ESHttp.setHost( AppPropertiesService.getProperty( GRUElasticsConstants.ES_HOST ) );
    		ESHttp.setIndice( AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE ) );
    		ESHttp.setPort(Integer.getInteger( AppPropertiesService.getProperty( GRUElasticsConstants.ES_PORT ) ) );
    		ESHttp.setType( AppPropertiesService.getProperty( GRUElasticsConstants.ES_TYPE ) );
    		
    		try 
    		{
    			selectRecordsFromDbUserTable();
    		}
    		catch ( Exception e )
    		{
    			AppLogService.error( e.getCause()  + " :" + e.getMessage(  ), e );

    			_service.addToLog( e + " :" + e.getMessage(  ) );
    			_service.setProgress( 100 );
    	        _service.setState( IAsynchronousService.STATE_ABORTED );
    	        _bRunning = false;
    		}
        	
        }
        catch ( Exception ex )
        {
        	AppLogService.error( ex.getCause()  + " :" + ex.getMessage(  ), ex );
        	_service.addToLog( ex + " :" + ex.getMessage(  ) );
			_service.setProgress( 100 );
	        _service.setState( IAsynchronousService.STATE_ABORTED );
	        _bRunning = false;
           
        }
    }
    

    
    private static void selectRecordsFromDbUserTable() throws SQLException, ClassNotFoundException, InterruptedException {
		
		List<Customer> listCustomer = CustomerHome.getCustomersList() ;
		if ( listCustomer.isEmpty( ) )
		{
			_service.addToLog( "Data Base is empty !" );
		}
		else
		{
		int count = 0 ;
			for ( Customer customer : listCustomer )
			{
				Random r = new Random();
				int cp = 0;
				int bday = 0;
				cp = 75000 + r.nextInt(19000);
				bday = 1000 + r.nextInt(951401617);
				JSONObject json = new JSONObject();
				JSONObject suggest = new JSONObject();
				JSONObject payload = new JSONObject();
				JSONArray input = new JSONArray();
				json.put("user_cid", customer.getId( ) );
				json.put("email", customer.getEmail( ));
				json.put("first_name", customer.getFirstname( ) );
				json.put("last_name",  customer.getLastname( ) );
				json.put("stayConnected", String.valueOf( customer.getHasAccount( ) ) );
				json.put("street", "test");
				json.put("telephoneNumber", customer.getMobilePhone( ) );
				json.put("city", "PARIS");
				json.put("cityOfBirth", "PARIS");
				json.put("birthday", bday);
				json.put("civility", ( customer.getIdTitle( ) == 1)? "M" : "F");
				json.put("postalCode", cp);
				input.add( customer.getFirstname( ) );
				input.add( customer.getLastname( ));
				input.add( customer.getFirstname( )+ " " + customer.getLastname( ));
				input.add( customer.getLastname( ) + " " + customer.getFirstname( ));
				input.add( customer.getMobilePhone( ));
				suggest.put("input", input);
				suggest.put("output", customer.getFirstname( ) + " " + customer.getLastname( ) );
				payload.put("user_cid", customer.getId( ) );
				payload.put("last_name", customer.getLastname( ) );
				payload.put("first_name",customer.getFirstname( ) );
				payload.put("birthday", bday);
				payload.put("telephoneNumber", customer.getMobilePhone( ) );
				payload.put("email",  customer.getEmail( ));
				suggest.put("payload", payload);
				json.put("suggest",  suggest);
				//
				count++;
				AppLogService.info( json.toJSONString( ) );
				try
				{
					if ( !ESHttp.indexExist( ) )
					{
						_service.addToLog("Index Does not exist !");
						AppLogService.debug( "ESHttp.indexExist( ) : "+String.valueOf( ESHttp.indexExist( )));
						_service.setProgress( 100 );
		    	        _service.setState( IAsynchronousService.STATE_ABORTED );
		    	        _bRunning = false;
						break;
					}
				}
				catch ( IOException ex)
				{
					_service.addToLog("Index Does not exist !");
					AppLogService.error( "IOException : "+ ex.getMessage());
					_service.addToLog( ex + " :" + ex.getMessage(  )  );
					_service.setProgress( 100 );
	    	        _service.setState( IAsynchronousService.STATE_ABORTED );
	    	        _bRunning = false;
				}
				try 
				{
					ESHttp.add( customer.getId( ) , json.toString( ) );
				}
				catch ( InterruptedException e) 
				{
					_service.addToLog( e + " :" + e.getMessage(  )  );
				}
				_service.addToLog( json.toJSONString( ) );
				_service.setProgress( ( 100 * count ) / listCustomer.size( ) );
			}
		}
		if ( _service.getState( ) != IAsynchronousService.STATE_ABORTED )
		{
			_service.setState( IAsynchronousService.STATE_FINISHED );
			_service.addToLog( "\nIndexation réussi avec succès. \nTotale : "+ listCustomer.size( )+ " documents."   );
			_bRunning = false;
		}
	}
 
	 
    /**
     * Return the running state
     * @return the running state
     */
    public boolean isRunning(  )
    {
        return _bRunning;
    }
    /**
     * Set the running state
     * @param bool the state running
     */
    public void setRunning( boolean bool )
    {
    	_bRunning = bool;
    }
    /**
     * Returns the process logs
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
