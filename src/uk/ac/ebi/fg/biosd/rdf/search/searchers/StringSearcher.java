package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import com.hp.hpl.jena.query.*;
import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.SemanticUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Does a sample search based on an input pair of sample attribute value and type (like "homo sapiens"/"organism").
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class StringSearcher extends KeySearcher
{

	/**
	 * Perform the required search by using SPARQL and the BioSD 
	 * <a href = "http://www.ebi.ac.uk/rdf/services/biosamples/sparql">SPARQL endpoint</a>. 
	 * 
	 * For a parameter descriptions see {@link KeySearcher#search(SearchKey, int, int)}
	 */
	@Override
	public Map<URI, SearchResult> search ( SearchKey key, int offset, int limit )
	{
		try
		{
			// TODO: Import Jena in the project and then write a code similar to 
			// http://opentox.org/data/documents/development/RDF%20files/JavaOnly/query-reasoning-with-jena-and-sparql
			
			// For the moment, it returns a mock-up test result
			Map<URI, SearchResult> results = new HashMap<URI, SearchResult> ();
		
			//empty res, what for
			SearchResult result = new SearchResult ( new URI ( "" ), "", 0 );
			results.put ( result.getUri (), result );
			
		
			//------------------------------------------------------------------

			String parmType = key.getType();
			String parmLabel = key.getValue();
			    String i =  "i";
			    String queryStr;
			    
			    String service="http://www.ebi.ac.uk/rdf/services/biosamples/sparql";
			    	        
			    Query query;
			    queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
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
			    	     + "select ?smp ?pvLabel ?propTypeLabel where { ?smp  a biosd-terms:Sample;  "
			             + "biosd-terms:has-bio-characteristic | obo:IAO_0000136 ?pv; "
			             + "pav:derivedFrom ?webRec. "
			             + "?pv a ?pvType; "
			             + "rdfs:label ?pvLabel. "
			             + "?pvType  rdfs:label ?propTypeLabel. "
			             + "FILTER ( REGEX ( STR ( ?propTypeLabel ), " +(char)34+  parmType  +(char)34+  "," +(char)34+ i +(char)34+") ). "
			             + "FILTER ( REGEX ( STR ( ?pvLabel  ), " +(char)34+  parmLabel  +(char)34+  "," +(char)34+ i +(char)34+") ).}";
			      
			    
			    
			              
			    
			    	     
			    	       query = QueryFactory.create(queryStr);
			    	        // Remote execution.
			    	        QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query);


			    	        final ResultSet rset = qexec.execSelect();

			    	        while(rset.hasNext()){
			    	        	 QuerySolution s = rset.nextSolution();
                                result = SemanticUtils.getResultFromQuerySolution(s);
			    				if(result != null) {
                                    results.put( result.getUri (), result);
                                }
			    	        	 
			    	           } 


			    	    
			    	        
			//-----------------------------------------------------------------
			
			
			

			return results;
		} 
		catch ( URISyntaxException ex )
		{
			throw new RuntimeException ( "Problem with the String-based searcher: " + ex.getMessage (), ex );
		}
	}

}
