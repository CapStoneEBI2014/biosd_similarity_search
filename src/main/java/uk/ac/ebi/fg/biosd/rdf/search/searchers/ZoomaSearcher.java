package uk.ac.ebi.fg.biosd.rdf.search.searchers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import uk.ac.ebi.fg.biosd.rdf.search.core.KeySearcher;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchKey;
import uk.ac.ebi.fg.biosd.rdf.search.core.SearchResult;
import uk.ac.ebi.fg.biosd.rdf.search.util.MiscUtils;
import uk.ac.ebi.fgpt.zooma.search.ontodiscover.CachedOntoTermDiscoverer;
import uk.ac.ebi.fgpt.zooma.search.ontodiscover.ZoomaOntoTermDiscoverer;
import uk.ac.ebi.fgpt.zooma.search.ontodiscover.OntologyTermDiscoverer.DiscoveredTerm;

/**
 * TODO: Comment me!
 *
 * <dl><dt>date</dt><dd>25 Aug 2014</dd></dl>
 * @author Marco Brandizi
 *
 */
public class ZoomaSearcher extends KeySearcher
{
	private OntologyExpansionSearcher ontoSearcher = new OntologyExpansionSearcher ();
	private CachedOntoTermDiscoverer zoomaDiscoverer = new CachedOntoTermDiscoverer ( new ZoomaOntoTermDiscoverer () );

	@Override
	public Map<URI, SearchResult> search ( SearchKey key, int offset, int limit )
	{
		Map<URI, SearchResult> results = new HashMap<> ();
		
		if ( key.getOntoTermURI () != null ) return results;
		String type = StringUtils.trimToNull ( key.getType () ), value = StringUtils.trimToNull ( key.getValue () );
		if ( type == null && value == null ) return results;
		
		for ( DiscoveredTerm term: zoomaDiscoverer.getOntologyTermUris ( value, type ) )
		{
			// They use a 0-100 scale, we try to keep it within 0-1
			float zoomaScore = term.getScore () / 100f;
			
			for ( SearchResult result: ontoSearcher.search ( new SearchKey ( term.getUri () ), offset, limit ).values () )
			{
				// Weight the current score with the score returned by ZOOMA
				result.setScore ( result.getScore () * zoomaScore );
				MiscUtils.addSearchResult ( results, result );
			}
		}
		
		return results;
	}

}
