package org.eleusoft.jaxs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.Node;





/** 
 * Static helpers to use the default DOMSerializer. 
 * The serializer is obtained from {@link JAXS#newDOMSerializer()}.
 **/
public final class DOMSerializeHelper
{
    private DOMSerializeHelper()
    {
        // no inst
    }
	
	private static DOMSerializer getImpl()
	{
		return JAXS.newDOMSerializer();
	}
	/**
	 * Serializes a Node to a Writer
	 */
	public static void serialize(Node node, Writer out) throws IOException
	{
		DOMSerializer impl = getImpl();
		impl.setWriter(out);
		impl.serialize(node);
	}
	/**
	 * Serializes a Node to an output stream with the passed character encoding.
	 */
	public static void serialize(Node node, 
	    OutputStream out, String encoding) throws IOException
	{
		DOMSerializer impl = getImpl();
		impl.setOutputStream(out);
		impl.setEncoding(encoding);
		impl.serialize(node);
	}
	/**
	 * Serializes a Node to a java String.
	 */
	public static String toString(Node node) throws IOException
	{
	    return _toString(false, node);	    
	}
	/**
	 * Serializes a Node to a java String pretty-printing the xml.
	 */
	public static String toPrettyString(Node node) throws IOException
	{
        return _toString(true, node);	    
	}
	private static String _toString(boolean pretty, Node node) throws IOException
	{
	   StringWriter sw = new StringWriter();
	    BufferedWriter bw = new BufferedWriter(sw);
		
		try
		{
		    DOMSerializer ds = getImpl();
		    ds.setPrettyPrint(pretty);
		    ds.setOmitXMLDeclaration(true);
		    ds.setWriter(sw);
		    ds.serialize(node);
	        
		}
		finally
		{   
		    try { bw.close();} catch(IOException ioe){}
		    try { sw.close();} catch(IOException ioe){}
		}
		return sw.toString();
	}
	/**
	 * Like {@link #toString(Node)} but
	 * doesn't throw any exception.
	 */
	public static String quietToString(Node node) 
	{
	    try
	    {
	        return toString(node);
	    }
	    catch(IOException ioe)
	    {
	        return null;
	    }
	}
	/**
	 * Like {@link #toPrettyString(Node)} but
	 * doesn't throw any exception.
	 */
	public static String quietToPrettyString(Node node) 
	{
	    try
	    {
	        return toPrettyString(node);
	    }
	    catch(IOException ioe)
	    {
	        return null;
	    }
	}
	
}
