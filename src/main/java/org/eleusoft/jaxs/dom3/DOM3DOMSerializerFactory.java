
package org.eleusoft.jaxs.dom3;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;



/** 
 * Serializer for DOM nodes based on Apache Xerces.
 * Not thread-safe.
 **/
class DOM3DOMSerializerFactory 
{
	//private final OutputFormat outputFormat= new OutputFormat();
	
	//private final Serializer serializer = new org.apache.xml.serialize.XMLSerializer();

/** http://java.sun.com/j2se/1.5.0/docs/guide/plugin/dom/org/w3c/dom/ls/package-summary.html
	Document doc = ...;
>    DOMImplementation impl = doc.getImplementation();
>    if (impl.hasFeature("LS", "3.0")) {
>      DOMImplementationLS ls = (DOMImplementation)
> impl.getFeature("LS", 
> "3.0");
>      LSSerializer serializer =
> ls.createLSSerializer();
>      LSOutput output = ls.createLSOutput();
>      // configure output ...
>      serializer.write(doc, output);
>    }

	public DOM3DOMSerializerFactory()
	{
	}	
	static class DOM3Serializer //extends AbstractDOMSerializer
	{
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
	    	else throw new RuntimeException("unsupported node:" + node.getNodeType());
	    }
    }	
**/
	 
}
