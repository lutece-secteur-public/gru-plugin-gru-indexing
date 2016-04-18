package fr.paris.lutece.plugins.grustorageelastic.service;

import fr.paris.lutece.plugins.grustorageelastic.util.ElasticIndexerException;

public interface IElasticIndexerService 
{

	void indexerES() throws ElasticIndexerException ;

	void killThreads();

}
