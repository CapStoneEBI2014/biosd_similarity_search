Enter file contents herepackage uk.ac.ebi.fg.biosd.rdf.search.test;

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
import static java.lang.System.out;

/**
 * A parameter-less command line that tests the {@link SearchEngine}. 
 * 
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class BasicTestOnt
{
  /**
   * You can invoke this by right-clicking on this class in the Eclipse package view and selecting 
   * 'Run As' -&gt; 'Jaba Application'. 
   * 
   * You can also run it by building the project and then typing this from the command line: 
   * java -jar &lt;project-binary.jar&gt; BasicTest
 * @throws URISyntaxException 
   * 
   */
	public static void main ( String[] args ) throws URISyntaxException
	{
		// Prepare a list testing search keys 
		List<SearchKey> keys = new LinkedList<SearchKey> ();
		//keys.add ( new SearchKey ( "homo sapiens", "organism" ) );
		//keys.add ( new SearchKey ( "liver", "organism part" ) );
		
		String paramLable;
		String paramType;
	    String answer;
		Scanner scan = new Scanner(System.in);
	
		
		do {  
	          System.out.print("Please enter SearchKey  Label :\n ");  
	           paramLable = scan.nextLine();
	           System.out.print("Please enter SearchKey Type:\n");  
	           paramType = scan.nextLine();
	           System.out.println("Do you want to add another SearchKey- y or n?\n");  
	           answer = scan.nextLine();  
	            }  
	               while (answer.equals("y"));  
	                 {  
	                 keys.add ( new SearchKey ( paramLable,paramType ) );		  
	                 System.out.println( "You Entered :" + paramLable + " , " + paramType );  
	     
	            } 
	                  
	             	// Pass it to the search engine
	                OntologyTermExpander Expander = new OntologyTermExpander ();
	                URI uri = new URI("http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10088");
  
	                Expander.getMoreTerms(uri, 0.7);
	              
		
		// Pass it to the search engine
		//SearchEngine engine = new SearchEngine ();
		//Map<URI, SearchResult> samples = engine.search ( keys, 0, 10 );
		
		// Show the results
		//for ( SearchResult result: samples.values () )
	//	{
	//		out.println ( "Sample URI: " + result.getUri () + ", Label: " + result.getLabel () + ", score: " + result.getScore () );
	//	}
	}

}
