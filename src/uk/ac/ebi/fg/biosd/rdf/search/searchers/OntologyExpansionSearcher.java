package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

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

	OntologyTermExpander ontologyTermExpander = new OntologyTermExpander ();
	OntologyKeySearcher ontologyKeySearcher = new OntologyKeySearcher ();

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
				URI sampleURI = sample.getUri ();

				// Weight the current sample's score with the score of the term that has found it
				double sampleScore = sample.getScore () * termScore;
				
				SearchResult existingSample = globalResults.get ( sampleURI );
				if ( existingSample == null )
				{
					// I see this sample for the first time, let's add it to the results
					sample.setScore ( sampleScore );
					globalResults.put ( sample.getUri (), sample );
				} 
				else
				{
					// This sample is not new, let's sum up the current and the already existing (i.e. accumulated) score 
					existingSample.setScore ( existingSample.getScore () + sampleScore );
				}
			}
		}
			
		// Done! Return what we have collected 
		return globalResults;
	}
}
