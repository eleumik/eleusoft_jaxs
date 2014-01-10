package org.eleusoft.jaxs.xalan;

import java.util.Properties;

class XalanSerializerUtil
{
    
    /**
	 * Configures the properties for Xalan serializer
	 */
	static void configureXMLSerializer(org.eleusoft.jaxs.XMLSerializer ser, Properties props)
	{
	    String encoding = ser.getEncoding();
	    boolean isPretty = ser.getPrettyPrint();
	    
	    props.setProperty("omit-xml-declaration", ser.getOmitXMLDeclaration() ? "yes" : "no");
	    props.setProperty("encoding", encoding);
	    props.setProperty("method", ser.getMethod());
	    props.setProperty("version", ser.getVersion());
	    props.setProperty("standalone", ser.getStandalone() ? "yes" : "no");
	    props.setProperty("indent", isPretty ? "yes" : "no");
	     
	    props.setProperty("{http://xml.apache.org/xalan}indent-amount", 
	        isPretty ? "0" : "3");
	    
		
	}
		
}
