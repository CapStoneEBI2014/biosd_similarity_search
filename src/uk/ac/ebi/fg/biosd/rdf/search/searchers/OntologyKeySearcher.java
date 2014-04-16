package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.SemanticUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Search samples having attributes associated to a given ontology term.
 * 
 * <dl>
 * <dt>date</dt>
 * <dd>26 Feb 2014</dd>
 * </dl>
 * 
 */
public class OntologyKeySearcher extends KeySearcher
{

	/**
	 * Perform the required search by using SPARQL and the BioSD <a href =
	 * "http://www.ebi.ac.uk/rdf/services/biosamples/sparql">SPARQL endpoint</a>.
	 * 
	 * For a parameter descriptions see {@link KeySearcher#search(SearchKey, int, int)}
	 */
	@Override
	public Map<URI, SearchResult> search ( SearchKey key, int offset, int limit )
	{
		Map<URI, SearchResult> results = new HashMap<URI, SearchResult> ();

		// Does the parameter contain a URI? Returns an empty result if not 
		URI ontoTermURI = key.getOntoTermURI ();
		if ( ontoTermURI == null ) return results; 

		// Query for the samples having annotations that are instances of this term
		//
		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";
		String queryStr =
	    	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n"
			+ "PREFIX biosd-terms: <http://rdf.ebi.ac.uk/terms/biosd/>\n"
			+ "\n"
			+ "#\n"
			+ "## Samples with a given property value and type, selects the sample URI and label\n"
			+ "#\n"
			+ "SELECT DISTINCT ?smp ?smpLabel\n"
			+ "{\n"
			+ "  ?smp\n"
			+ "    a biosd-terms:Sample;\n"
			+ "    rdfs:label ?smpLabel;\n"
			+ "    biosd-terms:has-bio-characteristic ?pv.  # is about\n"
			+ "\n"
			+ "   ?pv biosd-terms:has-bio-characteristic-type ?pt.\n"
			+ "   ?pt a <" + ontoTermURI.toASCIIString () + ">.\n"
			+ "} LIMIT " + limit + " OFFSET "+ offset + "\n";
		
		Query query = QueryFactory.create ( queryStr );
		QueryExecution qexec = QueryExecutionFactory.sparqlService ( service, query );

		// Collect the results
		ResultSet rset = qexec.execSelect ();
		while ( rset.hasNext () )
		{
			QuerySolution s = rset.nextSolution ();
			// All with the same score level, nothing makes them different here.
			SearchResult result = SemanticUtils.getResultFromQuerySolution ( s, "?smp", "?smpLabelStr", 1.0 );
			if ( result != null )
				results.put ( result.getUri (), result );
		}

		return results;
	}

}
