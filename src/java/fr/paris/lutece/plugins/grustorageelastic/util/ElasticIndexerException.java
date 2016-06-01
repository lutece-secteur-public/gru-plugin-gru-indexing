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
package fr.paris.lutece.plugins.grustorageelastic.util;


/**
 * The Class ElasticIndexerException.
 */
public class ElasticIndexerException extends Exception
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The _str title field. */
    private final String _strTitleField;

    /** The _str error message. */
    private final String _strErrorMessage;

    /** The _b mandatory error. */
    private final boolean _bMandatoryError;

    /**
     * Instantiates a new elastic indexer exception.
     *
     * @param strTitleField the str title field
     * @param strErrorMessage the str error message
     */
    public ElasticIndexerException( String strTitleField, String strErrorMessage )
    {
        _strTitleField = strTitleField;
        _strErrorMessage = strErrorMessage;
        _bMandatoryError = false;
    }

    /**
     * Gets the title field.
     *
     * @return the title field
     */
    public String getTitleField(  )
    {
        return _strTitleField;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage(  )
    {
        return _strErrorMessage;
    }

    /**
     * Checks if is mandatory error.
     *
     * @return true, if is mandatory error
     */
    public boolean isMandatoryError(  )
    {
        return _bMandatoryError;
    }
}
