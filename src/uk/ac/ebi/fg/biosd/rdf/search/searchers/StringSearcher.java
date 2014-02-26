package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

/**
 * Does a sample search based on an input pair of sample attribute value and type (like "homo sapiens"/"organism").
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class StringSearcher extends KeySearcher
{

	/**
	 * Perform the required search by using SPARQL and the BioSD 
	 * <a href = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql">SPARQL endpoint</a>. 
	 * 
	 * For a parameter descriptions see {@link KeySearcher#search(SearchKey, int, int)}
	 */
	@Override
	public Map<URI, SearchResult> search ( SearchKey key, int offset, int limit )
	{
		
		// TODO: Import Jena in the project and then write a code similar to 
		// http://opentox.org/data/documents/development/RDF%20files/JavaOnly/query-reasoning-with-jena-and-sparql
		
		// For the moment, it returns a mock-up test result
		Map<URI, SearchResult> results = new HashMap<URI, SearchResult> ();
		
		try
		{
			SearchResult result = new SearchResult ( new URI ( "http://foo/uri/to/sample1" ), "Sample 1", 100.0 );
			results.put ( result.getUri (), result );
			
			result = new SearchResult ( new URI ( "http://foo/uri/to/sample2" ), "Sample 3", 80.0 );
			results.put ( result.getUri (), result );

			result = new SearchResult ( new URI ( "http://foo/uri/to/sample3" ), "Sample 3", 80.0 );
			results.put ( result.getUri (), result );

		} 
		catch ( URISyntaxException ex )
		{
			throw new RuntimeException ( "Problem with the ttring-based searcher: " + ex.getMessage (), ex );
		}
		
	}

}
