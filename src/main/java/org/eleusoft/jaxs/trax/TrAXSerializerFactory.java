package org.eleusoft.jaxs.trax;


import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.DOMSerializerFactory;
import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;

/** 
 * Factory of DOMSerializer and SAXSerializer based on TrAX.
 * <p>Thread-safe.
 * <p>Implementations uses an empty / identity 
 * Transformer and TrasformerHandler instance obtained using
 * <pre class=code>
 * TransformerFactory.newTransformer()
 * and 
 * SAXTransformerFactory.newTransformerHandler()
 * </pre>
 * 
 **/
public class TrAXSerializerFactory
   implements  DOMSerializerFactory ,SAXSerializerFactory
{
	public TrAXSerializerFactory()
	{
	    super();
	}
	public DOMSerializer createDOMSerializer() 
	{
	    return new TrAXDOMSerializer();
	}
	public SAXSerializer createSAXSerializer() 
	{
	    return new TrAXSAXSerializer();
	}
		
}
