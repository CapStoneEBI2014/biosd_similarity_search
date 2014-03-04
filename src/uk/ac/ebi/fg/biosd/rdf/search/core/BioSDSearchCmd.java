package uk.ac.ebi.fg.biosd.rdf.search.core;

import static java.lang.System.out;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BioSDSearchCmd {

	public static void main(String[] args) {
		
		Iterator<String> iter = Arrays.asList(args).iterator();
		List<SearchKey> keys = new LinkedList<SearchKey> ();

		//if arguments present
		while(iter.hasNext()) {
			String term = iter.next();
			String query = "";
			//if another argument, read it
			if(iter.hasNext()) {
				query = iter.next();
			}
			if (query != "") {
				//create a SearchKey
				keys.add(new SearchKey(query, term));
			}
		}
		
		SearchEngine engine = new SearchEngine ();
		Map<URI, SearchResult> samples = engine.search ( keys, 0, 10 );
		
		// Show the results
		for ( SearchResult result: samples.values () )
		{
			out.println ( "Sample URI: " + result.getUri () + ", Label: " + result.getLabel () + ", score: " + result.getScore () );
		}
	}

}
