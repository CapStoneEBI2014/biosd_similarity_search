package uk.ac.ebi.fg.biosd.rdf.search.core;

import uk.ac.ebi.fg.biosd.rdf.search.searchers.KeyListSearcher;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * The entry point to the Sample Similarity Search in BioSD. 
 * Invokes the underline infrastructure for performing searches against the BioSD SPARQL end point.
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class SearchEngine
{
	/**
	 * In the current version, just invokes the {@link uk.ac.ebi.fg.biosd.rdf.search.searchers.KeyListSearcher}. Possible future developments (e.g., pre/post processing
	 * of search parameters and search results) will be put here.
	 * 
	 * @param offset where the search windows starts (allows to pick results with a paging-like mechanism)
	 * @param limit how many results the search window contains (allows to pick results with a paging-like mechanism)
	 *
	 */
	public Map<URI, SearchResult> search ( List<SearchKey> keys, int offset, int limit )
	{
		KeyListSearcher klSearch = new KeyListSearcher ();
		return klSearch.search ( keys, offset, limit );
	}
}
