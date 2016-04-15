/**
 * 
 */
package fr.paris.lutece.plugins.grustorageelastic.business.indexer;

/**
 * @author javadev
 *
 */
public class ConfigException extends Exception {

	/**
	 * @param message
	 */
	public ConfigException(String message) {
		super(message + " is missing.");
	}
}
