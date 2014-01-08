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

class XercesSerializerUtil
{
    
	static  void configureXMLSerializer(org.eleusoft.jaxs.XMLSerializer ser, 
	    OutputFormat of, String encoding) 
	{
	    
		boolean pretty = ser.getPrettyPrint();
		of.setIndenting(pretty);
		of.setPreserveSpace(!pretty);
		of.setIndent(pretty ? 3 : 0);
		of.setOmitXMLDeclaration(ser.getOmitXMLDeclaration());
		of.setEncoding(encoding);
		of.setMethod(ser.getMethod());
		of.setVersion(ser.getVersion());
		of.setStandalone(ser.getStandalone());
		
		
	}
		
}
