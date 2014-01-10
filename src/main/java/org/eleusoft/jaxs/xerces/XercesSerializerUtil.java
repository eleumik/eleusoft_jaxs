package org.eleusoft.jaxs.xerces;

import org.apache.xml.serialize.OutputFormat;

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
