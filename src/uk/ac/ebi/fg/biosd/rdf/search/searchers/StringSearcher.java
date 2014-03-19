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
 * Does a sample search based on an input pair of sample attribute value and type (like "homo sapiens"/"organism").
 * 
 * <dl>
 * <dt>date</dt>
 * <dd>26 Feb 2014</dd>
 * </dl>
 * 
 */
public class StringSearcher extends KeySearcher
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

		// ------------------------------------------------------------------

		String parmType = key.getType ();
		String parmLabel = key.getValue ();

		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";

		Query query;
		String queryStr =
	    	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
			+ "PREFIX dcterms: <http://purl.org/dc/terms/>"
			+ "PREFIX obo: <http://purl.obolibrary.org/obo/>"
			+ "PREFIX efo: <http://www.ebi.ac.uk/efo/>"
			+ "PREFIX biosd-terms: <http://rdf.ebi.ac.uk/terms/biosd/>"
			+ "PREFIX pav: <http://purl.org/pav/2.0/>"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			+ "PREFIX sio: <http://semanticscience.org/resource/>"
			+ ""
			+ "#"
			+ "## Samples with a given property value and type, selects the sample URI and label"
			+ "#"
			+ "SELECT DISTINCT ?smp ?smpLabelStr"
			+ "WHERE {"
			+ "  ?smp"
			+ "    a biosd-terms:Sample;"
			+ "    rdfs:label ?smpLabel;"
			+ "    biosd-terms:has-bio-characteristic|obo:IAO_0000136 ?pv. # is about"
			+ ""
			+ "  ?pv"
			+ "    a ?pvType;"
			+ "    rdfs:label ?pvLabel."
			+ ""
			+ "  ?pvType"
			+ "    rdfs:label ?propTypeLabel."
			+ ""
			+ "  FILTER ( REGEX ( STR ( ?propTypeLabel ), \"" + parmType + "\", \"i\" ) )."
			+ "  FILTER ( REGEX ( STR ( ?pvLabel ), \"" + parmLabel + "\", \"i\" ) )."
			+ ""
			+ "  BIND ( STR ( ?smpLabel ) AS ?smpLabelStr ) # removes the language annotation"
			+ "}";
		
		query = QueryFactory.create ( queryStr );
		// Remote execution.
		QueryExecution qexec = QueryExecutionFactory.sparqlService ( service, query );

		final ResultSet rset = qexec.execSelect ();

		while ( rset.hasNext () )
		{
			QuerySolution s = rset.nextSolution ();
			SearchResult result = SemanticUtils.getResultFromQuerySolution ( s, "?smp", "?smpLabelStr", 1.0 );
			if ( result != null )
			{
				results.put ( result.getUri (), result );
			}

		}

		// -----------------------------------------------------------------

		return results;
	}

}
