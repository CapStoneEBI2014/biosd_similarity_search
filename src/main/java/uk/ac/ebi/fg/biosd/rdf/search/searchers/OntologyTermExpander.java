package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

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
	
	private Logger log = LoggerFactory.getLogger ( this.getClass () );
	
	
	public List<SearchResult> getMoreTerms ( URI termURI, double initialScore )
	{
		log.debug ( "Expanding <{}>", termURI );
		List<SearchResult> result = getSubClasses ( termURI, initialScore, 0, -1, new ArrayList<SearchResult> () );
		log.debug ( "Expansion of <{}> done, {} results", termURI, result == null ? "0 (null)" : "" + result.size () );
		return result;
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
					
		// Search for children of this term
		String queryStr =
	    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
	    +"select distinct ?uri \n"
	    + "where { \n"
	    + "  ?uri rdfs:subClassOf <" + topTermURI + ">.\n"
	    +  "}";

		log.trace ( "Doing: {}", queryStr );
		Query query = QueryFactory.create ( queryStr );

		// Execute the query and obtain child term URIs
		// Against the cache or the external service, depending on what you have available
		OntClass cachedTerm = cache.getOntClass ( topTermURI.toASCIIString () );
		// DEBUG if ( cachedTerm != null ) out.println ( "Cached term: " + topTermURI );

		QueryExecution qe;
		
		if ( cachedTerm != null ) 
			qe = QueryExecutionFactory.create ( query, cache );
		else 
		{
			// Bioportal, more complete, but slower
			/*QueryEngineHTTP httpQe = QueryExecutionFactory.createServiceRequest ( "http://sparql.bioontology.org/ontologies/sparql", query );
			// This is an API key associated to a BioSD user 
			httpQe.addParam ( "apikey", "07732278-7854-4c4f-8af1-7a80a1ffc1bb" );
			// httpQe.addParam ( "rules", "SUBC" );
			qe = httpQe;*/
			
			// The ontologies in our endpoint, less complete, but faster
			qe = QueryExecutionFactory.sparqlService ( "http://www.ebi.ac.uk/rdf/services/biosamples/sparql", query );
		}
		
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
