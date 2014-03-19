package uk.ac.ebi.fg.biosd.rdf.search.util;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by shakur on 18/03/2014.
 */
public class SemanticUtils {

    public static SearchResult getResultFromQuerySolution(QuerySolution s) {
        RDFNode z = s.get("?propTypeLabel");
        RDFNode y = s.get("?pvLabel");
        String rdsy = y.toString();
        String rdsz = z.toString();
        rdsy = rdsy.substring(0, rdsy.indexOf("^")); //line 4
        rdsz = rdsz.substring(0, rdsz.indexOf("^")); //line 4

        SearchResult result = null;

        try {
            result = new SearchResult( new URI( s.getResource("?smp").toString() ),rdsz + "-" + rdsy , 0.8 );
        } catch (URISyntaxException e) {
            System.out.println("Exception raised parsing " + s.toString());
        }


        return result;
    }
}
