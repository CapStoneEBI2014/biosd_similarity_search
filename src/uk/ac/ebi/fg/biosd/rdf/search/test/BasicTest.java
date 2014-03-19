package uk.ac.ebi.fg.biosd.rdf.search.test;

import static java.lang.System.out;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchEngine;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

/**
 * A parameter-less command line that tests the {@link SearchEngine}. 
 * 
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class BasicTest
{
  /**
   * You can invoke this by right-clicking on this class in the Eclipse package view and selecting 
   * 'Run As' -&gt; 'Jaba Application'. 
   * 
   * You can also run it by building the project and then typing this from the command line: 
   * java -jar &lt;project-binary.jar&gt; BasicTest
   * 
   */
	public static void main ( String[] args )
	{
		// Prepare a list testing search keys 
		List<SearchKey> keys = new LinkedList<SearchKey> ();
		keys.add ( new SearchKey ( "homo sapiens", "organism" ) );
		keys.add ( new SearchKey ( "liver", "organism part" ) );
		
		// Pass it to the search engine
		SearchEngine engine = new SearchEngine ();
		Map<URI, SearchResult> samples = engine.search ( keys, 0, 10 );
		
		// Show the results
		for ( SearchResult result: samples.values () )
		{
			out.println ( "Sample URI: " + result.getUri () + ", Label: " + result.getLabel () + ", score: " + result.getScore () );
		}
	}

}
