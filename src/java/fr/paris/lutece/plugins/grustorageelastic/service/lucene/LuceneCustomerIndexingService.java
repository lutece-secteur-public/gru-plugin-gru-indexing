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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
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

    private static final String PATH_INDEX = "/WEB-INF/plugins/gru/modules/indexer/indexes";
    private static final Version LUCENE_VERSION = Version.LUCENE_4_9;
    private static final int INDENT = 4;

    private static final String FIELD_ID = "id";
    private static final String FIELD_CONNECTION_ID = "connection_id";
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
        IndexWriter writer = null;
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, getAnalyzer( ) );

            iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );

            writer = new IndexWriter( dir, iwc );
            Document document = customer2Document( customer );
            Customer customerIndexed = load( customer.getId( ) );

            if ( customerIndexed == null )
            {
                writer.addDocument( document );
            }
            else
            {
                writer.updateDocument( new Term( FIELD_ID, customer.getId( ) ), document );
            }

            AppLogService.info( "\n\n\n\n\n END LUCENE INDEXING \n\n\n" );
            writer.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
        }
        finally
        {
            if ( writer != null )
            {
                try
                {
                    writer.close( );
                }
                catch( IOException ex )
                {
                    AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
                }
            }
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
    private static class CustomAnalyzer extends Analyzer
    {
        @Override
        protected TokenStreamComponents createComponents( String fieldName, Reader reader )
        {
            final Tokenizer source = new StandardTokenizer( LUCENE_VERSION, reader );

            TokenStream tokenStream = source;
            tokenStream = new LowerCaseFilter( LUCENE_VERSION, tokenStream );
            tokenStream = new ASCIIFoldingFilter( tokenStream );
            return new TokenStreamComponents( source, tokenStream );
        }
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
        IndexWriter writer = null;
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, getAnalyzer( ) );

            iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );

            writer = new IndexWriter( dir, iwc );

            writer.deleteDocuments( new Term( FIELD_ID, customer.getId( ) ) );
            writer.commit( );
            writer.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
        }
        finally
        {
            if ( writer != null )
            {
                try
                {
                    writer.close( );
                }
                catch( IOException ex )
                {
                    AppLogService.error( "Error indexing customer : " + ex.getMessage( ), ex );
                }
            }
        }
    }

    /**
     * Method which returns a list of all customer found from the specified query
     * 
     * @param query
     * @return a list of all customer found or null
     */
    private static List<Customer> getCustomerSearchResult( Query queryToLaunch )
    {
        List<Customer> listCustomer = new ArrayList<Customer>( );
        try
        {
            IndexReader indexReader = DirectoryReader.open( FSDirectory.open( getIndexPath( ) ) );
            IndexSearcher indexSearcher = new IndexSearcher( indexReader );

            if ( indexSearcher != null )
            {
                String [ ] strTabFields = {
                        FIELD_FIRSTNAME, FIELD_LASTNAME
                };
                MultiFieldQueryParser mfqp = new MultiFieldQueryParser( LUCENE_VERSION, strTabFields, getAnalyzer( ) );
                mfqp.setAutoGeneratePhraseQueries( true );
                mfqp.setAllowLeadingWildcard( true );
                mfqp.setLowercaseExpandedTerms( true );
                Query query = mfqp.parse( queryToLaunch.toString( ) );

                // Get results documents
                TopDocs topDocs = indexSearcher.search( query, LuceneSearchEngine.MAX_RESPONSES );
                ScoreDoc [ ] hits = topDocs.scoreDocs;

                for ( int i = 0; i < hits.length; i++ )
                {
                    int docId = hits [i].doc;
                    Document document = indexSearcher.doc( docId );
                    listCustomer.add( document2Customer( document ) );
                }
            }

        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( ParseException e )
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

        List<Customer> listCustomer = new ArrayList<Customer>( );
        try
        {
            IndexReader indexReader = DirectoryReader.open( FSDirectory.open( getIndexPath( ) ) );
            IndexSearcher indexSearcher = new IndexSearcher( indexReader );

            if ( indexSearcher != null )
            {
                // Get results documents
                TopDocs topDocs = indexSearcher.search( booleanQueryMain, LuceneSearchEngine.MAX_RESPONSES );
                ScoreDoc [ ] hits = topDocs.scoreDocs;

                for ( int i = 0; i < hits.length; i++ )
                {
                    int docId = hits [i].doc;
                    Document document = indexSearcher.doc( docId );
                    listCustomer.add( document2Customer( document ) );
                }
            }
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        if ( listCustomer != null && !listCustomer.isEmpty( ) )
        {
            return listCustomer.get( 0 );
        }
        return null;
    }

    /**
     * Converts a {@link Customer} object to a {@link Document} object
     * 
     * @param customer
     *            The customer
     * @return the Document
     */
    private static Document customer2Document( Customer customer )
    {
        Document doc = new Document( );

        Field fielIdname = new StringField( FIELD_ID, customer.getId( ), Field.Store.YES );
        doc.add( fielIdname );
        

        Field fielFirstname = new TextField( FIELD_FIRSTNAME, ( customer.getFirstname( ) == null ? StringUtils.EMPTY : customer.getFirstname( ) ),
                Field.Store.YES );
        doc.add( fielFirstname );

        Field fielLastname = new TextField( FIELD_LASTNAME, ( customer.getLastname( ) == null ? StringUtils.EMPTY : customer.getLastname( ) ), Field.Store.YES );
        doc.add( fielLastname );

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

        if ( customer.getEmail( ) != null )
        {
            Field fieldEmail = new StoredField( FIELD_EMAIL, customer.getEmail( ) );
            doc.add( fieldEmail );
        }
        
        Field fieldConnectionId = new StoredField( FIELD_CONNECTION_ID, customer.getConnectionId( ) == null ? StringUtils.EMPTY : customer.getConnectionId( ) );
        doc.add( fieldConnectionId );

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

        return doc;
    }

    /**
     * Converts a {@link Document} object to a {@link Customer} object
     * 
     * @param document
     *            the document
     * @return the customer associated to the document, {@code null} otherwise
     */
    private static Customer document2Customer( Document document )
    {
        if ( document != null )
        {
            Customer customer = new Customer( );
            customer.setId( document.get( FIELD_ID ) );
            customer.setConnectionId( document.get( FIELD_CONNECTION_ID ) );
            customer.setFirstname( document.get( FIELD_FIRSTNAME ) );
            customer.setLastname( document.get( FIELD_LASTNAME ) );
            customer.setEmail( document.get( FIELD_EMAIL ) );
            customer.setMobilePhone( document.get( FIELD_PHONE ) );
            customer.setFixedPhoneNumber( document.get( FIELD_FIXED_PHONE_NUMBER ) );
            customer.setBirthDate( document.get( FIELD_BIRTHDATE ) );
            if ( document.get( FIELD_CIVILITY ) != null )
            {
                customer.setIdTitle( Integer.valueOf( document.get( FIELD_CIVILITY ) ) );
            }
            return customer;
        }
        return null;
    }

    /**
     * Returns a json string for autocomplete purpose
     * 
     * @param strQuery
     *            The query
     * @return The JSON
     */
    public static String search( String strQuery )
    {
        if ( StringUtils.isBlank( strQuery ) || strQuery.replaceAll( " ", StringUtils.EMPTY ).equals( "*" ) )
        {
            return StringUtils.EMPTY;
        }

        String [ ] terms = strQuery.split( " " );
        StringBuilder sbSearchQuery = new StringBuilder( );

        for ( int i = 0; i < terms.length; i++ )
        {
            if ( i > 0 )
            {
                sbSearchQuery.append( ' ' );
            }
            sbSearchQuery.append( terms [i] );
        }
        sbSearchQuery.append( '*' );

        List<Customer> listCustomers = loadBySearch( sbSearchQuery.toString( ) );
        Map<AbstractMap.SimpleEntry<String, String>, Customer> mapCustomer = new LinkedHashMap<AbstractMap.SimpleEntry<String, String>, Customer>( );
        if ( listCustomers != null && !listCustomers.isEmpty( ) )
        {
            for ( Customer customer : listCustomers )
            {
                mapCustomer.put( new AbstractMap.SimpleEntry<String, String>( customer.getFirstname( ), customer.getLastname( ) ), customer );
            }
        }

        JSONObject json = new JSONObject( );

        JSONArray jsonAutocomplete = new JSONArray( );

        for ( Entry<AbstractMap.SimpleEntry<String, String>, Customer> entryCustomer : mapCustomer.entrySet( ) )
        {
            if ( entryCustomer != null && entryCustomer.getValue( ) != null )
            {
                JSONObject jsonItem = new JSONObject( );
                JSONObject jsonItemContent = new JSONObject( );

                jsonItemContent.accumulate( "first_name", entryCustomer.getValue( ).getFirstname( ) );
                jsonItemContent.accumulate( "last_name", entryCustomer.getValue( ).getLastname( ) );
                jsonItem.accumulate( "item", jsonItemContent );
                jsonAutocomplete.add( jsonItem );
            }
        }

        json.accumulate( "autocomplete", jsonAutocomplete );

        return json.toString( INDENT );
    }

}
