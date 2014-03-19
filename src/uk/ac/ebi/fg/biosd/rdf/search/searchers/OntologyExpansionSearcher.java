package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import java.net.URI;
import java.util.Map;

/**
 * Created by shakur on 18/03/2014.
 */
public class OntologyExpansionSearcher extends KeySearcher{
    @Override
    public Map<URI, SearchResult> search(SearchKey key, int offset, int limit) {
        //TODO: implement calls to OntologyTermExpander and OntologyKeySearcher
        return null;
    }
}
