package org.eleusoft.jaxs.xalan;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.apache.xml.serializer.* ;
import org.apache.xml.serializer.SerializerFactory ;

import java.util.Properties;
import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;



/** 
 * Serializer for DOM nodes based on Apache Xalan.
 * Not thread-safe.
 **/
class XalanDOMSerializer extends AbstractDOMSerializer
{
    private final Properties prop = 
        OutputPropertiesFactory.getDefaultMethodProperties("xml");

	
	
	private Serializer serializer;
	
	public XalanDOMSerializer()
	{
	    //throw new Error(prop.toString());
	}	
	protected void serializeNode(Node node, Writer out) throws IOException
	{
		Serializer serializer = getSerializer("UTF-8");
		serializer.setWriter(out);
		_serialize(serializer, node);
	}
	protected void serializeNode(Node node, OutputStream out, String encoding) throws IOException
	{
		Serializer serializer = getSerializer(encoding);
		serializer.setOutputStream(out);
		_serialize(serializer, node);
	}
	private Serializer getSerializer(String encoding)
	{
	    if (serializer==null) serializer = SerializerFactory.getSerializer(prop);
		
		XalanSerializerUtil.configureXMLSerializer(this, prop);
		
	    
		return serializer;
	}
	private void _serialize(Serializer serializer, Node node) throws IOException
	{
		
		if (node instanceof Document)
			serializer.asDOMSerializer().serialize((Document)node);
		else if (node instanceof Element)
			serializer.asDOMSerializer().serialize((Element)node);
		else if (node instanceof DocumentFragment)
			serializer.asDOMSerializer().serialize((DocumentFragment)node);
		else if (node==null) throw new IllegalArgumentException("null node passed");
		else throw new RuntimeException("unsupported node:" + node.getNodeType());
	}
	
	 
}
