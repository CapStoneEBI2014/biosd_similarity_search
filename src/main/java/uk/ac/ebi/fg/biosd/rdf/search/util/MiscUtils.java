package uk.ac.ebi.fg.biosd.rdf.search.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

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
}
