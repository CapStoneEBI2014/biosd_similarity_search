package uk.ac.ebi.fg.biosd.rdf.search.util;

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Created by shakur on 18/03/2014.
 */
public class SemanticUtils
{

	/**
	 * Creates a SearchResult out of {@link QuerySolution}, taking the URI and the label to populate such result from the
	 * uriVarName and uriMethod parameters (i.e., assumes that SPARQL had these variables in the SELECT clause) and
	 * setting the specified rank. labelVarName might be null (a null label is set) and both these parameters must use the
	 * '?' prefix.
	 * 
	 */
	public static SearchResult getResultFromQuerySolution ( QuerySolution s, String uriVarName, String labelVarName, double score )
	{
		URI uri = getURIFromQuerySolution ( s, uriVarName );
		
		RDFNode labelNode = labelVarName == null ? null : s.get ( labelVarName );
		String labelStr = labelNode == null ? null : labelNode.toString ();

		return new SearchResult ( uri, labelStr, score );
	}


	/**
	 * Gets a URI from a {@link QuerySolution}, where uriVarName is the name of the bounded variable in the SELECT clause
	 * that returns the desired URI. uriVarName must be prefixed with '?'.
	 */
	public static URI getURIFromQuerySolution ( QuerySolution s, String uriVarName )
	{
		String uriStr = s.get ( uriVarName ).as ( Resource.class ).getURI ();
		
		try {
			return new URI ( uriStr );
		}
		catch ( URISyntaxException e ) {
			throw new RuntimeException ( "Problem while parsing " + s.toString () + ": " + e.getMessage (), e );
		}
	}
}
