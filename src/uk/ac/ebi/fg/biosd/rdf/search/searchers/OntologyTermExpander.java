package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Created by shakur on 13/03/2014.
 */
public class OntologyTermExpander {

    public List<SearchResult> getMoreTerms ( URI termURI, Integer initialScore ) {


//        String SOURCE = "http://www.opentox.org/api/1.1";
        String SOURCE="http://www.ebi.ac.uk/rdf/services/biosamples/sparql";

        String NS = SOURCE + "#";
        //create a model using reasoner
        OntModel model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        //create a model which doesn't use a reasoner
        OntModel model2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        // read the RDF/XML file
        model1.read( SOURCE, "RDF/XML" );
        model2.read( SOURCE, "RDF/XML" );
        //prints out the RDF/XML structure
//        qe.close();
        System.out.println(" ");


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
                        "?uri rdfs:subClassOf "+termURI+"  "+
                        "} \n ";
        Query query = QueryFactory.create(queryStr);

        System.out.println("----------------------");

        System.out.println("Query Result Sheet");

        System.out.println("----------------------");

        System.out.println("Direct&Indirect Descendants (model1)");

        System.out.println("-------------------");


        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model1);
        com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        qe.close();

        System.out.println("----------------------");
        System.out.println("Only Direct Descendants");
        System.out.println("----------------------");

        // Execute the query and obtain results
        qe = QueryExecutionFactory.create(query, model2);
        results =  qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);
        qe.close();

        return Collections.emptyList();

    }
}
