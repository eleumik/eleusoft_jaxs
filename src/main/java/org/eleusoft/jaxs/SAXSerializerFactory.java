package org.eleusoft.jaxs;

/** 
 * Factory of serializers for SAX events streams.
 **/
public interface SAXSerializerFactory
{
	/**
	 * Creates a new serializer.
	 * <P>This method is thread safe.
	 * <p>The created serializer instead is not
	 * thread safe and cannot be used by more than one thread
	 * simultaneously.
	 */
	SAXSerializer createSAXSerializer();
}
