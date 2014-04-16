package uk.ac.ebi.fg.biosd.rdf.search.core;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.fg.biosd.rdf.search.searchers.OntologyExpansionSearcher;
import uk.ac.ebi.fg.biosd.rdf.search.searchers.StringSearcher;

/**
 * Searches samples based on configured searchers and a list of input search keys. See {@link #search(List, int, int)} 
 * for details. 
 * 
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class KeyListSearcher
{
	/**
	 * The list of specific {@link KeySearcher key searchers} to be used in {@link #search(List, int, int)}. 
	 * For the moment it contains {@link StringSearcher} only.
	 */
	private List<KeySearcher> searchers = null;
	
	/**
	 * Initialises {@link #searchers} with a list containing an instance of {@link StringSearcher} as the only element.
	 */
	public KeyListSearcher ()
	{
		searchers = new LinkedList<KeySearcher> ();
		searchers.add ( new StringSearcher () );
    searchers.add(new OntologyExpansionSearcher());
	}


	/**
	 * See the method implementation for details.
	 * 
	 * @param keys the search keys (i.e. pairs of attribute value and type) to be used for searching
	 * @param offset where the search windows starts (allows to pick results with a paging-like mechanism)
	 * @param limit how many results the search window contains (allows to pick results with a paging-like mechanism)
	 * 
	 * @return a map structure, which, for each sample URI, gives the corresponding {@link SearchResult} describing 
	 * such found sample. It is useful to represent such results as a map, cause that ease the implementation of this method
	 * and the use of search results in other applications.
	 */
	public Map<URI, SearchResult> search ( List<SearchKey> keys, int offset, int limit )
	{
		// All the results found for all searchers and search keys
		Map<URI, SearchResult> allResults = new HashMap<URI, SearchResult>();
		
		// For each searcher
		for ( KeySearcher searcher: searchers )
		{
			// Used to give more importance to the top-listed keys, see below 
			double decayFactor = 1;
			
			// For each search key 
			for ( SearchKey key: keys ) 
			{
				// Get samples matching the search performed by current searcher and search key
				Map<URI, SearchResult> searcherResults = searcher.search ( key, offset, limit );
				
				if ( searcherResults == null ) continue; // we've nothing to work with, let's go ahead
				
				// For each of such result
				for ( SearchResult thisResult: searcherResults.values () )
				{
					// Decrease the current score by a decay factor, which takes account of the search key position
					double thisScore = thisResult.getScore () * decayFactor;

					URI thisURI = thisResult.getUri ();
					
					// Does the same sample already exist in the global results?
					SearchResult globalResult = allResults.get ( thisURI );
					
					if ( globalResult == null )
					{
						// if no, add it, after having decayed the score by the key-decaying factor
						thisResult.setScore ( thisScore  );
						allResults.put ( thisURI, thisResult );
					}
					else 
						// add the current (decayed) score to the previous accumulated score of the already existing sample 
						globalResult.setScore ( globalResult.getScore () + thisScore  );
					
				} // thisResult loop (and if)

				// keys listed at the end by the user are presumably less important, so penalise them via this score decay 
				// factor
				decayFactor *= 0.98;

			} // keys loop
		} // searcher loop
		
		// Done! Return the collected results
		return allResults;
	}
}
