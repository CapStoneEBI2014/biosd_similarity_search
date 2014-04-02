package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
	    	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n"
			+ "PREFIX biosd-terms: <http://rdf.ebi.ac.uk/terms/biosd/>\n"
			+ ""
			+ "SELECT DISTINCT ?smp ?smpLabelStr\n"
			+ "WHERE {\n"
			+ "  ?smp\n"
			+ "    a biosd-terms:Sample;\n"
			+ "    rdfs:label ?smpLabel;\n"
			+ "    biosd-terms:has-bio-characteristic|obo:IAO_0000136 ?pv. # is about\n"
			+ ""
			+ "  ?pv\n"
			+ "    a ?pvType;\n"
			+ "    rdfs:label ?pvLabel.\n"
			+ ""
			+ "  ?pvType\n"
			+ "    rdfs:label ?propTypeLabel.\n"
			+ ""
			+ "  FILTER ( REGEX ( STR ( ?propTypeLabel ), \"" + parmType + "\", \"i\" ) ).\n"
			+ "  FILTER ( REGEX ( STR ( ?pvLabel ), \"" + parmLabel + "\", \"i\" ) ).\n"
			+ ""
			+ "  BIND ( STR ( ?smpLabel ) AS ?smpLabelStr ) \n"
			+ "}";

        System.out.println(queryStr);

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
