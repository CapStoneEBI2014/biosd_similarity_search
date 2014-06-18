//package uk.ac.ebi.fg.biosd.rdf.search.test;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//
//import uk.ac.ebi.fg.biosd.rdf.search.core.SearchEngine;
//import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
//import uk.ac.ebi.fg.biosd.rdf.search.searchers.OntologyTermExpander;
//
///**
// * A parameter-less command line that tests the {@link SearchEngine}. 
// * 
// * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
// *
// */
//public class BasicTestOnt
//{
//	public static void main ( String[] args ) throws URISyntaxException
//	{
//	  OntologyTermExpander expander = new OntologyTermExpander ();
//	  URI uri = new URI("http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10088");
//	
//	  List<SearchResult> results = expander.getMoreTerms ( uri, 1.0 );
//	  for ( SearchResult result: results )
//	  	System.out.println ( result );
//	}
//
//}
