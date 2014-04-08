package uk.ac.ebi.fg.biosd.rdf.search.test;

import static java.lang.System.out;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import uk.ac.ebi.fg.biosd.rdf.search.core.SearchEngine;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.searchers.OntologyTermExpander;

/**
 * A parameter-less command line that tests the {@link SearchEngine}. 
 * 
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class BasicTest
{
  /**
   * You can invoke this by right-clicking on this class in the Eclipse package view and selecting 
   * 'Run As' -&gt; 'Jaba Application'. 
   * 
   * You can also run it by building the project and then typing this from the command line: 
   * java -jar &lt;project-binary.jar&gt; BasicTest
   * 
   */
	public static void main ( String[] args )  throws URISyntaxException
	{
		// Prepare a list testing search keys 
		List<SearchKey> keys = new LinkedList<SearchKey> ();
		//keys.add ( new SearchKey ( "homo sapiens", "organism" ) );
		//keys.add ( new SearchKey ( "liver", "organism part" ) );
		
		
		OntologyTermExpander expander = new OntologyTermExpander ();
		URI uri = new URI("http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10088");
		
		String paramLable;
		String paramType;
	    String answer;
		Scanner scan = new Scanner(System.in);
	       // enter filtering criteria
		
		do {  
	          System.out.print("Please enter SearchKey  Label :\n ");  
	           paramLable = scan.nextLine();
	           System.out.print("Please enter SearchKey Type:\n");  
	           paramType = scan.nextLine();
	           System.out.println("Do you want to add another SearchKey- y or n?");  
	           answer = scan.nextLine();  
	            }  
	               while (answer.equals("y"));  
	                 {  
	                 keys.add ( new SearchKey ( paramLable,paramType ) );		  
	                 System.out.println( "You Entered :" + paramLable + " , " + paramType );  
	     
	            } 
		
		 
		
		// Pass it to the search engines
		SearchEngine engine = new SearchEngine ();
	 	Map<URI, SearchResult> samples = engine.search ( keys, 0, 10 );
	 	List<SearchResult> results = expander.getMoreTerms ( uri, 1.0 );
	 	
	 	//Combine results
	 	results.addAll(samples.values ());
	 		 	
	     //print results
	     for ( SearchResult result: results)
		    	System.out.println ( result );
		
	}

}
