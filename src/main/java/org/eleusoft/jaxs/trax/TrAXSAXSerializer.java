package org.eleusoft.jaxs.trax;

import javax.xml.transform.TransformerFactory;



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
