package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shakur on 18/03/2014.
 */
public class OntologyExpansionSearcher extends KeySearcher{

    OntologyTermExpander ontologyTermExpander = new OntologyTermExpander();
    OntologyKeySearcher ontologyKeySearcher = new OntologyKeySearcher();

    Float decayFactor = 1f;

    @Override
    public Map<URI, SearchResult> search(SearchKey key, int offset, int limit) {

        Map<URI, SearchResult> globalResults = new HashMap<URI, SearchResult>();
        if(key.getOntoTermURI() != null) {
          List<SearchResult> termResults = ontologyTermExpander.getMoreTerms(key.getOntoTermURI(), 1.0);

          for(SearchResult sr: termResults) {
              Map<URI, SearchResult> keyResults = ontologyKeySearcher.search(new SearchKey(sr.getUri()), offset, limit);

              for(SearchResult kr: keyResults.values()) {
                  if(globalResults.get(kr.getUri()) == null) {
                      globalResults.put(kr.getUri(), kr);
                  } else {
                      SearchResult res = globalResults.get(kr.getUri());
//                      res.setScore(res.getScore() + kr.getScore());
//                      do we need to take care of a decay factor?
                      res.setScore(res.getScore() + kr.getScore() * decayFactor );
                  }
              }
          }
        }
        return globalResults;
    }
}