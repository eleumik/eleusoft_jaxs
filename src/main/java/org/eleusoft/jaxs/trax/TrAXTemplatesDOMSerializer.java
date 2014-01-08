package org.eleusoft.jaxs.trax;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

import org.w3c.dom.Node;

import javax.xml.transform.Templates;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource; 



import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;


/** 
 * Serializer for DOM nodes based on a TrAX Templates instance.
 * This serializer creates a Templates from an identity stylesheet
 * and use it to get a Transformer to serialize the document.
 * From the tests done it is much slower than the 
 * implementation of TrAXDOMSerializer that uses 
 * an empty default Transformer for each instance...so why use it ? 
 **/
class TrAXTemplatesDOMSerializer extends AbstractDOMSerializer
{
	private static final String IDENTITY_XSLT_SOURCE = 
	    "<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>" + 
	        "<xsl:template match='@*|node()'><xsl:copy>" + 
	        "<xsl:apply-templates select='@*|node()'/></xsl:copy></xsl:template>" + 
	    "</xsl:stylesheet>";
	private static Templates identityTemplates;
	private Transformer identityTransformer;
	
	
	static
	{
		try
		{
			java.io.Reader reader = new java.io.StringReader(IDENTITY_XSLT_SOURCE);
			TransformerFactory transFact = TransformerFactory.newInstance();
			StreamSource stsc = new StreamSource(reader);
			identityTemplates = transFact.newTemplates(stsc);
			
		}
		catch(TransformerConfigurationException tce)
		{
			tce.printStackTrace();
			throw new RuntimeException("Error initializing TrAXTemplatesDOMSerializer");
		}
		
	}
	
	TrAXTemplatesDOMSerializer()
	{
		try
		{
			identityTransformer = identityTemplates.newTransformer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Error initializing TrAXTemplatesDOMSerializer");
		}
	}
	    
	
	
	protected final Transformer getIdentityTransformer()
	{
	    return identityTransformer;
	}
	
	protected final void serializeNode(final Node node, final Writer out) throws IOException
	{
		final StreamResult streamResult = new StreamResult(out);
		serialize(node, streamResult, null);
	}
	protected final void serializeNode(final Node node, final OutputStream out, 
		final String encoding) throws IOException
	{
		final StreamResult streamResult = new StreamResult(out);
		serialize(node, streamResult, encoding);
	}
	
	private void serialize(final Node node, final StreamResult result, 
		String encoding) throws IOException
	{
		if (node==null) throw new IllegalArgumentException("null node passed");
		try
		{
			final Transformer serializer = getIdentityTransformer();
			TrAXSerializerUtil.configure(serializer, this);
			serializer.transform(new DOMSource(node), result);
			
		}
		catch(TransformerException te)
		{
			te.printStackTrace();
			throw new RuntimeException("internal Error in TrAXDOMSerializer");
		}
	}
	
		
}
