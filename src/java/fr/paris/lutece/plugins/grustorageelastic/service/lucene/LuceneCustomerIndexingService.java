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
package fr.paris.lutece.plugins.grustorageelastic.service.lucene;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.customer.ICustomerDAO;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IIndexingService;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The Class LuceneCustomerService.
 */
public class LuceneCustomerIndexingService implements IIndexingService<Customer>, ICustomerDAO
{

    private static final String PATH_INDEX = "/WEB-INF/plugins/identitystore/modules/indexer/indexes";
    private static final Version LUCENE_VERSION = Version.LUCENE_4_9;
    private static final int INDENT = 4;

    private static final String FIELD_ID = "id";
    private static final String FIELD_FIRSTNAME = "firstname";
    private static final String FIELD_LASTNAME = "lastname";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_FIXED_PHONE_NUMBER = "fixed_phone_phone";
    private static final String FIELD_BIRTHDATE = "birthdate";
    private static final String FIELD_CIVILITY = "civility";

    private static Analyzer _analyzer;

    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void index( Customer customer ) throws IndexingException
    {
        AppLogService.info( "\n\n\n\n\n LUCENE DAEMON CUSTOMER \n\n\n" );
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, getAnalyzer( ) );

            iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );

            IndexWriter writer = new IndexWriter( dir, iwc );

            index( writer, customer );

            AppLogService.info( "\n\n\n\n\n END LUCENE INDEXING \n\n\n" );
            writer.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
        }
    }

    /**
     * Returns the index PATH
     * 
     * @return The index path
     */
    private static File getIndexPath( )
    {
        String strIndexPath = AppPathService.getAbsolutePathFromRelativePath( PATH_INDEX );

        return Paths.get( strIndexPath ).toFile( );
    }

    /**
     * Returns the analyzer
     * 
     * @return The analyzer
     */
    private static Analyzer getAnalyzer( )
    {
        if ( _analyzer == null )
        {
            _analyzer = new CustomAnalyzer( );
        }
        return _analyzer;
    }

    /**
     * The Class CustomAnalyzer.
     */
    private static class CustomAnalyzer extends StopwordAnalyzerBase
    {
        public CustomAnalyzer( )
        {
            super( LUCENE_VERSION, StandardAnalyzer.STOP_WORDS_SET );
        }

        @Override
        protected TokenStreamComponents createComponents( String fieldName, Reader reader )
        {
            final Tokenizer source = new StandardTokenizer( LUCENE_VERSION, reader );

            TokenStream tokenStream = source;
            tokenStream = new StandardFilter( LUCENE_VERSION, tokenStream );
            tokenStream = new LowerCaseFilter( LUCENE_VERSION, tokenStream );
            tokenStream = new StopFilter( LUCENE_VERSION, tokenStream, getStopwordSet( ) );
            tokenStream = new ASCIIFoldingFilter( tokenStream );
            return new TokenStreamComponents( source, tokenStream );
        }
    }

    /**
     * Index a customer
     * 
     * @param writer
     *            The index writer
     * @param customer
     *            The customer
     * @throws IOException
     *             If an error occurs
     */
    private static void index( IndexWriter writer, Customer customer ) throws IOException
    {
        Document doc = new Document( );

        Field fielIdname = new StringField( FIELD_ID, customer.getId( ), Field.Store.YES );
        doc.add( fielIdname );

        Field fielFirstname = new StringField( FIELD_FIRSTNAME, customer.getFirstname( ), Field.Store.YES );
        doc.add( fielFirstname );

        Field fielLastname = new StringField( FIELD_LASTNAME, customer.getLastname( ), Field.Store.YES );
        doc.add( fielLastname );

        if ( customer.getEmail( ) != null )
        {
            Field fieldEmail = new StoredField( FIELD_EMAIL, customer.getEmail( ) );
            doc.add( fieldEmail );
        }

        if ( customer.getMobilePhone( ) != null )
        {
            Field fieldPhone = new StringField( FIELD_PHONE, customer.getMobilePhone( ), Field.Store.YES );
            doc.add( fieldPhone );
        }

        if ( customer.getFixedPhoneNumber( ) != null )
        {
            Field fieldPhone = new StringField( FIELD_FIXED_PHONE_NUMBER, customer.getFixedPhoneNumber( ), Field.Store.YES );
            doc.add( fieldPhone );
        }

        if ( customer.getBirthDate( ) != null )
        {
            Field fieldBirthdate = new StoredField( FIELD_BIRTHDATE, customer.getBirthDate( ) );
            doc.add( fieldBirthdate );
        }

        if ( Integer.toString( customer.getIdTitle( ) ) != null )
        {
            Field fieldCivility = new StoredField( FIELD_CIVILITY, Integer.toString( customer.getIdTitle( ) ) );
            doc.add( fieldCivility );
        }

        writer.addDocument( doc );
    }

    /**
     * {@inheritDoc }.
     *
     * @param customer
     *            the customer
     * @throws IndexingException
     *             indexing exception
     */
    @Override
    public void deleteIndex( Customer customer ) throws IndexingException
    {
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, getAnalyzer( ) );

            iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );

            IndexWriter writer = new IndexWriter( dir, iwc );

            Term termToDelete = new Term( FIELD_ID, customer.getId( ) );

            writer.deleteDocuments( termToDelete );
            writer.commit( );
            writer.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
        }
    }

    /**
     * Method which returns a list of all customer found from the specified query
     * 
     * @param query
     * @return a list of all customer found or null
     */
    private static List<Customer> getCustomerSearchResult ( Query queryToLaunch )
    {
        List<Customer> listCustomer = new ArrayList<Customer>( );
        try
        {
            IndexReader indexReader = DirectoryReader.open( FSDirectory.open( getIndexPath( ) ) );
            IndexSearcher indexSearcher = new IndexSearcher( indexReader );

            if ( indexSearcher != null )
            {                    
                // Get results documents
                TopDocs topDocs = indexSearcher.search( queryToLaunch, LuceneSearchEngine.MAX_RESPONSES );
                ScoreDoc [ ] hits = topDocs.scoreDocs;

                for ( int i = 0; i < hits.length; i++ )
                {
                    int docId = hits [i].doc;
                    Document document = indexSearcher.doc( docId );
                    listCustomer.add( buildCustomerFromDocument( document ) );
                }
            }

        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        return listCustomer;
    }

    /**
     * {@inheritDoc }.
     */
    @Override
    public List<Customer> loadByName( String strFirstName, String strLastName )
    {
        BooleanQuery booleanQueryMain = new BooleanQuery( );
        
        TermQuery termQueryFirstName = new TermQuery( new Term( FIELD_FIRSTNAME, strFirstName ) );
        booleanQueryMain.add( new BooleanClause( termQueryFirstName, BooleanClause.Occur.MUST ) );
        
        TermQuery termQueryLastName = new TermQuery( new Term( FIELD_LASTNAME, strLastName ) );
        booleanQueryMain.add( new BooleanClause( termQueryLastName, BooleanClause.Occur.MUST ) );
        
        return getCustomerSearchResult( booleanQueryMain );
    }
    
    /**
     * Method used to retrun a list of customer based on a search value
     * 
     * @param strSearch
     * @return the list of customer found by the search value
     */
    private static List<Customer> loadBySearch( String strSearch )
    {
        BooleanQuery booleanQueryMain = new BooleanQuery( );
        
        TermQuery termQueryFirstName = new TermQuery( new Term( FIELD_FIRSTNAME, strSearch ) );
        booleanQueryMain.add( new BooleanClause( termQueryFirstName, BooleanClause.Occur.SHOULD ) );
        
        TermQuery termQueryLastName = new TermQuery( new Term( FIELD_LASTNAME, strSearch ) );
        booleanQueryMain.add( new BooleanClause( termQueryLastName, BooleanClause.Occur.SHOULD ) );
        
        return getCustomerSearchResult( booleanQueryMain );
    }

    /**
     * {@inheritDoc }.
     */
    @Override
    public Customer load( String strCustomerId )
    {
        BooleanQuery booleanQueryMain = new BooleanQuery( );      
        TermQuery termQueryId = new TermQuery( new Term( FIELD_ID, strCustomerId ) );
        booleanQueryMain.add( new BooleanClause( termQueryId, BooleanClause.Occur.MUST ) );

        List<Customer> listCustomer = getCustomerSearchResult( booleanQueryMain );
        if ( listCustomer != null && !listCustomer.isEmpty( ) )
        {
            return listCustomer.get( 0 );
        }
        return null;
    }

    /**
     * Convert a Lucene document to a customer
     * 
     * @param document
     * @return the customer associated to the document, null otherwise
     */
    private static Customer buildCustomerFromDocument ( Document document )
    {
        if( document != null )
        {
            Customer customer = new Customer ( );
            customer.setId( document.get( FIELD_ID ) );
            customer.setFirstname( document.get( FIELD_FIRSTNAME ) );
            customer.setLastname( document.get( FIELD_LASTNAME ) );
            customer.setEmail( document.get( FIELD_EMAIL ) );
            customer.setMobilePhone( document.get( FIELD_PHONE ) );
            customer.setFixedPhoneNumber( document.get( FIELD_FIXED_PHONE_NUMBER ) );
            customer.setBirthDate( document.get( FIELD_BIRTHDATE ) );
            if( document.get( FIELD_CIVILITY  ) != null )
            {
                customer.setIdTitle( Integer.valueOf( document.get( FIELD_CIVILITY  ) ) );
            }
            return customer;
        }
        return null;
    }

    /**
     * Returns a json string for autocomplete purpose
     * @param strQuery The query
     * @return The JSON
     */
    public static String search( String strQuery )
    {
        String[] terms = strQuery.split( " " );
        StringBuilder sbSearchQuery = new StringBuilder(  );

        for ( int i = 0; i < terms.length; i++ )
        {
            if ( i > 0 )
            {
                sbSearchQuery.append( ' ' );
            }

            sbSearchQuery.append( terms[i] );
        }

        List<Customer> listCustomers = loadBySearch( sbSearchQuery.toString(  ) );
        
        JSONObject json = new JSONObject(  );

        JSONArray jsonAutocomplete = new JSONArray(  );

        for ( Customer customer : listCustomers )
        {
            JSONObject jsonItem = new JSONObject(  );
            JSONObject jsonItemContent = new JSONObject(  );

            jsonItemContent.accumulate( "first_name", customer.getFirstname(  ) );
            jsonItemContent.accumulate( "last_name", customer.getLastname(  ) );
            jsonItem.accumulate( "item", jsonItemContent );
            jsonAutocomplete.add( jsonItem );
        }

        json.accumulate( "autocomplete", jsonAutocomplete );

        return json.toString( INDENT );
    }
    
}
