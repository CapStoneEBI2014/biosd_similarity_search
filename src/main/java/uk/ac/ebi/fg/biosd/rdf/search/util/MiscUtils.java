package uk.ac.ebi.fg.biosd.rdf.search.util;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

/**
 * Various utilities for the similarity search application.
 *
 * <dl><dt>date</dt><dd>3 Apr 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class MiscUtils
{
	/**
	 * Sorts the parameter based on the score values (descending order)
	 */
	public static SearchResult[] sortSearchResult ( Collection<SearchResult> searchResults )
	{
		SearchResult[] result = searchResults.toArray ( new SearchResult [ 0 ] );
		Arrays.sort ( result, new Comparator<SearchResult>() {
			@Override
			public int compare ( SearchResult sr1, SearchResult sr2 )
			{
				return (int) - Math.signum ( sr1.getScore () - sr2.getScore () );
			}
		});
		
		return result;
	}
	
	/**
	 * Add a search result to globalResults, if its {@link SearchResult#getUri() URI} doesn't exist yet in such map.
	 * If it exists, adds up the existing score to the score of the new result.
	 * 
	 * This is the way of adding up search results normally used in the scorers.   
	 */
	public static boolean addSearchResult ( Map<URI, SearchResult> globalResults, SearchResult result )
	{
		SearchResult existingResult = globalResults.get ( result.getUri () );
		if ( existingResult == null )
		{
			// I see this sample for the first time, let's add it to the results
			globalResults.put ( result.getUri (), result );
			return true;
		}

		// This sample is not new, let's sum up the current and the already existing (i.e. accumulated) score 
		existingResult.setScore ( existingResult.getScore () + result.getScore () );
		return false;
	}
}
