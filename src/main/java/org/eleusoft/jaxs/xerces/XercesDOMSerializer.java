package org.eleusoft.jaxs.xerces;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Serializer;
import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



/** 
 * Serializer for DOM nodes based on Apache Xerces.
 * Not thread-safe.
 **/
class XercesDOMSerializer extends AbstractDOMSerializer
{
	private final OutputFormat outputFormat= new OutputFormat();
	
	private final Serializer serializer = new org.apache.xml.serialize.XMLSerializer();
	
	public XercesDOMSerializer()
	{
	    super();
	}	
	protected void serializeNode(Node node, Writer out) throws IOException
	{
		Serializer serializer = getSerializer("UTF-8");
		serializer.setOutputCharStream(out);
		_serialize(serializer, node);
	}
	protected void serializeNode(Node node, OutputStream out, String encoding) throws IOException
	{
		Serializer serializer = getSerializer(encoding);
		serializer.setOutputByteStream(out);
		_serialize(serializer, node);
	}
	private Serializer getSerializer(String encoding)
	{
		return serializer;
	}
	private void _serialize(Serializer serializer, Node node) throws IOException
	{
		XercesSerializerUtil.configureXMLSerializer(this, outputFormat, getEncoding());
		serializer.setOutputFormat(outputFormat);
		
		if (node instanceof Document)
			serializer.asDOMSerializer().serialize((Document)node);
		else if (node instanceof Element)
			serializer.asDOMSerializer().serialize((Element)node);
		else if (node instanceof DocumentFragment)
			serializer.asDOMSerializer().serialize((DocumentFragment)node);
		else if (node==null) throw new IllegalArgumentException("null node passed");
		else throw new RuntimeException("unsupported nodeType:" + node.getNodeType() + " node:" + node);
	}
	
	 
}
