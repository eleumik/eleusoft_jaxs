package org.eleusoft.jaxs;

/** 
 * Factory of serializers for DOM nodes.
 **/
public interface DOMSerializerFactory
{
	/**
	 * Creates a new serializer.
	 * <P>This method is thread safe.
	 * <p>The created serializer instead is not
	 * thread safe and cannot be used by more than one thread
	 * simultaneously.
	 */
	DOMSerializer createDOMSerializer();
}
