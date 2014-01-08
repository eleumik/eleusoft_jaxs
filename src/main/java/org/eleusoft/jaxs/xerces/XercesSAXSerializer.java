package org.eleusoft.jaxs.xerces;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

import org.xml.sax.ContentHandler;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.eleusoft.jaxs.helpers.AbstractSAXSerializer;
/** 
 * Serializer for SAX events streams based on apache
 * <code>org.apache.xml.serialize.XMLSerializer</code> class.
 **/
class XercesSAXSerializer extends AbstractSAXSerializer
{
    XMLSerializer serializer;
    
    private final OutputFormat outputFormat = new OutputFormat();
	
	/**
	 * Returns the serializer internal ContentHandler
	 * to be used to receive the SAX events.
	 */
	public ContentHandler asContentHandler() throws IOException
	{
	    Writer writer = getWriter();
	    String encoding = getEncoding();
	    if (writer==null) 
	    {
	        OutputStream os = getOutputStream();
	        if (os==null) throw new IllegalStateException("No output object set (writer or outputstream)");
	        
	        writer = new OutputStreamWriter(os, encoding);
	    }
	    
	    XercesSerializerUtil.configureXMLSerializer(this, outputFormat, encoding);
	    
		if (serializer==null) 
		    serializer = new XMLSerializer();
		else 
		{
		    serializer.reset();
		}
		serializer.setOutputFormat(outputFormat);
		serializer.setOutputCharStream(writer);
		return serializer.asContentHandler();
	}
		
}
