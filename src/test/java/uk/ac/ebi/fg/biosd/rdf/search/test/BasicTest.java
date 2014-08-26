package uk.ac.ebi.fg.biosd.rdf.search.test;

import static java.lang.System.out;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchEngine;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.MiscUtils;

/**
 * JUnit tests for the search engine. 
 * 
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class BasicTest
{
	/**
	 * Tests multiple searhers
	 */
	@Test
	public void testSearchEngine ()  throws URISyntaxException
	{
		// Prepare a list testing search keys 
		List<SearchKey> keys = new LinkedList<SearchKey> ();
		
		// Mus, which is then expanded to terms like  NCBITaxon_10090 (mus-musculus)
		keys.add ( new SearchKey ( new URI ( "http://purl.obolibrary.org/obo/NCBITaxon_10088" ) ));
		
		// Should resolve to http://purl.obolibrary.org/obo/UBERON_0002371
		keys.add ( new SearchKey ( "bone marrow", "organism part" ));

		// Pass it to the search engines
		SearchEngine engine = new SearchEngine ();
	 	Map<URI, SearchResult> samples = engine.search ( keys, 0, 100 );
	 		 	
	  // Show the results
	  for ( SearchResult result: MiscUtils.sortSearchResult ( samples.values () ))
	  	out.println ( result );
	}

}
