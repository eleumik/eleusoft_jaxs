package org.eleusoft.jaxs.trax;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;


import org.w3c.dom.Node;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource; 



import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;


/** 
 * Serializer for DOM nodes based on TrAX.
 * This implementation uses an empty Transformer instance
 * obtained using
 * <pre>
 * TransformerFactory.newInstance().newTransformer()
 * </pre>
 * 
 **/
class TrAXDOMSerializer extends AbstractDOMSerializer
{
	private final Transformer identityTransformer;
	
	public TrAXDOMSerializer()
	{
	    this(TransformerFactory.newInstance());
	}
	public TrAXDOMSerializer(final TransformerFactory factory)
	{
	    if (factory==null) throw new IllegalArgumentException("null factory passed");
	    try
		{
			identityTransformer =  factory.newTransformer();
		}
		catch(TransformerConfigurationException e)
		{
		    e.printStackTrace();
			throw new RuntimeException("Could not initialize TrAXDOMSerializer, " + 
			    "TransformerFactory configuration error:" + e.getMessage());
		}
	}
	protected final Transformer getIdentityTransformer()
	{
	    return identityTransformer;
	}
	
	protected final void serializeNode(
	    final Node node, 
	    final Writer out) throws IOException
	{
		final StreamResult streamResult = new StreamResult(out);
		serialize(node, streamResult, null);
	}
	protected final void serializeNode(
	    final Node node, 
	    final OutputStream out, 
		final String encoding) throws IOException
	{
		final StreamResult streamResult = new StreamResult(out);
		serialize(node, streamResult, encoding);
	}
	
	private void serialize(
	    final Node node, 
	    final StreamResult result, 
		final String encoding) throws IOException
	{
	    // TODO hmm..here the encoding may be different from that of
	    // instance ? no, the only exception is in toString(Node), but there
	    // a (String-)Writer is used..
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
			throw new RuntimeException("internal Error in TrAXDOMSerializer: " + te.getMessage(), te);
		}
	}
	
		
}
