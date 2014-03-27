package uk.ac.ebi.fg.biosd.rdf.search.core;

import java.net.URI;

/**
 * Sample similarity search is based on a list of attribute value and type pair, which for the moment consists of
 * two strings, such as "homo sapiens" and "organism". This class represents such pairs.
 *
 * <dl><dt>date</dt><dd>26 Feb 2014</dd></dl>
 *
 */
public class SearchKey
{
	private String value, type;
	private URI ontoTermURI;

	public SearchKey ( String value, String type )
	{
		super ();
		this.value = value;
		this.type = type;
	}
	
	

	public SearchKey ( URI ontoTermURI )
	{
		super ();
		this.ontoTermURI = ontoTermURI;
	}



	public String getValue ()
	{
		return value;
	}

	public void setValue ( String value )
	{
		this.value = value;
	}

	public String getType ()
	{
		return type;
	}

	public void setType ( String type )
	{
		this.type = type;
	}


	/**
	 * An ontology term URI, associated with this search key, i.e. a standardised OWL class that the current search key
	 * is instance of, such as http://purl.org/obo/owl/NCBITaxon#NCBITaxon_10088 (Mus)
	 */
	public URI getOntoTermURI ()
	{
		return ontoTermURI;
	}


	public void setOntoTermURI ( URI ontoTermURI )
	{
		this.ontoTermURI = ontoTermURI;
	}
	
	
}
