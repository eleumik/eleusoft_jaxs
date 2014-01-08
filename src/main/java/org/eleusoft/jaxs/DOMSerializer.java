package org.eleusoft.jaxs;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

import org.w3c.dom.Node;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


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
