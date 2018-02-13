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
package fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.AutocompletePlaceholderFilterChain;
import fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.IAutocompletePlaceholderFilter;
import fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template.MockAutocompletPlaceholderFilterChain;
import fr.paris.lutece.test.LuteceTestCase;

public class QueryAutocompletePlaceholderFilterTest extends LuteceTestCase
{
    private static final String PARAMETER_QUERY = "query";
    private static final String PLACEHOLDER_QUERY = PARAMETER_QUERY;
    private static final String PLACEHOLDER_VALUE = "placeholder_value";

    private static final QueryAutocompletePlaceholderFilter _queryAutocompletePlaceholderFilter = new QueryAutocompletePlaceholderFilter( );

    private MockHttpServletRequest _request;
    private Map<String, String> _mapPlaceholderValues;
    private AutocompletePlaceholderFilterChain _chain;

    public void testFilterWhenQueryIsNotPresent( )
    {
        init( );

        _queryAutocompletePlaceholderFilter.doFilter( _request, _mapPlaceholderValues, _chain );

        assertThatPlaceholderIs( StringUtils.EMPTY );
    }

    private void init( )
    {
        _request = new MockHttpServletRequest( );
        _mapPlaceholderValues = new HashMap<>( );
        _chain = MockAutocompletPlaceholderFilterChain.create( _queryAutocompletePlaceholderFilter );
    }

    private void assertThatPlaceholderIs( String strPlaceholder )
    {
        assertThat( _mapPlaceholderValues.size( ), is( not( 0 ) ) );

        String strPlaceholderFromMap = _mapPlaceholderValues.get( PLACEHOLDER_QUERY );

        assertThat( strPlaceholderFromMap, is( strPlaceholder ) );
    }

    public void testFilterWhenQueryIsNull( )
    {
        init( );
        addQueryToRequest( null );

        _queryAutocompletePlaceholderFilter.doFilter( _request, _mapPlaceholderValues, _chain );

        assertThatPlaceholderIs( StringUtils.EMPTY );
    }

    private void addQueryToRequest( String strQuery )
    {
        _request.addParameter( PARAMETER_QUERY, strQuery );
    }

    public void testFilterWhenQueryIsNotNull( )
    {
        init( );
        addQueryToRequest( PLACEHOLDER_VALUE );

        _queryAutocompletePlaceholderFilter.doFilter( _request, _mapPlaceholderValues, _chain );

        assertThatPlaceholderIs( PLACEHOLDER_VALUE );
    }

    public void testChainFilterIsCalled( )
    {
        init( );
        MockAutocompletePlaceholderFilter mockFilter = new MockAutocompletePlaceholderFilter( );
        _chain.addFilter( mockFilter );

        _queryAutocompletePlaceholderFilter.doFilter( _request, _mapPlaceholderValues, _chain );

        assertThat( mockFilter._bIsCalled, is( true ) );
    }

    private static class MockAutocompletePlaceholderFilter implements IAutocompletePlaceholderFilter
    {
        private boolean _bIsCalled = false;

        @Override
        public void doFilter( HttpServletRequest request, Map<String, String> mapPlaceholderValues, AutocompletePlaceholderFilterChain chain )
        {
            _bIsCalled = true;
        }
    }
}
