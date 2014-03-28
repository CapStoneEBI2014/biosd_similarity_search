package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.SemanticUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Created by shakur on 13/03/2014.
 */
public class OntologyTermExpander
{

	public List<SearchResult> getMoreTerms ( URI termURI, double initialScore )
	{

		System.out.println ( " " );

		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";
		
		// Create a new query
    String queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
        + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
        + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
        + "PREFIX dcterms: <http://purl.org/dc/terms/>\n"
        + "PREFIX obo: <http://purl.obolibrary.org/obo/>\n"
        + "PREFIX efo: <http://www.ebi.ac.uk/efo/>\n"
        + "PREFIX biosd-terms: <http://rdf.ebi.ac.uk/terms/biosd/>\n"
        + "PREFIX pav: <http://purl.org/pav/2.0/>\n"
        + "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
        + "PREFIX sio: <http://semanticscience.org/resource/>\n"
        +"select ?uri \n"
	      + "where { \n"
	      + "  ?uri rdfs:subClassOf <"+termURI+">.\n"
	      +  "}";

		System.out.println ( queryStr );
		Query query = QueryFactory.create ( queryStr );

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.sparqlService ( service, query );
		ResultSet results = qe.execSelect ();

		List<SearchResult> resultList = new ArrayList<SearchResult> ();

		// The top term is the most relevant
		// We don't care about the term label, since these are used to search samples and never shown
		resultList.add ( new SearchResult ( termURI, null, initialScore ) );

		// indirectly-related terms are a bit less relevant
		initialScore *= 0.8;
		
		while ( results.hasNext () )
		{
			QuerySolution s = results.nextSolution ();
			// Again the second null makes it to ignore the term label
			SearchResult result = SemanticUtils.getResultFromQuerySolution ( s, "?uri", null, initialScore );
			
			if ( result != null )
			{
				resultList.add ( result );
			}
		}

		qe.close ();

		return resultList;
	}
}
