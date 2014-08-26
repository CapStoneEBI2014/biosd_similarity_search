package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

		String paramType = key.getType ();
		String paramLabel = key.getValue ();

		// Empty result for null/empty strings
		if ( StringUtils.trimToNull ( paramType ) == null || StringUtils.trimToNull ( paramLabel ) == null )  
			return results;
		
		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";

		// All the samples annotated with an attribute value/type having these strings as labels.
		
		String queryStr =
			// The query constraints the samples to have an external web entry attached. This improves the speed and 
			// makes sense.
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
			"PREFIX dcterms: <http://purl.org/dc/terms/>\n" + 
			"PREFIX obo: <http://purl.obolibrary.org/obo/>\n" + 
			"PREFIX efo: <http://www.ebi.ac.uk/efo/>\n" + 
			"PREFIX biosd-terms: <http://rdf.ebi.ac.uk/terms/biosd/>\n" + 
			"PREFIX pav: <http://purl.org/pav/2.0/>\n" + 
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
			"PREFIX sio: <http://semanticscience.org/resource/>\n" + 
			"SELECT DISTINCT ?smp ?smpLabelStr\n" +
			"WHERE \n" +
			"{\n" +
			"  ?smp\n" +
			"    a biosd-terms:Sample;\n" +
			"    rdfs:label ?smpLabel;\n" +
			"    biosd-terms:has-bio-characteristic | sio:SIO_000332 ?pv; # is about\n" +
			"    pav:derivedFrom ?webRec.\n" +
			"\n" +
			"  ?pv\n" +
			"    rdfs:label ?pvLabel;\n" +
			"    biosd-terms:has-bio-characteristic-type ?pvType.\n" +
			"  \n" +
			"  ?pvType \n" +
			"    rdfs:label ?propTypeLabel.\n" +
			"\n" +
			"\n" +
			"  FILTER ( LCASE ( STR ( ?propTypeLabel ) ) = '" + paramType + "' ).\n" +
			"  FILTER ( LCASE ( STR ( ?pvLabel ) ) = '" + paramLabel + "' ).\n" +
			"\n" +
			"  ?webRec\n" +
			"    dcterms:identifier ?repoAcc;\n" +
			"    dcterms:source ?repoName;\n" +
			"    foaf:page ?repoUrl.\n" +
			"\n" + 
			"  BIND ( STR ( ?smpLabel ) AS ?smpLabelStr ) # removes the language annotation\n" +
			"} LIMIT " + limit + " OFFSET "+ offset + "\n";

		
		Query query = QueryFactory.create ( queryStr );
		// Remote execution.
		QueryExecution qexec = QueryExecutionFactory.sparqlService ( service, query );

		final ResultSet rset = qexec.execSelect ();

		while ( rset.hasNext () )
		{
			QuerySolution s = rset.nextSolution ();
			SearchResult result = SemanticUtils.getResultFromQuerySolution ( s, "?smp", "?smpLabelStr", 1.0 );
			if ( result != null )
				results.put ( result.getUri (), result );
		}

		return results;
	}

}
