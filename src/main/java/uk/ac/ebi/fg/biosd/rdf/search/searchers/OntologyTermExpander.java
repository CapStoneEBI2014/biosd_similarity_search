package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.SemanticUtils;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import static java.lang.System.out;

/**
 * Expand an ontology term, using the SPARQL endpoint of an ontology service. Yelds semantically close terms, such as
 * close subclasses or sibling.
 * 
 */
public class OntologyTermExpander
{
	/**
	 * The first time results are gathered from the external service, caches them here, makes things much faster when
	 * the same queries ara issued multiple times.
	 */
	private OntModel cache = ModelFactory.createOntologyModel ( OntModelSpec.OWL_MEM );
	
	public List<SearchResult> getMoreTerms ( URI termURI, double initialScore )
	{
		return getSubClasses ( termURI, initialScore, 0, -1, new ArrayList<SearchResult> () );
	}
	
	

	/**
	 * TODO: Comment me!
	 * @param topTermURI
	 * @param initialScore
	 * @param currentLevel
	 * @param maxLevel
	 * @param collectedResults
	 * @return
	 */
	private List<SearchResult> getSubClasses ( URI topTermURI, double initialScore, int currentLevel, int maxLevel, List<SearchResult> collectedResults )
	{
		
		// DEBUG out.println ( "Expanding: '" + topTermURI + "', level " + currentLevel );
		
		// First of all add the top term to the results
		collectedResults.add ( new SearchResult ( topTermURI, null, initialScore ) );
			
		// Stop if we reached the max desired level (-1 means unlimited)
		if ( maxLevel != -1 && currentLevel >= maxLevel ) return collectedResults; 
			
	  // else, go down the child terms
			
		String service = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql";

		// This is an API key associated to a BioSD user 
		//String service = "http://sparql.bioontology.org/ontologies/sparql/?apikey=07732278-7854-4c4f-8af1-7a80a1ffc1bb";
		
		// Search for children of this term
		String queryStr =
	    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	    +"select distinct ?uri \n"
	    + "where { \n"
	    + "  ?uri rdfs:subClassOf <" + topTermURI + ">.\n"
	    +  "}";

		//DEBUG out.println ( queryStr );
		Query query = QueryFactory.create ( queryStr );

		// Execute the query and obtain child term URIs
		// Against the cache or the external service, depending on what you have available
		OntClass cachedTerm = cache.getOntClass ( topTermURI.toASCIIString () );
		// DEBUG if ( cachedTerm != null ) out.println ( "Cached term: " + topTermURI );

		QueryExecution qe = cachedTerm != null
			? QueryExecutionFactory.create ( query, cache ) 
			: QueryExecutionFactory.sparqlService ( service, query );
		
		ResultSet queryResults = qe.execSelect ();
		List<URI> childTermURIs = new LinkedList<> ();
		
		while ( queryResults.hasNext () )
		{
			QuerySolution s = queryResults.nextSolution ();
			// Again the second null makes it to ignore the term label
			childTermURIs.add ( SemanticUtils.getURIFromQuerySolution ( s, "?uri" ) );
		}
		qe.close ();


		// indirectly-related terms are a bit less relevant
		initialScore *= 0.98;
		currentLevel++;

		// Now go again through all the child terms, to recurse in breadth-first fashion (which might be needed in future, to
		// stop expansion after a given number of terms
		for ( URI childTermURI: childTermURIs )
			getSubClasses ( childTermURI, initialScore, currentLevel, maxLevel, collectedResults );

		// if the term comes from the outside, cache it
		if ( cachedTerm == null ) 
		{
			// OK, let's cache the term and its children
			cachedTerm = cache.createClass ( topTermURI.toASCIIString () );
			for ( URI childTermURI: childTermURIs ) {
				cachedTerm.addSubClass ( cache.createClass ( childTermURI.toString () ) );
			}
		}

		return collectedResults;
	}

}
