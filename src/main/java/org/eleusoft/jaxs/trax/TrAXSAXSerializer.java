package org.eleusoft.jaxs.trax;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.OutputStream;


import org.xml.sax.ContentHandler;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource; 

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;



/** 
 * Serializer for DOM nodes based on TrAX.
 * This implementation uses an empty Transformer instance
 * obtained using
 * <pre>
 * TransformerFactory.newInstance().newTransformer()
 * </pre>
 * <p>Note: the java 1.4 embedded TransformerFactory 
 * is buggy and does not allow multiple serializations,
 * in my opinion this TraX serializer should be the last choice,
 * the other serializers are better also if their core classes
 * are deprecated. 
 **/
class TrAXSAXSerializer extends ConfigurableTraXSAXSerializer
{
	
	public TrAXSAXSerializer()
	{
	    super(TransformerFactory.newInstance());
	}
	
	
	
	
		
}
