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
package fr.paris.lutece.plugins.grustorageelastic.business;

import org.codehaus.jackson.annotate.JsonProperty;


// TODO: Auto-generated Javadoc
/**
 * The Class ESSuggestDTO.
 */
public class ESSuggestDTO
{
    /** The _str input. */
    private String[] _strInput;

    /** The _str output. */
    private String _strOutput;

    /** The _o payload. */
    private ESPayload _oPayload;

    /**
     * Gets the input.
     *
     * @return the input
     */
    @JsonProperty( "input" )
    public String[] getInput(  )
    {
        return _strInput.clone(  );
    }

    /**
     * Sets the input.
     *
     * @param strInput the new input
     */
    public void setInput( String[] strInput )
    {
        this._strInput = strInput.clone(  );
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    @JsonProperty( "output" )
    public String getOutput(  )
    {
        return _strOutput;
    }

    /**
     * Sets the output.
     *
     * @param strOutput the new output
     */
    public void setOutput( String strOutput )
    {
        this._strOutput = strOutput;
    }

    /**
     * Gets the payload.
     *
     * @return the payload
     */
    @JsonProperty( "payload" )
    public ESPayload getPayload(  )
    {
        return _oPayload;
    }

    /**
     * Sets the payload.
     *
     * @param oPayload the new payload
     */
    public void setPayload( ESPayload oPayload )
    {
        this._oPayload = oPayload;
    }
}
