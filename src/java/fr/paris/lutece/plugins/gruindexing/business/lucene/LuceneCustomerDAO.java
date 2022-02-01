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
package fr.paris.lutece.plugins.gruindexing.business.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.indexing.IndexingException;
import fr.paris.lutece.plugins.gruindexing.business.IIndexCustomerDAO;
import fr.paris.lutece.portal.service.search.LuceneSearchEngine;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

/**
 * DAO and indexer implementation with Lucene for Customer
 */
public class LuceneCustomerDAO implements IIndexCustomerDAO
{
    private static final String FIELD_ID = "id";
    private static final String FIELD_CONNECTION_ID = "connection_id";
    private static final String FIELD_FIRSTNAME = "firstname";
    private static final String FIELD_LASTNAME = "lastname";
    private static final String FIELD_FAMILYNAME = "familyname";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_FIXED_PHONE_NUMBER = "fixed_phone_phone";
    private static final String FIELD_BIRTHDATE = "birthdate";
    private static final String FIELD_CIVILITY = "civility";

    // Keys
    private static final String KEY_AUTOCOMPLETE = "autocomplete";
    private static final String KEY_OUTPUT = "output";
    private static final String KEY_SEARCH = "search";
    private static final String KEY_FIRSTNAME = FIELD_FIRSTNAME;
    private static final String KEY_LASTNAME = FIELD_LASTNAME;

    private static final String LUCENE_WILDCARD = "*";

    private Analyzer _analyzer;
    /** property index path */
    private String _strIndexPath;
    /** property index in webapp */
    private Boolean _bIndexInWebapp;

    /**
     * Constructor
     * 
     * @param strIndexPath
     *            the index path
     * @param bIndexInWebapp
     *            {@code true} if the index folder is in the webapp, {@code false} otherwise
     */
    public LuceneCustomerDAO( String strIndexPath, Boolean bIndexInWebapp )
    {
        super( );
        this._strIndexPath = strIndexPath;
        this._bIndexInWebapp = bIndexInWebapp;
        this._analyzer = new CustomAnalyzer( );
    }

