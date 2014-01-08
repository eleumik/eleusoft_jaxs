package org.eleusoft.jaxs.trax;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;


import org.w3c.dom.Node;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource; 



import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;


/** 
 * Abstract DOM Serializer based on TrAX.
 * This implementation implements all methods of the 
 * AbstractSerializer leaving to subclasses to implement
 * the abstract protected {@link #getIdentityTransformer()} method to
 * provide an identity tranformer.
 **/
abstract class AbstractTrAXDOMSerializer extends AbstractDOMSerializer
{
	
	  
		
}
