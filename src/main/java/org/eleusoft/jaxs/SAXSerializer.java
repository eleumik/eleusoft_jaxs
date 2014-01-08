package org.eleusoft.jaxs;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;


/** 
 * Serializer for SAX events streams.
 * <p>It offers an implementation of org.xml.sax.ContentHandler
 * that will be used to receive SAX events and serializing
 * them to the contained stream. 
 * <p>The {@link ContentHandler}
 * instance can be retrieved with the only method
 * of this interface: {@link #asContentHandler()}.
 * <p>Note that each time the configuration changes
 * as effect of one of the setters of {@link XMLSerializer},
 * the {@link ContentHander} must be retrieved again in order 
 * for the effects to be seen, behavior for previously obtained
 * ContentHandler instances after a configuration change 
 * is undefined, might be applied immediately or not be applied 
 * at all.
 * <p>Is not thread-safe by design,
 * one instance must not be used by more than one thread
 * simultaneously.
 **/
public interface SAXSerializer extends XMLSerializer
{
	/**
	 * Returns the serializer as a sax {@link ContentHandler}
	 * to be used to receive the SAX events.
	 * <p>Note that each time the configuration changes
	 * as effect of one of the setters of {@link XMLSerializer},
	 * the {@link ContentHander} must be retrieved again. 
	 * @throws IOException when there is a problem 
	 *  in creating the handler.
	 */
	public ContentHandler asContentHandler() throws IOException; 
	
    
	
}
