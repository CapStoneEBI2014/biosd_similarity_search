package uk.ac.ebi.fg.biosd.rdf.search.core;

import uk.ac.ebi.fg.biosd.rdf.search.searchers.OntologyTermExpander;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TODO: To be completed later. For the moment, we're invoking it all via the test classes into the test package. 
 *
 * <dl><dt>date</dt><dd>16 Apr 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class BioSDSearchCmd
{

	public static void main ( String[] args )
	{

		Iterator<String> iter = Arrays.asList ( args ).iterator ();
		List<SearchKey> keys = new LinkedList<SearchKey> ();

		// if arguments present
		while ( iter.hasNext () )
		{
			String term = iter.next ();
			String query = "";
			// if another argument, read it
			if ( iter.hasNext () )
			{
				query = iter.next ();
			}
			if ( query != "" )
			{
				// create a SearchKey
				keys.add ( new SearchKey ( query, term ) );
			}
		}

		SearchEngine engine = new SearchEngine ();
		Map<URI, SearchResult> samples = engine.search ( keys, 0, 10 );

		// Show the results
		Collection<SearchResult> resultValues = samples.values ();
		for ( SearchResult result: resultValues )
		{
			// out.println ( "Sample URI: " + result.getUri () + ", Label: " + result.getLabel () + ", score: " +
			// result.getScore () );
			if ( result.getUri () != null && result.getUri ().getPath () != "" )
			{
				List score = new OntologyTermExpander ().getMoreTerms ( result.getUri (), 1 );
			}
		}
	}

}
