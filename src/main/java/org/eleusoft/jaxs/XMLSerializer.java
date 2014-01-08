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
 * Base interface for xml serializers.
 * 
 **/
public interface XMLSerializer
{
	/**
	 * Sets a Writer to serialize to.
	 * @param out the Writer to serialize to.
	 */
	public void setWriter(Writer out);

	/**
	 * Sets the OutputStream to serialize to.
	 * @param out the OutputStream to serialize to.
	 */
	public void setOutputStream(OutputStream out);

    
	/**
	 * Returns the encoding for the serializer.
	 * Default encoding is UTF-8.
	 * <p><b>From <a href='http://www.w3.org/TR/xslt#output'>XSLT</a> spec:</b>
	 * <blockquote class=code>
	 * specifies the preferred character encoding that 
	 * the XSLT processor should use to encode sequences 
	 * of characters as sequences of bytes; 
	 * the value of the attribute should be treated case-insensitively; 
	 * the value must contain only characters in 
	 * the range #x21 to #x7E (i.e. printable ASCII characters); 
	 * the value should either be a charset registered 
	 * with the Internet Assigned Numbers Authority [IANA], 
	 * [RFC2278] or start with X-.
	 * </blockquote>
	 * @return the encoding, never null.
	 */
	public String getEncoding();
	
	/**
	 * Sets the encoding for when writing to an output stream.
	 * @param encoding the encoding to use for serialization.
	 * @see #getEncoding() getEncoding() for more info
	 */
	public void setEncoding(String encoding);
	
	/**
	 * Returns whether the printed xml should be indented.
	 * By default this method returns <code>false</code>.
	 * <p><b>From <a href='http://www.w3.org/TR/xslt#output'>XSLT</a> spec:</b>
	 * <blockquote class=code>
     * specifies whether the XSLT processor may add additional 
     * whitespace when outputting the result tree; 
     * the value must be yes or no
	 * <p>..The default value is no. The xml output method should 
	 * use an algorithm to output additional whitespace 
	 * that ensures that the result if whitespace were 
	 * to be stripped from the output using the process 
	 * described in [3.4 Whitespace Stripping] with the 
	 * set of whitespace-preserving elements consisting 
	 * of just xsl:text would be the same when additional 
	 * whitespace is output as when additional whitespace is not output
	 * <p><b>NOTE:</b>It is usually not safe to 
	 * use indent="yes" with document types that include 
	 * element types with mixed content.
	 * </blockquote>
	 * @return <code>true</code> when indenting the output.
     */
	public boolean getPrettyPrint();
	
	/**
	 * Sets whether the printed xml should be indented.
	 * @see #getPrettyPrint() getPrettyPrint() for more info
	 */
	public void setPrettyPrint(boolean how);
	
	/**
	 * Returns whether the xml standlaone declaration should be printed
	 * By default this method returns <code>false</code>.
	 * <p><b>From <a href='http://www.w3.org/TR/xslt#output'>XSLT</a> spec:</b>
	 * <blockquote class=code>
	 * specifies whether the XSLT processor should output 
	 * a standalone document declaration; the value must be yes or no.
	 * </blockquote>
     * @return <code>true</code> when the standalone declaration should
     * be printed.
	 */
	public boolean getStandalone();
	
	/**
	 * Sets whether the standalone property should be printed
	 * with value 'yes'. Default value is <code>false</code>.
	 * @see #getStandalone() getStandalone() for more info
	 */
	public void setStandalone(boolean how);
	
	/**
	 * Returns whether the xml text declaration should be printed
	 * By default this method returns <code>false</code>
	 * and the declaration is included.
	 * <p><b>From <a href='http://www.w3.org/TR/xslt#output'>XSLT</a> spec:</b>
	 * <blockquote class=code>	 
	 * The xml output method should output an XML declaration 
	 * unless the omit-xml-declaration attribute has the value yes. 
	 * The XML declaration should include both version information 
	 * and an encoding declaration. If the standalone attribute 
	 * is specified, it should include a standalone document 
	 * declaration with the same value as the value as the value 
	 * of the standalone attribute. Otherwise, it should not 
	 * include a standalone document declaration; this ensures 
	 * that it is both a XML declaration (allowed at the 
	 * beginning of a document entity) and a text declaration 
	 * (allowed at the beginning of an external general parsed entity).
	 * </blockquote>
     * @return <code>true</code> when the xml declaration should
     * be omitted.
	 */
	public boolean getOmitXMLDeclaration();
	
	/**
	 * Sets whether the xml text declaration should be printed.
	 * The default behavior is to print the declaration.
	 * @see #getOmitXMLDeclaration() getOmitXMLDeclaration() For more info.
	 */
	public void setOmitXMLDeclaration(boolean how);
	
	/**
	 * Sets the xml version, default value is "1.0".
	 */
	public void setVersion(String version);
	/**
	 * Retrieves the xml version, default value is "1.0".
	 */
	public String getVersion();
	/**
	 * Sets the output content type, default value is <code>xml</code>.
	 * Equivalent to xsl:method.
	 */
	public void setMethod(String method);
	/**
	 * Retrieves the output content type, default value is <code>xml</code>.
	 * Equivalent to xsl:method.
	 */
	public String getMethod();
	
}
