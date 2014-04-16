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
 * Expand an ontology term, using the SPARQL endpoint of an ontology service. Yelds semantically close terms, such as
 * close subclasses or sibling.
 * 
 */
public class OntologyTermExpander
{

	public List<SearchResult> getMoreTerms ( URI termURI, double initialScore )
	{
		// TODO: BioSD has only a few ontologies, we need to use Bioportal, pretty much the same way
		// 
		
		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";
		// TEST String service = "http://sparql.bioontology.org/ontologies/sparql/?apikey=c6ae1b27-9f86-4e3c-9dcf-087e1156eabe";
		
		// Fetch the subclasses of first level. TODO: a recursive search that goes down a few levels and decrease the 
		// initial score based on the level
		// 
    String queryStr =
	    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	    +"select distinct ?uri \n"
	    + "where { \n"
	    + "  ?uri rdfs:subClassOf <" + termURI + ">.\n"
	    +  "}";

		// DEBUG System.out.println ( queryStr );
		Query query = QueryFactory.create ( queryStr );

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.sparqlService ( service, query );
		ResultSet results = qe.execSelect ();

		List<SearchResult> resultList = new ArrayList<SearchResult> ();

		// The top term is the most relevant
		// We don't care about the term label, since these are used to search samples and never shown
		resultList.add ( new SearchResult ( termURI, null, initialScore ) );

		// indirectly-related terms are a bit less relevant
		initialScore *= 0.98;
		
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
