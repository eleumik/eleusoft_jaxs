package org.eleusoft.jaxs.resin;


import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.DOMSerializerFactory;

/** 
 * Factory of DOMSerializer based on Resin.
 * <p>Thread-safe.
 **/
public class ResinSerializerFactory
   implements  DOMSerializerFactory //, SAXSerializerFactory
{
	
    public 	DOMSerializer createDOMSerializer() 
    {
        return new ResinDOMSerializer();
    }
    /**
    public 	SAXSerializer createSAXSerializer() 
    {
        return new XercesSAXSerializer();
    }
	 **/
}
