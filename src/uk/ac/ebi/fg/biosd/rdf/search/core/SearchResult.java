package uk.ac.ebi.fg.biosd.rdf.search.core;

import java.net.URI;

/**
 * A search result contains a URI, like a sample URI (i.e., an identifier), an optional label 
 * (to be used for visualisation purposes) and a score number, which accounts for the significant of a searh result
 * with respect to the search criteria (i.e., a {@link SearchKey search key} or a list of search keys.
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class SearchResult
{
	private URI uri;
	private String label;
	private double score;
	
	public SearchResult ( URI uri, String label, double score )
	{
		super ();
		this.uri = uri;
		this.label = label;
		this.score = score;
	}

	public URI getUri ()
	{
		return uri;
	}

	public void setUri ( URI uri )
	{
		this.uri = uri;
	}

	public String getLabel ()
	{
		return label;
	}

	public void setLabel ( String label )
	{
		this.label = label;
	}

	public double getScore ()
	{
		return score;
	}

	public void setScore ( double score )
	{
		this.score = score;
	}
	
	
}
