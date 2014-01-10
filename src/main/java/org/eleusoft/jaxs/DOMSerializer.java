package org.eleusoft.jaxs;

import java.io.IOException;

import org.w3c.dom.Node;


/** 
 * Serializer for DOM nodes.
 * <p>It is able to serialize instances of:
 * <ul>
 * <li>org.w3c.dom.Element
 * <li>org.w3c.dom.Document
 * <li>org.w3c.dom.DocumentFragment
 * <li>org.w3c.dom.Text
 * </ul>
 * <p>Is not thread-safe by design,
 * one instance must not be used by more than one thread
 * simultaneously.
 **/
public interface DOMSerializer extends XMLSerializer
{
	/**
	 * Serialize the passed node.
	 * See class description for a list of supported nodes.
	 * @throws IOException for an i/o error happened during serialization
	 * @throws RuntimeException when the passed node is not supported;
	 *				see class description for a list of supported nodes.
	 */
	public void serialize(Node node) throws IOException;
	
	/**
	 * Helper method that serializes to a StringWriter
	 * and returns the result.
	 */
	public String toString(Node node) throws IOException;
	
	
}
