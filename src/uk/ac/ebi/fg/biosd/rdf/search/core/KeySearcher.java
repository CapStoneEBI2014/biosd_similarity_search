package uk.ac.ebi.fg.biosd.rdf.search.core;

import java.net.URI;
import java.util.Map;

import uk.ac.ebi.fg.biosd.rdf.search.searchers.StringSearcher;

/**
 * An abstract searcher that models the idea of taking a {@link SearchKey search attribute pair} and returning some 
 * samples relevant to the search key.
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public abstract class KeySearcher
{
	/**
	 * @return a map that, for each sample URI tells the sample label and a score that accounts how much the sample 
	 * is relevant to the search key. It is convenient to return such result in the form of a map.
	 * 
	 * @param key the search key (e.g., new SearchKey ( "homo sapiens", "organism" )
	 * @param offset where the search windows starts (allows to pick results with a paging-like mechanism)
	 * @param limit how many results the search window contains (allows to pick results with a paging-like mechanism)
   *
   * Being abstract, this methd doesn't implement anything here, since this class is just an interface for concrete
   * implementations, such as {@link StringSearcher}.
	 */
	public abstract Map<URI, SearchResult> search ( SearchKey key, int offset, int limit );
}
