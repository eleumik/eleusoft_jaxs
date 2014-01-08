package org.eleusoft.jaxs.xalan;


import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.DOMSerializerFactory;
import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;

/** 
 * Factory of DOMSerializer and SAXSerializer based on Apache Xalan.
 * <p>Thread-safe.
 * <p>Note that <code>org.apache.xml.serialize<b>r</b></code>
 * is from Xalan, 
 * <code>org.apache.xml.serialize</code> is from Xerces. 
 * Ah and there is also the deprecated 
 * <code>org.apache.xalan.serialize</code>. Easy  ;-).
 **/
public class XalanSerializerFactory
   implements  DOMSerializerFactory, SAXSerializerFactory
{
	
    public 	DOMSerializer createDOMSerializer() 
    {
        return new XalanDOMSerializer();
    }
    public 	SAXSerializer createSAXSerializer() 
    {
        return new XalanSAXSerializer();
    }
	 
}
