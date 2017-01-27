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
package fr.paris.lutece.plugins.grustorageelastic.web.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexer;
import fr.paris.lutece.plugins.grustorageelastic.util.constant.GRUElasticsConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * This class provides the user interface to index elements
 */
@Controller( controllerJsp = ElementIndexingJspBean.ADMIN_FEATURE_CONTROLLER_JSP, controllerPath = ElementIndexingJspBean.ADMIN_FEATURE_CONTROLLLER_PATH, right = ElementIndexingJspBean.ADMIN_FEATURE_RIGHT )
public class ElementIndexingJspBean extends MVCAdminJspBean
{
    // //////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String ADMIN_FEATURE_CONTROLLER_JSP = "IndexElements.jsp";
    public static final String ADMIN_FEATURE_CONTROLLLER_PATH = "jsp/admin/plugins/grustorageelastic/index/";
    public static final String ADMIN_FEATURE_RIGHT = "GRUSTORAGE_INDEXING";
    public static final String PROPERTY_PAGE_TITLE_INDEX_ELEMENTS = "grustorageelastic.index_elements.pageTitle";

    // Serial
    private static final long serialVersionUID = -2781183979097986731L;

    // Marks
    private static final String MARK_INDEXERS_LIST = "indexers_list";

    // Views
    private static final String VIEW_INDEX_ELEMENTS = "indexElements";

    // Actions
    private static final String ACTION_INDEX_ELEMENTS = "doIndexElements";

    // Templates
    private static final String TEMPLATE_INDEX_ELEMENTS = "/admin/plugins/grustorageelastic/index/index_elements.html";

    private List<IIndexer> _listElasticIndexer = SpringContextService.getBeansOfType( IIndexer.class );

    /**
     * Displays the indexing parameters
     *
     * @param request
     *            the http request
     * @return the html code which displays the parameters page
     */
    @View( value = VIEW_INDEX_ELEMENTS, defaultView = true )
    public String getIndexingProperties( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        model.put( MARK_INDEXERS_LIST, _listElasticIndexer );

        if ( _listElasticIndexer == null || _listElasticIndexer.isEmpty( ) )
        {
            addInfo( GRUElasticsConstants.NO_INDEX_MESSAGE, getLocale( ) );
            fillCommons( model );
        }

        return getPage( PROPERTY_PAGE_TITLE_INDEX_ELEMENTS, TEMPLATE_INDEX_ELEMENTS, model );
    }

    /**
     * Indexes the elements
     * 
     * @param request
     *            The Http Request
     * @return The JSP URL to display after the process
     */
    @Action( ACTION_INDEX_ELEMENTS )
    public String doIndexingElements( HttpServletRequest request )
    {
        // Create the map of the bean which implements the IElasticSearchIndexer interface
        LinkedHashMap<String, IIndexer> mapBeanImplementation = new LinkedHashMap<String, IIndexer>( );
        for ( IIndexer beanImplementation : _listElasticIndexer )
        {
            mapBeanImplementation.put( beanImplementation.getName( ), beanImplementation );
        }

        // Create the list of all enabled indexer
        if ( ( _listElasticIndexer != null && !_listElasticIndexer.isEmpty( ) ) && !mapBeanImplementation.isEmpty( ) )
        {
            try
            {
                for ( IIndexer elasticSearchIndexer : _listElasticIndexer )
                {
                    if ( elasticSearchIndexer.isEnable( ) )
                    {
                        IIndexer correctBeanImplementation = mapBeanImplementation.get( elasticSearchIndexer.getName( ) );
                        if ( correctBeanImplementation != null )
                        {
                            correctBeanImplementation.indexAllElements( );
                        }
                        else
                        {
                            AppLogService.info( "No implementation found for the indexer: " + elasticSearchIndexer.getName( ) );
                        }
                    }
                }
            }
            catch( AppException appException )
            {
                addError( GRUElasticsConstants.FULL_INDEX_ERROR_MESSAGE, getLocale( ) );
                return redirectView( request, VIEW_INDEX_ELEMENTS );
            }
        }

        addInfo( GRUElasticsConstants.FULL_INDEX_SUCCESS_MESSAGE, getLocale( ) );
        return redirectView( request, VIEW_INDEX_ELEMENTS );
    }

}
