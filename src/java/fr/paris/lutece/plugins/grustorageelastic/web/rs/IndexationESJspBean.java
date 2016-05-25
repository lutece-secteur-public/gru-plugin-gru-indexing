/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.grustorageelastic.business.ElasticConnexion;
import fr.paris.lutece.plugins.grustorageelastic.business.indexer.ESHttp;
import fr.paris.lutece.plugins.grustorageelastic.service.ElasticIndexerService;
import fr.paris.lutece.plugins.grustorageelastic.util.ElasticIndexerException;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage the launching of the indexing of the site pages
 */

@Controller( controllerJsp = "IndexerES.jsp", controllerPath = "jsp/admin/plugins/grustorageelastic/", right = "ES_INDEXATION" )
public class IndexationESJspBean extends  MVCAdminJspBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	////////////////////////////////////////////////////////////////////////////
    // Constantes
    /**
     * Right to manage indexation
     */
    public static final String RIGHT_INDEXER = "/";
    private static final String TEMPLATE_MANAGE_INDEXER = "admin/plugins/grustorageelastic/manage_es_indexation.html";
    
    private static final String MARK_RUNNING = "running";
    private static final String MARK_PROGRESS = "progress";
    private static final String MARK_LOGS = "logs";
    
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_INDEXER_ES = "grustorageelastic.manage_es.pageTitle";
    private static final String MESSAGE_CONFIRM_DELETE_INDEX = "grustorageelastic.message.confirmDeleteIndex";    
   
    
    //Parameters
    private static final String RESPONSE_ES_SUCCEES = "{\"acknowledged\":true}" ;
    
    //views
    private static final String VIEW_INDEXER_ES = "manageelastic";
    
    //Actions
    private static final String ACTION_INDEXER_ES = "indexer";
    private static final String ACTION_DELETE_INDEX = "deleteIndex";
    private static final String ACTION_CONFIRM_DELETE_INDEX = "confirmDeleteIndex";
    
    private static final String _uri = AppPropertiesService.getProperty( GRUElasticsConstants.PATH_ELK_SERVER ) +
            AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE ) ;
  
    /**
     * Displays the indexing parameters
     *
     * @param request the http request
     * @return the html code which displays the parameters page
     */
    @View( value = VIEW_INDEXER_ES, defaultView = true )
    public String getIndexingProperties( HttpServletRequest request )
    {
    	if ( ElasticIndexerService.getService(  ).getState( ) == IAsynchronousService.STATE_FINISHED )
    	{
    		ElasticIndexerService.getService(  ).setState(IAsynchronousService.STATE_NOT_STARTED) ;
    		addInfo( "Indexation réussi avec succès" );
    	}
    	if ( ElasticIndexerService.getService(  ).getState( ) == IAsynchronousService.STATE_ABORTED )
    	{
    		ElasticIndexerService.getService(  ).setState(IAsynchronousService.STATE_NOT_STARTED) ;
    		addError( "Erreur d'indexation" );
    	}
    	
    	 Map<String, Object> model = getModel(  );
    	
    	 model.put( MARK_PROGRESS, ElasticIndexerService.getService(  ).getProgress(  ) );
         model.put( MARK_LOGS, ElasticIndexerService.getService(  ).getLogs(  ) );
         model.put( MARK_RUNNING, ElasticIndexerService.getService(  ).isRunning(  ) );
    	
        return getPage( PROPERTY_PAGE_TITLE_INDEXER_ES, TEMPLATE_MANAGE_INDEXER, model );
    }

    /**
     * Calls the indexing process
     *
     * @param request the http request
     * @return the result of the indexing process
     * @throws IOException 
     */
    @Action ( ACTION_INDEXER_ES )
    public String doIndexing( HttpServletRequest request ) throws IOException
    {
		if ( ElasticIndexerService.getService(  ).isRunning( ) )
		{
			addError( "Un utilisateur à déja lancer l'indexation ");
			return redirectView( request, VIEW_INDEXER_ES );
		}
		
		setConnexion ( ) ;
		if ( ! ESHttp.indexExist() )
		{
			createIndex( request ) ;
			addInfo( "index ajouté ");
		}
		
		try
		{
			ElasticIndexerService.getService(  ).indexerES( );
		}
		catch ( ElasticIndexerException ex )
		{
			addError( "Un utilisateur à déja lancer l'indexation :  "+ ex.getErrorMessage( ) );
			return redirectView( request, VIEW_INDEXER_ES );
		}
		return redirectView( request, VIEW_INDEXER_ES );

    }
    
    /**
     * Delete the index
     * @return the page manage index
     * @throws IOException 
     */
    
    private static void setConnexion( )
    {
    	ESHttp.setIndice( AppPropertiesService.getProperty( GRUElasticsConstants.ES_INDICE ) );
    	ESHttp.setType( AppPropertiesService.getProperty( GRUElasticsConstants.ES_TYPE ) );
    	
    }
    
    private void createIndex( HttpServletRequest request  ) throws IOException
    {
    	
    	BufferedReader br = new BufferedReader(	new InputStreamReader (	AppPathService.getResourceAsStream( "/WEB-INF/plugins/grustorageelastic/", "mapping.json" ) ) );
    	
    	String json = deserializeString ( br ) ;
		
    	AppLogService.debug("uri :" +  _uri.trim( ) );
    	String strResponse = ElasticConnexion.sentToElasticPUT( _uri.trim( ) , json) ;
    	if ( !strResponse.equals( RESPONSE_ES_SUCCEES ))
    	{
    		addError( "Erreur d'ajout d'index : ");
    		ElasticIndexerService.getService(  ).addToLog( strResponse );
    		redirectView( request, VIEW_INDEXER_ES );
    		
    	}
    }
    
    public static String deserializeString(BufferedReader reader) throws IOException 
    {
	      int len;
	      char[] chr = new char[4096];
	      final StringBuffer buffer = new StringBuffer();
	      try 
	      {
	          while ((len = reader.read(chr)) > 0) {
	              buffer.append(chr, 0, len);
	          }
	      }
	      catch (IOException e)
	      {
	    	  ElasticIndexerService.getService(  ).addToLog("Erreur lecture ficher mapping.json" + e.getMessage( ) );
	      }
	      finally 
	      {
	          reader.close();
	          
	      }
	      return buffer.toString();
    }
    
    
    @Action ( ACTION_DELETE_INDEX )
    public String doDeleteIndex( HttpServletRequest request ) throws IOException
    {
    	setConnexion( ) ;
		
		if ( !ESHttp.indexExist() )
		{
			addError( "Index n'existe pas. ");
		}
		else
		{
						
	    	String strResponse = ElasticConnexion.sentToElasticDELETE( _uri.trim( ) ) ;
			if ( strResponse.equals( RESPONSE_ES_SUCCEES ) )
			{
				addInfo( "Index supprimé avec succès.");
			}
			else
			{
				addError( "Erreur suppression d'index ");
				ElasticIndexerService.getService(  ).addToLog( strResponse );
			}
		}
	    	
    	Map<String, Object> model = getModel(  );
    	
   	 	model.put( MARK_PROGRESS, ElasticIndexerService.getService(  ).getProgress(  ) );
        model.put( MARK_LOGS, ElasticIndexerService.getService(  ).getLogs(  ) );
        model.put( MARK_RUNNING, ElasticIndexerService.getService(  ).isRunning(  ) );
   	
       return getPage( PROPERTY_PAGE_TITLE_INDEXER_ES, TEMPLATE_MANAGE_INDEXER, model );
    }
    
    @Action( ACTION_CONFIRM_DELETE_INDEX )
    public String getConfirmDeleteIndex( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( getActionUrl( ACTION_DELETE_INDEX ) );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_INDEX,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }
}