    /**
     * Constructor
     * 
     * @param strIndexPath
     *            the index path
     */
    public LuceneCustomerDAO( String strIndexPath )
    {
        this( strIndexPath, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Customer customer ) throws IndexingException
    {
        IndexWriter writer = null;
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( _analyzer );

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
    private Path getIndexPath( )
    {
        String strIndexPath = _strIndexPath;

        if ( _bIndexInWebapp )
        {
            strIndexPath = AppPathService.getAbsolutePathFromRelativePath( _strIndexPath );
        }

        return Paths.get( strIndexPath );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( List<Customer> listCustomers ) throws IndexingException
    {
        // Bulk indexing for Lucene is TODO.
        for ( Customer customer : listCustomers )
        {
            insert( customer );
        }
    }

    /**
     * The Class CustomAnalyzer.
     */
    private static class CustomAnalyzer extends Analyzer
    {
        @Override
        protected TokenStreamComponents createComponents( String fieldName )
        {
            final Tokenizer source = new StandardTokenizer( );

            TokenStream tokenStream = source;
            tokenStream = new LowerCaseFilter( tokenStream );
            tokenStream = new ASCIIFoldingFilter( tokenStream );
            return new TokenStreamComponents( source, tokenStream );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( Customer customer ) throws IndexingException
    {
        IndexWriter writer = null;
        try
        {
            Directory dir = FSDirectory.open( getIndexPath( ) );
            IndexWriterConfig iwc = new IndexWriterConfig( _analyzer );

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
     * @param queryToLaunch
     *            the query to launch
     * @return a list of all customer found or null
     */
    private List<Customer> getCustomerSearchResult( String queryToLaunch )
    {
        List<Customer> listCustomer = new ArrayList<Customer>( );
        try
        {
            IndexReader indexReader = DirectoryReader.open( FSDirectory.open( getIndexPath( ) ) );
            IndexSearcher indexSearcher = new IndexSearcher( indexReader );

            if ( indexSearcher != null )
            {
                String [ ] strTabFields = {
                        FIELD_FIRSTNAME, FIELD_LASTNAME, FIELD_FIXED_PHONE_NUMBER, FIELD_PHONE,
                };
                MultiFieldQueryParser mfqp = new MultiFieldQueryParser( strTabFields, _analyzer );
                Query query = mfqp.parse( queryToLaunch );

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
     * {@inheritDoc}
     */
    @Override
    public List<Customer> selectByFilter( Map<String, String> mapFilter )
    {
        String strFirstName = mapFilter.get( FIELD_FIRSTNAME );
        String strLastName = mapFilter.get( FIELD_LASTNAME );

        return selectByName( strFirstName, strLastName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> selectByName( String strFirstName, String strLastName )
    {
        Builder booleanQueryMainBuilder = new BooleanQuery.Builder( );

        if ( StringUtils.isNotBlank( strFirstName ) )
        {
            TermQuery termQueryFirstName = new TermQuery( new Term( FIELD_FIRSTNAME, strFirstName ) );
            booleanQueryMainBuilder.add( new BooleanClause( termQueryFirstName, BooleanClause.Occur.MUST ) );
        }

        if ( StringUtils.isNotBlank( strLastName ) )
        {
            TermQuery termQueryLastName = new TermQuery( new Term( FIELD_LASTNAME, strLastName ) );
            booleanQueryMainBuilder.add( new BooleanClause( termQueryLastName, BooleanClause.Occur.MUST ) );
        }

        return getCustomerSearchResult( booleanQueryMainBuilder.build( ).toString( ) );
    }

    /**
     * Method used to return a list of customer based on a search value
     * 
     * @param strSearch
     *            the search value
     * @return the list of customer found by the search value
     */
    private List<Customer> selectBySearch( String strSearch )
    {
        StringBuilder sbQuery = new StringBuilder( );

        try
        {
            // Analyzer is applied separately as wildcard queries are not analyzed.
            TokenStream stream = _analyzer.tokenStream( null, new StringReader( strSearch ) );
            CharTermAttribute cattr = stream.addAttribute( CharTermAttribute.class );
            stream.reset( );

            while ( stream.incrementToken( ) )
            {
                sbQuery.append( ' ' ).append( cattr.toString( ) );
            }

            sbQuery.append( LUCENE_WILDCARD );

            stream.end( );
            stream.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error search customer : " + ex.getMessage( ), ex );
        }

        return getCustomerSearchResult( sbQuery.toString( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer load( String strCustomerId )
    {
        Builder booleanQueryMainBuilder = new BooleanQuery.Builder( );
        TermQuery termQueryId = new TermQuery( new Term( FIELD_ID, strCustomerId ) );
        booleanQueryMainBuilder.add( new BooleanClause( termQueryId, BooleanClause.Occur.MUST ) );

        List<Customer> listCustomer = new ArrayList<Customer>( );
        try
        {
            IndexReader indexReader = DirectoryReader.open( FSDirectory.open( getIndexPath( ) ) );
            IndexSearcher indexSearcher = new IndexSearcher( indexReader );

            if ( indexSearcher != null )
            {
                // Get results documents
                TopDocs topDocs = indexSearcher.search( booleanQueryMainBuilder.build( ), LuceneSearchEngine.MAX_RESPONSES );
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

        doc.add( new StringField( FIELD_ID, customer.getId( ), Field.Store.YES ) );
        doc.add( new TextField( FIELD_FIRSTNAME, manageNullValue( customer.getFirstname( ) ), Field.Store.YES ) );
        doc.add( new TextField( FIELD_LASTNAME, manageNullValue( customer.getLastname( ) ), Field.Store.YES ) );
        doc.add( new TextField( FIELD_FAMILYNAME, manageNullValue( customer.getFamilyname( ) ), Field.Store.YES ) );
        doc.add( new StringField( FIELD_PHONE, manageNullValue( customer.getMobilePhone( ) ), Field.Store.YES ) );
        doc.add( new StringField( FIELD_FIXED_PHONE_NUMBER, manageNullValue( customer.getFixedPhoneNumber( ) ), Field.Store.YES ) );
        doc.add( new StoredField( FIELD_EMAIL, manageNullValue( customer.getEmail( ) ) ) );
        doc.add( new StoredField( FIELD_CONNECTION_ID, manageNullValue( customer.getConnectionId( ) ) ) );
        doc.add( new StoredField( FIELD_BIRTHDATE, manageNullValue( customer.getBirthDate( ) ) ) );
        doc.add( new StoredField( FIELD_CIVILITY, Integer.toString( customer.getIdTitle( ) ) ) );

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
            customer.setFamilyname( document.get( FIELD_FAMILYNAME ) );
            customer.setLastname( document.get( FIELD_LASTNAME ) );
            customer.setEmail( document.get( FIELD_EMAIL ) );
            customer.setMobilePhone( document.get( FIELD_PHONE ) );
            customer.setFixedPhoneNumber( document.get( FIELD_FIXED_PHONE_NUMBER ) );
            customer.setBirthDate( document.get( FIELD_BIRTHDATE ) );

            if ( document.get( FIELD_CIVILITY ) != null )
            {
                customer.setIdTitle( Integer.parseInt( document.get( FIELD_CIVILITY ) ) );
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
    public String search( String strQuery )
    {
        String strResult = StringUtils.EMPTY;
        List<Customer> listCustomers = selectBySearch( strQuery );

        // In order to display distinct first name + last name
        Set<AbstractMap.SimpleEntry<String, String>> setCustomers = new HashSet<>( );

        if ( listCustomers != null && !listCustomers.isEmpty( ) )
        {
            for ( Customer customer : listCustomers )
            {
                setCustomers.add( new AbstractMap.SimpleEntry<String, String>( customer.getFirstname( ), customer.getLastname( ) ) );
            }
        }

        ObjectMapper mapper = new ObjectMapper( );
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode nodeRoot = new ObjectNode( factory );
        ArrayNode nodeAutocomplete = new ArrayNode( factory );

        for ( AbstractMap.SimpleEntry<String, String> entryCustomer : setCustomers )
        {
            ObjectNode nodeItem = new ObjectNode( factory );
            ObjectNode nodeSearch = new ObjectNode( factory );

            nodeItem.put( KEY_OUTPUT, entryCustomer.getKey( ) + " " + entryCustomer.getValue( ) );

            nodeSearch.put( KEY_FIRSTNAME, entryCustomer.getKey( ) );
            nodeSearch.put( KEY_LASTNAME, entryCustomer.getValue( ) );

            nodeItem.set( KEY_SEARCH, nodeSearch );

            nodeAutocomplete.add( nodeItem );
        }

        nodeRoot.set( KEY_AUTOCOMPLETE, nodeAutocomplete );

        try
        {
            strResult = mapper.writeValueAsString( nodeRoot );
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( "Cannot convert the customers to JSON" );
        }

        return strResult;
    }

    /**
     * Manages the case the specified String is {@code null}
     * 
     * @param strValue
     *            the String to manage
     * @return the correct String when the specified String is {@code null}, {@code strValue} otherwise
     */
    private static String manageNullValue( String strValue )
    {
        return ( strValue == null ) ? StringUtils.EMPTY : strValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll( ) throws IndexingException
    {
        // TODO Auto-generated method stub

    }
}
