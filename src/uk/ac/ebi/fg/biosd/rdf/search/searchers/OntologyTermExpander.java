package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.SemanticUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shakur on 13/03/2014.
 */
public class OntologyTermExpander {

    public List<SearchResult> getMoreTerms ( URI termURI, Integer initialScore ) {


        String SOURCE="http://www.ebi.ac.uk/rdf/services/biosamples/sparql";

        String NS = SOURCE + "#";
        //create a model which doesn't use a reasoner
        OntModel model2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        // read the RDF/XML file
        model2.read( SOURCE, "RDF/XML" );
        System.out.println(" ");

        String service="http://www.ebi.ac.uk/rdf/services/biosamples/sparql";
        // Create a new query
        String queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
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
                +"select ?uri "+
                        "where { "+
                        "?uri rdfs:subClassOf <"+termURI+">  "+
                        "} \n ";

        System.out.println(queryStr);
        Query query = QueryFactory.create(queryStr);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
        ResultSet results =  qe.execSelect();

        List<SearchResult> resultList = new ArrayList<SearchResult>();

        while(results.hasNext()) {
            QuerySolution s =results.nextSolution();
            SearchResult result = SemanticUtils.getResultFromQuerySolution(s);
            if(result != null) {
                resultList.add(result);
            }
        }

        qe.close();

        return resultList;
    }
}
