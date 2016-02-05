package fr.paris.lutece.plugins.grustorage.elastic.business;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotificationDemandDTO {
	private String _strDemandId;
	private String _strDemandIdType;
	
	
	public NotificationDemandDTO() {
		super();
	}
	public NotificationDemandDTO(String _strDemandId, String _strDemandIdType) {
		super();
		this._strDemandId = _strDemandId;
		this._strDemandIdType = _strDemandIdType;
	}
	/**
	 * Returns the Demand Id
	 * @return the Demand ID
	 */
    @JsonProperty( "demande_id" )
	public String getDemandId() {
		return _strDemandId;
	}
    /**
     * Sets the Demand ID
     * @param _strDemandId
     */
    @JsonProperty( "demande_id" )
	public void setDemandId(String _strDemandId) {
		this._strDemandId = _strDemandId;
	}
	
	/**
	 * Return the Demand ID Type
	 * @return
	 */
    @JsonProperty( "demande_id_type" )
	public String getDemandIdType() {
		return _strDemandIdType;
	}
    
    /**
     * Sets the Demand ID Type
     * @param _strDemandIdType
     */
    @JsonProperty( "demande_id_type" )
	public void setDemandIdType(String _strDemandIdType) {
		this._strDemandIdType = _strDemandIdType;
	}
}
