package org.eleusoft.jaxs.xerces;


import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.DOMSerializerFactory;
import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;

/** 
 * Factory of DOMSerializer and SAXSerializer based on Apache Xerces.
 * <p>Thread-safe.
 * <p>Note that <code>org.apache.xml.serialize<b>r</b></code>
 * is from Xalan, 
 * <code>org.apache.xml.serialize</code> is from Xerces. 
 * Ah and there is also the deprecated 
 * <code>org.apache.xalan.serialize</code>. Easy  ;-).
 **/
public class XercesSerializerFactory
   implements  DOMSerializerFactory, SAXSerializerFactory
{
	
    public 	DOMSerializer createDOMSerializer() 
    {
        return new XercesDOMSerializer();
    }
    public 	SAXSerializer createSAXSerializer() 
    {
        return new XercesSAXSerializer();
    }
	 
}
