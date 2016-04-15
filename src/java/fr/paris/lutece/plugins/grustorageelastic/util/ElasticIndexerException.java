package fr.paris.lutece.plugins.grustorageelastic.util;

public class ElasticIndexerException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String _strTitleField;
    private String _strErrorMessage;
    private boolean _bMandatoryError;

    /**
     * Creates a new DocumentSaveException
     * @param strTitleField The title of the filed that caused the error
     * @param strErrorMessage The error message
     */
    public ElasticIndexerException( String strTitleField, String strErrorMessage )
    {
        _strTitleField = strTitleField;
        _strErrorMessage = strErrorMessage;
        _bMandatoryError = false;
    }

	public String getTitleField( ) 
	{
		return _strTitleField;
	}

	public void setTitleField( String _strTitleField ) 
	{
		this._strTitleField = _strTitleField;
	}

	public String getErrorMessage( )  
	{
		return _strErrorMessage;
	}

	public void setErrorMessage( String _strErrorMessage ) 
	{
		this._strErrorMessage = _strErrorMessage;
	}

	public boolean isMandatoryError( ) 
	{
		return _bMandatoryError;
	}

	public void setMandatoryError( boolean _bMandatoryError )  
	{
		this._bMandatoryError = _bMandatoryError;
	}
    

}
