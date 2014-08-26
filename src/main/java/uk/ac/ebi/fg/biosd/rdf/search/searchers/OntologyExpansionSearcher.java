package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.MiscUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search samples based on ontology terms, combining results from the {@link OntologyTermExpander} and the 
 * {@link OntologyKeySearcher}.
 */
public class OntologyExpansionSearcher extends KeySearcher
{

	private OntologyTermExpander ontologyTermExpander = new OntologyTermExpander ();
	private OntologyKeySearcher ontologyKeySearcher = new OntologyKeySearcher ();

	@Override
	public Map<URI, SearchResult> search ( SearchKey key, int offset, int limit )
	{
		Map<URI, SearchResult> globalResults = new HashMap<URI, SearchResult> ();
		URI termURI = key.getOntoTermURI ();

		if ( termURI == null ) 
			// If there isn't a term to work with, let's return an empty result
			return globalResults;
		
		// Do the ontology term expansion
		List<SearchResult> ontoTerms = ontologyTermExpander.getMoreTerms ( key.getOntoTermURI (), 1.0 );

		// And use it to find samples
		for ( SearchResult ontoTerm: ontoTerms )
		{
			double termScore = ontoTerm.getScore ();
			Map<URI, SearchResult> samples = ontologyKeySearcher.search ( new SearchKey ( ontoTerm.getUri () ), offset, limit );

			for ( SearchResult sample: samples.values () )
			{
				// Weight the current sample's score with the score of the term that has found it
				sample.setScore ( sample.getScore () * termScore );
				
				// Add the current result to global results, considering already existing samples
				MiscUtils.addSearchResult ( globalResults, sample );
			}
		}
			
		// Done! Return what we have collected 
		return globalResults;
	}
}
