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
    String queryStr =
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
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
