package org.eleusoft.jaxs.xalan;


import org.apache.xml.serializer.OutputPropertiesFactory;
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
    // Just to trigger load of lib and fail,
    // otherwise thinks there is Xalan but then
    // does not work
    static
    {
        // just to trigger load..
        OutputPropertiesFactory factory = new OutputPropertiesFactory();
        factory = null;
    }
	
    public 	DOMSerializer createDOMSerializer() 
    {
        return new XalanDOMSerializer();
    }
    public 	SAXSerializer createSAXSerializer() 
    {
        return new XalanSAXSerializer();
    }
	 
}
