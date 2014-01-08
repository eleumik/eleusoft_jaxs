package org.eleusoft.jaxs.trax;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;


class TrAXSerializerUtil
{
    
	static  void configure(Transformer serializer, 
	    org.eleusoft.jaxs.XMLSerializer ser) 
	{
	    serializer.setOutputProperty(OutputKeys.METHOD, 
		    ser.getMethod());
		serializer.setOutputProperty(OutputKeys.ENCODING,
		    ser.getEncoding());
		serializer.setOutputProperty(OutputKeys.VERSION,
		    ser.getVersion());
		// http://www.xmlplease.com/xml/xmlquotations/standalone  
		// Hmm.. comes out that SAXON takes it as 3-states
		// 201401 so trying this fix, if [false] do not specify
		if(ser.getStandalone()) serializer.setOutputProperty(OutputKeys.STANDALONE, 
		    getYesNo(true));
		serializer.setOutputProperty(OutputKeys.INDENT, 
		    getYesNo(ser.getPrettyPrint()));
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		    getYesNo(ser.getOmitXMLDeclaration()));
		//serializer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "test");
			
		
	}
		
	private static final String getYesNo(final boolean param)
	{
	    return param ? "yes" : "no";
	}
	
}
