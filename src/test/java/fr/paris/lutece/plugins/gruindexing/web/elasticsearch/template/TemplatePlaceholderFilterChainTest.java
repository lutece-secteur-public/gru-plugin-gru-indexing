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
package fr.paris.lutece.plugins.gruindexing.web.elasticsearch.template;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.test.LuteceTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TemplatePlaceholderFilterChainTest extends LuteceTestCase
{
    private AutocompletePlaceholderFilterChain _chain = new AutocompletePlaceholderFilterChain( );
    private Map<String, String> _mapPlaceholderValues = new HashMap<>( );
    private MockHttpServletRequest _request = new MockHttpServletRequest( );

    public void testDoFilterWithNoFilter( )
    {
        init( );

        _chain.doFilter( _request, _mapPlaceholderValues );

        assertThat( _mapPlaceholderValues.size( ), is( 0 ) );
    }

    private void init( )
    {
        _chain = new AutocompletePlaceholderFilterChain( );
        _mapPlaceholderValues = new HashMap<>( );
        _request = new MockHttpServletRequest( );
    }

    public void testDoFilterWithOneFilter( )
    {
        init( );
        addOneFilterInChain( );

        _chain.doFilter( _request, _mapPlaceholderValues );

        assertThat( _mapPlaceholderValues.size( ), is( 1 ) );
    }

    private void addOneFilterInChain( )
    {
        _chain.addFilter( new AddOnePlaceholderFilter( ) );
    }

    public void testDoFilterWithTwoFilter( )
    {
        init( );
        addOneFilterInChain( );
        addOneFilterInChain( );

        _chain.doFilter( _request, _mapPlaceholderValues );

        assertThat( _mapPlaceholderValues.size( ), is( 2 ) );
    }

    private static final class AddOnePlaceholderFilter implements IAutocompletePlaceholderFilter
    {
        private static int _nCount = 0;

        @Override
        public void doFilter( HttpServletRequest request, Map<String, String> mapPlaceholderValues, AutocompletePlaceholderFilterChain chain )
        {
            _nCount++;
            String strCount = String.valueOf( _nCount );
            mapPlaceholderValues.put( strCount, strCount );

            chain.doFilter( request, mapPlaceholderValues );
        }
    }
}
