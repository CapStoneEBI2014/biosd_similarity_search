package uk.ac.ebi.fg.biosd.rdf.search.util;

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Created by shakur on 18/03/2014.
 */
public class SemanticUtils {

  	/** 
  	 * Creates a SearchResult out of {@link QuerySolution}, taking the URI and the label to populate such result from 
  	 * the uriVarName and uriMethod parameters (i.e., assumes that SPARQL had these variables in the SELECT clause)
  	 * and setting the specified rank. labelVarName might be null (a null label is set) and both these parameters
  	 * must use the '?' prefix.
  	 * 
  	 */
  	public static SearchResult getResultFromQuerySolution ( QuerySolution s, String uriVarName, String labelVarName, double score ) 
  	{
      	RDFNode uriNode = s.get( uriVarName );
      	String uriStr = uriNode.toString ();
      	
        RDFNode labelNode = labelVarName == null ? null : s.get( labelVarName );
        String labelStr = labelNode == null ? null : labelNode.toString (); 
        
        SearchResult result = null;

        try {
            result = new SearchResult( new URI( uriStr ), labelStr, score );
        } 
        catch (URISyntaxException e) {
            throw new RuntimeException ( "Problem while parsing " + s.toString() + ": " + e.getMessage (), e );
        }


        return result;
    }
}
